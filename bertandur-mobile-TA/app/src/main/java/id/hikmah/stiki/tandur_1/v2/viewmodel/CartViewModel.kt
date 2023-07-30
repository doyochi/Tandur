package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finna.sales.database.Database
import com.finna.sales.database.Repository
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

class CartViewModel(application: Application) : AndroidViewModel(application) {
    val repository: Repository

    init {
        val appDAO = Database.getDatabase(application).appDAO()
        repository = Repository(appDAO)
    }

    fun removeProductFromCart(idProduct: String) {
        Completable.fromAction { repository.deleteProduct(idProduct) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.single())
            .subscribe()
    }
}