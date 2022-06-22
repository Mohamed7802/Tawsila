package com.mabdelhafz850.tawsila.ui.activity.all.online.driverProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.mabdelhafz850.tawsila.databinding.FragmentProfileBinding
import com.shoohna.shoohna.util.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment() {

    lateinit var binding : FragmentProfileBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val model = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        binding.vm = model

        model.getUser(requireContext(),binding.profileImgId)

//        try {
//            model.fName.value = sharedHelper.getKey(requireContext(),"fName")
//            model.lName.value = sharedHelper.getKey(requireContext(),"lName")
//            model.email.value = sharedHelper.getKey(requireContext(),"E-Mail")
//            model.phone.value = sharedHelper.getKey(requireContext(),"Phone")
//
//            Glide.with(this).load(sharedHelper.getKey(requireContext(), "Image")).into(binding.profileImgId)
//
//        }catch (e:Exception)
//        {
//            Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()
//        }

        binding.editImgId.setOnClickListener {
            showPictureDialog(binding.profileImgId)
            model.selectedImage.value = true
        }


        return binding.root
    }


}
