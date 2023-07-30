package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import id.hikmah.stiki.tandur_1.v2.util.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class DetailLandViewModel: ViewModel() {
    var lands = MutableLiveData<List<LandData>>()
    var landsRecommendation = MutableLiveData<List<LandData>>()
    var landsReview = MutableLiveData<List<ReviewLandData>>()
    var state = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()

    fun getDetailLand(context: Context, idLand: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getDetailLand(
                Prefs(context).jwt.toString(),
                idLand.toString()
            )
            .enqueue(object : Callback<LandModel>{
                override fun onResponse(call: Call<LandModel>, response: Response<LandModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            lands.value = response.body()?.data ?: emptyList()
                        } else if (response.body()?.statusCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                            state.value = State.ERROR
                            errorMessage.value = "Detail tidak ditemukan"
                        } else {
                            state.value = State.ERROR
                        }
                    } else if (response.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                        state.value = State.ERROR
                        errorMessage.value = "Detail tidak ditemukan"
                    } else {
                        state.value = State.ERROR
                        errorMessage.value = "Terjadi kesalahan pada server, seilahkan coba lagi"
                    }
                }

                override fun onFailure(call: Call<LandModel>, t: Throwable) {
                    Log.e("getDetailLand", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }

    fun getReviewLand(context: Context, idLand: String) {
        NetworkClient().getService(context)
            .getReviewLand(
                Prefs(context).jwt.toString(),
                idLand.toString()
            )
            .enqueue(object : Callback<ReviewLandResponseModel>{
                override fun onResponse(call: Call<ReviewLandResponseModel>, response: Response<ReviewLandResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            landsReview.value = response.body()?.data ?: emptyList()
                        } else {
                        }
                    } else {
                    }
                }

                override fun onFailure(call: Call<ReviewLandResponseModel>, t: Throwable) {
                    Log.e("getReviewLand", t.message.toString())
                }
            })
    }

    fun getLand(context: Context) {
        NetworkClient().getService(context)
            .getLand(
                Prefs(context).jwt.toString()
            )
            .enqueue(object : Callback<LandModel>{
                override fun onResponse(call: Call<LandModel>, response: Response<LandModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            landsRecommendation.value = response.body()?.data ?: emptyList()
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
}