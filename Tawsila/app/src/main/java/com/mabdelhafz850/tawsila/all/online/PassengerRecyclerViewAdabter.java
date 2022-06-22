package com.mabdelhafz850.tawsila.all.online;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mabdelhafz850.tawsila.R;


public class PassengerRecyclerViewAdabter extends RecyclerView.Adapter<ViewHolderPassenger> {
    View view;
    Context context;

    public PassengerRecyclerViewAdabter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderPassenger onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_user_taxi, parent, false);
        ViewHolderPassenger viewHolderNotification = new ViewHolderPassenger(view);
        return viewHolderNotification;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPassenger holder, int position) {


        holder.tv_tapForMor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ProfileAsTaxi7 profileAsTaxi7 = new ProfileAsTaxi7();
//                FragmentManager manager=context.getFragmentManager();
//                manager.beginTransaction().replace(R.id.main_fragment,microbusDetails5_1,microbusDetails5_1.getTag()).commit();
//                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.relativ1, profileAsTaxi7); // Add your fragment class
//                transaction.addToBackStack(null);
//                transaction.commit();
//                Navigation.findNavController(view).navigate(R.id.action_onlineAsTaxi6_to_profile5_1_1);

            }
        });

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                OnlineAsTaxi8 OnlineAsTaxi8 = new OnlineAsTaxi8();
//                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.relativ1, OnlineAsTaxi8); // Add your fragment class
//                transaction.addToBackStack(null);
//                transaction.commit();

//                Navigation.findNavController(view).navigate(R.id.action_onlineAsTaxi6_to_onlineAsTaxi8);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 8;
    }
}

class ViewHolderPassenger extends RecyclerView.ViewHolder {

    TextView tv_tapForMor;
    Button btnAccept;

    public ViewHolderPassenger(@NonNull View itemView) {
        super(itemView);
        tv_tapForMor = itemView.findViewById(R.id.tv_tapForMore);
        btnAccept = itemView.findViewById(R.id.btnAccept);

    }
}