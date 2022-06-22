package com.mabdelhafz850.tawsila.ui.activity.all

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.shoohna.shoohna.util.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : BaseViewModel() {

    var loader = MutableLiveData<Boolean>(false)
    var sharedHelper: SharedHelper = SharedHelper()

//    fun login(v: View)
//    {
//
//                loader.value = true
//                val service = ApiClient.makeRetrofitService()
//                Log.i("Login", "1")
//                CoroutineScope(Dispatchers.IO).launch {
//                    Log.i("Login", "2")
//
//                    val response = service.login(email.value!!, password.value!!, "")
//                    Log.i("Response1", response.message())
//                    Log.i("Response2", response.code().toString())
//                    withContext(Dispatchers.Main) {
//                        try {
//                            Log.i("Register", "3")
//
//                            if (response.isSuccessful && response.body()!!.error == 0) {
//                                Log.i("Login", "4")
//                                Log.i("Login", response.message())
//                                loader.value = false
//                                email.value = ""
//                                password.value = ""
//                                sharedHelper.putKey(v.context, "Token", response.body()!!.data.token)
//                                sharedHelper.putKey(v.context, "fName", response.body()!!.data.first_name)
//                                sharedHelper.putKey(v.context, "lName", response.body()!!.data.last_name)
//                                sharedHelper.putKey(v.context, "Image", response.body()!!.data.image)
//                                sharedHelper.putKey(v.context, "E-Mail", response.body()!!.data.email)
//                                sharedHelper.putKey(v.context, "Phone", response.body()!!.data.phone)
//                                sharedHelper.putKey(v.context, "UserId", response.body()!!.data.id.toString())
////                                sharedHelper.putKey(v.context, "DriverFirstName", response.body()!!.data.first_name)
////                                sharedHelper.putKey(v.context, "DriverImage", response.body()!!.data.image.toString())
//                                if(response.body()!!.data.car_type == "1") {
//                                    sharedHelper.putKey(v.context, "car_type","1")
//                                }else if(response.body()!!.data.car_type == "2") {
//                                    sharedHelper.putKey(v.context, "car_type","2")
//                                }
//
//                                val intent: Intent = Intent(v!!.context, Main2Activity::class.java)
//                                v!!.context.startActivity(intent)
//                                (v.context as Activity).finish()
//                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
//
//                            } else if (response.isSuccessful) {
//                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
//                                loader.value = false
//                            } else {
//                                Log.i("RegisterError", response.message())
//
//                            }
//                        } catch (e: Exception) {
//                            Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
//                            loader.value = false
//
//                        }
//                    }
//                }
//            }



}