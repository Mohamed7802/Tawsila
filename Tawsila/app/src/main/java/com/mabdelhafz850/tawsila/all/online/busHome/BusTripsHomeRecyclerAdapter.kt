package com.mabdelhafz850.tawsila.ui.activity.all.online.busHome

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.databinding.BusRideItemRowBinding
import com.mabdelhafz850.tawsila.ui.activity.response.UpcomingBusData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper

class BusTripsHomeRecyclerAdapter (private var dataList: LiveData<List<UpcomingBusData>>,
                                   private val context: Context?) : RecyclerView.Adapter<BusTripsHomeRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BusRideItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.value!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList.value!![position] , context)
    }


    class ViewHolder(private var binding: BusRideItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var sharedHelper = SharedHelper()
        fun bind(item: UpcomingBusData , context: Context?) {
            binding.model = item
            binding.executePendingBindings()
            binding.mainRideConstraintId.setOnClickListener {
                try {
//                val action = OnlineAsTaxi12Directions
//                Navigation.findNavController(it).navigate(action)

                    sharedHelper.putKey(context!!,"BusTripId",item.id.toString())
                    sharedHelper.putKey(context,"BusTripLocationFrom",item.fromcity_name)
                    sharedHelper.putKey(context,"BusTripLocationTo",item.tocity_name )
//                    sharedHelper.putKey(context,"BusTripLocationTo",item.tocity_name + " - " + item.to_area)
                    sharedHelper.putKey(context,"BusTripDate",item.date)

                    Navigation.findNavController(it).navigate(R.id.action_onlineAsTaxi12_to_rideDetails)

//                    val action = OnlineAsTaxi12Directions.actionOnlineAsTaxi12ToRideDetails22(item.id, item.fromcity_name + " - " + item.from_area, item.tocity_name + " - " + item.to_area, item.date)
//                    Log.i("Trip Id", item.id.toString())
//
//                    Navigation.findNavController(it).navigate(action)
                }catch (e:Exception)
                {
                    Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}