package com.mabdelhafz850.tawsila.ui.activity.all.online.createRide

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.ui.activity.response.AllCityData
import com.mabdelhafz850.tawsila.ui.activity.response.AreaByIdData
import com.mabdelhafz850.tawsila.ui.activity.response.ShowAreaCityData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.shoohna.shoohna.util.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class LocationRideViewModel : BaseViewModel() {

    var loader = MutableLiveData<Boolean>(false)
    var sharedHelper: SharedHelper = SharedHelper()
    var citys: MutableLiveData<List<ShowAreaCityData>> = MutableLiveData()
    var areasFrom: MutableLiveData<List<AreaByIdData>> = MutableLiveData()
//    var areasFromLive : LiveData<List<AreaByIdData>> = areasFrom
    var areasTo: MutableLiveData<List<AreaByIdData>> = MutableLiveData()
//    var areasToLive : LiveData<List<AreaByIdData>> = areasTo
    var cityFromId = MutableLiveData<Int>(0)
    var cityToId = MutableLiveData<Int>(0)
    var areaId = MutableLiveData<Int>(0)

    fun loadCityFrom(context: Context, countryFromIdSpinner: Int)
    {
        if(countryFromIdSpinner == 0){
            Toast.makeText(context,context.resources.getString(R.string.select_country),Toast.LENGTH_SHORT).show()
            return
        }
        else {
            loadCity(context,countryFromIdSpinner)
        }
    }

    fun loadCityTo(context: Context, countryToIdSpinner: Int)
    {
        if(countryToIdSpinner == 0){
            Toast.makeText(context,context.resources.getString(R.string.select_country),Toast.LENGTH_SHORT).show()
            return
        }
        else {
            loadCity(context,countryToIdSpinner)
        }
    }

    fun loadCity(context:Context ,countryCity : Int){
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.showAreaCity("Bearer ${sharedHelper.getKey(context, "Token")}", countryCity)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body()!!.error == 0) {
                        loader.value = false
                        citys.value = response.body()!!.data

                    } else {
                        Toast.makeText(context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                        loader.value = false

                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false
                }
            }
        }
    }
    fun loadAreaFrom(context: Context)
    {
        Log.i("Load Area From","1")
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.showAreaById("Bearer ${sharedHelper.getKey(context, "Token")}" ,  cityFromId.value!!)
            withContext(Dispatchers.Main) {
                try {
                    Log.i("Load Area From","2")

                    if (response.isSuccessful && response.body()!!.error == 0) {
                        loader.value = false
                        areasFrom.value = response.body()!!.data
                        Log.i("Load Area Value",areasFrom.value.toString())
                        Log.i("Load Area From","3")

                        Log.i("Area From",response.body()!!.data.toString())

                    } else {
                        Toast.makeText(context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                        loader.value = false
                        Log.i("Load Area From","4")

                    }
                }catch (e:Exception)
                {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false
                    Log.i("Load Area From","5")

                }
            }
        }
    }

    fun loadAreaTo(context: Context)
    {
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.showAreaById("Bearer ${sharedHelper.getKey(context, "Token")}" ,  cityToId.value!!)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body()!!.error == 0) {
                        loader.value = false
                        areasTo.value = response.body()!!.data
                        Log.i("Area to",response.body()!!.data.toString())
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