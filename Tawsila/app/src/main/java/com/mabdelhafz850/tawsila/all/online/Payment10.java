package com.mabdelhafz850.tawsila.all.online;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mabdelhafz850.tawsila.R;

import static com.mabdelhafz850.tawsila.ui.activity.all.chat.Chat.currantFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class Payment10 extends Fragment {

    ImageButton menu;
    Button finishRideBtn ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment10, container, false);
        currantFrag = R.layout.fragment_online_as_taxi9;
        menu = getActivity().findViewById(R.id.menu);
        finishRideBtn = view.findViewById(R.id.finishRideBtnId);
        finishRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OnlineAsTaxi11 onlineAsTaxi11 = new OnlineAsTaxi11();
//                FragmentManager manager = getFragmentManager();
//                manager.beginTransaction().replace(R.id.relativ1, onlineAsTaxi11).commit();
//                Navigation.findNavController(v).navigate(R.id.onlineAsTaxi11);
            }
        });
        menu.setVisibility(View.GONE);
        return view;
    }
}
