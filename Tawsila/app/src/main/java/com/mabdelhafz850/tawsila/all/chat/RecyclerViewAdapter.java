package com.mabdelhafz850.tawsila.all.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxina2020.BR;
import com.example.taxina2020.R;
import com.example.taxina2020.databinding.ItemChatBackBinding;
import com.example.taxina2020.databinding.ItemChatBinding;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    // replace 1 with 0
    private static final int VIEW_TYPE_MESSAGE_SENT = 0;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 1;

    private ArrayList<Message> MessageList;

    public RecyclerViewAdapter( ArrayList<Message> MessageList) {
        this.MessageList = MessageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            // self message
            ItemChatBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat,parent,false);

            return new SentMessageHolder(binding);

        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            // WatBot message
            ItemChatBackBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_chat_back,parent,false);

            return new ReceivedMessageHolder(binding);

        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message Message =  MessageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(Message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(Message);
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        Message message = MessageList.get(position);
        if (message.getId() != null && message.getId().equals("0")) {
            return VIEW_TYPE_MESSAGE_SENT;
        }
        else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    private static class SentMessageHolder extends RecyclerView.ViewHolder {

        ItemChatBinding binding;
        SentMessageHolder(ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(Object obj) {
            binding.setVariable(BR.Model, obj);
            binding.executePendingBindings();
        }
    }

    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        ItemChatBackBinding binding;
        ReceivedMessageHolder(ItemChatBackBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(Object obj) {
            binding.setVariable(BR.Model, obj);
            binding.executePendingBindings();
        }
    }
}