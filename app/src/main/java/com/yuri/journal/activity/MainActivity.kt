package com.yuri.journal.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.google.android.material.appbar.AppBarLayout
import com.yuri.journal.R
import com.yuri.journal.components.JournalListDecoration

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
        window.statusBarColor = Color.TRANSPARENT

        binding.journalList.addItemDecoration(JournalListDecoration())
        binding.journalList.layoutManager = GridLayoutManager(this@MainActivity, 1)
        lifecycleScope.launch {
            val res = AppDatabase.journalDao.list()
            withContext(Dispatchers.Main) {
                binding.journalList.adapter = JournalListAdapter(res)
            }
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
            startActivity(Intent(this, EditJournalActivity::class.java))
        }
    }
}