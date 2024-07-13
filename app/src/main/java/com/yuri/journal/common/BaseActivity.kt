package com.yuri.journal.common

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.yuri.journal.utils.MessageUtils

abstract class BaseActivity<VB: ViewBinding> : BaseBinding<VB>, AppCompatActivity() {
    override lateinit var binding: VB
    protected lateinit var messageUtils: MessageUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 绑定binding - 绑定显示
        binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

        // 初始化消息工具
        messageUtils = MessageUtils(this)

        // 初始化顶部
        enableEdgeToEdge()
    }
}