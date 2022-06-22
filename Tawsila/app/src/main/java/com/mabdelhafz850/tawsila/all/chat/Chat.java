package com.mabdelhafz850.tawsila.all.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxina2020.R;
import com.example.taxina2020.databinding.FragmentChatBinding;
import com.example.taxina2020.ui.activity.utils.SharedHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoohna.shoohna.util.base.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;


public class Chat extends BaseFragment {

    public static int currantFrag;

//    RecyclerView rv_chat;
//    ImageButton menu, back;
    private Calendar calendar;
    private int  minuteT, hourT;
    private String TripID ;
    private String UserID ;
    private String UserPhone ;
    public SharedHelper sharedHelper;
    public FragmentChatBinding binding;
    public ArrayList<Message> Messages;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater);

        sharedHelper = new SharedHelper();
        TripID = sharedHelper.getKey(getContext(),"ChatTripId");
        UserID = sharedHelper.getKey(getContext(),"ChatUserId");
        UserPhone = sharedHelper.getKey(getContext(),"ChatUserPhone");
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(binding.messageId.getText().toString());
            }
        });

        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserPhone.isEmpty())
                {
                    if (!UserPhone.equals(""))
                    {

                        if(checkPermission()){
                            callingProcess(UserPhone);
                        }else{
                            requestPermission();

                        }

                    }
                }
            }
        });

        Picasso.get().load(sharedHelper.getKey(getContext(),"ChatUserImage")).into(binding.userImage);
        binding.userName.setText(sharedHelper.getKey(getContext(),"ChatUserName"));
        getMessages();
        sharedHelper.putKey(requireContext(),"HomeControllerEnable","1");

//        currantFrag = R.layout.fragment_online_as_taxi8;
//        menu = getActivity().findViewById(R.id.menu);
//        menu.setVisibility(View.GONE);
//
//        back = view.findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                OnlineAsTaxi8 onlineAsTaxi8 = new OnlineAsTaxi8();
////                FragmentManager manager = getFragmentManager();
////                manager.beginTransaction().replace(R.id.relativ1, onlineAsTaxi8).commit();
//
////                Bundle bundle = new Bundle();
////                bundle.putBoolean("openSecondProcess", true);
//                getActivity().onBackPressed();
//
////                Navigation.findNavController(v).navigate(R.id.action_chat_to_online5,bundle);
//
//
//            }
//        });
//
//
//        rv_chat = view.findViewById(R.id.rv_chat);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
//        ChatRecyclerViewAdabter chatRecyclerViewAdabter = new ChatRecyclerViewAdabter(view.getContext());
//        rv_chat.setAdapter(chatRecyclerViewAdabter);
//        rv_chat.setLayoutManager(linearLayoutManager);
        return binding.getRoot();
    }

    private void sendMessage(String message){

        calendar = Calendar.getInstance();
        hourT = calendar.get(Calendar.HOUR_OF_DAY);
        minuteT = calendar.get(Calendar.MINUTE);

        String CurrentTime = hourT+"-"+minuteT ;
        DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child(TripID);
        DB.child("Driver").setValue(UserID);
        String id = DB.push().getKey();
        DB.child("Messages").child(id).child("Content").setValue(message);
        DB.child("Messages").child(id).child("Time").setValue(CurrentTime);
        DB.child("Messages").child(id).child("User").setValue("0");
        binding.messageId.getText().clear();
//        chatRecyclerViewAdabter.notifyDataSetChanged();
//        contentMessage.setText("");

    }

    private void getMessages(){
//        this.progressDialog.setTitle("Fetch Messages process is loading");
//        this.progressDialog.setMessage("Please wait ..... ");
//        this.progressDialog.show();
//        this.progressDialog.setCanceledOnTouchOutside(false);
//        this.progressDialog.setCancelable(false);
//        progressDialog.show();

        DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child(TripID).child("Messages");
        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Messages = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        if (snapshot.child("User").exists())
                        {

                            Message message = new Message();
                            message.setId(snapshot.child("User").getValue().toString());
                            message.setContent(snapshot.child("Content").getValue().toString());
                            if (snapshot.child("Time").getValue() != null)
                            {
                                message.setCurrentTime(snapshot.child("Time").getValue().toString());
                            }
                            Messages.add(message);
                            binding.rvChat.setAdapter( new RecyclerViewAdapter(Messages));

                        }

                    }
//                    progressDialog.dismiss();
                }
                else {
//                    progressDialog.dismiss();
//                    binding.rvChat.setAdapter( new RecyclerViewAdapter(Messages));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                progressDialog.dismiss();
                binding.rvChat.setAdapter( new RecyclerViewAdapter(Messages));

                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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
