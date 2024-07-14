package com.yuri.journal.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yuri.journal.constants.DataBaseConstant.JOURNAL_TABLE_NAME
import com.yuri.journal.utils.TimeUtils

@Entity(tableName = JOURNAL_TABLE_NAME)
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null, // 主键id
    @ColumnInfo val title: String? = null, // 标题
    @ColumnInfo val content: String, // 日记内容
    @ColumnInfo val images: List<String>? = null, // 关联图片
    @ColumnInfo val createTime: String = TimeUtils.now, // 创建时间
    @ColumnInfo val updateTime: String = TimeUtils.now, // 更新时间
)