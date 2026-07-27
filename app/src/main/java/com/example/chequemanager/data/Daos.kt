package com.example.chequemanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM person ORDER BY name")
    fun getAll(): Flow<List<Person>>

    @Insert
    suspend fun insert(person: Person): Long

    @Update
    suspend fun update(person: Person)

    @Delete
    suspend fun delete(person: Person)
}

@Dao
interface PersonGroupDao {
    @Query("SELECT * FROM person_group ORDER BY name")
    fun getAll(): Flow<List<PersonGroup>>

    @Insert
    suspend fun insert(group: PersonGroup): Long
}

@Dao
interface AccountDao {
    @Query("SELECT * FROM account ORDER BY name")
    fun getAll(): Flow<List<Account>>

    @Insert
    suspend fun insert(account: Account): Long

    @Update
    suspend fun update(account: Account)
}

@Dao
interface ChequeDao {
    @Query("SELECT * FROM cheque WHERE direction = :direction ORDER BY dueDate")
    fun getByDirection(direction: String): Flow<List<Cheque>>

    @Query("SELECT * FROM cheque WHERE id = :id")
    suspend fun getById(id: Long): Cheque?

    @Insert
    suspend fun insert(cheque: Cheque): Long

    @Update
    suspend fun update(cheque: Cheque)

    @Delete
    suspend fun delete(cheque: Cheque)
}

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder ORDER BY remindAt")
    fun getAll(): Flow<List<Reminder>>

    @Insert
    suspend fun insert(reminder: Reminder): Long
}
