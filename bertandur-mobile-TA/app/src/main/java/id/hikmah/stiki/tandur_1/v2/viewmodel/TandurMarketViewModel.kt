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

class TandurMarketViewModel: ViewModel() {
    var categories = MutableLiveData<List<CategoryData>>()
    var products = MutableLiveData<List<ListProductData>>()

    fun getCategory(context: Context, type: Int) {
        NetworkClient().getService(context)
            .getCategory(
                Prefs(context).jwt.toString(),
                type
            )
            .enqueue(object : Callback<CategoryModel>{
                override fun onResponse(call: Call<CategoryModel>, response: Response<CategoryModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            categories.value = response.body()?.data ?: emptyList()
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<CategoryModel>, t: Throwable) {
                    Log.e("getCategory", t.message.toString())
                }
            })
    }

    fun getProduct(context: Context, category: Int) {
        NetworkClient().getService(context)
            .getProduct(
                Prefs(context).jwt.toString(),
                null,
                null,
                category,
                null
            )
            .enqueue(object : Callback<ListProductModel>{
                override fun onResponse(call: Call<ListProductModel>, response: Response<ListProductModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            products.value = response.body()?.data ?: emptyList()
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

    fun getProductUser(context: Context) {
        NetworkClient().getService(context)
            .getproductUser(
                Prefs(context).jwt.toString(),
            )
            .enqueue(object : Callback<ListProductModel>{
                override fun onResponse(call: Call<ListProductModel>, response: Response<ListProductModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            products.value = response.body()?.data ?: emptyList()
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