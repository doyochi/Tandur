package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finna.sales.database.Database
import com.finna.sales.database.Repository
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

class PaymentProductViewModel(application: Application) : AndroidViewModel(application) {
    var state = MutableLiveData<State>()
    var idPurchase = MutableLiveData<String>()
    var purchaseData = MutableLiveData<PurchaseResponseData>()
    var errorMessage = MutableLiveData<String>()

    val repository: Repository

    init {
        val appDAO = Database.getDatabase(application).appDAO()
        repository = Repository(appDAO)
    }

    fun purchaseProduct(context: Context, requestProductModel: RequestProductModel) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .purchaseProduct(
                Prefs(context).jwt.toString(),
                requestProductModel
            )
            .enqueue(object : Callback<PurchaseResponseModel>{
                override fun onResponse(call: Call<PurchaseResponseModel>, response: Response<PurchaseResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            requestProductModel.product?.forEach {
                                removeProductFromCart(it.idProduct.toString())
                            }
//                            idPurchase.value = response.body()?.data?.idPurchase.toString()
                            purchaseData.value = response.body()?.data!!
                        } else if (response.body()?.statusCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                            state.value = State.ERROR
                            errorMessage.value = "Pembelian produk melebihi jumlah stok tersedia"
                        } else {
                            state.value = State.ERROR
                        }
                    } else if (response.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                        state.value = State.ERROR
                        errorMessage.value = "Pembelian produk melebihi jumlah stok tersedia"
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<PurchaseResponseModel>, t: Throwable) {
                    Log.e("purchaseProduct", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }

    fun removeProductFromCart(idProduct: String) {
        Completable.fromAction { repository.deleteProduct(idProduct) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.single())
            .subscribe()
    }
}