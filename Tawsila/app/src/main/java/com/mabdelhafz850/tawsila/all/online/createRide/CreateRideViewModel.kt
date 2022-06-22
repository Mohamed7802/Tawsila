package com.mabdelhafz850.tawsila.ui.activity.all.online.createRide

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.ui.activity.response.AllCityData
import com.mabdelhafz850.tawsila.ui.activity.response.AreaByIdData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.google.android.material.snackbar.Snackbar
import com.shoohna.shoohna.util.base.BaseViewModel
import kotlinx.coroutines.*

class CreateRideViewModel : BaseViewModel() {

    var loader = MutableLiveData<Boolean>(false)
    var sharedHelper: SharedHelper = SharedHelper()

    var locationFrom = MutableLiveData<String>("")
    var locationTo = MutableLiveData<String>("")

    var locationFromNameCity = MutableLiveData<String>()
    var locationToNameCity = MutableLiveData<String>()


    var locationFromNameArea = MutableLiveData<String>()
    var locationToNameArea = MutableLiveData<String>()


    var date = MutableLiveData<String>("")
    var fromTime = MutableLiveData<String>("")
    var toTime = MutableLiveData<String>("")
    var price = MutableLiveData<String>("")
    var noOfSeats = MutableLiveData<String>("")


    var areas: MutableLiveData<List<AreaByIdData>> = MutableLiveData()
    var areaId = MutableLiveData<Int>(0)
    var countriesListModel = ArrayList<CountriesSpinnerModel>()
    var countriesList = ArrayList<String>()
//    var countriesListId = ArrayList<Int>()
    var countryFromIdSpinner = MutableLiveData<Int>(0)
    var countryToIdSpinner = MutableLiveData<Int>(0)

    var areaFromUser = MutableLiveData<String>("")

    var insertCityId = MutableLiveData<Int>(0)
    fun addSpecialTrip(v:View,locationFromET:EditText , locationToET:EditText , dateET:EditText,fromTimeET:EditText , toTimeET:EditText , priceET:EditText,noOfSeatsET:EditText)
    {


//        Log.i("EmptyLocationFrom" , locationFrom.value)
//        Log.i("EmptyLocationTo" , locationTo.value)
//        Log.i("EmptyDate" , date.value)
//        Log.i("EmptyFromTime" , fromTime.value)
//        Log.i("EmptyToTime" , toTime.value)
//        Log.i("EmptyPrice" , price.value)
//        Log.i("EmptyNoOfSeats" , noOfSeats.value)
//            if (locationFrom.value?.isEmpty()!! ||
//                    locationTo.value?.isEmpty()!! ||
//                    date.value?.isEmpty()!! ||
//                    fromTime.value?.isEmpty()!! || toTime.value?.isEmpty()!! ||
//                    price.value?.isEmpty()!! || noOfSeats.value?.isEmpty()!!) {
//                Toast.makeText(v.rootView.context, "All Field Required !", Toast.LENGTH_SHORT).show()
//            }
                try {
            if(locationFromNameCity.value?.isEmpty()!!) {
                locationFromET.error = v.rootView.context.resources.getString(R.string.locationRequied)
            }
            if(locationToNameCity.value?.isEmpty()!!)
                locationToET.error = v.rootView.context.resources.getString(R.string.locationRequied)
            if(date.value?.isEmpty()!!)
                dateET.error = v.rootView.context.resources.getString(R.string.dateRequied)
            if(fromTime.value?.isEmpty()!!)
                fromTimeET.error = v.rootView.context.resources.getString(R.string.timeRequied)
            if(toTime.value?.isEmpty()!!)
                toTimeET.error = v.rootView.context.resources.getString(R.string.timeRequied)
            if(price.value?.isEmpty()!!)
                priceET.error = v.rootView.context.resources.getString(R.string.priceRequied)
            if(noOfSeats.value?.isEmpty()!!)
                noOfSeatsET.error = v.rootView.context.resources.getString(R.string.noOfSeatsRequied)
            if( sharedHelper.getKey(v.rootView.context,"LatStart")!!.isEmpty())
                Toast.makeText(v.rootView.context,v.rootView.context.resources.getString(R.string.startLocationRequired) , Toast.LENGTH_SHORT).show()
            if(sharedHelper.getKey(v.rootView.context,"LatEnd")!!.isEmpty())
                Toast.makeText(v.rootView.context,v.rootView.context.resources.getString(R.string.endLocationRequired) , Toast.LENGTH_SHORT).show()
            else {
                Log.i("Da1",locationFrom.value.toString())
                Log.i("Da2",locationTo.value.toString())
                Log.i("Da3",date.value.toString())
                loader.value = true
                val service = ApiClient.makeRetrofitServiceHome()
                CoroutineScope(Dispatchers.IO).async {
                    val response = service.addSpecialTrip("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}",
                            locationFrom.value!!.toInt(),
                            locationTo.value!!.toInt(),
                            noOfSeats.value!!.toInt(),
                            date.value!!,
                            fromTime.value!!,
                            toTime.value!!,
                            price.value!!.toInt(),
                            sharedHelper.getKey(v.rootView.context,"LatStart")!!,
                            sharedHelper.getKey(v.rootView.context,"LonStart")!!,
                            sharedHelper.getKey(v.rootView.context,"LatEnd")!!,
                            sharedHelper.getKey(v.rootView.context,"LonEnd")!!)
                    withContext(Dispatchers.Main) {
                        try {
                            Log.i("response1", response.message())
                            if (response.isSuccessful && response.body()!!.error == 0) {
                                loader.value = false
                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                Log.i("response2", response.message())
                                Log.i("response2", response.body()!!.message)
                                sharedHelper.putKey(v.rootView.context, "CountrySelectedTripFromId", "")
                                sharedHelper.putKey(v.rootView.context, "CountrySelectedTripToId", "")
                                sharedHelper.putKey(v.rootView.context, "LatStart", "")
                                sharedHelper.putKey(v.rootView.context, "LatEnd", "")

                            } else {
                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                loader.value = false
                                Log.i("response3", response.message())

                            }
                        } catch (e: Exception) {
                            Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                            loader.value = false
                            Log.i("response4", response.message())

                        }
                    }
                }

        }

                }catch (e:Exception)
        {
            Toast.makeText(v.rootView.context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

//    fun loadArea(context: Context)
//    {
//        loader.value = true
//        val service = ApiClient.makeRetrofitServiceHome()
//        CoroutineScope(Dispatchers.IO).async {
//            val response = service.showAreaById("Bearer ${sharedHelper.getKey(context, "Token")}" ,  cityId.value!!)
//            withContext(Dispatchers.Main) {
//                try {
//                    if (response.isSuccessful && response.body()!!.error == 0) {
//                        loader.value = false
//                        areas.value = response.body()!!.data
//
//                    } else {
//                        Toast.makeText(context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
//                        loader.value = false
//
//                    }
//                }catch (e:Exception)
//                {
//                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
//                    loader.value = false
//                }
//            }
//        }
//    }


    fun showLocationAlert(v:View,cityType:String) {

        if (sharedHelper.getKey(v.rootView.context, "CountrySelectedTripFromId")?.isNotEmpty()!! && cityType == "cityFrom"){
            insertCityId.value = sharedHelper.getKey(v.rootView.context, "CountrySelectedTripFromId")?.toInt()
            showDialog(v)
        }
        else if (sharedHelper.getKey(v.rootView.context, "CountrySelectedTripToId")?.isNotEmpty()!! && cityType == "cityTo"){
            insertCityId.value = sharedHelper.getKey(v.rootView.context, "CountrySelectedTripToId")?.toInt()
            showDialog(v)

        }
        else {
//
//            if (cityType == "cityFrom")
//                insertCityId.value = sharedHelper.getKey(v.rootView.context, "CountrySelectedTripFromId")?.toInt()
//            if (cityType == "cityTo")
//                insertCityId.value = sharedHelper.getKey(v.rootView.context, "CountrySelectedTripToId")?.toInt()
            Toast.makeText(v.rootView.context, v.rootView.context.resources.getString(R.string.pleaseSelectCityFirst), Toast.LENGTH_SHORT).show()
        }

//        Toast.makeText(v.rootView.context, v.rootView.context.resources.getString(R.string.pleaseSelectCityFirst)+"1", Toast.LENGTH_SHORT).show()

    }

    fun showDialog(v:View){
        val alert: Dialog? = Dialog(v.rootView.context)
        alert?.setContentView(R.layout.user_location)
        alert?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alert?.findViewById<Button>(R.id.confirmBtnId)?.setOnClickListener {
            areaFromUser.value = alert.findViewById<EditText>(R.id.addressEditTextId).text.toString()
            if (areaFromUser.value!!.isEmpty())
                Toast.makeText(v.rootView.context, v.rootView.context.resources.getString(R.string.pleaseWriteArea), Toast.LENGTH_SHORT).show()
            else {
//            userCurrentAddressCheck.value = true
                loader.value = true
                val service = ApiClient.makeRetrofitServiceHome()
                CoroutineScope(Dispatchers.IO).launch {
                    val response = service.insertArea("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}", areaFromUser.value!!, insertCityId.value!!.toInt())
                    withContext(Dispatchers.Main) {
                        try {
                            Log.i("response1", response.message())
                            if (response.isSuccessful && response.body()!!.error == 0) {
                                loader.value = false
                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                Log.i("response2", response.message())
                                alert.dismiss()

                            } else {
                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                loader.value = false
                                Log.i("response3", response.message())

                            }
                        } catch (e: Exception) {
                            Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                            loader.value = false
                            Log.i("response4", response.message())

                        }
                    }

                }
            }
        }
        alert?.findViewById<Button>(R.id.cancelBtnId)?.setOnClickListener {
            alert.dismiss()
        }
        alert?.show()
    }



    fun getCountries(context:Context)
    {
        loader.value = true
        val service = ApiClient.makeUserAPIService()
        Log.i("Register", "1")
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("Register", "2")
            val response = service.showCountries()
            Log.i("Response1", response.message())
            Log.i("Response2", response.code().toString())
//                    Log.i("Response3", response.body()!!.message_en)
            withContext(Dispatchers.Main) {
                try {
                    Log.i("Register", "3")

                    if (response.isSuccessful && response.body()!!.error == 0) {

                        loader.value = false

                        for (i in response.body()!!.data)
                        {
                            countriesListModel.add(CountriesSpinnerModel(i.name,i.id))
                            countriesList.add(i.name)
//                            countriesListId.add(i.id)
                        }


                    } else if(response.isSuccessful){
                        loader.value = false
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    }

                    else {
                        Log.i("RegisterError", response.message())
                        loader.value = false
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    }
                } catch (e: java.lang.Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false

                }
            }
        }
    }

}