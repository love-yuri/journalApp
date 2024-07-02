package com.yuri.journal.database.entity

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.yuri.journal.constants.DataBaseConstant.JOURNAL_TABLE_NAME
import com.yuri.journal.utils.TimeUtils

@Entity
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null, // 主键id
    @ColumnInfo val title: String?, // 标题
    @ColumnInfo val content: String, // 日记内容
    @ColumnInfo val images: List<String>?, // 关联图片
    @ColumnInfo val createTime: String = TimeUtils.now, // 创建时间
    @ColumnInfo val updateTime: String = TimeUtils.now, // 更新时间
)