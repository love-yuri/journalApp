package com.yuri.journal.fragment

import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yuri.journal.R
import com.yuri.journal.common.BaseFragment
import com.yuri.journal.common.log
import com.yuri.journal.databinding.WebdavConfigBinding
import com.yuri.journal.utils.ViewUtils.screenHeight
import com.yuri.journal.constants.SharedPreferencesConstant.SpType
import com.yuri.journal.constants.SharedPreferencesConstant.WebDavKey
import com.yuri.journal.constants.WebDavConfig.DB_FOLDER
import com.yuri.journal.constants.WebDavConfig.HOST
import com.yuri.journal.retrofit.WebDavRetrofit
import com.yuri.journal.retrofit.WebDavRetrofit.WebDavService
import com.yuri.journal.utils.MessageUtils.createToast
import com.yuri.journal.utils.SharedPreferencesUtils.getSp
import com.yuri.journal.utils.SharedPreferencesUtils.setSp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WebdavConfigFragment : BaseFragment<WebdavConfigBinding>() {

    private var account: String?
        get() = context?.getSp(SpType.WEBDAV, WebDavKey.ACCOUNT)
        set(value) = context?.setSp(SpType.WEBDAV, WebDavKey.ACCOUNT, value) ?: Unit

    private var password: String?
        get() = context?.getSp(SpType.WEBDAV, WebDavKey.PASSWORD)
        set(value) = context?.setSp(SpType.WEBDAV, WebDavKey.PASSWORD, value) ?: Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.account.editText?.setText(account)
        binding.password.editText?.setText(password)

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
            account = accountText.toString()
            password = passwordText.toString()
//            context?.createToast(getString(R.string.saveOk))

            lifecycleScope.launch {
                try {
                    val response = WebDavRetrofit.service.dir(
                        "/dav/$DB_FOLDER/",
                        "Basic ${Base64.encodeToString("$account:$password".toByteArray(), Base64.NO_WRAP)}",
                        "text/xml"
                    )

                    val responseBody = response
                    context?.createToast("发送成功: $responseBody")
                } catch (e: Exception) {
                    log.e("错误: $e")
                    context?.createToast("失败!! msg: ${e}")
                }

            }


        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}