package com.mabdelhafz850.tawsila.ui.activity.all.online.createRide

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.tuyenmonkey.mkloader.MKLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class MapsAreaActivity : FragmentActivity(), OnMapReadyCallback, OnMarkerDragListener {
    private var mMap: GoogleMap? = null
    private var sydney: LatLng? = LatLng(29.9426994,31.3939916)
    private var mapFragment: MapFragment? = null
    private var adress: String? = null
    var selectedlatLng: LatLng? = null
    private var marker: ImageView? = null
    var dropOff = false
    lateinit var sharedHelper: SharedHelper

    //    DateTimeAddressInform pickUp;
    private var placeName: EditText? = null
    private var onCameraIdleListener: OnCameraIdleListener? = null
    var autocompleteFragment: AutocompleteSupportFragment? = null
    var doneButton: Button? = null
    var mkLoaderId: MKLoader? = null

    lateinit var enterType : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_favorite)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        setupAutoCompleteFragment()
        configureCameraIdle()
        marker = findViewById(R.id.imgLocationPinUp)
        placeName = findViewById(R.id.placeNameID)
        mkLoaderId = findViewById(R.id.mkLoaderId2)
        sharedHelper= SharedHelper()

        enterType = intent.getStringExtra("EnterType")

        //        Toolbar toolbar=findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MapsFavoriteActivity.this.onBackPressed();
//            }
//        });
//        toolbar.setTitle(getIntent().getStringExtra("title"));
        doneButton = findViewById(R.id.done)
        doneButton!!.setOnClickListener(View.OnClickListener { Done() })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private fun setupAutoCompleteFragment() {
        Places.initialize(this, getString(R.string.google_map_key))
        mapFragment!!.getMapAsync(this@MapsAreaActivity)
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocompleteDes_fragmentt) as AutocompleteSupportFragment?
        autocompleteFragment!!.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)).setCountry(getUserCountry(this))
        autocompleteFragment!!.setHint(resources.getString(R.string.searchForPlace))
        autocompleteFragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                sydney = place.latLng
            }

            override fun onError(status: Status) {
                Log.e("Error", status.statusMessage)
            }
        })
    }

    private fun configureCameraIdle() {
        onCameraIdleListener = OnCameraIdleListener {
            marker!!.visibility = View.GONE
            selectedlatLng = mMap!!.cameraPosition.target
            val geocoder = Geocoder(this@MapsAreaActivity)
            try {
                val addressList = geocoder.getFromLocation(selectedlatLng!!.latitude, selectedlatLng!!.longitude, 1)
                if (addressList != null && addressList.size > 0) {
                    adress = addressList[0].getAddressLine(0)
                    val country = addressList[0].countryName
                    if (adress!!.isNotEmpty() && country.isNotEmpty()) {
                        Toast.makeText(this@MapsAreaActivity, adress, Toast.LENGTH_SHORT).show()
                        mMap!!.addMarker(MarkerOptions()
                                .position(mMap!!.cameraPosition.target)
                                .title(adress)
                                .draggable(false)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_down_two)))
                        doneButton!!.isEnabled = true
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isZoomControlsEnabled = true
        if (dropOff) {
            mMap!!.addMarker(MarkerOptions()
                    .position(sydney!!)
                    .title("Test")
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_down_two)))
            doneButton!!.isEnabled = true
        }
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
        //        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title(title!=null?title:"")
//                .draggable(false)
////                .anchor(0,0)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


//        mMap.setOnMarkerDragListener(this);
        mMap!!.setOnCameraIdleListener(onCameraIdleListener)
        mMap!!.setOnCameraMoveListener {
            mMap!!.clear()
            marker!!.visibility = View.VISIBLE
        }

        mMap!!.isMyLocationEnabled = true

    }

    override fun onMarkerDragStart(marker: Marker) {}
    override fun onMarkerDrag(marker: Marker) {
        val center = CameraUpdateFactory.newLatLng(marker.position)
        val zoom = CameraUpdateFactory.zoomTo(15f)
        mMap!!.moveCamera(center)
        mMap!!.animateCamera(zoom)
    }

    override fun onMarkerDragEnd(marker: Marker) {}
    override fun onResume() {
        super.onResume()
        if (mMap != null) {
            mMap!!.clear()
        }
    }

    fun Done() {


        if(enterType == "From"){
            sharedHelper.putKey(this,"LatStart",selectedlatLng?.latitude!!.toString())
            sharedHelper.putKey(this,"LonStart",selectedlatLng?.longitude!!.toString())
            finish();
        }
        else if(enterType == "To"){
            sharedHelper.putKey(this,"LatEnd",selectedlatLng?.latitude!!.toString())
            sharedHelper.putKey(this,"LonEnd",selectedlatLng?.longitude!!.toString())
            finish();
        }


//        Intent data = new Intent();
//        data.putExtra("address",adress);
//        data.putExtra("lat",String.valueOf(selectedlatLng.latitude));
//        data.putExtra("lon",String.valueOf(selectedlatLng.longitude));
//        setResult(RESULT_OK,data);
//        finish();
//        if (placeName!!.text.toString().isEmpty()) Toast.makeText(this, resources.getString(R.string.pleaseEnterPlaceName), Toast.LENGTH_SHORT).show()
//        else {
////            Toast.makeText(this, placeName!!.text.toString()+"\n"+selectedlatLng?.latitude+"\n"+selectedlatLng?.longitude, Toast.LENGTH_SHORT).show()
//            mkLoaderId?.visibility = View.VISIBLE
//            val service = ApiClient.makeRetrofitServiceHome()
//            CoroutineScope(Dispatchers.IO).launch {
//
//                val response = service.insertArea("Bearer ${sharedHelper.getKey(applicationContext, "Token")}", placeName!!.text.toString(), enterType.toInt())
//
//                withContext(Dispatchers.Main) {
//                    try {
//                        Log.i("response1", response.message())
//                        if (response.isSuccessful && response.body()!!.error == 0) {
//                            mkLoaderId?.visibility = View.INVISIBLE
//                            Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
//                            Log.i("response2", response.message())
//
//                        } else {
//                            Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
//                            mkLoaderId?.visibility = View.INVISIBLE
//                            Log.i("response3", response.message())
//
//                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
//                        mkLoaderId?.visibility = View.INVISIBLE
//                        Log.i("response4", response.message())
//
//                    }
//                }
//
//            }

//        }
        //        else
//        {
//            mkLoaderId.setVisibility(View.VISIBLE);
//
//            ((Car) new Retrofit.Builder()
//                    .baseUrl("http://taxiApi.codecaique.com/api/")
//                    .build()
//                    .create(Car.class))
//                    .addFavoritePlace("bearer "+ SharedHelpers.getKey(this,"TokenFirebase"),placeName.getText().toString(),String.valueOf(selectedlatLng.latitude),String.valueOf(selectedlatLng.longitude),adress)
//                    .enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                            if (response.code() == 200)
//                            {
//                                try {
//                                    String responseData = response.body().string();
//
//                                    GeneralResponse generalResponse = new Gson().fromJson(responseData, GeneralResponse.class);
//                                    if (generalResponse.getError() == 0) {
//                                        mkLoaderId.setVisibility(View.GONE);
//                                        BaseResponse myBaseResponse = new Gson().fromJson(responseData, BaseResponse.class);
//                                        Toast.makeText(com.example.taxinaclient.MapsFavoriteActivity.this, myBaseResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                                        Intent data = new Intent();
//                                        data.putExtra("AllDoneMap","true");
//                                        setResult(RESULT_OK,data);
//                                        finish();
//                                    }
//                                    else
//                                    {
//                                        mkLoaderId.setVisibility(View.GONE);
//                                        Toast.makeText(com.example.taxinaclient.MapsFavoriteActivity.this, "Error "+ generalResponse.getMessage_en(), Toast.LENGTH_SHORT).show();
//                                    }
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                    mkLoaderId.setVisibility(View.GONE);
//                                    Toast.makeText(com.example.taxinaclient.MapsFavoriteActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                            else
//                            {
//                                mkLoaderId.setVisibility(View.GONE);
//                                Toast.makeText(com.example.taxinaclient.MapsFavoriteActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            mkLoaderId.setVisibility(View.GONE);
//                            Toast.makeText(com.example.taxinaclient.MapsFavoriteActivity.this, "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
    }

    fun getUserCountry(context: Context): String? {
        try {
            val tm = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val simCountry = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US)
            } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Exception " + e.message, Toast.LENGTH_SHORT).show()
        }
        return null
    }
}