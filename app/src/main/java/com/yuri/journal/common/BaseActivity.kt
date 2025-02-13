package com.yuri.journal.common

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.yuri.journal.utils.MessageUtils

abstract class BaseActivity<VB: ViewBinding> : BaseBinding<VB>, AppCompatActivity() {
    override lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 绑定binding - 绑定显示
        binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

        // 初始化顶部
        enableEdgeToEdge()

        window.statusBarColor = Color.TRANSPARENT
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = Color.TRANSPARENT

        window.decorView.post {
            window.navigationBarColor = Color.TRANSPARENT
            window.isNavigationBarContrastEnforced = false
        }
    }
}