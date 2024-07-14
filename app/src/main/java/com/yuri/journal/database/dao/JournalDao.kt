package com.yuri.journal.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yuri.journal.constants.DataBaseConstant.JOURNAL_TABLE_NAME
import com.yuri.journal.database.entity.JournalEntity


@Dao
interface JournalDao {

    @Insert
    suspend fun insert(value: JournalEntity): Long

    @Update
    suspend fun update(value: JournalEntity): Int

    @Delete
    suspend fun delete(value: JournalEntity): Int

    @Query("select * from $JOURNAL_TABLE_NAME")
    suspend fun list(): List<JournalEntity>

    @Query("delete from $JOURNAL_TABLE_NAME where id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("select * from $JOURNAL_TABLE_NAME where id = :id")
    suspend fun getById(id: Int): JournalEntity?
}