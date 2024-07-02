package com.yuri.journal.database

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yuri.journal.constants.DataBaseConstant
import com.yuri.journal.database.dao.JournalDao

object AppDatabase: AutoCloseable {
    private lateinit var db: RoomDataService
    val journalDao: JournalDao get() = db.journalDao()

    fun init(context: Application) {
        if (!::db.isInitialized) {
            db = Room.databaseBuilder(
                context,
                RoomDataService::class.java, DataBaseConstant.DATABASE_FILE_NAME
            ).build()
        }
    }

    override fun close() {
        if (::db.isInitialized) {
            db.close()
        }
    }
}