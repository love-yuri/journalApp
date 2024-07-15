package com.yuri.journal.activity


import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import com.yuri.journal.activity.EditJournalActivity.Companion.startEditJournalActivity
import com.yuri.journal.common.BaseActivity
import com.yuri.journal.constants.GlobalSharedConstant
import com.yuri.journal.databinding.ActivityMainBinding
import com.yuri.journal.utils.MessageUtils.notify
import com.yuri.journal.utils.ViewUtils.goToSetNotify
import com.yuri.journal.utils.ViewUtils.isOpenNotify
import com.yuri.journal.viewModel.JournalViewModel


class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: JournalViewModel = GlobalSharedConstant.journalViewModel

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
        binding.fab.setOnClickListener {
            startEditJournalActivity(EditJournalActivity.Mode.CREATE)
        }

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }
    }
}