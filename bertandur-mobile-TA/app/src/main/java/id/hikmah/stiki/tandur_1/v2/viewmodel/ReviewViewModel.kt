package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finna.sales.database.Database
import com.finna.sales.database.Repository
import id.hikmah.stiki.tandur_1.v2.database.entity.FavoriteEntity
import id.hikmah.stiki.tandur_1.v2.database.entity.ProductEntity
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import id.hikmah.stiki.tandur_1.v2.util.State
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class ReviewViewModel(application: Application) : AndroidViewModel(application) {
    var state = MutableLiveData<State>()

    fun postReviewLand(context: Context, idLand: String, idRent: String, rating: String, reviewTitle: String, reviewContent: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .postReviewLand(
                Prefs(context).jwt.toString(),
                idLand,
                idRent,
                rating,
                reviewTitle,
                reviewContent
            )
            .enqueue(object : Callback<BaseModel>{
                override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    Log.e("postReviewLand", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }

    fun postReviewProduct(context: Context, idProduct: String, idPurchase: String, rating: String, reviewTitle: String, reviewContent: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .postReviewProduct(
                Prefs(context).jwt.toString(),
                idProduct,
                idPurchase,
                rating,
                reviewTitle,
                reviewContent
            )
            .enqueue(object : Callback<BaseModel>{
                override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    Log.e("postReviewProduct", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }

}