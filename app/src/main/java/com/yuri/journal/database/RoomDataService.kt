package com.yuri.journal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yuri.journal.database.converters.ListConverters
import com.yuri.journal.database.dao.JournalDao
import com.yuri.journal.database.entity.JournalEntity



@Database(entities = [JournalEntity::class], version = 1)
@TypeConverters(ListConverters::class)
abstract class RoomDataService : RoomDatabase(){
    abstract fun journalDao(): JournalDao
}