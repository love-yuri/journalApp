package com.yuri.journal.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.R
import androidx.recyclerview.widget.RecyclerView
import com.yuri.journal.database.entity.JournalEntity
import com.yuri.journal.databinding.JournalListCardBinding
import com.yuri.journal.viewModel.JournalViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.yuri.journal.common.BaseMediatorLiveData
import com.yuri.journal.constants.GlobalSharedConstant
import com.yuri.journal.utils.MessageUtils.createToast
import com.yuri.journal.utils.StringUtils.ellipsize
import com.yuri.journal.utils.TimeUtils.formatDateTime

/**
 * 重写RecycleView 实现特定的功能和数据展示
 */
class JournalList @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.recyclerViewStyle
): RecyclerView(context, attrs, defStyleAttr) {
    private val liveData: BaseMediatorLiveData<MutableList<JournalEntity>> = BaseMediatorLiveData()
    private val callBackFun: MutableList<(Int) -> Unit> = mutableListOf()
    private val viewModel: JournalViewModel = GlobalSharedConstant.journalViewModel

    private val data: List<JournalEntity>
        get() = liveData.value ?: run { listOf() }

    init {
        addItemDecoration(JournalListDecoration())
        layoutManager = GridLayoutManager(context, 1)
        adapter = JournalListAdapter()
        liveData.addSource(viewModel.data) { items ->
            liveData.value = items
            (adapter as? JournalListAdapter)?.also {
                it.submitList(items.toList())
            }
        }
    }

    /**
     * 点击事件回调
     * 可以添加回调
     */
    private fun itemClickCallBack(id: Int) {
        callBackFun.forEach { it(id) }
    }

    /**
     * 添加回调函数
     */
    fun addCallBack(f: (Int) -> Unit) {
        callBackFun.add(f)
    }

    /**
     * 适配器
     */
     private inner class JournalListAdapter: ListAdapter<JournalEntity, Holder>(
        object : DiffUtil.ItemCallback<JournalEntity>() {
            override fun areItemsTheSame(oldItem: JournalEntity, newItem: JournalEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: JournalEntity, newItem: JournalEntity): Boolean {
                return oldItem == newItem
            }
        }
     ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(JournalListCardBinding.inflate(LayoutInflater.from(parent.context)))
        }

        override fun getItemCount(): Int {
            return data.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: Holder, position: Int) {
            val value = data[position]
            val binding = holder.binding
            binding.root.apply {
                setOnClickListener {
                    itemClickCallBack(value.id!!)
                }
            }
            binding.headTime.text = value.createTime.formatDateTime()
            binding.time.text = value.updateTime
            binding.desc.text = value.content.substring(0, value.content.length.coerceAtMost(30))
//            binding.desc.ellipsize(10)

        }
    }

    /**
     * Holder 类
     */
     private inner class Holder(
        val binding: JournalListCardBinding
    ) : ViewHolder(binding.root)


    /**
     * 上下间隔类
     */
    private inner class JournalListDecoration: ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            with(outRect) {
                bottom = 20
            }
        }
    }
}


