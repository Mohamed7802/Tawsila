package com.example.taxina2020.ui.activity.all.notification11_1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.taxina2020.databinding.ItemNotificationBinding
import com.example.taxina2020.ui.activity.response.NotificationData

class NotificationRecyclerAdapter  (private var dataList: LiveData<List<NotificationData>>,
                                    private val context: Context?) : RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.value!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (context != null) {
            holder.bind(dataList.value!![position]  )
        }


    }


    class ViewHolder(private var binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationData ) {
            binding.model = item
            binding.executePendingBindings()

        }

    }

}