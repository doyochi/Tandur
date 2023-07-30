package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class ListVideoBelajarNandurViewModel: ViewModel() {
    var tutorialDetails = MutableLiveData<List<TutorialDetailData>>()

    fun getTutorialVideos(context: Context, id: String) {
        NetworkClient().getService(context)
            .getDetailTutorial(
                Prefs(context).jwt.toString(),
                id
            )
            .enqueue(object : Callback<TutorialDetailResponseModel> {
                override fun onResponse(call: Call<TutorialDetailResponseModel>, response: Response<TutorialDetailResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            tutorialDetails.value = response.body()?.data ?: emptyList()
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<TutorialDetailResponseModel>, t: Throwable) {
                    Log.e("getDetailTutorial", t.message.toString())
                }
            })
    }
}