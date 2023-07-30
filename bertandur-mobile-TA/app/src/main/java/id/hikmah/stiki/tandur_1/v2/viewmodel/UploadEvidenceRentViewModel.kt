package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import id.hikmah.stiki.tandur_1.v2.util.State
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.net.ssl.HttpsURLConnection

class UploadEvidenceRentViewModel: ViewModel() {
    var state = MutableLiveData<State>()
    var errorMassage = MutableLiveData<String>()

    fun uploadBuktiTransfer(context: Context, idRent: String, buktiTransfer: File) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .uploadBuktiTransfer(
                Prefs(context).jwt.toString(),
                idRent.toString(),
                MultipartBody.Part.createFormData("evidence_transfer", buktiTransfer.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), buktiTransfer))
            )
            .enqueue(object : Callback<BaseModel> {
                override fun onResponse(
                    call: Call<BaseModel>,
                    response: Response<BaseModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                        } else {
                            state.value = State.ERROR
                            errorMassage.value = response.body()?.statusMessage.toString()
                        }
                    } else {
                        state.value = State.ERROR
                        errorMassage.value = response.body()?.statusMessage.toString()
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    state.value = State.ERROR
                    Log.e("uploadBuktiTransfer", t.message.toString())
                    errorMassage.value = t.message.toString()
                }
            })
    }

    fun uploadBuktiTransferProduk(context: Context, idPurchase: String, buktiTransfer: File) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .uploadBuktiTransferProduk(
                Prefs(context).jwt.toString(),
                idPurchase.toString(),
                MultipartBody.Part.createFormData("evidence_transfer", buktiTransfer.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), buktiTransfer))
            )
            .enqueue(object : Callback<BaseModel> {
                override fun onResponse(
                    call: Call<BaseModel>,
                    response: Response<BaseModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                        } else {
                            state.value = State.ERROR
                            errorMassage.value = response.body()?.statusMessage.toString()
                        }
                    } else {
                        state.value = State.ERROR
                        errorMassage.value = response.body()?.statusMessage.toString()
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    state.value = State.ERROR
                    Log.e("uploadBuktiTransfer", t.message.toString())
                    errorMassage.value = t.message.toString()
                }
            })
    }
}