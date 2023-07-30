package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finna.sales.database.Database
import com.finna.sales.database.Repository
import id.hikmah.stiki.tandur_1.v2.database.entity.FavoriteEntity
import id.hikmah.stiki.tandur_1.v2.database.entity.ProductEntity
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import id.hikmah.stiki.tandur_1.v2.util.State
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class ListReviewViewModel(application: Application) : AndroidViewModel(application) {
    var reviewProduct = MutableLiveData<List<ReviewProductData>>()
    var landsReview = MutableLiveData<List<ReviewLandData>>()
    var state = MutableLiveData<State>()

    fun getReviewProduct(context: Context, idProduct: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getReviewProduct(
                Prefs(context).jwt.toString(),
                idProduct
            )
            .enqueue(object : Callback<ReviewProductResponseModel>{
                override fun onResponse(call: Call<ReviewProductResponseModel>, response: Response<ReviewProductResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            reviewProduct.value = response.body()?.data ?: emptyList()
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<ReviewProductResponseModel>, t: Throwable) {
                    Log.e("getReviewProduct", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }

    fun getReviewLand(context: Context, idLand: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getReviewLand(
                Prefs(context).jwt.toString(),
                idLand.toString()
            )
            .enqueue(object : Callback<ReviewLandResponseModel>{
                override fun onResponse(call: Call<ReviewLandResponseModel>, response: Response<ReviewLandResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            landsReview.value = response.body()?.data ?: emptyList()
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<ReviewLandResponseModel>, t: Throwable) {
                    Log.e("getReviewLand", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }

}