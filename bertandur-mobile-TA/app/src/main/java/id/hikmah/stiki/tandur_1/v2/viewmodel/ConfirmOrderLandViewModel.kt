package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.model.LandModel
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import id.hikmah.stiki.tandur_1.v2.util.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class ConfirmOrderLandViewModel: ViewModel() {
    var lands = MutableLiveData<List<LandData>>()
    var state = MutableLiveData<State>()

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
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<LandModel>, t: Throwable) {
                    Log.e("getDetailLand", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }
}