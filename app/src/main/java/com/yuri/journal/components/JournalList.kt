package com.yuri.journal.components

import android.annotation.SuppressLint
import android.content.Context
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yuri.journal.database.entity.JournalEntity
import com.yuri.journal.databinding.JournalListCardBinding

class JournalListAdapter(
    private val data: List<JournalEntity>
): RecyclerView.Adapter<JournalListAdapter.Holder>() {
    class Holder(
        val binding: JournalListCardBinding
    ) : RecyclerView.ViewHolder(binding.root)

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
        binding.text.text = "$position: $value"
    }
}