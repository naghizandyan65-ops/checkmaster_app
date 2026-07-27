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
fun PersonListScreen(vm: AppViewModel) {
    val persons by vm.persons.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "افزودن شخص")
            }
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(persons) { person ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(person.name, style = MaterialTheme.typography.titleMedium)
                        if (person.mobile.isNotBlank()) Text(person.mobile, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddPersonDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, mobile, nationalCode ->
                vm.addPerson(name, mobile, nationalCode, null)
                showDialog = false
            }
        )
    }
}

@Composable
private fun AddPersonDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var nationalCode by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("افزودن شخص جدید") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("نام") })
                OutlinedTextField(value = mobile, onValueChange = { mobile = it }, label = { Text("موبایل") })
                OutlinedTextField(value = nationalCode, onValueChange = { nationalCode = it }, label = { Text("کد ملی/اقتصادی") })
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onConfirm(name, mobile, nationalCode) }) { Text("ذخیره") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}
