package com.mabdelhafz850.tawsila.ui.activity.all.wallet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mabdelhafz850.tawsila.ui.activity.utils.SharedHelper
import com.shoohna.shoohna.util.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class WalletViewModel : BaseViewModel() {

    var loader = MutableLiveData<Boolean>(false)
    var sharedHelper: SharedHelper = SharedHelper()
    var target = MutableLiveData<String>("")
    var driverWallet = MutableLiveData<String>("")
    var totalEarned = MutableLiveData<String>("")
    var tripCount = MutableLiveData<String>("")

    fun getWallet(context: Context)
    {
        loader.value = true
        val service = ApiClient.makeRetrofitServiceHome()
        CoroutineScope(Dispatchers.IO).async {
            val response = service.userWallet("Bearer ${sharedHelper.getKey(context, "Token")}")
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body()!!.error == 0) {
                        loader.value = false
                        target.value = response.body()!!.data.target
                        driverWallet.value = response.body()!!.data.driver_wallet
                        totalEarned.value = response.body()!!.data.total_earned
                        tripCount.value = response.body()!!.data.trip_count
                    } else {
                        Toast.makeText(context, response.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                        loader.value = false

                    }
                }catch (e:Exception)
                {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    loader.value = false
                }
            }
        }
    }
}