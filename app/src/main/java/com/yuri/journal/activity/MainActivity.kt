package com.yuri.journal.activity

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.yuri.journal.base.BaseActivity
import com.yuri.journal.components.JournalListAdapter
import com.yuri.journal.database.AppDatabase
import com.yuri.journal.database.entity.JournalEntity
import com.yuri.journal.databinding.ActivityMainBinding
import com.yuri.journal.utils.MessageUtils
import com.yuri.journal.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
        setSupportActionBar(binding.toolbar)
        lifecycleScope.launch {
            val res = AppDatabase.journalDao.list()
            messageUtils.createToast("创建成功 $res")
            withContext(Dispatchers.Main) {
                binding.journalList.layoutManager = GridLayoutManager(this@MainActivity, 1)
                binding.journalList.adapter = JournalListAdapter(
                    res
                )
            }
        }
    }

    /**
     * 初始化各种事件
     * 点击事件
     */
    private fun initEvent() {
        binding.fab.setOnClickListener {
            val journal = JournalEntity(
                null,
                null,
                "yuri is yes",
                null,
                TimeUtils.now,
                TimeUtils.now,
            )
            lifecycleScope.launch {
                val res = AppDatabase.journalDao.insert(journal)
                messageUtils.createToast("创建成功 $res")
            }
        }
    }
}