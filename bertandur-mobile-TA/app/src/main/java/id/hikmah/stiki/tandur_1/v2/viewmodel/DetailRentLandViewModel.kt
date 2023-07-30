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

class DetailRentLandViewModel: ViewModel() {
    var rents = MutableLiveData<RentData>()
    var state = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()

    fun getDetailRent(context: Context, idRent: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getDetailRent(
                Prefs(context).jwt.toString(),
                idRent.toString()
            )
            .enqueue(object : Callback<RentModel>{
                override fun onResponse(call: Call<RentModel>, response: Response<RentModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            rents.value = response.body()?.data!!
                        } else if (response.body()?.statusCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                            state.value = State.ERROR
                            errorMessage.value = "Detail tidak ditemukan"
                        } else {
                            state.value = State.ERROR
                            errorMessage.value = "Terjadi kesalahan pada server"
                        }
                    } else if (response.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                        state.value = State.ERROR
                        errorMessage.value = "Detail tidak ditemukan"
                    } else {
                        state.value = State.ERROR
                        errorMessage.value = "Terjadi kesalahan pada server"
                    }
                }

                override fun onFailure(call: Call<RentModel>, t: Throwable) {
                    Log.e("getDetailRent", t.message.toString())
                    state.value = State.ERROR
                    errorMessage.value = "Terjadi kesalahan pada server"
                }
            })
    }
}