package com.mabdelhafz850.tawsila.all.online;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mabdelhafz850.tawsila.R;
import com.mabdelhafz850.tawsila.ui.activity.all.profile.ProfileFromMicrobus5_1_1;

import static com.mabdelhafz850.tawsila.ui.activity.all.chat.Chat.currantFrag;
import static com.mabdelhafz850.tawsila.ui.activity.all.online.OnlineAsTaxi6.driverIs;


public class OnlineMicrobusDetails5_1 extends Fragment {

    Button btn_Confirm;
    ImageButton menu;
    ImageView iv_1conf;
    boolean myBool ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_microbus_details5_1, container, false);
        driverIs = "microbus";
        currantFrag = R.layout.fragment_online_as_microbus5;
        menu = getActivity().findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            myBool = bundle.getBoolean("key", false);
//        }
        btn_Confirm = view.findViewById(R.id.btn_Confirm);
        btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(myBool)
//                {
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("key", true);
//                    OnlineMicrobusDetails5_1 microbusDetails5_1 = new OnlineMicrobusDetails5_1();
//                    FragmentManager manager = getFragmentManager();
//                    microbusDetails5_1.setArguments(bundle);
//                    manager.popBackStackImmediate();
//                }
//                else {
                    OnlineAsTaxi9 onlineAsTaxi9 = new OnlineAsTaxi9();
                    FragmentManager manager = getFragmentManager();
//                    manager.beginTransaction().replace(R.id.relativ1, onlineAsTaxi9).commit();
                    // manager.beginTransaction().replace(R.id.main_fragment,onlineAsTaxi9,onlineAsTaxi9.getTag()).commit();
//                }
            }
        });
        iv_1conf = view.findViewById(R.id.iv_1conf);
        iv_1conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFromMicrobus5_1_1 profile = new ProfileFromMicrobus5_1_1();
                FragmentManager manager = getFragmentManager();
//                manager.beginTransaction().replace(R.id.relativ1, profile).commit();
            }
        });

        return view;
    }


}
