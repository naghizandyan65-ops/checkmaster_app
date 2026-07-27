package com.example.chequemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

object Status {
    const val IN_PROGRESS = "در جریان"
    const val CLEARED = "پاس شده"
    const val BOUNCED = "برگشتی"
    const val CANCELED = "باطل شده"
    const val TRANSFERRED = "واگذار شده"
}

object Direction {
    const val RECEIVED = "دریافتی"
    const val PAID = "پرداختی"
}

@Entity(tableName = "person_group")
data class PersonGroup(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

@Entity(tableName = "person")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val mobile: String = "",
    val nationalCode: String = "",
    val groupId: Long? = null
)

@Entity(tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String = "بانکی",
    val bankName: String = "",
    val initialBalance: Double = 0.0
)

@Entity(tableName = "financial_year")
data class FinancialYear(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val startDate: Long,
    val endDate: Long,
    val isActive: Boolean = true
)

@Entity(tableName = "cheque")
data class Cheque(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sayadId: String = "",
    val chequeNumber: String,
    val personId: Long,
    val accountId: Long? = null,
    val financialYearId: Long? = null,
    val direction: String,
    val amount: Double,
    val issueDate: Long,
    val dueDate: Long,
    val status: String = Status.IN_PROGRESS,
    val convertedFromId: Long? = null,
    val notes: String = ""
)

@Entity(tableName = "reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chequeId: Long? = null,
    val personId: Long? = null,
    val remindAt: Long,
    val message: String,
    val isSent: Boolean = false
)
