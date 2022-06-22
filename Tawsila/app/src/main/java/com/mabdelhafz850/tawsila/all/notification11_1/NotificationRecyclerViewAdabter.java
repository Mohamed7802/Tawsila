package com.mabdelhafz850.tawsila.all.notification11_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxina2020.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationRecyclerViewAdabter extends RecyclerView.Adapter<NotificationRecyclerViewAdabter.ViewHolderNotification> {
//    public NotificationRecyclerViewAdabter(Context context) {
//        this.context = context;
//    }

    View view;
    Context context;
    private ArrayList<String> data;

    public NotificationRecyclerViewAdabter(ArrayList<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolderNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        ViewHolderNotification viewHolderNotification = new ViewHolderNotification(view);
        return viewHolderNotification;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderNotification holder, int position) {
        holder.notifications_name.setText(data.get(position));
        //holder.notifications_details.setText(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(String item, int position) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<String> getData() {
        return data;
    }

    class ViewHolderNotification extends RecyclerView.ViewHolder {

        CardView notifications_card;
        TextView notifications_name;
        TextView notifications_details;
        CircleImageView notifications_icon;

        public ViewHolderNotification(@NonNull View itemView) {
            super(itemView);
            notifications_name = itemView.findViewById(R.id.notification_title);
            notifications_details = itemView.findViewById(R.id.notification_details);
            notifications_icon = itemView.findViewById(R.id.notification_icon);

        }
    }
}


