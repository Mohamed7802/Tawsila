package com.mabdelhafz850.tawsila.ui.activity.all.online.userBookingDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.databinding.ItemUserSeatBinding
import com.mabdelhafz850.tawsila.ui.activity.response.UserBookingDetailsData
import com.mabdelhafz850.tawsila.ui.activity.response.UserSeatsData
import com.mabdelhafz850.tawsila.ui.activity.response.UserSelectedSeatData

class UserBookingRecyclerAdapter (private var dataList: LiveData<List<UserBookingDetailsData>>,
                                  private val context: Context? , private val userId:String) : RecyclerView.Adapter<UserBookingRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.value!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList.value!![position] , userId )
    }


    class ViewHolder(private var binding: ItemUserSeatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserBookingDetailsData , userId:String) {
            binding.model = item
            binding.executePendingBindings()
            if(item.user_id == userId)
            {
                binding.imageSeat.setImageResource(R.drawable.seat_yellow)
            }
            else
            {
                binding.imageSeat.setImageResource(R.drawable.seat_hint)

            }
        }

    }

}