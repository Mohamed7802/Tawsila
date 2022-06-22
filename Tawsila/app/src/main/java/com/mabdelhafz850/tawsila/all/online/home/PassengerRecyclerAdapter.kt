package com.mabdelhafz850.tawsila.ui.activity.all.online.home

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.databinding.LayoutProcessOneBinding
import com.mabdelhafz850.tawsila.ui.activity.response.DriverRequestedTripData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.google.android.gms.maps.GoogleMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception

class PassengerRecyclerAdapter (private var dataList: LiveData<List<DriverRequestedTripData>>,
                                private val context: Context?,var homeViewModel: HomeViewModel ,
                                var view:View ,
                                var recyclerView: RecyclerView ,
                                var map:GoogleMap) : RecyclerView.Adapter<PassengerRecyclerAdapter.ViewHolder>() {

    var sharedHelper = SharedHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutProcessOneBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.value!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList.value!![position] , homeViewModel , view , recyclerView , map , context , sharedHelper)
    }


    class ViewHolder(private var binding: LayoutProcessOneBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DriverRequestedTripData , homeViewModel: HomeViewModel , v:View , recyclerView: RecyclerView , map:GoogleMap,context: Context? , sharedHelper: SharedHelper) {
            binding.model = item
            binding.executePendingBindings()
//            binding.distanceTxtViewId.text = homeViewModel.getGoogleApiDirectionData2(context,item.start_location,item.end_location)
            binding.btnAccept.setOnClickListener {
                try {
//                var startLocation = item.start_lat+","+item.start_long
//                var endLocation = item.end_lat+","+item.end_long
                    homeViewModel.acceptedTripId.value = item.id
                    sharedHelper.putKey(context!!,"ChatTripId",item.id.toString())
                    sharedHelper.putKey(context,"ChatUserId",item.user_id)
                    sharedHelper.putKey(context,"ChatUserPhone",item.phone)
                    sharedHelper.putKey(context,"ChatUserName",item.first_name+" "+item.last_name)
                    if(item.image.isNotEmpty()) {
                        homeViewModel.driveAcceptTrip(v, recyclerView, item.start_location, item.end_location, item.first_name, item.last_name, item.image, item.price, item.user_id)
                        sharedHelper.putKey(context,"ChatUserImage",item.image)

                    }else
                    {
                        homeViewModel.driveAcceptTrip(v, recyclerView, item.start_location, item.end_location, item.first_name, item.last_name, "", item.price, item.user_id)
                        sharedHelper.putKey(context,"ChatUserImage","")

                    }
//                Log.i("DirctionsRecycler",startLocation+"||"+endLocation)
                    homeViewModel.drawDirections(item.start_lat.toDouble(), item.start_long.toDouble(), item.end_lat.toDouble()
                            , item.end_long.toDouble(), map,context!!)
                }catch (e:Exception)
                {
                    Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
                }
            }
        }


        fun getGoogleApiDirectionDistance(context: Context , origin:String , destination:String)
        {
//            loader.value = true
            val service = ApiClient.makeGoogleDirectionsAPISericve()
            CoroutineScope(Dispatchers.IO).async {
                val response = service.getDistanceAndTimeGoogleAPI(origin,destination,context.getString(R.string.google_map_key),"driving")
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful && response.body()!!.status == "OK") {
//                            distanceAPI.value = response.body()!!.routes[0].legs[0].distance.text
//                            durationAPI.value =  response.body()!!.routes[0].legs[0].duration.text
//                        Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
//                            loader.value = false



                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
//                            loader.value = false
                            Log.i("ExceptionDistanceAPI1",response.message())

                        }
                    }catch (e:Exception)
                    {
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
//                        loader.value = false
                        Log.i("ExceptionDistanceAPI2",e.message.toString())

                    }
                }
            }
        }

    }

}