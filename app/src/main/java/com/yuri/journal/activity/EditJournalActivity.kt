package com.yuri.journal.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import androidx.activity.viewModels
import com.yuri.journal.R
import com.yuri.journal.common.BaseActivity
import com.yuri.journal.common.log
import com.yuri.journal.constants.ActivityConstant.ACTIVITY_CONFIG_NAME
import com.yuri.journal.databinding.ActivityEditJournalBinding
import com.yuri.journal.utils.ViewUtils.inView
import com.yuri.journal.utils.ViewUtils.showSoftInput
import com.yuri.journal.viewModel.JournalViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class EditJournalActivity : BaseActivity<ActivityEditJournalBinding>() {

    private lateinit var config: Config
    private val viewModel: JournalViewModel by viewModels()

    enum class Mode {
        CREATE,
        EDIT
    }

    @Serializable
    data class Config(
        val mode: Mode = Mode.CREATE, // 工作模式
        val id: Int? = null // 编辑模式需要的id
    )

    private var title: String
        get() = binding.title.text.toString()
        set(value) {
            binding.title.setText(value)
        }


    private var content: String
        get() = binding.content.text.toString()
        set(value) {
            binding.content.setText(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        // 初始化界面
        initView()

        // 初始化配置
        initConfig()
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

            // 拦截toolbar的事件
            if (binding.toolbar.inView(ev)) {
                return super.dispatchTouchEvent(ev)
            }

            // 点击空白区域，输入框获取焦点并弹出键盘
            if (checkEmptyViewClick(x, y)) {
                showSoftInput(binding.content)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        messageUtils.createToast("销毁了.... ")
        super.onDestroy()
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

    /**
     * 解析配置文件
     */
    private fun initConfig() {
        intent.getStringExtra(ACTIVITY_CONFIG_NAME)?.also {
            try {
                config = Json.decodeFromString<Config>(it)
                if (config.mode == Mode.EDIT) {
                    if (config.id == null) {
                        error("id为空!!!，无法编辑")
                    }
                }
            } catch (e: Exception) {
                log.e("配置解析错误, msg: ${e.message}")
                config = Config()
            }
        } ?: run {
            config = Config()
        }
    }

    companion object {
        // 开启新activity
        fun  Activity.startEditJournalActivity(mode: Mode, id: Int? = null) {
            startActivity(Intent(this, EditJournalActivity::class.java).apply {
                putExtra(ACTIVITY_CONFIG_NAME, Json.encodeToString(Config(
                    mode,
                    id
                )))
            })
        }
    }
}