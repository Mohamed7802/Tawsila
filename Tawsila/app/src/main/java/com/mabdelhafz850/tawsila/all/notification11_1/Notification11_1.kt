package com.mabdelhafz850.tawsila.ui.activity.all.notification11_1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mabdelhafz850.tawsila.databinding.FragmentNotification111Binding
import com.mabdelhafz850.tawsila.ui.activity.response.NotificationData
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Notification11_1 : Fragment() {
//    var rv_notification: RecyclerView? = null
//    var constrain_notification: CoordinatorLayout? = null
//    var stringArrayList = ArrayList<String>()
//    var notificationRecyclerViewAdabter: NotificationRecyclerViewAdabter? = null
    lateinit var binding: FragmentNotification111Binding
    var notifications: MutableLiveData<List<NotificationData>> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotification111Binding.inflate(inflater)
        binding.lifecycleOwner = this
        val model = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
        binding.vm = model

        model.getNotification(requireContext())
        model.notifications.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it.isNotEmpty())
            {
                notifications.value = it
                binding.rvNotification.adapter = NotificationRecyclerAdapter(notifications,requireContext())
            }
        })

//        currantFrag = R.layout.fragment_online_as_taxi6;
//        constrain_notification = view.findViewById(R.id.constrain_notification);
//        rv_notification = view.findViewById(R.id.rv_notification);
//        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL,false);
//        // notificationRecyclerViewAdabter = new NotificationRecyclerViewAdabter();
//        // rv_notification.setAdapter(notificationRecyclerViewAdabter);
//        // rv_notification.setLayoutManager(linearLayoutManager);
//        populateRecyclerView(view);
//        enableSwipeToDeleteAndUndo(view);
        return binding.root
    }

//    private fun populateRecyclerView(view: View) {
//        for (i in 0..14) stringArrayList.add("Taxina")
//        val layoutManager = LinearLayoutManager(view.context,
//                LinearLayoutManager.VERTICAL, false)
//        notificationRecyclerViewAdabter = NotificationRecyclerViewAdabter(stringArrayList)
//        rv_notification!!.adapter = notificationRecyclerViewAdabter
//        rv_notification!!.layoutManager = layoutManager
//        Log.e("", "fill:54321111111 ")
//    }
//
//    private fun enableSwipeToDeleteAndUndo(view: View) {
//        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(view.context) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
//                val position = viewHolder.adapterPosition
//                val item = notificationRecyclerViewAdabter!!.data[position]
//                notificationRecyclerViewAdabter!!.removeItem(position)
//                val snackbar = Snackbar
//                        .make(constrain_notification!!, "Item was removed from the list.", Snackbar.LENGTH_LONG)
//                snackbar.setAction("UNDO") {
//                    notificationRecyclerViewAdabter!!.restoreItem(item, position)
//                    rv_notification!!.scrollToPosition(position)
//                }
//                snackbar.setActionTextColor(Color.YELLOW)
//                snackbar.show()
//            }
//        }
//        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
//        itemTouchhelper.attachToRecyclerView(rv_notification)
//    }
}