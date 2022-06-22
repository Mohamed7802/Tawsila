package com.mabdelhafz850.tawsila.ui.activity.all.online.createRide

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.databinding.FragmentCreateRideBinding
import com.mabdelhafz850.tawsila.ui.activity.response.AllCityData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CreateRide : Fragment() {

    lateinit var binding : FragmentCreateRideBinding
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val hour = c.get(Calendar.HOUR)
    val min = c.get(Calendar.MINUTE)
    lateinit var model : CreateRideViewModel
    private lateinit var myDialog: AlertDialog
    var citys: MutableLiveData<List<AllCityData>> = MutableLiveData()
    val list: MutableList<String> = ArrayList()
    lateinit var sharedHelper : SharedHelper
    lateinit var placeName :String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateRideBinding.inflate(inflater)
        binding.lifecycleOwner = this

        model = ViewModelProviders.of(this).get(CreateRideViewModel::class.java)
        binding.vm = model

        sharedHelper = SharedHelper()
//        model.loadCity(requireContext())
//        model.citys.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            if(it.isNotEmpty()) {
//                citys.value = it
//            }
//        })

        binding.dayTxtId.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                binding.dayTxtId.setText("$dayOfMonth")
                binding.monthTxtId.text = "${monthOfYear + 1}"
                binding.yearTxtId.text = "$year"
                model.date.value = "$year/${monthOfYear + 1}/$dayOfMonth"
            }, year, month, day)

            dpd.datePicker.minDate = System.currentTimeMillis() - 1000

            dpd.show()
        }

        binding.startHourTxtId.setOnClickListener {
            val time = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                binding.startHourTxtId.setText("$hourOfDay : $minute ${onTimeSet(view, hourOfDay, minute)}")
                model.fromTime.value = "$hourOfDay:$minute"
            }, hour, min, false)

//            time.updateTime(hour,min)

            time.show()
        }

        binding.endHourTxtId.setOnClickListener {
            val time = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                binding.endHourTxtId.setText("$hourOfDay : $minute ${onTimeSet(view, hourOfDay, minute)}")
                model.toTime.value = "$hourOfDay:$minute"
            }, hour, min, false)

//            time.updateTime(hour,min)
            time.show()
        }

        binding.cityFromBtnId.setOnClickListener {
            val locationRideFragment = LocationRideFragment(model, "locationFrom", "cityDialog")
            locationRideFragment.show(requireActivity().supportFragmentManager, "")
        }

        binding.locationToBtnId.setOnClickListener {
            val locationRideFragment = LocationRideFragment(model, "locationTo", "cityDialog")
            locationRideFragment.show(requireActivity().supportFragmentManager, "")
        }

        binding.areaFromId.setOnClickListener {
            if(sharedHelper.getKey(requireContext(), "CountrySelectedTripFromId")?.isEmpty()!!)
                Toast.makeText(requireContext(), resources.getString(R.string.pleaseSelectCityFirst), Toast.LENGTH_SHORT).show()
            else {
                val locationRideFragment = LocationAreaFragment(model, "locationFrom", "areaDialog")
                locationRideFragment.show(requireActivity().supportFragmentManager, "")
            }
        }

        binding.areaToId.setOnClickListener {
            if(sharedHelper.getKey(requireContext(), "CountrySelectedTripToId")?.isEmpty()!!)
                Toast.makeText(requireContext(), resources.getString(R.string.pleaseSelectCityFirst), Toast.LENGTH_SHORT).show()
            else {
                val locationRideFragment = LocationAreaFragment(model, "locationTo", "areaDialog")
                locationRideFragment.show(requireActivity().supportFragmentManager, "")
            }
        }

        binding.addAreaFromId.setOnClickListener {

//                startActivity(new Intent(getActivity(), MapsFavoriteActivity.class));
            if (sharedHelper.getKey(requireContext(), "CountrySelectedTripFromId")?.isNotEmpty()!! )
                if(model.locationFrom.value?.isEmpty()!!){
                    showDialog("From")
                }else {
                    startActivityForResult(Intent(activity, MapsAreaActivity::class.java).putExtra("EnterType", "From"), 1)
                }
            else
                Toast.makeText(requireContext(), resources.getString(R.string.pleaseSelectCityFirst), Toast.LENGTH_SHORT).show()

        }

        binding.addAreaToId.setOnClickListener {
            if (sharedHelper.getKey(requireContext(), "CountrySelectedTripToId")?.isNotEmpty()!! )
                if(model.locationTo.value?.isEmpty()!!){
                    showDialog("To")
                }else {
                    startActivityForResult(Intent(activity, MapsAreaActivity::class.java).putExtra("EnterType", "To"), 2)
                }
            else
                Toast.makeText(requireContext(), resources.getString(R.string.pleaseSelectCityFirst), Toast.LENGTH_SHORT).show()

        }

//        binding.fromAreaId.setOnClickListener {
//            val locationRideFragment = LocationRideFragment(model,"locationFrom" , "areaDialog")
//            locationRideFragment.show(requireActivity().supportFragmentManager,"")
//        }
//        binding.toAreaId.setOnClickListener {
//            val locationRideFragment = LocationRideFragment(model,"locationTo" , "areaDialog")
//            locationRideFragment.show(requireActivity().supportFragmentManager,"")
//        }



        model.getCountries(requireContext())
        binding.fromCitySpinner.setItems(model.countriesList)
        binding.fromCitySpinner.setOnItemSelectedListener { view, position, id, item ->
//            Toast.makeText(requireContext(), "Position : $position",Toast.LENGTH_SHORT).show()


            for (i in model.countriesListModel){
                if(i.name==item.toString()){
                    model.countryFromIdSpinner.value = i.id
                }
            }

        }

        binding.toCitySpinner.setItems(model.countriesList)
        binding.toCitySpinner.setOnItemSelectedListener { view, position, id, item ->
//            Toast.makeText(requireContext(), "Position : $position",Toast.LENGTH_SHORT).show()
            for (i in model.countriesListModel){
                if(i.name==item.toString()){
                    model.countryToIdSpinner.value = i.id
                }
            }

//            model.countryToIdSpinner.value = position+1
        }

        return binding.root
    }
//
//    private fun showAlert() {
//        //INSTANTIATE ALERTDIALOG BUILDER
//        val myBuilder = AlertDialog.Builder(requireActivity())
//        //DATA SOURCE
//        val stringsOrNulls = arrayOfNulls<String>(citys.value?.size!!) // returns Array<String?>
//
//        for ( i in citys.value!!)
//        {
//            stringsOrNulls[i.id] = i.name.toString()
//        }
//        //SET PROPERTIES USING METHOD CHAINING
//        myBuilder.setTitle("Science Tips").setItems(stringsOrNulls) { dialogInterface, position ->
//            Toast.makeText(requireActivity(),"", Toast.LENGTH_SHORT).show()
//        }
//        //CREATE DIALOG
//        myDialog = myBuilder.create()
//        //SHOW DIALOG
//        myDialog.show()
//    }

//    val someStrings = Array<String>(5) { "it = $it" }
//    val otherStrings = arrayOf("a", "b", "c")

    fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) : String {
        var hourOfDay = hourOfDay
        var AM_PM = " AM"
        var mm_precede = ""
        if (hourOfDay >= 12) {
            AM_PM = " PM"
            if (hourOfDay in 13..23) {
                hourOfDay -= 12
            } else {
                hourOfDay = 12
            }
        } else if (hourOfDay == 0) {
            hourOfDay = 12
        }
        if (minute < 10) {
            mm_precede = "0"
        }
        return AM_PM
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==1  && resultCode == Activity.RESULT_OK){
            Toast.makeText(requireContext(),"Done",Toast.LENGTH_SHORT).show()
            model.locationFromNameCity.value = data?.getStringExtra("PlaceName")!!
//            binding.cityFromId.setText(data?.getStringExtra("PlaceName")!!)
        }
        if(requestCode ==2  && resultCode == Activity.RESULT_OK){
            Toast.makeText(requireContext(),"Done",Toast.LENGTH_SHORT).show()
            model.locationToNameCity.value = data?.getStringExtra("PlaceName")!!

//            binding.locationToId.setText(data?.getStringExtra("PlaceName")!!)
        }
    }

    fun showDialog(type : String){
        val alert: Dialog? = Dialog(requireContext())
        alert?.setContentView(R.layout.user_location)
        alert?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alert?.findViewById<Button>(R.id.confirmBtnId)?.setOnClickListener {
            placeName = alert.findViewById<EditText>(R.id.addressEditTextId).text.toString()
            if(placeName.isEmpty())
                Toast.makeText(requireContext(),resources.getString(R.string.pleaseWriteArea), Toast.LENGTH_SHORT).show()
            else{
                binding.mkLoaderId.visibility = View.VISIBLE
                val service = ApiClient.makeRetrofitServiceHome()
                var countryId : Int = 0
                if(type == "From"){
                    countryId = sharedHelper.getKey(requireContext(), "CountrySelectedTripFromId")?.toInt()!!
                    model.locationFromNameCity.value = placeName
                }
                else if(type == "To"){
                    countryId = sharedHelper.getKey(requireContext(), "CountrySelectedTripToId")?.toInt()!!
                    model.locationToNameCity.value = placeName

                }
                CoroutineScope(Dispatchers.IO).launch {

                    val response = service.insertArea("Bearer ${sharedHelper.getKey(requireContext(), "Token")}", placeName,countryId )
                    withContext(Dispatchers.Main) {
                        try {
                            Log.i("response1", response.message())
                            if (response.isSuccessful && response.body()!!.error == 0) {
                                binding.mkLoaderId.visibility = View.INVISIBLE
                                Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()
                                Log.i("response2", response.message())
                                alert.dismiss()

                                if(type == "From")
                                    startActivityForResult(Intent(activity, MapsAreaActivity::class.java).putExtra("EnterType", "From"), 1)
                                else 
                                    startActivityForResult(Intent(activity, MapsAreaActivity::class.java).putExtra("EnterType", "To"), 2)

                            } else {
                                Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()
                                binding.mkLoaderId.visibility = View.INVISIBLE
                                Log.i("response3", response.message())

                            }
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
                            binding.mkLoaderId.visibility = View.INVISIBLE
                            Log.i("response4", response.message())

                        }
                    }

                }
                
                
                }
        }
        alert?.findViewById<Button>(R.id.cancelBtnId)?.setOnClickListener {
            alert.dismiss()
        }
        alert?.show()
    }
}
