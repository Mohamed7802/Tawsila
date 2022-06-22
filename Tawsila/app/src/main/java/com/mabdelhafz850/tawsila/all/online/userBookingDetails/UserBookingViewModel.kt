package com.mabdelhafz850.tawsila.ui.activity.all.online.userBookingDetails

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mabdelhafz850.tawsila.ui.activity.response.UserBookingDetailsData
import com.mabdelhafz850.tawsila.ui.activity.response.UserSeatsData
import com.mabdelhafz850.tawsila.ui.activity.response.UserSelectedSeatData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.shoohna.shoohna.util.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class UserBookingViewModel : BaseViewModel() {

    var loader = MutableLiveData<Boolean>(false)
    var sharedHelper: SharedHelper = SharedHelper()

    var tripId = MutableLiveData<Int>(0)
    var userId = MutableLiveData<Int>(0)

    var name = MutableLiveData<String>("")
    var image = MutableLiveData<String>("")
    var price = MutableLiveData<String>("")
    var locationFrom = MutableLiveData<String>("")
    var locationTo = MutableLiveData<String>("")
    var date = MutableLiveData<String>("")
    var users : MutableLiveData<List<UserBookingDetailsData>> = MutableLiveData()

    fun getUserSeatData(context: Context)
    {
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {

            Log.i("TripIdBooking",tripId.value!!.toString())
            Log.i("UserIdBooking",userId.value!!.toString())
//            val response = service.showUserSeat("Bearer ${sharedHelper.getKey(context, "Token")}" , tripId.value!! , userId.value!!)
            val response = service.showSelectedUserSeat("Bearer ${sharedHelper.getKey(context, "Token")}" , tripId.value!! ,userId.value!! )

            withContext(Dispatchers.Main) {
                try {

                    if (response.isSuccessful && response.body()!!.error == 0) {
                        Log.i("Respo",response.body()!!.data.toString())
                        users.value = response.body()!!.data
                        name.value = response.body()!!.details.first_name+" "+response.body()!!.details.last_name
                        image.value = response.body()!!.details.image
                        price.value = response.body()!!.details.price
                        locationFrom.value = response.body()!!.details.from_area
                        locationTo.value = response.body()!!.details.to_area
                        date.value = response.body()!!.details.date
                        sharedHelper.putKey(context,"NotificationIdTrip","0")
                        sharedHelper.putKey(context,"NotificationUserID","0")
                        Log.i("ResponseNeed1",response.body()!!.data.toString())
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false

                    } else {
                        Toast.makeText(context, "Exception 1\n"+response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false
                        Log.i("ResponseNeed2",response.body()!!.data.toString())

                    }
                } catch (e: Exception) {
//                    Toast.makeText(context, "Exception 2\n"+response.body()!!.message, Toast.LENGTH_SHORT).show()
                    Log.i("ExceptionUserSeat",e.message.toString())
                    loader.value = false

                }
            }
        }
    }

    fun confirmSeat(v:View)
    {
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {

//            Log.i("TripIdBooking",tripId.value!!.toString())
//            Log.i("UserIdBooking",userId.value!!.toString())
//            val response = service.showUserSeat("Bearer ${sharedHelper.getKey(context, "Token")}" , tripId.value!! , userId.value!!)
            val response = service.acceptSpecialTripSeat("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}" , userId.value!! , tripId.value!!)

            withContext(Dispatchers.Main) {
                try {

                    if (response.isSuccessful && response.body()!!.error == 0) {

                        Log.i("ResponseNeed1",response.body()!!.message.toString())
                        Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false
                        getUserSeatData(v.rootView.context)
                    } else {
                        Toast.makeText(v.rootView.context, "Exception 1 confirm\n"+response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false

                    }
                } catch (e: Exception) {
                    Toast.makeText(v.rootView.context, "Exception 2 confirm\n"+response.body()!!.message, Toast.LENGTH_SHORT).show()
                    loader.value = false

                }
            }
        }
    }

    fun rejectSeat(v: View)
    {
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {

//            Log.i("TripIdBooking",tripId.value!!.toString())
//            Log.i("UserIdBooking",userId.value!!.toString())
//            val response = service.showUserSeat("Bearer ${sharedHelper.getKey(context, "Token")}" , tripId.value!! , userId.value!!)
            val response = service.refusedSpecialTripSeat("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}" , userId.value!! , tripId.value!!)

            withContext(Dispatchers.Main) {
                try {

                    if (response.isSuccessful && response.body()!!.error == 0) {

                        Log.i("ResponseNeed1",response.body()!!.message.toString())
                        Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false
                        getUserSeatData(v.rootView.context)

                    } else {
                        Toast.makeText(v.rootView.context, "Exception 1 reject\n"+response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false

                    }
                } catch (e: Exception) {
                    Toast.makeText(v.rootView.context, "Exception 2 reject\n"+response.body()!!.message, Toast.LENGTH_SHORT).show()
                    loader.value = false

                }
            }
        }
    }

}