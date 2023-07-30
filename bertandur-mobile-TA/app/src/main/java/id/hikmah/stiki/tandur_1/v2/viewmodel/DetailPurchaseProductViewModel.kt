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

class DetailPurchaseProductViewModel: ViewModel() {
    var purcase = MutableLiveData<NewPurchaseData>()
    var state = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()

    fun getDetailPurchase(context: Context, idPurchase: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getDetailPurchase(
                Prefs(context).jwt.toString(),
                idPurchase.toString()
            )
            .enqueue(object : Callback<NewPurchaseResponseModel>{
                override fun onResponse(call: Call<NewPurchaseResponseModel>, response: Response<NewPurchaseResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            purcase.value = response.body()?.data!!
                        } else if (response.body()?.statusCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                            state.value = State.ERROR
                            errorMessage.value = "Detail purchase tidak ditemukan"
                        } else {
                            state.value = State.ERROR
                            errorMessage.value = "Terjadi kesalahan pada server"
                        }
                    } else if (response.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                        state.value = State.ERROR
                        errorMessage.value = "Detail purchase tidak ditemukan"
                    } else {
                        state.value = State.ERROR
                        errorMessage.value = "Terjadi kesalahan pada server"
                    }
                }

                override fun onFailure(call: Call<NewPurchaseResponseModel>, t: Throwable) {
                    Log.e("getDetailPurchase", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }
}