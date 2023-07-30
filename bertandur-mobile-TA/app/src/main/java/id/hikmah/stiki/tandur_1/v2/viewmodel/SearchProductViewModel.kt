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

class SearchProductViewModel: ViewModel() {
    var products = MutableLiveData<List<ListProductData>>()

    fun getProduct(context: Context, search: String?, sort: Int?, category: Int?, idProductCategory: Int?) {
        NetworkClient().getService(context)
            .getProduct(
                Prefs(context).jwt.toString(),
                search,
                sort,
                if (category == 0) {null} else {category},
                if (idProductCategory == 0) {null} else {idProductCategory}
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