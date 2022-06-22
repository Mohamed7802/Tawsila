package com.mabdelhafz850.tawsila.ui.activity.all.online.createRide

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.mabdelhafz850.tawsila.databinding.LocationRideItemAreaBinding
import com.mabdelhafz850.tawsila.databinding.LocationRideItemBinding
import com.mabdelhafz850.tawsila.ui.activity.response.AllCityData
import com.mabdelhafz850.tawsila.ui.activity.response.AreaByIdData
import com.mabdelhafz850.tawsila.ui.activity.response.ShowAreaCityData

class LocationAreaRecyclerAdapter(private var dataList: LiveData<List<AreaByIdData>>,
                                  private val context: Context?,
                                  private var createRideViewModel: CreateRideViewModel,
                                  var locationType: String,
                                  var dialog: Dialog?,
                                  var locationRideViewModel: LocationAreaViewModel) : RecyclerView.Adapter<LocationAreaRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LocationRideItemAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.value!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (context != null) {
            holder.bind(dataList.value!![position] , createRideViewModel , context , locationType , dialog ,locationRideViewModel)
        }
    }
    class ViewHolder(private var binding: LocationRideItemAreaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AreaByIdData, createRideViewModel: CreateRideViewModel, context: Context?, locationType: String, dialog: Dialog?, locationRideViewModel:LocationAreaViewModel) {
            binding.model = item
            binding.executePendingBindings()
            binding.nameId.setOnClickListener {
//                if (dialogType == "cityDialog") {
                    if (locationType == "locationFrom") {
//                        createRideViewModel.locationFrom.value = item.id.toString()
                        createRideViewModel.locationFromNameCity.value = item.name
                        createRideViewModel.locationFrom.value = item.city_id
                        locationRideViewModel.cityFromId.value = item.city_id.toInt()
//                        Toast.makeText(context, "Selected ID From " + item.id, Toast.LENGTH_SHORT).show()

//                        locationRideViewModel.loadAreaFrom(context!!)
                        dialog?.dismiss()
                    } else if (locationType == "locationTo") {
//                        createRideViewModel.locationTo.value = item.id.toString()
                        createRideViewModel.locationToNameCity.value = item.name
                        createRideViewModel.locationTo.value = item.city_id

                        locationRideViewModel.cityToId.value = item.city_id.toInt()
//                        locationRideViewModel.loadAreaTo(context!!)
//                        Toast.makeText(context, "Selected ID To " + item.id, Toast.LENGTH_SHORT).show()

                        dialog?.dismiss()

                    }
//                    Toast.makeText(context, "Selected Address " + item.name, Toast.LENGTH_SHORT).show()
//                }
//                if (dialogType == "areaDialog")
//                {
//                    if (locationType == "locationFrom") {
//                        createRideViewModel.locationFrom.value = item.id.toString()
//                        createRideViewModel.locationFromNameArea.value = item.name
//
//                        dialog?.dismiss()
//                    } else if (locationType == "locationTo") {
//                        createRideViewModel.locationTo.value = item.id.toString()
//                        createRideViewModel.locationToNameArea.value = item.name
//                        dialog?.dismiss()
//
//                    }
//                }
            }
        }

    }

}