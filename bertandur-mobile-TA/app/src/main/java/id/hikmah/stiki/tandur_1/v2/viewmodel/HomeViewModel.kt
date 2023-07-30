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

class HomeViewModel: ViewModel() {
    var lands = MutableLiveData<List<LandData>>()
    var products = MutableLiveData<List<ListProductData>>()
    var productsGround = MutableLiveData<List<ListProductData>>()

    fun getLand(context: Context) {
        NetworkClient().getService(context)
            .getLand(
                Prefs(context).jwt.toString()
            )
            .enqueue(object : Callback<LandModel>{
                override fun onResponse(call: Call<LandModel>, response: Response<LandModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            lands.value = response.body()?.data ?: emptyList()
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<LandModel>, t: Throwable) {
                    Log.e("getLand", t.message.toString())
                }
            })
    }

    fun getProduct(context: Context, productType: Int) {
        NetworkClient().getService(context)
            .getProduct(
                Prefs(context).jwt.toString(),
                null,
                null,
                productType,
                null
            )
            .enqueue(object : Callback<ListProductModel>{
                override fun onResponse(call: Call<ListProductModel>, response: Response<ListProductModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            if (productType == 1) {
                                products.value = response.body()?.data ?: emptyList()
                            } else {
                                productsGround.value = response.body()?.data ?: emptyList()
                            }
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<ListProductModel>, t: Throwable) {
                    Log.e("getProduct", t.message.toString())
                }
            })
    }
}