package com.mabdelhafz850.tawsila.ui.activity.all.online.busSeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mabdelhafz850.tawsila.databinding.FragmentOnlineAsTaxi121Binding
import com.mabdelhafz850.tawsila.ui.activity.all.online.home.HomeViewModel
import com.mabdelhafz850.tawsila.ui.activity.response.ShowTripSeatsSeat

/**
 * A simple [Fragment] subclass.
 */
class OnlineAsTaxi12_1 : Fragment() {

    lateinit var  binding : FragmentOnlineAsTaxi121Binding
    var busSeatViewModel: BusSeatViewModel? = null
    var seats: MutableLiveData<List<ShowTripSeatsSeat>> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnlineAsTaxi121Binding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        busSeatViewModel = ViewModelProviders.of(this).get(BusSeatViewModel::class.java)
        binding.vm = busSeatViewModel

        val args = arguments?.let { OnlineAsTaxi12_1Args.fromBundle(it) }
        busSeatViewModel!!.tripId.value = args?.tripId

        busSeatViewModel!!.getSeats(requireContext())
        busSeatViewModel!!.seats.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())
            {
                seats.value = it
                binding.seatsRecyclerViewId.adapter = BusSeatRecyclerAdapter(seats,context,busSeatViewModel!!)
            }
        })

        busSeatViewModel!!.notifyRecycler.observe(viewLifecycleOwner, Observer {
            if(it)
                binding.seatsRecyclerViewId.adapter?.notifyDataSetChanged()
        })

        binding.btnConfirm.setOnClickListener {
            activity?.onBackPressed()
        }



        return binding.root

    }

}
