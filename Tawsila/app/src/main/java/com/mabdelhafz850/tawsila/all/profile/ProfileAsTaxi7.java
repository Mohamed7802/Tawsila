package com.mabdelhafz850.tawsila.all.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mabdelhafz850.tawsila.R;
import com.mabdelhafz850.tawsila.ui.activity.all.online.OnlineAsTaxi8;

import static com.mabdelhafz850.tawsila.ui.activity.rides.UpcomingTap.DontShow;
import static com.mabdelhafz850.tawsila.ui.activity.all.chat.Chat.currantFrag;


public class ProfileAsTaxi7 extends Fragment {

    Button btn_Confirm;
    ImageView menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_taxi7, container, false);
        currantFrag = R.layout.fragment_online_as_taxi6;
        menu = getActivity().findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);


        btn_Confirm = view.findViewById(R.id.btn_Confirm);

        if (DontShow == true) {
            btn_Confirm.setVisibility(View.GONE);
            currantFrag = 0;
            DontShow = false;
        } else
            btn_Confirm.setVisibility(View.VISIBLE);

        btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnlineAsTaxi8 onlineAsTaxi8 = new OnlineAsTaxi8();
                FragmentManager manager = getFragmentManager();
//                manager.beginTransaction().replace(R.id.relativ1, onlineAsTaxi8).commit();
            }
        });


        return view;
    }
}
