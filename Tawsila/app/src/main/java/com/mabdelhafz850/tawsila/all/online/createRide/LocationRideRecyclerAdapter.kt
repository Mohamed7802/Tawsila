package com.mabdelhafz850.tawsila.ui.activity.all.online.createRide

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.mabdelhafz850.tawsila.databinding.LocationRideItemBinding
import com.mabdelhafz850.tawsila.ui.activity.response.AllCityData
import com.mabdelhafz850.tawsila.ui.activity.response.ShowAreaCityData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper

class LocationRideRecyclerAdapter(private var dataList: LiveData<List<ShowAreaCityData>>,
                                  private val context: Context?,
                                  private var createRideViewModel: CreateRideViewModel,
                                  var locationType: String,
                                  var dialog: Dialog?,
                                  var locationRideViewModel: LocationRideViewModel) : RecyclerView.Adapter<LocationRideRecyclerAdapter.ViewHolder>() {

    val sharedHelper = SharedHelper()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LocationRideItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.value!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (context != null) {
            holder.bind(dataList.value!![position] ,sharedHelper,context,locationType,dialog)
        }
    }
    class ViewHolder(private var binding: LocationRideItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowAreaCityData, sharedHelper: SharedHelper, context: Context, locationType: String, dialog: Dialog?) {
            binding.model = item
            binding.executePendingBindings()
            binding.nameId.setOnClickListener {
                if (locationType == "locationFrom") {
                    sharedHelper.putKey(context, "CountrySelectedTripFromId", item.area_id.toString())

//                    Toast.makeText(context,"Country Selected ID: "+item.area_id+" \n"+item.area_name+"\n"+item.city_name,Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }else if (locationType == "locationTo") {
                    sharedHelper.putKey(context, "CountrySelectedTripToId", item.area_id.toString())
//                    Toast.makeText(context,"Country Selected : "+item.name,Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()

                }
            }
        }

    }

}