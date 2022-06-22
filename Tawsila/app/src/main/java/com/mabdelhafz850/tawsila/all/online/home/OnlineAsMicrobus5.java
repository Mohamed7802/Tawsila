package com.mabdelhafz850.tawsila.all.online.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.mabdelhafz850.tawsila.R;
import com.mabdelhafz850.tawsila.databinding.FragmentOnlineAsMicrobus5Binding;
import com.mabdelhafz850.tawsila.ui.activity.all.online.home.HomeViewModel;
import com.mabdelhafz850.tawsila.ui.activity.all.online.home.PassengerRecyclerAdapter;
import com.mabdelhafz850.tawsila.ui.activity.response.DriverRequestedTripData;
import com.mabdelhafz850.tawsila.ui.activity.roomDB.AppDatabase;
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoohna.shoohna.util.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineAsMicrobus5 extends BaseFragment implements OnMapReadyCallback , SensorEventListener {

//    ConstraintLayout conOffline, cinAsMicrobus5;
//    TextView tv_offline, tv_online;
//    ImageView openTop, openChat;


//    @NonNull
//    @Override
//    protected Navigator<? extends FragmentNavigator.Destination> createFragmentNavigator() {
//        return super.createFragmentNavigator();
//
//    }

    LocationManager locationManager;
    boolean GpsStatus = false ;

    MapView mapView;
    GoogleMap map;
//    RecyclerView rv_passenger;
    LocationManager mLocationManager;
    Display display ;
    private Polyline currentPolyline;
    ArrayList markerPoints= new ArrayList();
    Double myLocationLat ;
    Double myLocationLon ;
    SensorManager sensorManager;
    boolean running ;
    AppDatabase db;
//    var bestSaleData= MutableLiveData<List<BestSeller>>()

    public MutableLiveData<List<DriverRequestedTripData>> usersList = new MutableLiveData<>();

    boolean myBool;
    FragmentOnlineAsMicrobus5Binding binding;
    HomeViewModel homeViewModel;
    PassengerRecyclerAdapter passengerRecyclerAdapter;
    private FusedLocationProviderClient fusedLocationClient;
    private SharedHelper sharedHelper;
    NavController nav;
    long elapsedMillis;

    boolean allowTrackInFirebase = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_online_as_microbus5, container, false);

        sharedHelper = new SharedHelper();
        binding =  FragmentOnlineAsMicrobus5Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        binding.setVM(homeViewModel);

        nav=Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        running = false;
        db = Room.databaseBuilder(requireActivity(), AppDatabase.class, "stepsDB").allowMainThreadQueries().build();
        if(Objects.equals(sharedHelper.getKey(requireActivity(), "car_type"), "2"))
        {
            Log.i("car_type_frag","entered");
            nav.navigate(R.id.action_online5_to_onlineAsTaxi122);
        }else {

            homeViewModel.getView1Visiable().setValue(Objects.equals(sharedHelper.getKey(requireContext(), "UserIsOnline"), "1"));
            homeViewModel.getView2Visiable().setValue(Objects.equals(sharedHelper.getKey(requireContext(), "UserIsOnline"), "1"));

            homeViewModel.getView1Visiable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean)
                        homeViewModel.fetchUsers(requireView());
                }
            });

            homeViewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<List<DriverRequestedTripData>>() {
                @Override
                public void onChanged(List<DriverRequestedTripData> driverRequestedTripData) {
                    Log.i("UsersInMainFetch",driverRequestedTripData.toString());
                    usersList.setValue(driverRequestedTripData);
                    passengerRecyclerAdapter = new PassengerRecyclerAdapter(usersList, getActivity(), homeViewModel, requireView(), binding.userRecyclerViewId, map);
                    binding.userRecyclerViewId.setAdapter(passengerRecyclerAdapter);
                    Objects.requireNonNull(binding.userRecyclerViewId.getAdapter()).notifyDataSetChanged();

                }
            });


        homeViewModel.getAllowObserveInUserRecycler().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("ObserverAllow",aBoolean.toString());
                if(aBoolean) {
//                    homeViewModel.fetchUsers(requireView());
                    binding.userRecyclerViewId.getAdapter().notifyDataSetChanged();
                    map.clear();
                }
            }
        });


            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

            if (CheckGpsStatus(getContext())) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
//                Log.i("Last Location",location.toString());
                                homeViewModel.getMyLocationLat().setValue(location.getLatitude());
                                homeViewModel.getMyLocationLon().setValue(location.getLongitude());

                            }
                        });
            } else
                Toast.makeText(getActivity(), "You need to open GPS First", Toast.LENGTH_SHORT).show();


            display = getActivity().getWindowManager().getDefaultDisplay();
            homeViewModel.getDisplayHigh().setValue(display.getHeight());
            homeViewModel.include2View = binding.include2Id;
            homeViewModel.include3View = binding.include3Id;
            homeViewModel.include4View = binding.include4Id;

            binding.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Objects.requireNonNull(homeViewModel.getAcceptTripUserPhone().getValue()).isEmpty())
                    {
                            if(checkPermission()){
                                callingProcess(homeViewModel.getAcceptTripUserPhone().getValue());
                            }else{
                                requestPermission();
                            }
                    }
                }
            });

//        mapView = (MapView) view.findViewById(R.id.mapViewId);
            binding.mapViewId.onCreate(savedInstanceState);
            MapsInitializer.initialize(this.requireActivity());
            binding.mapViewId.getMapAsync(this);

            mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                        0, mLocationListener);
            }



            binding.include2Id.setTranslationY(display.getHeight());
            binding.include3Id.setTranslationY(display.getHeight());
            binding.include4Id.setTranslationY(display.getHeight());

        }


        homeViewModel.getAllowSendDataToFirebase().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                allowTrackInFirebase = aBoolean;

            }
        });


        homeViewModel.getStartSensor().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    acceptTripBySensorStart();
                    binding.simpleChronometer.setBase(SystemClock.elapsedRealtime());

                    binding.simpleChronometer.start();
                    binding.simpleChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        @Override
                        public void onChronometerTick(Chronometer chronometer) {
                            elapsedMillis = TimeUnit.MILLISECONDS.toMinutes(SystemClock.elapsedRealtime() - chronometer.getBase()) ;
                            Log.i("elapsedMillis1",String.valueOf(elapsedMillis));
                            binding.sensorDataId.setText("1 Time : "+elapsedMillis);
                            homeViewModel.getFinalMinutesByUser().setValue(elapsedMillis);
                        }
                    });

                }
                else {
                    acceptTripBySensorStop();
                    Log.i("elapsedMillis2",String.valueOf(elapsedMillis));

                    binding.sensorDataId.setText("1 Time : "+homeViewModel.getFinalMinutesByUser().getValue()+"\n2 Meters : "+homeViewModel.getFinalMetersToUser().getValue());

                    binding.simpleChronometer.stop();

//                    Toast.makeText(getActivity(), ""+binding.simpleChronometer.getBase(), Toast.LENGTH_SHORT).show();
//                    binding.simpleChronometer.setBase(SystemClock.elapsedRealtime());

                }
            }
        });

        return binding.getRoot();
    }

    private void checkHomeController() {
        if(Objects.equals(sharedHelper.getKey(requireContext(), "HomeControllerEnable"), "1"))
        {
            try {
                homeViewModel.drawDirections(Double.parseDouble(Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerStartLat"))),
                        Double.parseDouble(Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerStartLon"))),
                        Double.parseDouble(Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerEndLat"))),
                        Double.parseDouble(Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerEndLon"))), map
                        ,requireContext());

                homeViewModel.driveAcceptTrip2(binding.userRecyclerViewId,
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerStartLocation")),
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerEndLocation")),
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerfName")),
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerlName")),
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerImage")),
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerPrice")),
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerUserId")),
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerfName"))+" "+Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerlName")),
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerImage")),
                        Objects.requireNonNull(sharedHelper.getKey(requireContext(), "HomeControllerPhone")));



                sharedHelper.putKey(requireContext(),"HomeControllerEnable","0");
                sharedHelper.putKey(requireContext(),"HomeControllerIsOnline","0");
                sharedHelper.putKey(requireContext(),"HomeControllerStartLat","");
                sharedHelper.putKey(requireContext(),"HomeControllerStartLon","");
                sharedHelper.putKey(requireContext(),"HomeControllerEndLat","");
                sharedHelper.putKey(requireContext(),"HomeControllerEndLon","");
                sharedHelper.putKey(requireContext(),"HomeControllerfName","");
                sharedHelper.putKey(requireContext(),"HomeControllerlName","");
                sharedHelper.putKey(requireContext(),"HomeControllerImage","");
                sharedHelper.putKey(requireContext(),"HomeControllerPhone","");
                sharedHelper.putKey(requireContext(),"HomeControllerPrice","");
                sharedHelper.putKey(requireContext(),"HomeControllerTripId","");
                sharedHelper.putKey(requireContext(),"HomeControllerUserId","");
                sharedHelper.putKey(requireContext(),"HomeControllerStartLocation","");
                sharedHelper.putKey(requireContext(),"HomeControllerEndLocation","");
//                homeViewModel.getFNameFinal().setValue(sharedHelper.getKey(requireContext(),"HomeControllerfName"));
//                homeViewModel.getLNameFinal().setValue(sharedHelper.getKey(requireContext(),"HomeControllerlName"));
//                homeViewModel.getImageFinal().setValue(sharedHelper.getKey(requireContext(),"HomeControllerImage"));
//                homeViewModel.getPriceFinal().setValue(sharedHelper.getKey(requireContext(),"HomeControllerPrice"));
//                homeViewModel.getAcceptTripUserPhone().setValue(sharedHelper.getKey(requireContext(),"HomeControllerPhone"));

            }catch (Exception e)
            {
                Toast.makeText(requireActivity(), "Exception Home Controller \n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("ExceptionInController",e.getMessage().toString());
            }
        }
    }

    private  void offlineClicked(View view)
    {
        binding.conOffline.setVisibility(View.GONE);
        binding.cinAsMicrobus5.setVisibility(View.VISIBLE);
//        Navigation.findNavController(view).navigate(R.id.action_online5_to_onlineAsTaxi6);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        checkHomeController();
//        homeViewModel.getAllowObserveInUserRecycler().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if(aBoolean) {
//                    Log.i("UserRecyclerNotify","true");
//                    binding.userRecyclerViewId.getAdapter().notifyDataSetChanged();
//                    map.clear();
//                }
//            }
//        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

//                if (markerPoints.size() > 1) {
//                    markerPoints.clear();
//                    map.clear();
//                }
//
//                // Adding new item to the ArrayList
//                markerPoints.add(latLng);
//
//                // Creating MarkerOptions
//                MarkerOptions options = new MarkerOptions();
//
//                // Setting the position of the marker
//                options.position(latLng);
//
//                if (markerPoints.size() == 1) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//                } else if (markerPoints.size() == 2) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                }
//
//                // Add new marker to the Google Map Android API V2
//                map.addMarker(options);
//
//                // Checks, whether start and end locations are captured
//                if (markerPoints.size() >= 2) {
//                    LatLng origin = (LatLng) markerPoints.get(0);
//                    LatLng dest = (LatLng) markerPoints.get(1);
//
//
//                    String or = origin.latitude+","+origin.longitude;
//                    String de = dest.latitude+","+dest.longitude;
//
////                    homeViewModel.drawDirections(or,de,map);
//
//                    //Execute Directions API request
//
//
//                    //Draw the polyline
//
//                }

            }
        });





    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (Objects.equals(sharedHelper.getKey(requireContext(), "HomeControllerEnable"), "0")) {
//            if (Objects.equals(sharedHelper.getKey(requireActivity(), "car_type"), "1") ) {
//                homeViewModel.goOnlineOffline(requireView());
//            }
//        }
//        if((Objects.equals(sharedHelper.getKey(requireContext(), "UserIsOnline"), "1")))
//        {
//            homeViewModel.fetchUsers(requireView());
//        }

        if(Objects.equals(sharedHelper.getKey(requireContext(), "CancelDialog"), "1"))
        {
            homeViewModel.showAlert(requireView());
        }
        if(sharedHelper.getKey(requireContext(),"HomeControllerView1Visiable").equals("0"))
            homeViewModel.getView1Visiable().setValue(false);
        if(sharedHelper.getKey(requireContext(),"HomeControllerView2Visiable").equals("0"))
            homeViewModel.getView2Visiable().setValue(false);
        if(sharedHelper.getKey(requireContext(),"HomeControllerView1Visiable").equals("1"))
            homeViewModel.getView1Visiable().setValue(true);
        if(sharedHelper.getKey(requireContext(),"HomeControllerView2Visiable").equals("1"))
            homeViewModel.getView2Visiable().setValue(true);
    }

    @Override
    public void onResume() {
        if(Objects.equals(sharedHelper.getKey(requireActivity(), "car_type"), "1")) {
            binding.mapViewId.onResume();
            super.onResume();
        }
        else
        {
            super.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if(Objects.equals(sharedHelper.getKey(requireActivity(), "car_type"), "1")) {
            super.onDestroy();
            binding.mapViewId.onDestroy();
        }
        else
        {
            super.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        if(Objects.equals(sharedHelper.getKey(requireActivity(), "car_type"), "1")) {
            super.onLowMemory();
            binding.mapViewId.onLowMemory();
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
            map.animateCamera(cameraUpdate);
            homeViewModel.getMyLocationLat().postValue(location.getLatitude());
            homeViewModel.getMyLocationLon().postValue(location.getLongitude());
           myLocationLat = location.getLatitude();
           myLocationLon = location.getLongitude();
           if(allowTrackInFirebase) {
//               Toast.makeText(getActivity(), "Observable Firebase "+aBoolean, Toast.LENGTH_SHORT).show();
               sendLocationToFirebase(sharedHelper.getKey(requireContext(), "UserId").toString(), location.getLatitude(), location.getLongitude());
           }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

////    public void processOne(View view)
////    {
//////        OnlineTaxiAnimations.animationTranslationY(binding.include1Id,display.getHeight());
////        OnlineTaxiAnimations.animationTranslationYAfterOtherAnimate(binding.include2Id,0);
////    }
//
//    public void processTwo(View view)
//    {
////        OnlineTaxiAnimations.animationTranslationY(binding.include2Id,display.getHeight());
//        OnlineTaxiAnimations.animationTranslationYAfterOtherAnimate(binding.include3Id,0);
//    }
//
//    public void processThree(View view)
//    {
//        OnlineTaxiAnimations.animationTranslationY(binding.include3Id,display.getHeight());
//        OnlineTaxiAnimations.animationTranslationYAfterOtherAnimate(binding.include4Id,0);
//    }


    private boolean CheckGpsStatus(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert(locationManager != null);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return GpsStatus;
    }


    private void sendLocationToFirebase( String userId  ,Double lat ,Double lon)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("driverLocation");
        Map<String,String> map = new HashMap<>();
        map.put("lat",lat.toString());
        map.put("lon",lon.toString());
        map.put("userId",userId);
        myRef.child(userId).setValue(map);
        Log.i("AllowFirebase2","true");

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
//            stepsValue.text = "" + event.values[0];
//            meterTxtViewId.text = "Meters : "+convertStepToMeter(stepsValue.text.toString().toDouble())
            Log.i("SensorMeters",convertStepToMeter((double)event.values[0])+"");
            db.stepsDao().updateMetersOfUserId(convertStepToMeter((double)event.values[0]),Integer.parseInt(homeViewModel.getUserIdSensor().getValue()));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void acceptTripBySensorStart()
    {
        running = true;
        Sensor stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepsSensor == null) {
            Toast.makeText(requireContext(), "No Step Counter Sensor !", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void acceptTripBySensorStop()
    {
        running = false;
        sensorManager.unregisterListener(this);
    }

    private Double convertStepToMeter(Double step){
        return step *  0.762;
    }


//    @Override
//    public void onSaveInstanceState(Bundle state){
//        super.onSaveInstanceState(state);
//        if(state!=null){
//            state.putString("website","Inducesmile");
//            Toast.makeText(getActivity(),"Fragment Save State",Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState){
//        super.onViewStateRestored(savedInstanceState);
//        if(savedInstanceState!=null){
//            String website = savedInstanceState.getString("website");
//            Toast.makeText(getActivity(),"Fragment Restore State: "+website,Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState){
//        super.onActivityCreated(savedInstanceState);
//    }

    private void callingProcess(String phone){

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        return result1 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE  }, 200);
    }
}
