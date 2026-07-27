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
import com.example.chequemanager.viewmodel.AppViewModel

@Composable
fun AccountListScreen(vm: AppViewModel) {
    val accounts by vm.accounts.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "افزودن حساب")
            }
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(accounts) { account ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(account.name, style = MaterialTheme.typography.titleMedium)
                        Text("نوع: ${account.type}" + if (account.bankName.isNotBlank()) " — ${account.bankName}" else "")
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddAccountDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, type, bank ->
                vm.addAccount(name, type, bank)
                showDialog = false
            }
        )
    }
}

@Composable
private fun AddAccountDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("بانکی") }
    var bank by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("افزودن حساب/صندوق") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("نام حساب/صندوق") })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = type == "بانکی", onClick = { type = "بانکی" }, label = { Text("بانکی") })
                    FilterChip(selected = type == "نقدی", onClick = { type = "نقدی" }, label = { Text("نقدی") })
                }
                if (type == "بانکی") {
                    OutlinedTextField(value = bank, onValueChange = { bank = it }, label = { Text("نام بانک") })
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onConfirm(name, type, bank) }) { Text("ذخیره") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}
