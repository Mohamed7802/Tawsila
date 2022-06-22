package com.mabdelhafz850.tawsila.ui.activity.all.online

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.databinding.FragmentOnlineAsTaxi11Binding


/**
 * A simple [Fragment] subclass.
 */
class OnlineAsTaxi11 : Fragment() {

    lateinit var  binding : FragmentOnlineAsTaxi11Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnlineAsTaxi11Binding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this

        binding.busDriverBtnId.setOnClickListener {
//            val onlineAsMicrobus = OnlineAsMicrobus5()
//            val manager = fragmentManager
//            val bundle = Bundle()
//            bundle.putBoolean("key", true)
//            onlineAsMicrobus.arguments = bundle
//            manager!!.beginTransaction().replace(R.id.relativ1, onlineAsMicrobus).commit()
//            val onlineTaxi12 = OnlineAsTaxi12()
//            val manager = fragmentManager
//            manager!!.beginTransaction().replace(R.id.relativ1 , onlineTaxi12).commit()
            Navigation.findNavController(it).navigate(R.id.onlineAsTaxi12)
        }

        binding.easyAccessBtnId.setOnClickListener {
            Navigation.findNavController(it).popBackStack(R.id.online5,false)
        }


        return binding.root
    }

}
