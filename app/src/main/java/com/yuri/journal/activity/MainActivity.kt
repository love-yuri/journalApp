package com.yuri.journal.activity

import android.os.Bundle
import com.yuri.journal.base.BaseActivity
import com.yuri.journal.databinding.ActivityMainBinding
import com.yuri.journal.utils.MessageUtils

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        messageUtils.createToast("初始化成功!")
        messageUtils.createDialog("找到类", "哈哈啥")
    }
}