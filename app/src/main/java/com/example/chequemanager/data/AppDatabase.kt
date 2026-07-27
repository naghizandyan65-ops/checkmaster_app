package com.example.chequemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Person::class, PersonGroup::class, Account::class, Cheque::class, Reminder::class, FinancialYear::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun personGroupDao(): PersonGroupDao
    abstract fun accountDao(): AccountDao
    abstract fun chequeDao(): ChequeDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cheque_manager.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
