package com.mabdelhafz850.tawsila.all.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxina2020.R;


public class ChatRecyclerViewAdabter extends RecyclerView.Adapter<ViewHolderChat> {
    View view;
    Context context;

    public ChatRecyclerViewAdabter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        ViewHolderChat viewHolderChat = new ViewHolderChat(view);
        return viewHolderChat;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderChat holder, int position) {
//        holder.tvUserName.setText("***سع");
//        holder.tvOrderName.setText("قهوة حجم كبير");
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(holder.itemView.getContext(), DelievryPlaceActivity2.class);
//                view.getContext().startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

class ViewHolderChat extends RecyclerView.ViewHolder {

    TextView tvUserName;
    TextView tvOrderName;

    public ViewHolderChat(@NonNull View itemView) {
        super(itemView);
//        tvUserName = itemView.findViewById(R.id.tv_userName);
//        tvOrderName = itemView.findViewById(R.id.tv_OrderName);
    }
}