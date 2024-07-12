package com.yuri.journal.activity


import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.yuri.journal.R
import com.yuri.journal.base.BaseActivity
import com.yuri.journal.databinding.ActivityEditJournalBinding
import com.yuri.journal.utils.ViewUtils.inView
import com.yuri.journal.utils.ViewUtils.showSoftInput


class EditJournalActivity : BaseActivity<ActivityEditJournalBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        initView()
    }

    private fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * 监听点击事件，处理空白区域点击
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (binding.toolbar.inView(ev)) {
                // 拦截toolbar的事件
                return super.dispatchTouchEvent(ev)
            }

            // 点击空白区域，输入框获取焦点并弹出键盘
            if (checkEmptyViewClick(x, y)) {
                showSoftInput(binding.journalEdit)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    /**
     * 检查有没有点击到空白区域
     */
    private fun checkEmptyViewClick(x: Int, y: Int): Boolean {
        for (i in 0 until binding.scrollView.childCount) {
            val child = binding.scrollView.getChildAt(i)
            val location = IntArray(2)
            child.getLocationOnScreen(location)

            if (x >= location[0] && x <= location[0] + child.width
                && y >= location[1] && y <= location[1] + child.height
            ) {
                return false
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}