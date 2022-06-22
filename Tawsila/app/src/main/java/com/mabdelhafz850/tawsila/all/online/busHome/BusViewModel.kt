package com.mabdelhafz850.tawsila.ui.activity.all.online.busHome

import android.content.Context
import android.location.LocationManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mabdelhafz850.tawsila.ui.activity.response.UpcomingBusData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.shoohna.shoohna.util.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class BusViewModel : BaseViewModel() {

    var view1Visiable = MutableLiveData<Boolean>(false)
    var view2Visiable = MutableLiveData<Boolean>(false)
    var loader = MutableLiveData<Boolean>(false)
    var locationManager: LocationManager? = null
    var GpsStatus = false
    var sharedHelper: SharedHelper = SharedHelper()
    var users: MutableLiveData<List<UpcomingBusData>> = MutableLiveData()

    var dateFromSearch = MutableLiveData<String>("")
    var dateToSearch = MutableLiveData<String>("")

    fun getUpcoming(context: Context){
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {

            val response = service.upcomingBus("Bearer ${sharedHelper.getKey(context, "Token")}")

            withContext(Dispatchers.Main) {
                try {

                    if (response.isSuccessful && response.body()!!.error == 0) {
                       users.value = response.body()!!.data
                        Log.i("Response",response.body()!!.data.toString())
                        Toast.makeText(context, "Bus\n"+response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false

                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false

                }
            }
        }
    }

    fun applySearch(context: Context){
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {

            val response = service.searchSpecialTrip("Bearer ${sharedHelper.getKey(context, "Token")}",dateFromSearch.value!!,dateToSearch.value!!)

            withContext(Dispatchers.Main) {
                try {

                    if (response.isSuccessful && response.body()!!.error == 0) {
                        users.value = response.body()!!.data
                        Log.i("Response",response.body()!!.data.toString())
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false

                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false

                }
            }
        }
    }

//
//    fun goOnlineOffline(v: View)
//    {
//        if(CheckGpsStatus(v)) {
//            loader.value = true
//            val service = ApiClient.makeRetrofitServiceHome()
//            CoroutineScope(Dispatchers.IO).async {
//
//                val response = service.onlineOffline("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}")
//
//                withContext(Dispatchers.Main) {
//                    try {
//
//                        if (response.isSuccessful) {
//                            if (response.body()!!.message == "you are offline now") {
//                                view1Visiable.value = false
//                                view2Visiable.value = false
//                            } else if (response.body()!!.message == "you are online now") {
//                                view1Visiable.value = true
//                                view2Visiable.value = true
////                                fetchUsers(v)
//                                Log.i("Token", sharedHelper.getKey(v.rootView.context, "Token"))
//                            }
//                            Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
//                            loader.value = false
//
//                        } else {
//
//                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
//                        loader.value = false
//
//                    }
//                }
//            }
//        }else
//        {
//            Toast.makeText(v.rootView.context, "You need to open GPS First", Toast.LENGTH_SHORT).show()
//
//        }
//    }


//    private fun CheckGpsStatus(v:View) : Boolean {
//        locationManager = v.rootView.context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        assert(locationManager != null)
//        GpsStatus = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        return GpsStatus
//    }
}