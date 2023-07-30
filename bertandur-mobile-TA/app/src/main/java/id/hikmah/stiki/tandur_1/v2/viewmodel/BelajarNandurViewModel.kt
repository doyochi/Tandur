package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.hikmah.stiki.tandur_1.v2.model.LandModel
import id.hikmah.stiki.tandur_1.v2.model.TutorialData
import id.hikmah.stiki.tandur_1.v2.model.TutorialResponseModel
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class BelajarNandurViewModel: ViewModel() {
    var tutorials = MutableLiveData<List<TutorialData>>()

    fun getTutorials(context: Context) {
        NetworkClient().getService(context)
            .getTutorial(
                Prefs(context).jwt.toString()
            )
            .enqueue(object : Callback<TutorialResponseModel> {
                override fun onResponse(call: Call<TutorialResponseModel>, response: Response<TutorialResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            tutorials.value = response.body()?.data ?: emptyList()
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<TutorialResponseModel>, t: Throwable) {
                    Log.e("getTutorials", t.message.toString())
                }
            })
    }
}