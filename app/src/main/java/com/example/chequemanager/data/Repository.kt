package com.example.chequemanager.data

class Repository(private val db: AppDatabase) {
    val persons = db.personDao().getAll()
    val groups = db.personGroupDao().getAll()
    val accounts = db.accountDao().getAll()
    val reminders = db.reminderDao().getAll()

    fun chequesByDirection(direction: String) = db.chequeDao().getByDirection(direction)

    suspend fun addPerson(person: Person) = db.personDao().insert(person)
    suspend fun addGroup(group: PersonGroup) = db.personGroupDao().insert(group)
    suspend fun addAccount(account: Account) = db.accountDao().insert(account)

    suspend fun addCheque(cheque: Cheque) = db.chequeDao().insert(cheque)
    suspend fun updateCheque(cheque: Cheque) = db.chequeDao().update(cheque)

    suspend fun addReminder(reminder: Reminder) = db.reminderDao().insert(reminder)

    /** تبدیل چک دریافتی به چک پرداختی جدید، با حفظ ارتباط با چک اصلی */
    suspend fun convertReceivedToPaid(received: Cheque, newPersonId: Long, newAccountId: Long?): Long {
        db.chequeDao().update(received.copy(status = Status.TRANSFERRED))
        val paid = Cheque(
            sayadId = received.sayadId,
            chequeNumber = received.chequeNumber,
            personId = newPersonId,
            accountId = newAccountId,
            financialYearId = received.financialYearId,
            direction = Direction.PAID,
            amount = received.amount,
            issueDate = received.issueDate,
            dueDate = received.dueDate,
            status = Status.IN_PROGRESS,
            convertedFromId = received.id
        )
        return db.chequeDao().insert(paid)
    }
}
