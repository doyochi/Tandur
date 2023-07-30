package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import id.hikmah.stiki.tandur_1.remote.model.LoginInfo
import id.hikmah.stiki.tandur_1.remote.model.LoginJwt
import id.hikmah.stiki.tandur_1.remote.model.LoginResponse
import id.hikmah.stiki.tandur_1.v2.model.BaseModel
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class SignInViewModel: ViewModel() {
    var jwt = MutableLiveData<String>()
    var state = MutableLiveData<State>()
    var stateToken = MutableLiveData<State>()
    var errorMessage = MutableLiveData<String>()

    fun signIn(context: Context, email: String, passwrod: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .loginAccount(LoginInfo(email, passwrod))
            .enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            if (response.body()?.data != null) {
//                                updateToken(context, response.body()?.data?.jwt.toString())
                                jwt.value = response.body()?.data?.jwt.toString()
                                state.value = State.COMPLETE
                            } else {
                                state.value = State.ERROR
                                errorMessage.value = response.body()?.statusMessage.toString()
                            }
                        } else if (response.body()?.statusCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                            state.value = State.ERROR
                            errorMessage.value = "Kombinasi username dan password salah atau user belum aktivasi"
                        } else {
                            state.value = State.ERROR
                            errorMessage.value = response.body()?.statusMessage.toString()
                        }
                    } else if (response.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                        state.value = State.ERROR
                        errorMessage.value = "Kombinasi username dan password salah atau user belum aktivasi"
                    } else {
                        state.value = State.ERROR
                        errorMessage.value = response.body()?.statusMessage.toString()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("signIn", t.message.toString())
                    state.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }

            })
    }

    fun updateToken(context: Context, token: String) {
        stateToken.value = State.LOADING
        NetworkClient().getService(context)
            .putUserToken(
                jwt.value.toString(),
                token
            ).enqueue(object : Callback<BaseModel>{
                override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
//                            jwt.value = rJwt
                            stateToken.value = State.COMPLETE
                        } else {
                            stateToken.value = State.ERROR
                            errorMessage.value = response.body()?.statusMessage.toString()
                        }
                    } else {
                        stateToken.value = State.ERROR
                        errorMessage.value = response.body()?.statusMessage.toString()
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    Log.e("updateToken", t.message.toString())
                    stateToken.value = State.ERROR
                    errorMessage.value = t.message.toString()
                }
            })
    }
}