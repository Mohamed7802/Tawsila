package com.mabdelhafz850.tawsila.ui.activity.all.online.busHome

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.databinding.FragmentOnlineAsTaxi12Binding
import com.mabdelhafz850.tawsila.ui.activity.auth.login.LoginViewModel
import com.mabdelhafz850.tawsila.ui.activity.response.UpcomingBusData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

/**
 * A simple [Fragment] subclass.
 */
class OnlineAsTaxi12 : Fragment()  {
    lateinit var  binding : FragmentOnlineAsTaxi12Binding
//    lateinit var map: GoogleMap
    lateinit var mLocationManager : LocationManager
    var busViewModel: BusViewModel? = null
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    var users: MutableLiveData<List<UpcomingBusData>> = MutableLiveData()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnlineAsTaxi12Binding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        busViewModel = ViewModelProviders.of(this).get(BusViewModel::class.java)

        binding.vm = busViewModel

        binding.dateFromId.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                binding.dateFromId.setText("$dayOfMonth - $monthOfYear - $year")
                busViewModel?.dateFromSearch?.value = "$year-$monthOfYear-$dayOfMonth"

            }, year, month, day)
            dpd.show()
        }

        binding.dateToId.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                binding.dateToId.setText("$dayOfMonth - $monthOfYear - $year")
                busViewModel?.dateToSearch?.value = "$year-$monthOfYear-$dayOfMonth"
                busViewModel?.applySearch(requireContext())

            }, year, month, day)

            dpd.show()
        }

        binding.createRideBtnId.setOnClickListener {
//            val dialog = AlertDialog.Builder(requireContext())
//            dialog.setMessage(resources.getString(R.string.localTripOrInternational))
//            dialog.setPositiveButton(resources.getString(R.string.localTrip)) { dialog, which ->
//                Navigation.findNavController(it).navigate(R.id.action_onlineAsTaxi12_to_createRide)
//            }
//            dialog.setNegativeButton(resources.getString(R.string.InternationalTrip)) {dialog, which ->
//                Navigation.findNavController(it).navigate(R.id.action_onlineAsTaxi12_to_nationalityTrips)
//            }
//            dialog.create().show()
            Navigation.findNavController(it).navigate(R.id.action_onlineAsTaxi12_to_createRide)

        }

        busViewModel?.getUpcoming(requireContext())
        busViewModel?.users?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            users.value = it
            binding.upcomingRecyclerViewId.adapter = BusTripsHomeRecyclerAdapter(users,requireContext())
        })

        return binding.root
    }




}
