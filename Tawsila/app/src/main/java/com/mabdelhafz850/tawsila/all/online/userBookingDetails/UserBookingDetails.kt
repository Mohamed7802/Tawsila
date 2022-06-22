package com.mabdelhafz850.tawsila.ui.activity.all.online.userBookingDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.databinding.FragmentUserBookingDetailsBinding
import com.mabdelhafz850.tawsila.ui.activity.all.online.rideDetails.UsersTripRecyclerAdapter
import com.mabdelhafz850.tawsila.ui.activity.response.UserBookingDetailsData
import com.mabdelhafz850.tawsila.ui.activity.response.UserSeatsData
import com.mabdelhafz850.tawsila.ui.activity.response.UserSelectedSeatData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper

/**
 * A simple [Fragment] subclass.
 */
class UserBookingDetails : Fragment() {

    lateinit var binding : FragmentUserBookingDetailsBinding
    lateinit var model : UserBookingViewModel
    var users: MutableLiveData<List<UserBookingDetailsData>> = MutableLiveData()
    lateinit var sharedHelper :SharedHelper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentUserBookingDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        model = ViewModelProviders.of(this).get(UserBookingViewModel::class.java)
        binding.vm = model
        sharedHelper = SharedHelper()

        model.userId.value = sharedHelper.getKey(requireContext(),"NotificationUserID")?.toInt()
        model.tripId.value = sharedHelper.getKey(requireContext(),"NotificationIdTrip")?.toInt()

        Log.i("UserBookingDataTripId",sharedHelper.getKey(requireContext(),"NotificationIdTrip"))
        Log.i("UserBookingDataUserId",sharedHelper.getKey(requireContext(),"NotificationUserID"))


//        val args = arguments?.let { UserBookingDetailsArgs.fromBundle(it) }
//        model.date.value = args?.date!!
//        model.locationTo.value = args.locationTo
//        model.locationFrom.value = args.locationFrom
//        model.name.value = args.name
//        model.image.value = args.image
//        model.price.value = args.price
//        model.tripId.value = args.tripId
//        model.userId.value = args.userId.toInt()




        model.getUserSeatData(requireContext())
        model.users.observe(viewLifecycleOwner, Observer {
            users.value = it
            binding.seatsRecyclerViewId.adapter = UserBookingRecyclerAdapter(users,requireContext() , model.userId.value.toString())
        })

        return binding.root
    }

}
