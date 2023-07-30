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

class NotificationViewModel: ViewModel() {
    var notifications = MutableLiveData<List<NotificationData>>()
    var stateUpdateNotification = MutableLiveData<State>()
    var idLog = 0

    fun getLogNotification(context: Context) {
        NetworkClient().getService(context)
            .getLogNotification(
                Prefs(context).jwt.toString()
            )
            .enqueue(object : Callback<NotificationResponseModel>{
                override fun onResponse(call: Call<NotificationResponseModel>, response: Response<NotificationResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            notifications.value = response.body()?.data ?: emptyList()
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<NotificationResponseModel>, t: Throwable) {
                    Log.e("getLogNotification", t.message.toString())
                }
            })
    }

    fun putNotification(context: Context, idLog: Int) {
        stateUpdateNotification.postValue(State.LOADING)
        NetworkClient().getService(context)
            .putNotification(
                Prefs(context).jwt.toString(),
                idLog
            )
            .enqueue(object : Callback<BaseModel>{
                override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            stateUpdateNotification.postValue(State.COMPLETE)
                        } else {
                            stateUpdateNotification.postValue(State.ERROR)
                        }
                    } else {
                        stateUpdateNotification.postValue(State.ERROR)
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    Log.e("putNotification", t.message.toString())
                    stateUpdateNotification.postValue(State.ERROR)
                }
            })
    }
}