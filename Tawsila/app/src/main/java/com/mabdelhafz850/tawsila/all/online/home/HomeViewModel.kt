package com.mabdelhafz850.tawsila.ui.activity.all.online.home

import ApiClient
import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.mabdelhafz850.tawsila.BuildConfig
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.ui.activity.response.DriverRequestedTripData
import com.mabdelhafz850.tawsila.ui.activity.roomDB.AppDatabase
import com.mabdelhafz850.tawsila.ui.activity.roomDB.StepsModel
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.shoohna.shoohna.util.base.BaseViewModel
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class HomeViewModel :BaseViewModel()  {

    var loader = MutableLiveData<Boolean>(false)
    var sharedHelper: SharedHelper = SharedHelper()

    var view1Visiable = MutableLiveData<Boolean>(false)
    var view2Visiable = MutableLiveData<Boolean>(false)

    var locationManager: LocationManager? = null
    var GpsStatus = false

     var users: MutableLiveData<List<DriverRequestedTripData>> = MutableLiveData()
    var acceptedTripId = MutableLiveData<Int>(0)
    var displayHigh = MutableLiveData<Int>(0)
    var myLocationLat = MutableLiveData<Double>(0.0)
    var myLocationLon = MutableLiveData<Double>(0.0)
    var originLocationToAPI = MutableLiveData<String>("")
    var destinationLocationToAPI = MutableLiveData<String>("")
    var distanceAPI = MutableLiveData<String>("")
    var durationAPI = MutableLiveData<String>("")
    var acceptTripUserImage = MutableLiveData<String>("")
    var acceptTripUserName = MutableLiveData<String>("")
    var acceptTripUserPhone = MutableLiveData<String>("")
    var startLocationFinal = MutableLiveData<String>("")
    var endLocationFinal = MutableLiveData<String>("")
    var fNameFinal = MutableLiveData<String>("")
    var lNameFinal = MutableLiveData<String>("")
    var imageFinal = MutableLiveData<String>("")
    var priceFinal = MutableLiveData<String>("")
    var paidQuantity = MutableLiveData<String>("")
    var allowObserveInUserRecycler = MutableLiveData<Boolean>(false)
    lateinit var include2View : View
    lateinit var include3View : View
    lateinit var include4View : View
    var myLocationLatFB = MutableLiveData<String>("")
    var myLocationLonFB = MutableLiveData<String>("")
    var allowSendDataToFirebase = MutableLiveData<Boolean>(false)
    var tripCanceledDialog = MutableLiveData<Boolean>(false)

    var startSensor = MutableLiveData<Boolean>(false)
    var UserIdSensor = MutableLiveData<String>("")
    var finalMetersToUser = MutableLiveData<Double>(0.0)
    var finalMinutesByUser = MutableLiveData<Long>(0)

    var finalTripPrice = MutableLiveData<String>("")
    var finalTripGain = MutableLiveData<String>("")

    private var homeControllerStartLat = MutableLiveData<Double>(0.0)
    private var homeControllerStartLon = MutableLiveData<Double>(0.0)
    private var homeControllerEndLat = MutableLiveData<Double>(0.0)
    private var homeControllerEndLon = MutableLiveData<Double>(0.0)
    private var homeControllerUserId = MutableLiveData<String>("")
    fun goOnlineOffline(v: View)
    {
        if(CheckGpsStatus(v)) {
            loader.value = true
            val service = ApiClient.makeRetrofitServiceHome()
            CoroutineScope(Dispatchers.IO).async {

                val response = service.onlineOffline("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}")

                withContext(Dispatchers.Main) {
                    try {

                        if (response.isSuccessful) {
                            if (response.body()!!.message == "you are offline now" || response.body()!!.message == "انت الان غير متاح") {
                                view1Visiable.value = false
                                view2Visiable.value = false
                                sharedHelper.putKey(v.rootView.context,"HomeControllerView1Visiable","0")
                                sharedHelper.putKey(v.rootView.context,"HomeControllerView2Visiable","0")
                                allowSendDataToFirebase.value = false
                            } else if (response.body()!!.message == "you are online now" || response.body()!!.message == "انت الان متاح") {
                                view1Visiable.value = true
                                view2Visiable.value = true
                                sharedHelper.putKey(v.rootView.context,"HomeControllerView1Visiable","1")
                                sharedHelper.putKey(v.rootView.context,"HomeControllerView2Visiable","1")
                                allowSendDataToFirebase.value = true

                                fetchUsers(v)
                                Log.i("Token", sharedHelper.getKey(v.rootView.context, "Token"))
                            }
                            Toast.makeText(v.rootView.context, "Home\n"+response.body()!!.message, Toast.LENGTH_SHORT).show()
                            loader.value = false

                        } else {

                        }
                    } catch (e: Exception) {
                        Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                        loader.value = false

                    }
                }
            }
        }else
        {
            Toast.makeText(v.rootView.context, "You need to open GPS First", Toast.LENGTH_SHORT).show()
        }
    }


    fun fetchUsers(v:View)
    {
        allowSendDataToFirebase.value = true

        loader.value = true
        Log.i("Lat Fetch", myLocationLat.value!!.toString())
        Log.i("Lon Fetch", myLocationLon.value!!.toString())
        Log.i("Worked","true");
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.driverRequestedTrip("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}",myLocationLat.value!!,myLocationLon.value!!)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body()!!.error == 0) {
                        Log.i("Users1",response.body()!!.data.toString())
                        Log.i("Users2",response.body()!!.message.toString())
                        users.value = response.body()!!.data
                        loader.value = false

                    } else {
                        users.value = response.body()!!.data

                        Toast.makeText(v.rootView.context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                        loader.value = false

                    }
                }catch (e:Exception)
                {
                    Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false
                }
            }
        }
    }

    fun driveAcceptTrip(v: View , recyclerView: RecyclerView , startLocation:String , endLocation:String , fName:String , lName:String , image:String , price:String , userId:String )
    {
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        val db = Room.databaseBuilder(v.rootView.context, AppDatabase::class.java, "stepsDB").build()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.driveAcceptTrip(acceptedTripId.value!!,"Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}")
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body()!!.error == 0) {
                        Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false
                        startLocationFinal.value = startLocation
                        endLocationFinal.value = endLocation
                        fNameFinal.value = fName
                        lNameFinal.value = lName
                        imageFinal.value = image
                        priceFinal.value = price
                        OnlineTaxiAnimations.animationTranslationY(recyclerView,displayHigh.value!!)
                        OnlineTaxiAnimations.animationTranslationYAfterOtherAnimate(include2View, 0)
                        acceptTripUserImage.value = response.body()!!.data.image
                        acceptTripUserName.value = response.body()!!.data.first_name+""+response.body()!!.data.last_name
                        acceptTripUserPhone.value = response.body()!!.data.phone
                        getGoogleApiDirectionData(v)
                        UserIdSensor.value = userId
                        homeControllerUserId.value = userId
                        allowSendDataToFirebase.value = true
                        val stepsModel = StepsModel(userId,acceptedTripId.value!!.toString(),0.0,0.0)
                        CoroutineScope(Dispatchers.IO).async { db.stepsDao().insertSteps(stepsModel) }
//                        sendLocationToFirebase(v,sharedHelper.getKey(v.rootView.context,"UserId").toString(),myLocationLatFB.value!!,myLocationLonFB.value!!)
                    } else {
                        Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false

                    }
                }catch (e:Exception)
                {
                    Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false
                }
            }
        }
    }

    fun requestCancelTrip(reasonTxt: String)
    {
        val service = ApiClient.makeRetrofitServiceHome()

    }

    fun driveAcceptTrip2( recyclerView: RecyclerView , startLocation:String , endLocation:String , fName:String , lName:String , image:String , price:String , userId:String , userName : String , userImage : String , userPhone:String )
    {

                        startLocationFinal.value = startLocation
                        endLocationFinal.value = endLocation
                        fNameFinal.value = fName
                        lNameFinal.value = lName
                        imageFinal.value = image
                        priceFinal.value = price
//                        OnlineTaxiAnimations.animationTranslationY(recyclerView,displayHigh.value!!)
                        recyclerView.translationY = displayHigh.value!!.toFloat()
                        OnlineTaxiAnimations.animationTranslationYAfterOtherAnimate(include2View, 0)
                        acceptTripUserImage.value = userImage
                        acceptTripUserName.value = userName
                        acceptTripUserPhone.value = userPhone
//                        getGoogleApiDirectionData(v)
                        UserIdSensor.value = userId
                        homeControllerUserId.value = userId
                        allowSendDataToFirebase.value = true

//                        sendLocationToFirebase(v,sharedHelper.getKey(v.rootView.context,"UserId").toString(),myLocationLatFB.value!!,myLocationLonFB.value!!)

    }

    fun DriverArrived(v: View,constraintLayout: ConstraintLayout)
    {
        OnlineTaxiAnimations.animationTranslationY(constraintLayout,displayHigh.value!!)
        OnlineTaxiAnimations.animationTranslationYAfterOtherAnimate(include3View, 0)
        allowSendDataToFirebase.value = false
        startSensor.value = true

    }

    fun DropOffUser(v:View , cardView: CardView)
    {

        loader.value = true

        try {
            val db = Room.databaseBuilder(v.rootView.context, AppDatabase::class.java, "stepsDB").allowMainThreadQueries().build()
            finalMetersToUser.value = db.stepsDao().getAllStepsById(UserIdSensor.value!!.toInt())
            Toast.makeText(v.rootView.context, "Meters By User ${finalMetersToUser.value!!.toInt()}\nTime in Minutes ${finalMinutesByUser.value}", Toast.LENGTH_SHORT).show()
            // TODO here call api to by distance and time to get price of trip then delete this user
            val service = ApiClient.makeRetrofitServiceHome()
            CoroutineScope(Dispatchers.IO).async {
                Log.i("Data","1")
                val response = service.showTripCost("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}", acceptedTripId.value!!, finalMinutesByUser.value!!.toInt(), finalMetersToUser.value?.toInt()!!)
                Log.i("Data","2")

                withContext(Dispatchers.Main) {
                    try {
                        Log.i("Data","3")

                        if (response.isSuccessful && response.body()!!.error == 0) {
                            Log.i("Data","4")

                            Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                            finalTripPrice.value = response.body()!!.total_price.toString()
                            finalTripGain.value = response.body()!!.your_gain.toString()
//                            startSensor.value = false
                            OnlineTaxiAnimations.animationTranslationY(cardView, displayHigh.value!!)
                            OnlineTaxiAnimations.animationTranslationYAfterOtherAnimate(include4View, 0)
                            startSensor.value = false
                            loader.value = false
                            Log.i("responsePrice", response.body()!!.total_price.toString())
                            Log.i("responseGain", response.body()!!.your_gain.toString())
                            Log.i("End Trip1", response.body()!!.message)
                            db.stepsDao().deleteStepsById(UserIdSensor.value!!.toInt())

                        } else {
                            Toast.makeText(v.rootView.context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                            loader.value = false
                            Log.i("End Trip2", response.body()!!.message)

                        }
                    } catch (e: Exception) {
                        Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                        loader.value = false
                        Log.i("End Trip3", e.message.toString())
                        Log.i("End Trip4", response.message())


                    }
                }
            }
        }catch (e:Exception)
        {
            Toast.makeText(v.rootView.context,e.message,Toast.LENGTH_SHORT).show()
        }
    }

    fun finishRide(v:View , constraintLayout: ConstraintLayout , recyclerView: RecyclerView)
    {
        if(paidQuantity.value?.isEmpty()!!)
            Toast.makeText(v.rootView.context,"No price !!",Toast.LENGTH_SHORT).show()
        else {
            loader.value = true
            val service = ApiClient.makeRetrofitServiceHome()

            CoroutineScope(Dispatchers.IO).async {
                val response = service.tripPayment("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}", acceptedTripId.value!!, paidQuantity.value?.toInt()!!)
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful && response.body()!!.error == 0) {
                            Toast.makeText(v.rootView.context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                            OnlineTaxiAnimations.animationTranslationY(constraintLayout, displayHigh.value!!)
                            OnlineTaxiAnimations.animationTranslationYAfterOtherAnimate(recyclerView, 0)
                            fetchUsers(v)
                            allowObserveInUserRecycler.value = true

                            loader.value = false
                            paidQuantity.value = ""
//                            startSensor.value = false
                            sharedHelper.putKey(v.rootView.context, "HomeControllerEnable", "0")


                        } else {
                            Toast.makeText(v.rootView.context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                            loader.value = false

                        }
                    } catch (e: Exception) {
                        Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                        loader.value = false
                    }
                }
            }
        }
    }


    fun getGoogleApiDirectionData(v: View)
    {
        loader.value = true
        val service = ApiClient.makeGoogleDirectionsAPISericve()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.getDistanceAndTimeGoogleAPI(originLocationToAPI.value!!,destinationLocationToAPI.value!!,v.context.getString(R.string.google_map_key),"driving")
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body()!!.status == "OK") {
                        distanceAPI.value = response.body()!!.routes[0].legs[0].distance.text
                        durationAPI.value =  response.body()!!.routes[0].legs[0].duration.text
//                        Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false



                    } else {
                        Toast.makeText(v.rootView.context, response.message(), Toast.LENGTH_SHORT).show()
                        loader.value = false
                        Log.i("ExceptionDistanceAPI1",response.message())

                    }
                }catch (e:Exception)
                {
                    Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false
                    Log.i("ExceptionDistanceAPI2",e.message.toString())

                }
            }
        }
    }

//    fun getGoogleApiDirectionData2(context: Context , origin:String , destination:String)
//    {
//        loader.value = true
//        val service = ApiClient.makeGoogleDirectionsAPISericve()
//        CoroutineScope(Dispatchers.IO).async {
//            val response = service.getDistanceAndTimeGoogleAPI(origin,destination,context.getString(R.string.google_map_key),"driving")
//            withContext(Dispatchers.Main) {
//                try {
//                    if (response.isSuccessful && response.body()!!.status == "OK") {
//                        distanceAPI.value = response.body()!!.routes[0].legs[0].distance.text
//                        durationAPI.value =  response.body()!!.routes[0].legs[0].duration.text
//                        Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
//                        loader.value = false
//
//                    } else {
//                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
//                        loader.value = false
//                        Log.i("ExceptionDistanceAPI1",response.message())
//
//                    }
//                }catch (e:Exception)
//                {
//                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
//                    loader.value = false
//                    Log.i("ExceptionDistanceAPI2",e.message.toString())
//
//                }
//            }
//        }
//    }



    fun drawDirections(startLat : Double , startLon:Double , endLat : Double , endLon : Double , map:GoogleMap,cont: Context) {
        originLocationToAPI.value = "$startLat,$startLon"
        destinationLocationToAPI.value = "$endLat,$endLon"
        homeControllerStartLat.value = startLat
        homeControllerStartLon.value = startLon
        homeControllerEndLat.value = endLat
        homeControllerEndLon.value = endLon
        val path: MutableList<LatLng> = ArrayList()
        val context = GeoApiContext().setQueryRateLimit(3).setApiKey(cont.getString(R.string.google_map_key))
                .setConnectTimeout(1, TimeUnit.SECONDS).setReadTimeout(1, TimeUnit.SECONDS).setWriteTimeout(1, TimeUnit.SECONDS)
        var latLngOrigin = LatLng(startLat,startLon)
        var latLngDestination = LatLng(endLat,endLon)
        map.addMarker(MarkerOptions().position(latLngOrigin).title("Origin").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
        map.addMarker(MarkerOptions().position(latLngDestination).title("Destintaion"))

        // animate camera to show map with 2 points only
        val builder: LatLngBounds.Builder = LatLngBounds.Builder()
        builder.include(latLngOrigin)
        builder.include(latLngDestination)
        val bounds: LatLngBounds = builder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 0)
        map.animateCamera(cu)

        Log.i("Draw","$startLat,$startLon")
        val req = DirectionsApi.getDirections(context, "$startLat,$startLon", "$endLat,$endLon")
        try {
            val res = req.await()

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.size > 0) {
                val route = res.routes[0]
                if (route.legs != null) {
                    for (i in route.legs.indices) {
                        val leg = route.legs[i]
                        if (leg.steps != null) {
                            for (j in leg.steps.indices) {
                                val step = leg.steps[j]
                                if (step.steps != null && step.steps.size > 0) {
                                    for (k in step.steps.indices) {
                                        val step1 = step.steps[k]
                                        val points1 = step1.polyline
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            val coords1 = points1.decodePath()
                                            for (coord1 in coords1) {
                                                path.add(LatLng(coord1.lat, coord1.lng))
                                            }
                                        }
                                    }
                                } else {
                                    val points = step.polyline
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        val coords = points.decodePath()
                                        for (coord in coords) {
                                            path.add(LatLng(coord.lat, coord.lng))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: java.lang.Exception) {
        }
        if (path.size > 0) {
            val opts = PolylineOptions().addAll(path).color(Color.BLUE).width(8f)
            map.addPolyline(opts)
        }
    }

//    fun makeCall(v:View)
//    {
//        if(checkPermission(v)){
//        val intent = Intent(Intent.ACTION_CALL)
//
//        intent.data = Uri.parse("tel:${acceptTripUserPhone.value}")
//        if (ActivityCompat.checkSelfPermission(v.rootView.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            return
//        }
//        v.rootView.context.startActivity(intent)
//        }else
//            requestPermission(v)
//    }
//
//    private fun checkPermission(v: View): Boolean {
//        val result1 = ContextCompat.checkSelfPermission(v.rootView.context, Manifest.permission.CALL_PHONE)
//        return result1 == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestPermission(v: View) {
//        ActivityCompat.requestPermissions(v.rootView.context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 200)
//    }

    private fun CheckGpsStatus(v:View) : Boolean {
        locationManager = v.rootView.context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (BuildConfig.DEBUG && locationManager == null) {
            error("Assertion failed")
        }
        GpsStatus = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return GpsStatus
    }

    fun sendLocationToFirebase( userId:String , lat:String , lon:String)
    {
        val database = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("driverLocation")
        val map = HashMap<String,String>()
        map["lat"] = lat
        map["lon"] = lon
        myRef.child(userId).setValue(map)

    }


    fun goToChat(v:View)
    {
        sharedHelper.putKey(v.rootView.context,"HomeControllerEnable","0")
        sharedHelper.putKey(v.rootView.context,"HomeControllerIsOnline","0")
        sharedHelper.putKey(v.rootView.context,"HomeControllerStartLat",homeControllerStartLat.value.toString())
        sharedHelper.putKey(v.rootView.context,"HomeControllerStartLon",homeControllerStartLon.value.toString())
        sharedHelper.putKey(v.rootView.context,"HomeControllerEndLat",homeControllerEndLat.value.toString())
        sharedHelper.putKey(v.rootView.context,"HomeControllerEndLon",homeControllerEndLon.value.toString())
        sharedHelper.putKey(v.rootView.context,"HomeControllerfName",fNameFinal.value)
        sharedHelper.putKey(v.rootView.context,"HomeControllerlName",lNameFinal.value)
        sharedHelper.putKey(v.rootView.context,"HomeControllerImage",imageFinal.value)
        sharedHelper.putKey(v.rootView.context,"HomeControllerPhone",acceptTripUserPhone.value)
        sharedHelper.putKey(v.rootView.context,"HomeControllerPrice",priceFinal.value)
        sharedHelper.putKey(v.rootView.context,"HomeControllerTripId",acceptedTripId.value.toString())
        sharedHelper.putKey(v.rootView.context,"HomeControllerUserId",homeControllerUserId.value)
        sharedHelper.putKey(v.rootView.context,"HomeControllerStartLocation",startLocationFinal.value)
        sharedHelper.putKey(v.rootView.context,"HomeControllerEndLocation",endLocationFinal.value)


        Navigation.findNavController(v).navigate(R.id.action_online5_to_chat)
    }


    fun showAlert(v: View )
    {
        val alert: Dialog? = Dialog(v.rootView.context)
        alert?.setContentView(R.layout.trip_canceled_alert)
        alert?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alert?.findViewById<Button>(R.id.confirmBtnId)?.setOnClickListener {
            Log.i("SharedHelperAlert",sharedHelper.getKey(v.rootView.context, "GoToLogin").toString())
            alert.dismiss()
            sharedHelper.putKey(v.rootView.context,"CancelDialog","0")
        }
        alert?.findViewById<Button>(R.id.cancelBtnId)?.setOnClickListener {
            alert.dismiss()
        }

        alert?.show()
    }
}