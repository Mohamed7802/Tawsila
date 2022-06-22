package com.mabdelhafz850.tawsila.ui.activity.all.online.busSeat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.mabdelhafz850.tawsila.databinding.ItemImageSeatBinding
import com.mabdelhafz850.tawsila.ui.activity.response.ShowTripSeatsSeat

class BusSeatRecyclerAdapter (private var dataList: LiveData<List<ShowTripSeatsSeat>>,
                              private val context: Context? , private val busSeatViewModel: BusSeatViewModel) : RecyclerView.Adapter<BusSeatRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ItemImageSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.value!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (context != null) {
            holder.bind(dataList.value!![position] , busSeatViewModel , context )
        }


    }


    class ViewHolder(private var binding: ItemImageSeatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowTripSeatsSeat , busSeatViewModel:BusSeatViewModel , context:Context) {
            binding.model = item
            binding.executePendingBindings()
            binding.imageSeat.setOnClickListener {
                busSeatViewModel.blockSeat(context,item.id)
            }
        }

    }

}