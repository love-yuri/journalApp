package com.yuri.journal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yuri.journal.database.dao.JournalDao
import com.yuri.journal.database.entity.JournalEntity


@Database(entities = [JournalEntity::class], version = 1)
abstract class RoomDataService : RoomDatabase(){
    abstract fun journalDao(): JournalDao
}