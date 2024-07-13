package com.yuri.journal.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.yuri.journal.activity.EditJournalActivity.Companion.startEditJournalActivity
import com.yuri.journal.common.BaseActivity
import com.yuri.journal.components.JournalListAdapter
import com.yuri.journal.database.AppDatabase
import com.yuri.journal.databinding.ActivityMainBinding
import com.yuri.journal.viewModel.JournalViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: JournalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
        window.statusBarColor = Color.TRANSPARENT

        binding.journalList.addItemDecoration(JournalListAdapter.JournalListDecoration())
        binding.journalList.layoutManager = GridLayoutManager(this@MainActivity, 1)

        viewModel.data.value?.also {
            binding.journalList.adapter = JournalListAdapter(it)
        }

        viewModel.data.observe(this) { items ->
            binding.journalList.adapter = JournalListAdapter(items)
        }

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }
    }

    /**
     * 初始化各种事件
     * 点击事件
     */
    private fun initEvent() {
        binding.fab.setOnClickListener {
            startEditJournalActivity(EditJournalActivity.Mode.EDIT)
        }
    }
}