package com.mabdelhafz850.tawsila.ui.activity.all.online.busSeat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mabdelhafz850.tawsila.ui.activity.response.ShowTripSeatsSeat
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.shoohna.shoohna.util.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class BusSeatViewModel :BaseViewModel() {

    var loader = MutableLiveData<Boolean>(false)
    var sharedHelper: SharedHelper = SharedHelper()

    var startLocation = MutableLiveData<String>("")
    var endLocation = MutableLiveData<String>("")
    var carName = MutableLiveData<String>("")
    var date = MutableLiveData<String>("")
    var fromTime = MutableLiveData<String>("")
    var toTime = MutableLiveData<String>("")
    var price = MutableLiveData<String>("")
    var plate = MutableLiveData<String>("")
    var seats: MutableLiveData<List<ShowTripSeatsSeat>> = MutableLiveData()
    var notifyRecycler = MutableLiveData<Boolean>(false)

    var tripId = MutableLiveData<Int>(0)
    fun getSeats(context: Context)
    {
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.showTripSeats("Bearer ${sharedHelper.getKey(context, "Token")}",tripId.value!!) // TODO change trip id
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body()!!.error == 0) {
                        seats.value = response.body()!!.data.seats
                        loader.value = false
                        startLocation.value = response.body()!!.data.start_location
                        endLocation.value = response.body()!!.data.end_location
                        carName.value = response.body()!!.data.car_name
                        date.value = response.body()!!.data.date
                        fromTime.value = response.body()!!.data.from_time
                        toTime.value = response.body()!!.data.to_time
                        price.value = response.body()!!.data.price
                        plate.value = response.body()!!.data.plate

                    } else {
                        Toast.makeText(context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                        loader.value = false

                    }
                }catch (e:Exception)
                {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false
                }
            }
        }
    }


    fun blockSeat(context: Context , seatId:Int)
    {
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.blockTrip("Bearer ${sharedHelper.getKey(context, "Token")}",tripId.value!!,seatId) // TODO change trip id
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body()!!.error == 0) {

                        loader.value = false
                        Toast.makeText(context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                        getSeats(context)
                        notifyRecycler.value = true
                    } else {
                        Toast.makeText(context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                        loader.value = false

                    }
                }catch (e:Exception)
                {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false
                }
            }
        }
    }



}