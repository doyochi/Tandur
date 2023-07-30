package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class TransactionViewModel: ViewModel() {
    var history = MutableLiveData<List<HistoryRentData>>()
    var historyProduct = MutableLiveData<List<HistoryProductData>>()

    fun getHistoryRent(context: Context) {
        NetworkClient().getService(context)
            .getHistoryRent(
                Prefs(context).jwt.toString()
            )
            .enqueue(object : Callback<HistoryRentModel>{
                override fun onResponse(call: Call<HistoryRentModel>, response: Response<HistoryRentModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            history.value = response.body()?.data ?: emptyList()
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<HistoryRentModel>, t: Throwable) {
                    Log.e("getHistoryRent", t.message.toString())
                }
            })
    }

    fun getHistoryProduct(context: Context) {
        NetworkClient().getService(context)
            .getHistoryProduct(
                Prefs(context).jwt.toString()
            )
            .enqueue(object : Callback<HistoryProductModel>{
                override fun onResponse(call: Call<HistoryProductModel>, response: Response<HistoryProductModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            historyProduct.value = response.body()?.data ?: emptyList()
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<HistoryProductModel>, t: Throwable) {
                    Log.e("getHistoryProduct", t.message.toString())
                }
            })
    }
}