package com.mabdelhafz850.tawsila.all;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.mabdelhafz850.tawsila.R;
import com.mabdelhafz850.tawsila.ui.activity.notifications.dialogs.ForegroundDialog;
import com.mabdelhafz850.tawsila.ui.activity.rides.Rides;

import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper;
import com.google.android.material.navigation.NavigationView;

import static com.mabdelhafz850.tawsila.ui.activity.rides.UpcomingTap.DontShow;

public class
Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;
    ImageButton menuBtn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConstraintLayout home_content;
    //Fragment fragment2;
    ImageView headerImage ;
    TextView nameHeader ;
    FragmentManager manager;
    NavController nav;
    SharedHelper sharedHelper;
    private NavController navController ;



    // For Forground
    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ForegroundDialog dialog = new ForegroundDialog();
            dialog.show(getSupportFragmentManager() , "Foreground");
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter("FCM_MESSAGE" ));

        setContentView(R.layout.activity_main2);
        manager = getSupportFragmentManager();
        sharedHelper = new SharedHelper();
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        initiation();
        navigation();
        try {
            View header = navigationView.getHeaderView(0);
            nameHeader = header.findViewById(R.id.headerTxtNameId);
            headerImage = header.findViewById(R.id.user_image);
            nameHeader.setText(sharedHelper.getKey(this, "fName") + " " + sharedHelper.getKey(this, "lName"));
            Glide.with(this).load(sharedHelper.getKey(this, "Image")).into(headerImage);

        }catch (Exception e )
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        navigationView.setNavigationItemSelectedListener(this);
        // navigationAnimation();
        nav = Navigation.findNavController(this,R.id.nav_host_fragment);
        Log.i("RestartIntent","1");
        if(getIntent().getExtras() != null)
        {
            Log.i("RestartIntent","2");

            if(getIntent().getBooleanExtra("openProfile", true)) {
                if (nav.getGraph().getStartDestination() != nav.getCurrentDestination().getId()) {
                    nav.popBackStack(R.id.online5, false);
//                    nav.navigate(R.id.action_online5_to_profile7);
                    Log.i("RestartIntent", "3");
                } else {
//                    nav.navigate(R.id.action_online5_to_profile7);
                    Log.i("RestartIntent", "4");

                }
            }
        }


        SharedHelper SharedHelpers = new SharedHelper();
        if (!SharedHelpers.getKey(this , "OPEN").equals("OPEN"))
        {
            SharedHelpers.putKey(this , "OPEN" , "OPEN");
            setDataForNotification();
        }
    }


    private void setDataForNotification() {

        Log.d("HERE" , "HEREHOME");
        String id = getIntent().getExtras().getString("redirect_id");
        String status = getIntent().getExtras().getString("status");
        String type = getIntent().getExtras().getString("type");
        navigationView = findViewById(R.id.navigation_view);
        navController= Navigation.findNavController(this , R.id.nav_host_fragment);

        SharedHelper SharedHelpers = new SharedHelper();
        Log.d("DATA" , id+" : "+status+" : "+type);
        if(status != null)
        {
            Log.d("ERROE" , "Notification is not null"+status);
            if (status.equals("1"))
            {
                navController.navigate(R.id.online5);
            }
            else if (status.equals("2"))
            {
                SharedHelpers.putKey(this , "NotificationIdTrip" , id);
                SharedHelpers.putKey(this , "NotificationUserID" , type);
                navController.navigate(R.id.action_global_userBookingDetails);
            }
            else if (status.equals("3"))
            {
                SharedHelpers.putKey(this, "CancelDialog" , "1");
                SharedHelpers.putKey(this , "CancelIdTrip" , id);
                navController.navigate(R.id.online5);
            }
//            else if(status.equals("4"))
//            {
//                // TODO api accept handle
//            }
//            else if(status.equals("5"))
//            {
//                // TODO api refuse handle
//
//            }
        }

        else
        {
            Log.d("ERROE" , "Notification is null");
        }
    }

    private void navigationAnimation() {

//        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaledOffSet = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffSet;
                home_content.setScaleX(offsetScale);
                home_content.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = home_content.getWidth() * diffScaledOffSet / 2;
                final float xtranslation = xOffset - xOffsetDiff;
                home_content.setTranslationX(xtranslation);

//                super.onDrawerSlide(drawerView, slideOffset);
            }

        });
    }

    @Override
    public void onBackPressed() {
//        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }
//        switch (currantFrag) {
//            case R.layout.fragment_online_as_taxi6:
//                OnlineAsTaxi6 fragment = new OnlineAsTaxi6();
//                manager.beginTransaction().replace(R.id.relativ1, fragment).commit();
//                break;
//            case R.layout.fragment_online_as_microbus5:
//                OnlineAsMicrobus5 onlineAsMicrobus5 = new OnlineAsMicrobus5();
//                manager.beginTransaction().replace(R.id.relativ1, onlineAsMicrobus5).commit();
//                break;
//            case R.layout.fragment_online_as_taxi9:
//                OnlineAsTaxi9 onlineAsTaxi9 = new OnlineAsTaxi9();
//                manager.beginTransaction().replace(R.id.relativ1, onlineAsTaxi9).commit();
//                break;
//            case R.layout.fragment_online_as_taxi8:
//                OnlineAsTaxi8 onlineAsTaxi8 = new OnlineAsTaxi8();
//                manager.beginTransaction().replace(R.id.relativ1, onlineAsTaxi8).commit();
//                break;
//            case R.layout.fragment_online_microbus_details5_1:
//                OnlineMicrobusDetails5_1 onlineMicrobusDetails5_1 = new OnlineMicrobusDetails5_1();
//                manager.beginTransaction().replace(R.id.relativ1, onlineMicrobusDetails5_1).commit();
//                break;
//            case R.layout.fragment_profile_taxi7:
//                ProfileAsTaxi7 profileAsTaxi7 = new ProfileAsTaxi7();
//                manager.beginTransaction().replace(R.id.relativ1, profileAsTaxi7).commit();
//                break;
//            default:
//                super.onBackPressed();
//        }


        if(nav.getCurrentDestination().getLabel().equals("fragment_chat") ||
                nav.getCurrentDestination().getLabel().equals("fragment_notification11_1") ||
                nav.getCurrentDestination().getLabel().equals("fragment_wallet")  ||
                nav.getCurrentDestination().getLabel().equals("fragment_online_as_taxi6") ||
        nav.getCurrentDestination().getLabel().equals("fragment_setting"))
        {
            super.onBackPressed();
            menuBtn.setVisibility(View.VISIBLE);

        }else if(nav.getCurrentDestination().getLabel().equals("OnlineAsTaxi12"))
        {
            finish();
        }else if(nav.getCurrentDestination().getLabel().equals("fragment_create_ride")){
            sharedHelper.putKey(this, "CountrySelectedTripFromId", "");
            sharedHelper.putKey(this, "CountrySelectedTripToId", "");
            sharedHelper.putKey(this, "LatStart", "");
            sharedHelper.putKey(this, "LatEnd", "");

            super.onBackPressed();
        }
        else if(sharedHelper.getKey(this , "exitPage").toString().equals("false")){
            Toast.makeText(this, getResources().getString(R.string.youNeedToFinishTripToGoBack), Toast.LENGTH_SHORT).show();
        }
        else
        super.onBackPressed();

    }


    public final void navigation() {
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_Home);
//        OnlineAsMicrobus5 onlineAsMicrobus5 = new OnlineAsMicrobus5();
//        manager.beginTransaction().replace(R.id.relativ1, onlineAsMicrobus5).commit();
        if (DontShow == true) {
//            ProfileAsTaxi7 profileAsTaxi7 = new ProfileAsTaxi7();
//            manager.beginTransaction().replace(R.id.relativ1, profileAsTaxi7).commit();
        }
        // getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fragment2).commit();

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

//    public void goSignup(View view) {
//        startActivity(new Intent(getApplicationContext(), Delivery.class));
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        switch (menuItem.getItemId()) {
            case R.id.nav_Home:
                menuBtn.setVisibility(View.VISIBLE);
                drawerLayout.closeDrawer(GravityCompat.START);
//                OnlineAsTaxi6 onlineAsTaxi6 = new OnlineAsTaxi6();
//                manager.beginTransaction().replace(R.id.relativ1, onlineAsTaxi6).commit();
                //getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fragment).commit();
//                Navigation.findNavController()
                return true;
            case R.id.nav_Rides:
                // startActivity(new Intent(getApplicationContext(), Rides.class));
                menuBtn.setVisibility(View.VISIBLE);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, Rides.class));
                return true;
            case R.id.nav_Wallet:
                menuBtn.setVisibility(View.GONE);
                drawerLayout.closeDrawer(GravityCompat.START);
//                Wallet wallet = new Wallet();
//                manager.beginTransaction().replace(R.id.relativ1, wallet).commit();
                // getSupportFragmentManager().beginTransaction().replace(R.id.re,wallet).commit();
                if(nav.getGraph().getStartDestination() != nav.getCurrentDestination().getId()) {
                    nav.popBackStack(R.id.online5,false);
                    nav.navigate(R.id.action_online5_to_wallet);
                }
                else
                {
                    nav.navigate(R.id.action_online5_to_wallet);
                }
                return true;
            case R.id.nav_Notification:
                menuBtn.setVisibility(View.GONE);
                drawerLayout.closeDrawer(GravityCompat.START);

//                Notification11_1 notification11_1 = new Notification11_1();
//                manager.beginTransaction().replace(R.id.relativ1, notification11_1).commit();
                //getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fragment2).commit();
                if(nav.getGraph().getStartDestination() != nav.getCurrentDestination().getId()) {
                    nav.popBackStack(R.id.online5,false);
                    nav.navigate(R.id.action_online5_to_notification11_1);
                }
                else
                {
                    nav.navigate(R.id.action_online5_to_notification11_1);
                }
                return true;
            case R.id.nav_Settings:
                menuBtn.setVisibility(View.GONE);
                drawerLayout.closeDrawer(GravityCompat.START);

//                Notification11_1 notification11_1 = new Notification11_1();
//                manager.beginTransaction().replace(R.id.relativ1, notification11_1).commit();
                //getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fragment2).commit();
                if(nav.getGraph().getStartDestination() != nav.getCurrentDestination().getId()) {
                    nav.popBackStack(R.id.online5,false);
                    nav.navigate(R.id.action_online5_to_settingFragment);
                }
                else
                {
                    nav.navigate(R.id.action_online5_to_settingFragment);
                }                return true;

            default:
                return false;
        }
    }

    private void initiation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuBtn = findViewById(R.id.menu);
        home_content = findViewById(R.id.home_content);


    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedHelper SharedHelpers = new SharedHelper();
        SharedHelpers.putKey(this , "OPEN" , "");

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedHelper SharedHelpers = new SharedHelper();
        SharedHelpers.putKey(this , "OPEN" , "OPEN");
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedHelper SharedHelpers = new SharedHelper();
        SharedHelpers.putKey(this , "OPEN" , "");
    }
}
