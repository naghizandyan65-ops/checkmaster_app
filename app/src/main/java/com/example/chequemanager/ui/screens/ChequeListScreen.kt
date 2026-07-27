package com.example.chequemanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chequemanager.data.Cheque
import com.example.chequemanager.data.Direction
import com.example.chequemanager.data.Status
import com.example.chequemanager.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChequeListScreen(vm: AppViewModel, direction: String) {
    val cheques by (if (direction == Direction.RECEIVED) vm.receivedCheques else vm.paidCheques)
        .collectAsState(initial = emptyList())
    val persons by vm.persons.collectAsState(initial = emptyList())
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Icon(Icons.Default.Add, contentDescription = "افزودن چک")
            }
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(cheques) { cheque ->
                val personName = persons.find { it.id == cheque.personId }?.name ?: "—"
                ChequeCard(cheque, personName, onStatusChange = { newStatus ->
                    vm.updateChequeStatus(cheque, newStatus)
                })
            }
        }
    }

    if (showAdd) {
        AddChequeDialog(
            direction = direction,
            personNames = persons.map { it.id to it.name },
            onDismiss = { showAdd = false },
            onConfirm = { number, sayad, personId, amount, due ->
                vm.addCheque(number, sayad, personId, null, direction, amount, System.currentTimeMillis(), due)
                showAdd = false
            }
        )
    }
}

@Composable
private fun ChequeCard(cheque: Cheque, personName: String, onStatusChange: (String) -> Unit) {
    val df = remember { SimpleDateFormat("yyyy/MM/dd", Locale.US) }
    var expanded by remember { mutableStateOf(false) }

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text("شماره چک: ${cheque.chequeNumber}", style = MaterialTheme.typography.titleMedium)
            Text("طرف حساب: $personName")
            Text("مبلغ: ${cheque.amount.toLong()} ریال")
            Text("سررسید: ${df.format(Date(cheque.dueDate))}")
            Text("وضعیت: ${cheque.status}", style = MaterialTheme.typography.bodyMedium)

            Box {
                TextButton(onClick = { expanded = true }) { Text("تغییر وضعیت") }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf(Status.IN_PROGRESS, Status.CLEARED, Status.BOUNCED, Status.CANCELED).forEach { s ->
                        DropdownMenuItem(text = { Text(s) }, onClick = {
                            onStatusChange(s)
                            expanded = false
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun AddChequeDialog(
    direction: String,
    personNames: List<Pair<Long, String>>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long, Double, Long) -> Unit
) {
    var number by remember { mutableStateOf("") }
    var sayad by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedPerson by remember { mutableStateOf(personNames.firstOrNull()) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (direction == Direction.RECEIVED) "ثبت چک دریافتی" else "ثبت چک پرداختی") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("شماره چک") })
                OutlinedTextField(value = sayad, onValueChange = { sayad = it }, label = { Text("شناسه صیاد") })
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("مبلغ") })

                OutlinedTextField(
                    value = selectedPerson?.second ?: "انتخاب شخص",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("طرف حساب") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(onClick = { expanded = true }) { Text("انتخاب از لیست اشخاص") }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    personNames.forEach { p ->
                        DropdownMenuItem(text = { Text(p.second) }, onClick = {
                            selectedPerson = p
                            expanded = false
                        })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val amt = amount.toDoubleOrNull() ?: 0.0
                val personId = selectedPerson?.first
                if (number.isNotBlank() && personId != null) {
                    val dueDate = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000
                    onConfirm(number, sayad, personId, amt, dueDate)
                }
            }) { Text("ذخیره") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}
