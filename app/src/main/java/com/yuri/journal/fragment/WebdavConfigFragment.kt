package com.yuri.journal.fragment

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yuri.journal.R
import com.yuri.journal.common.BaseFragment
import com.yuri.journal.common.log
import com.yuri.journal.constants.GlobalSharedConstant
import com.yuri.journal.databinding.WebdavConfigBinding
import com.yuri.journal.utils.ViewUtils.screenHeight
import com.yuri.journal.constants.SharedPreferencesConstant.SpType
import com.yuri.journal.constants.SharedPreferencesConstant.WebDavKey
import com.yuri.journal.constants.WebDavConfig.DB_FOLDER
import com.yuri.journal.retrofit.WebDavRetrofit
import com.yuri.journal.utils.MessageUtils.createToast
import com.yuri.journal.utils.SharedPreferencesUtils.getSp
import com.yuri.journal.utils.SharedPreferencesUtils.setSp
import com.yuri.journal.utils.ViewUtils.s
import kotlinx.coroutines.launch



class WebdavConfigFragment : BaseFragment<WebdavConfigBinding>() {

    override val tagName: String get() = "WebdavConfigFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.account.editText?.setText(GlobalSharedConstant.account)
        binding.password.editText?.setText(GlobalSharedConstant.password)

        /**
         * 设置他的弹出高度
         */
        dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.also {
            val params = it.layoutParams as CoordinatorLayout.LayoutParams
            params.width = FrameLayout.LayoutParams.MATCH_PARENT
            params.height = FrameLayout.LayoutParams.MATCH_PARENT
            it.layoutParams = params
            BottomSheetBehavior.from(it).apply {
                val h = activity?.screenHeight ?: 1000
                isFitToContents = false
                peekHeight = (h * 0.4).toInt()
                expandedOffset = 200
            }
        }

        /**
         * 设置保存按钮事件
         */
        binding.save.setOnClickListener {
            val accountText = binding.account.editText?.text
            val passwordText = binding.password.editText?.text
            if (accountText.isNullOrEmpty()) {
                context?.createToast(getString(R.string.webdavUsernameEmpty))
                return@setOnClickListener
            }
            if (passwordText.isNullOrEmpty()) {
                context?.createToast(getString(R.string.webdavPasswordEmpty))
                return@setOnClickListener
            }
            GlobalSharedConstant.account = accountText.toString()
            GlobalSharedConstant.password = passwordText.toString()
//            context?.createToast(getString(R.string.saveOk))

            lifecycleScope.launch {
                try {

                    val fileList = WebDavRetrofit.dir(GlobalSharedConstant.account!!, GlobalSharedConstant.password!!)
                    val folder = fileList.find { it.isFolder && it.path?.endsWith("$DB_FOLDER/") ?: false }
                    if (folder == null) {
                        context?.createToast("${context?.s(R.string.webdavFolderEmptyError)}")
                    } else {
                        context?.createToast(context?.s(R.string.webdavAuthSuccess) ?: "认证成功!!")
                        this@WebdavConfigFragment.dismiss()
                    }
                } catch (e: Exception) {
                    log.e("错误: $e")
                    context?.createToast("${context?.s(R.string.webdavAuthError)} msg: ${e.message}")
                }
            }
        }
    }
}