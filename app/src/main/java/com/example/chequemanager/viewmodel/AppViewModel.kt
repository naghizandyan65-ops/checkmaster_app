package com.example.chequemanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chequemanager.data.Account
import com.example.chequemanager.data.AppDatabase
import com.example.chequemanager.data.Cheque
import com.example.chequemanager.data.Direction
import com.example.chequemanager.data.Person
import com.example.chequemanager.data.PersonGroup
import com.example.chequemanager.data.Repository
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = Repository(AppDatabase.getInstance(application))

    val persons = repo.persons
    val groups = repo.groups
    val accounts = repo.accounts
    val receivedCheques = repo.chequesByDirection(Direction.RECEIVED)
    val paidCheques = repo.chequesByDirection(Direction.PAID)

    fun addPerson(name: String, mobile: String, nationalCode: String, groupId: Long?) {
        viewModelScope.launch {
            repo.addPerson(Person(name = name, mobile = mobile, nationalCode = nationalCode, groupId = groupId))
        }
    }

    fun addGroup(name: String) {
        viewModelScope.launch { repo.addGroup(PersonGroup(name = name)) }
    }

    fun addAccount(name: String, type: String, bankName: String) {
        viewModelScope.launch { repo.addAccount(Account(name = name, type = type, bankName = bankName)) }
    }

    fun addCheque(
        chequeNumber: String,
        sayadId: String,
        personId: Long,
        accountId: Long?,
        direction: String,
        amount: Double,
        issueDate: Long,
        dueDate: Long
    ) {
        viewModelScope.launch {
            repo.addCheque(
                Cheque(
                    chequeNumber = chequeNumber,
                    sayadId = sayadId,
                    personId = personId,
                    accountId = accountId,
                    direction = direction,
                    amount = amount,
                    issueDate = issueDate,
                    dueDate = dueDate
                )
            )
        }
    }

    fun updateChequeStatus(cheque: Cheque, newStatus: String) {
        viewModelScope.launch { repo.updateCheque(cheque.copy(status = newStatus)) }
    }

    fun convertToPaid(cheque: Cheque, newPersonId: Long, newAccountId: Long?) {
        viewModelScope.launch { repo.convertReceivedToPaid(cheque, newPersonId, newAccountId) }
    }
}
