package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.State
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.net.ssl.HttpsURLConnection

class SignUpKtpViewModel: ViewModel() {
    var state = MutableLiveData<State>()
    var errorMassage = MutableLiveData<String>()

    fun register(context: Context, registerModel: RegisterModel, profile: File, ktp: File, selfieKtp: File) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .register(
                registerModel.idDistrict,
                registerModel.idCity,
                registerModel.idProvince,
                registerModel.email,
                registerModel.ktp,
                registerModel.name,
                registerModel.password,
                registerModel.telp,
                registerModel.address,
                MultipartBody.Part.createFormData("img_user", profile.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), profile)),
                MultipartBody.Part.createFormData("img_ktp", ktp.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), ktp)),
                MultipartBody.Part.createFormData("img_ktp_selfie", selfieKtp.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), selfieKtp))
            )
            .enqueue(object : Callback<BaseModel> {
                override fun onResponse(
                    call: Call<BaseModel>,
                    response: Response<BaseModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                        } else if (response.body()?.statusCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                            state.value = State.ERROR
                            errorMassage.value = "Email telah digunakan"
                        } else {
                            state.value = State.ERROR
                            errorMassage.value = response.body()?.statusMessage.toString()
                        }
                    } else if (response.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                        state.value = State.ERROR
                        errorMassage.value = "Email telah digunakan"
                    } else {
                        state.value = State.ERROR
                        errorMassage.value = response.body()?.statusMessage.toString()
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    state.value = State.ERROR
                    Log.e("register", t.message.toString())
                    errorMassage.value = t.message.toString()
                }
            })
    }
}