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

class DetailProductViewModel(application: Application) : AndroidViewModel(application) {
    var products = MutableLiveData<List<ListProductData>>()
    var product = MutableLiveData<List<ProductData>>()
    var reviewProduct = MutableLiveData<List<ReviewProductData>>()
    var state = MutableLiveData<State>()

    val repository: Repository

    init {
        val appDAO = Database.getDatabase(application).appDAO()
        repository = Repository(appDAO)
    }

    fun getDetailProduct(context: Context, idProduct: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getDetailProduct(
                Prefs(context).jwt.toString(),
                idProduct
            )
            .enqueue(object : Callback<ProductModel>{
                override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            product.value = response.body()?.data ?: emptyList()
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                    Log.e("getDetailProduct", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }

    fun getReviewProduct(context: Context, idProduct: String) {
        NetworkClient().getService(context)
            .getReviewProduct(
                Prefs(context).jwt.toString(),
                idProduct
            )
            .enqueue(object : Callback<ReviewProductResponseModel>{
                override fun onResponse(call: Call<ReviewProductResponseModel>, response: Response<ReviewProductResponseModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            reviewProduct.value = response.body()?.data ?: emptyList()
                        } else {
                        }
                    } else {
                    }
                }

                override fun onFailure(call: Call<ReviewProductResponseModel>, t: Throwable) {
                    Log.e("getReviewProduct", t.message.toString())
                }
            })
    }

    fun getProduct(context: Context, category: Int) {
        NetworkClient().getService(context)
            .getProduct(
                Prefs(context).jwt.toString(),
                null,
                null,
                category,
                null
            )
            .enqueue(object : Callback<ListProductModel>{
                override fun onResponse(call: Call<ListProductModel>, response: Response<ListProductModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            products.value = response.body()?.data ?: emptyList()
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<ListProductModel>, t: Throwable) {
                    Log.e("getProduct", t.message.toString())
                }
            })
    }

    fun addToCart(productData: ProductData) {
        val data = ProductEntity(
            productData.idProduct.toString(),
            productData.photoProduct?.firstOrNull(),
            productData.nameProduct,
            productData.priceProduct,
            productData.ratingProduct,
            productData.addressUser,
            productData.nameUser,
            productData.imgUser,
            productData.telpUser,
            productData.stockProduct,
            productData.jmlTerjual,
            productData.conditionProduct,
            productData.namePcat,
            productData.descProduct,
            productData.noteProduct,
        )

        Completable.fromAction { repository.insertProduct(data) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.single())
            .subscribe()
    }

    fun addToFav(productData: ProductData) {
        val data = FavoriteEntity(
            productData.idProduct.toString(),
            productData.photoProduct?.firstOrNull(),
            productData.nameProduct,
            productData.priceProduct,
            productData.ratingProduct,
            productData.addressUser,
            productData.nameUser,
            productData.imgUser,
            productData.telpUser,
            productData.stockProduct,
            productData.jmlTerjual,
            productData.conditionProduct,
            productData.namePcat,
            productData.descProduct,
            productData.noteProduct,
        )

        Completable.fromAction { repository.insertFavorite(data) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.single())
            .subscribe()
    }

    fun removeFromFav(idProduct: String) {
        Completable.fromAction { repository.deleteFavorite(idProduct) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.single())
            .subscribe()
    }
}