package com.mabdelhafz850.tawsila.ui.activity.all.online.createRide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mabdelhafz850.tawsila.databinding.FragmentLocationRideBinding
import com.mabdelhafz850.tawsila.ui.activity.response.AllCityData
import com.mabdelhafz850.tawsila.ui.activity.response.AreaByIdData
import com.mabdelhafz850.tawsila.ui.activity.response.ShowAreaCityData

/**
 * A simple [Fragment] subclass.
 */
class LocationRideFragment( var createRideViewModel: CreateRideViewModel , var locationType:String , var dialogType : String) : DialogFragment() {

    lateinit var binding:FragmentLocationRideBinding
    lateinit var model : LocationRideViewModel
    var citys: MutableLiveData<List<ShowAreaCityData>> = MutableLiveData()
    var areasFrom: MutableLiveData<List<AreaByIdData>> = MutableLiveData()
    var areasTo: MutableLiveData<List<AreaByIdData>> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentLocationRideBinding.inflate(inflater)
        binding.lifecycleOwner = this
        model = ViewModelProviders.of(this).get(LocationRideViewModel::class.java)
        binding.vm = model

//        if(dialogType == "cityDialog") {
        if(locationType == "locationFrom") {
            model.loadCityFrom(requireActivity(), createRideViewModel.countryFromIdSpinner.value!!)
            model.citys.observe(viewLifecycleOwner, Observer {
                citys.value = it
                binding.locationRecyclerViewId.adapter = LocationRideRecyclerAdapter(citys, requireActivity(), createRideViewModel, locationType, dialog, model)
            })
        }
        if(locationType == "locationTo") {
            model.loadCityTo(requireActivity(), createRideViewModel.countryToIdSpinner.value!!)
            model.citys.observe(viewLifecycleOwner, Observer {
                citys.value = it
                binding.locationRecyclerViewId.adapter = LocationRideRecyclerAdapter(citys, requireActivity(), createRideViewModel, locationType, dialog, model)
            })
        }
//        }
//        if(dialogType == "areaDialog")
//        {
//            Log.i("In Area Dialog","true")
//            model.loadArea(requireActivity())
//            model.areasFrom.observe(viewLifecycleOwner, Observer {
//                areasFrom.value = it
//                Log.i("AreaFromObserver",areasFrom.value.toString())
//                binding.locationRecyclerViewId.adapter?.notifyDataSetChanged()
//                binding.locationRecyclerViewId.adapter = LocationRideAreaRecyclerAdapter(areasFrom, requireActivity(), createRideViewModel, locationType, dialog , model)
//                binding.locationRecyclerViewId.adapter?.notifyDataSetChanged()
//
//            })
//
//            model.areasTo.observe(viewLifecycleOwner, Observer {
//                areasTo.value = it
//                Log.i("AreaToObserver",it.toString())
//                binding.locationRecyclerViewId.adapter?.notifyDataSetChanged()
//                binding.locationRecyclerViewId.adapter = LocationRideAreaRecyclerAdapter(areasTo, requireActivity(), createRideViewModel, locationType, dialog , model)
//                binding.locationRecyclerViewId.adapter?.notifyDataSetChanged()
//            })


//        }

//        binding.submitBtnId.setOnClickListener {
//            this.dismiss()
//        }




        return binding.root
    }

}
