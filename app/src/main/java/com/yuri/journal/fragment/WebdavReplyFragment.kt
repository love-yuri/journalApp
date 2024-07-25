package com.yuri.journal.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.yuri.journal.R
import com.yuri.journal.common.BaseFragment
import com.yuri.journal.common.log
import com.yuri.journal.constants.GlobalSharedConstant
import com.yuri.journal.databinding.WebdavReplyBinding
import com.yuri.journal.retrofit.WebDavRetrofit
import com.yuri.journal.utils.FileUtils
import com.yuri.journal.utils.MessageUtils.createDialog
import com.yuri.journal.utils.MessageUtils.createToast
import com.yuri.journal.utils.StringUtils.baseFileName
import com.yuri.journal.utils.TimeUtils
import com.yuri.journal.utils.ViewUtils.s
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class WebdavReplyFragment : BaseFragment<WebdavReplyBinding>() {
    override val tagName: String get() = "WebdavReplyFragment"

    private lateinit var files: List<WebDavRetrofit.WebdavFile>

    @SuppressLint("SdCardPath")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()

        binding.list.apply {
            setOnItemClickListener { _, _, position, _ ->
                context.createDialog("Tips", context.s(R.string.replyHistory)) { _, _ ->
                    val file = files[position]
                    lifecycleScope.launch {
                        val baseName = "${TimeUtils.now}-${file.fileName?.baseFileName()}"
                        val newFile = File("${GlobalSharedConstant.tempDir.path}/$baseName")
                        val res = WebDavRetrofit.download(fileName = file.fileName!!, destination = newFile)
                        if (res) {
                            context.createToast("恢复成功!!!")
                            val tempDir = File("${GlobalSharedConstant.tempDir.path}/$baseName/")
                            FileUtils.decompressZip(newFile.path, tempDir.path)
                        }
                    }
                }
            }
        }
    }

    private fun setData() {
        lifecycleScope.launch {
            files = WebDavRetrofit.dir().filter { it.isFile && !it.fileName.isNullOrEmpty() }
            binding.list.apply {
                withContext(Dispatchers.Main) {
                    adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, files.map { it.fileName })
                }
            }
        }
    }

}