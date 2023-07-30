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

class PaymentLandViewModel: ViewModel() {
    var state = MutableLiveData<State>()
    var paymentUrl = MutableLiveData<String>()
    var errorMessage = MutableLiveData<String>()

    fun rentLahan(context: Context, paymentRentLandModel: PaymentRentLandModel) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .rentLahan(
                Prefs(context).jwt.toString(),
                paymentRentLandModel
            )
            .enqueue(object : Callback<RentResponseModel>{
                override fun onResponse(call: Call<RentResponseModel>, response: Response<RentResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            paymentUrl.value = response.body()?.data?.paymentUrl.toString()
                        } else if (response.body()?.statusCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                            state.value = State.ERROR
                            errorMessage.value = "Lahan tidak aktif atau sudah disewa pengguna lain"
                        } else {
                            state.value = State.ERROR
                            errorMessage.value = "Terjadi kesalahan pada server"
                        }
                    } else if (response.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                        state.value = State.ERROR
                        errorMessage.value = "Lahan tidak aktif atau sudah disewa pengguna lain"
                    } else {
                        state.value = State.ERROR
                        errorMessage.value = "Terjadi kesalahan pada server"
                    }
                }

                override fun onFailure(call: Call<RentResponseModel>, t: Throwable) {
                    Log.e("getDetailLand", t.message.toString())
                    state.value = State.ERROR
                    errorMessage.value = "Terjadi kesalahan pada server"
                }
            })
    }
}