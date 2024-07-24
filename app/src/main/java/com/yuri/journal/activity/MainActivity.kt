package com.yuri.journal.activity

import android.graphics.Color
import android.os.Bundle
import com.yuri.journal.R
import com.yuri.journal.activity.EditJournalActivity.Companion.startEditJournalActivity
import com.yuri.journal.common.BaseActivity
import com.yuri.journal.constants.GlobalSharedConstant
import com.yuri.journal.databinding.ActivityMainBinding
import com.yuri.journal.fragment.WebdavConfigFragment
import com.yuri.journal.utils.ViewUtils.goToSetNotify
import com.yuri.journal.utils.ViewUtils.isOpenNotify
import com.yuri.journal.viewModel.JournalViewModel


class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: JournalViewModel = GlobalSharedConstant.journalViewModel
    private val webdavConfig = WebdavConfigFragment()

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

        binding.journalList.addCallBack { id ->
            startEditJournalActivity(EditJournalActivity.Mode.EDIT, id)
        }

        if (!isOpenNotify()) {
            goToSetNotify()
        }
    }

    /**
     * 初始化各种事件
     * 点击事件
     */
    private fun initEvent() {
        // 开启编辑界面
        binding.fab.setOnClickListener {
            startEditJournalActivity(EditJournalActivity.Mode.CREATE)
        }

        // 打开侧边页
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        // 菜单点击
        binding.navView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            when(item.itemId) {
                R.id.action_webdav_settings -> {
                    webdavConfig.show(supportFragmentManager, WebdavConfigFragment.TAG)
                }
            }
            true
        }
    }
}