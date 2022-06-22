package com.mabdelhafz850.tawsila.ui.activity.all.online.driverProfile

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.shoohna.shoohna.util.base.BaseViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileViewModel : BaseViewModel() {

    var fName = MutableLiveData<String>("")
    var lName = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var phone = MutableLiveData<String>("")
//    var image = MutableLiveData<String>("")

    var loader = MutableLiveData<Boolean>(false)
    var sharedHelper: SharedHelper = SharedHelper()

    var selectedImage = MutableLiveData<Boolean>(false)

    fun getUser(context: Context , imageView: CircleImageView)
    {
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.getUserData("Bearer ${sharedHelper.getKey(context, "Token")}", sharedHelper.getKey(context, "UserId")?.toInt()!!)
            withContext(Dispatchers.Main) {
                try {
                    Log.i("response1",response.message())
                    if (response.isSuccessful && response.body()!!.error == 0) {
                        loader.value = false
                        fName.value = response.body()!!.data.first_name
                        lName.value = response.body()!!.data.last_name
                        email.value = response.body()!!.data.email
                        phone.value = response.body()!!.data.phone

                        Glide.with(context).load(response.body()!!.data.image).into(imageView)

//                        image.value = response.body()!!.data.image
                        
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        Log.i("response2",response.message())


                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        loader.value = false
                        Log.i("response3",response.message())

                    }
                }catch (e:Exception)
                {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false
                    Log.i("response4",response.message())

                }
            }
        }
    }
    


    fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {

            val proj =
                    arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.getContentResolver().query(contentUri!!, proj, null, null, null)
            val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    fun updateProfileData(v:View , FName:EditText , LName:EditText , Email:EditText , Phone:EditText , profileImg: CircleImageView )
    {
        try{
                val phonePart: RequestBody =
                        RequestBody.create(MediaType.parse("text/plain"), phone.value!!)
                val fNamePart: RequestBody =
                        RequestBody.create(MediaType.parse("text/plain"), fName.value!!)
                val lNamePart: RequestBody =
                        RequestBody.create(MediaType.parse("text/plain"), lName.value!!)
                val emailPart: RequestBody =
                        RequestBody.create(MediaType.parse("text/plain"), email.value!!)
//            val password: RequestBody =
//                    RequestBody.create(MediaType.parse("text/plain"), password.value!!)
            if(selectedImage.value!!) {

                val myUri = Uri.parse(sharedHelper.getKey(v!!.context, "Image"))

                val file = File(getRealPathFromUri(v.context, myUri))

                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)


                loader.value = true
                val service = ApiClient.makeRetrofitServiceHome()
                CoroutineScope(Dispatchers.IO).async {
                    val response = service.editProfileFunction("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}", fNamePart, lNamePart, emailPart, phonePart, body)
                    withContext(Dispatchers.Main) {
                        try {
                            Log.i("response1", response.message())
                            if (response.isSuccessful && response.body()!!.error == 0) {
                                loader.value = false
                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                Log.i("response2", response.message())
                                selectedImage.value = false

                            } else {
                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                loader.value = false
                                Log.i("response3", response.message())

                            }
                        } catch (e: Exception) {
                            Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                            loader.value = false
                            Log.i("response4", response.message())

                        }
                    }
                }
            }else
            {
                loader.value = true
                Log.i("F-Name Part",fName.value)

                val service = ApiClient.makeRetrofitServiceHome()
                CoroutineScope(Dispatchers.IO).async {
                    val response = service.editProfileFunctionWithoutImage("Bearer ${sharedHelper.getKey(v.rootView.context, "Token")}", fName.value!!, lName.value!!, email.value!!, phone.value!!)
                    withContext(Dispatchers.Main) {
                        try {
                            Log.i("response1", response.message())
                            if (response.isSuccessful && response.body()!!.error == 0) {
                                loader.value = false
                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                Log.i("response2", response.message())


                            } else {
                                Toast.makeText(v.rootView.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                loader.value = false
                                Log.i("response3", response.message())

                            }
                        } catch (e: Exception) {
                            Toast.makeText(v.rootView.context, e.message.toString(), Toast.LENGTH_SHORT).show()
                            loader.value = false
                            Log.i("response4", response.message())

                        }
                    }
                }
            }
        }catch (e:java.lang.Exception)
        {
            Toast.makeText(v.rootView.context,"Exception \n ${e.message}" , Toast.LENGTH_SHORT).show()
        }
    }


}