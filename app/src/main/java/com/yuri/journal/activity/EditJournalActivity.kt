package com.yuri.journal.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.lifecycle.lifecycleScope
import com.yuri.journal.R
import com.yuri.journal.common.BaseActivity
import com.yuri.journal.common.log
import com.yuri.journal.constants.ActivityConstant.ACTIVITY_CONFIG_NAME
import com.yuri.journal.constants.GlobalSharedConstant
import com.yuri.journal.database.AppDatabase
import com.yuri.journal.database.entity.JournalEntity
import com.yuri.journal.databinding.ActivityEditJournalBinding
import com.yuri.journal.service.BackupDbService
import com.yuri.journal.utils.MessageUtils.createErrorDialog
import com.yuri.journal.utils.MessageUtils.createToast
import com.yuri.journal.utils.MessageUtils.notify
import com.yuri.journal.utils.StringUtils.parseJson
import com.yuri.journal.utils.TimeUtils
import com.yuri.journal.utils.ViewUtils.inView
import com.yuri.journal.utils.ViewUtils.showSoftInput
import com.yuri.journal.viewModel.JournalViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class EditJournalActivity : BaseActivity<ActivityEditJournalBinding>() {

    private val viewModel: JournalViewModel = GlobalSharedConstant.journalViewModel
    private var journalEntity: JournalEntity? = null
    private lateinit var config: Config

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

    private var time: String
        get() = binding.time.text.toString()
        set(value) {
            binding.time.text = value
        }

    private var textCount: String
        get() = binding.textCount.text.toString()
        set(value) {
            binding.textCount.text = value
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        window.statusBarColor = getColor(R.color.toolbarColor)

        // 初始化界面
        initView()

        // 初始化配置
        initConfig()
    }

    private fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.content.changeCallBack = { text ->
            textCount = "${text.length}字"
        }
    }

    override fun onDestroy() {
        autoSave()
        super.onDestroy()
    }

    /**
     * 退出执行自动保存任务
     */
    private fun autoSave() {
        when(config.mode) {
            Mode.CREATE -> {
                if(config.id == null && content.isNotEmpty()) {
                    saveJournal()
                }
            }
            Mode.EDIT -> {
                if(config.id != null && content.isNotEmpty()) {
                    updateJournal()
                } else {
                    createErrorDialog("数据异常!!!")
                    notify("数据保存异常")
                }
            }
        }
    }

    /**
     * 保存数据
     */
    private fun saveJournal() {
        viewModel.insert(JournalEntity(
            title = title,
            content = content
        ))
        startService(Intent(this, BackupDbService::class.java))
    }

    /**
     * 更新数据
     */
    private fun updateJournal() {
        journalEntity?.also {
            if (it.content == content && it.title == title) {
                return
            }
            viewModel.update(it.copy(
                title = title,
                content = content,
                updateTime = TimeUtils.now
            ))
            startService(Intent(this, BackupDbService::class.java))
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

    /**
     * 检查有没有点击到空白区域
     */
    private fun checkEmptyViewClick(x: Int, y: Int): Boolean {
        for (i in 0 until binding.nestedScrollView.childCount) {
            val child = binding.nestedScrollView.getChildAt(i)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete -> {
                if(config.mode == Mode.EDIT && config.id != null) {
                    viewModel.delete(config.id!!)
                    createToast("删除成功")
                    finish()
                } else {
                   finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 解析配置文件
     */
    private fun initConfig() {
        intent.getStringExtra(ACTIVITY_CONFIG_NAME)?.also {
            try {
                config = it.parseJson()
                if (config.mode == Mode.EDIT) {
                    if (config.id == null) {
                        error("id为空!!!，无法编辑")
                    }
                    val dao = AppDatabase.journalDao
                    lifecycleScope.launch {
                        dao.getById(config.id!!)?.also { journal ->
                            journalEntity = journal
                            withContext(Dispatchers.Main) {
                                title = journal.title ?: ""
                                content = journal.content
                                time = journal.updateTime
                                textCount = "${content.length}字"
                            }
                        }
                    }
                } else {
                    time = TimeUtils.now
                    textCount = "0字"
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
        fun Activity.startEditJournalActivity(mode: Mode, id: Int? = null) {
            startActivity(Intent(this, EditJournalActivity::class.java).apply {
                putExtra(ACTIVITY_CONFIG_NAME, Json.encodeToString(Config(
                    mode,
                    id
                )))
            })
        }
    }
}