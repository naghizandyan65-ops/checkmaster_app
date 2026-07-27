package com.example.chequemanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chequemanager.data.Status
import com.example.chequemanager.viewmodel.AppViewModel

@Composable
fun DashboardScreen(vm: AppViewModel, onOpen: (String) -> Unit) {
    val received by vm.receivedCheques.collectAsState(initial = emptyList())
    val paid by vm.paidCheques.collectAsState(initial = emptyList())

    val receivedInProgress = received.count { it.status == Status.IN_PROGRESS }
    val paidInProgress = paid.count { it.status == Status.IN_PROGRESS }
    val receivedSum = received.filter { it.status == Status.IN_PROGRESS }.sumOf { it.amount }
    val paidSum = paid.filter { it.status == Status.IN_PROGRESS }.sumOf { it.amount }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("داشبورد مدیریت چک", style = MaterialTheme.typography.headlineSmall)

        SummaryCard("چک‌های دریافتی در جریان", "$receivedInProgress عدد — ${receivedSum.toLong()} ریال")
        SummaryCard("چک‌های پرداختی در جریان", "$paidInProgress عدد — ${paidSum.toLong()} ریال")

        Button(onClick = { onOpen("persons") }, modifier = Modifier.fillMaxWidth()) { Text("مدیریت اشخاص") }
        Button(onClick = { onOpen("cheques_received") }, modifier = Modifier.fillMaxWidth()) { Text("چک‌های دریافتی") }
        Button(onClick = { onOpen("cheques_paid") }, modifier = Modifier.fillMaxWidth()) { Text("چک‌های پرداختی") }
        Button(onClick = { onOpen("accounts") }, modifier = Modifier.fillMaxWidth()) { Text("صندوق و حساب‌ها") }
    }
}

@Composable
private fun SummaryCard(title: String, value: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
    }
}
