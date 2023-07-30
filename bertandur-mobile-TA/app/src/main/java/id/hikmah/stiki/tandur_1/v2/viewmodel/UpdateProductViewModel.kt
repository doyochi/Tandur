package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
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
import javax.net.ssl.HttpsURLConnection

class UpdateProductViewModel: ViewModel() {
    var product = MutableLiveData<List<ProductData>>()
    var categories = MutableLiveData<List<CategoryData>>()
    var state = MutableLiveData<State>()
    var stateInsert = MutableLiveData<State>()
    var errorMassage = MutableLiveData<String>()

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

    fun getCategory(context: Context, type: Int) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getCategory(
                Prefs(context).jwt.toString(),
                type
            )
            .enqueue(object : Callback<CategoryModel>{
                override fun onResponse(call: Call<CategoryModel>, response: Response<CategoryModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            categories.value = response.body()?.data ?: emptyList()
                            state.value = State.COMPLETE
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<CategoryModel>, t: Throwable) {
                    Log.e("getCategory", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }

    fun updateProduct(context: Context, sellProductModel: UpdateProductModel) {
        stateInsert.value = State.LOADING
        val gallery1 = if (sellProductModel.gallery1.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(sellProductModel.gallery1).toFile()
        }
        val gallery2 = if (sellProductModel.gallery2.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(sellProductModel.gallery2).toFile()
        }
        val gallery3 = if (sellProductModel.gallery3.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(sellProductModel.gallery3).toFile()
        }
        val gallery4 = if (sellProductModel.gallery4.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(sellProductModel.gallery4).toFile()
        }
        val gallery5 = if (sellProductModel.gallery5.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(sellProductModel.gallery5).toFile()
        }

        state.value = State.LOADING
        NetworkClient().getService(context)
            .updateProduct(
                Prefs(context).jwt.toString(),
                sellProductModel.name_product,
                sellProductModel.category,
                sellProductModel.id_product,
                sellProductModel.id_product_category,
                sellProductModel.price,
                sellProductModel.weigth_product,
                sellProductModel.stock,
                sellProductModel.condition,
                sellProductModel.desc,
                sellProductModel.note,
                if (gallery1 == null) { null } else {
                    MultipartBody.Part.createFormData("gallery_1", gallery1.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery1))},
                if (gallery2 == null) { null } else {
                    MultipartBody.Part.createFormData("gallery_2", gallery2.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery2))},
                if (gallery3 == null) { null } else {
                    MultipartBody.Part.createFormData("gallery_3", gallery3.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery3))},
                if (gallery4 == null) { null } else {
                    MultipartBody.Part.createFormData("gallery_4", gallery4.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery4))},
                if (gallery5 == null) { null } else {
                    MultipartBody.Part.createFormData("gallery_5", gallery5.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery5))}
            )
            .enqueue(object : Callback<BaseModel> {
                override fun onResponse(
                    call: Call<BaseModel>,
                    response: Response<BaseModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            stateInsert.value = State.COMPLETE
                        } else {
                            stateInsert.value = State.ERROR
                            errorMassage.value = response.body()?.statusMessage.toString()
                        }
                    } else {
                        stateInsert.value = State.ERROR
                        errorMassage.value = response.body()?.statusMessage.toString()
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    stateInsert.value = State.ERROR
                    Log.e("updateProduct", t.message.toString())
                    errorMassage.value = t.message.toString()
                }
            })
    }
}