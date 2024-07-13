package com.yuri.journal.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yuri.journal.constants.DataBaseConstant
import com.yuri.journal.database.converters.ListConverters
import com.yuri.journal.database.dao.JournalDao
import com.yuri.journal.database.entity.JournalEntity

object AppDatabase: AutoCloseable {

    @Database(entities = [JournalEntity::class], version = 1)
    @TypeConverters(ListConverters::class)
    abstract class RoomDataService : RoomDatabase(){
        abstract fun journalDao(): JournalDao
    }

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