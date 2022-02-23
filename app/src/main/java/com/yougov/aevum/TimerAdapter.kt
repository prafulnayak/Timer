package com.yougov.aevum

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.TimeUnit


class TimerAdapter() : ListAdapter<TimerData, TimerAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(timerData: TimerData) {
            itemView.findViewById<TextView>(R.id.timerText).text = timerData.convertSecondsToHMmSs()
            Log.d("test", "${timerData.time}")
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TimerData>() {
        override fun areItemsTheSame(oldItem: TimerData, newItem: TimerData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimerData, newItem: TimerData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitTimerList(it: List<TimerData>) {
        submitList(it)
        notifyDataSetChanged()
    }
}

private fun TimerData.convertSecondsToHMmSs(): String {
    return String.format(
        "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
        TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(
                time
            )
        ),
        TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(
                time
            )
        )
    )
}
