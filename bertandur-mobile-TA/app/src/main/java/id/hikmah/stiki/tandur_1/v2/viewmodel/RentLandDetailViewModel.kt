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
import java.io.File
import javax.net.ssl.HttpsURLConnection

class RentLandDetailViewModel: ViewModel() {
    var state = MutableLiveData<State>()
    var errorMassage = MutableLiveData<String>()

    fun insertLahan(context: Context, rentLandModel: RentLandModel) {
        state.value = State.LOADING
        val foto1 = Uri.parse(rentLandModel.foto1).toFile()
        val foto2 = Uri.parse(rentLandModel.foto2).toFile()
        val gallery1 = if (rentLandModel.gallery1.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(rentLandModel.gallery1).toFile()
        }
        val gallery2 = if (rentLandModel.gallery2.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(rentLandModel.gallery2).toFile()
        }
        val gallery3 = if (rentLandModel.gallery3.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(rentLandModel.gallery3).toFile()
        }
        val gallery4 = if (rentLandModel.gallery4.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(rentLandModel.gallery4).toFile()
        }
        val gallery5 = if (rentLandModel.gallery5.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(rentLandModel.gallery5).toFile()
        }

        state.value = State.LOADING
        NetworkClient().getService(context)
            .insertLahan(
                Prefs(context).jwt.toString(),
                rentLandModel.nameLand,
                rentLandModel.address,
                rentLandModel.province,
                rentLandModel.city,
                rentLandModel.district,
                rentLandModel.locationLand,
                rentLandModel.noCertificateLand,
                rentLandModel.desc,
                rentLandModel.ownNameLand,
                rentLandModel.ownKtp,
                rentLandModel.ownEmail,
                rentLandModel.ownTelp,
                rentLandModel.widthLand,
                rentLandModel.lengthLand,
                rentLandModel.rule,
                rentLandModel.price,
                rentLandModel.rating,
                rentLandModel.longtitude,
                rentLandModel.latitude,
                rentLandModel.facility,
                MultipartBody.Part.createFormData("foto_1", foto1.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), foto1)),
                MultipartBody.Part.createFormData("foto_2", foto2.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), foto2)),
                if (gallery1 == null) { null } else {MultipartBody.Part.createFormData("gallery_1", gallery1.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery1))},
                if (gallery2 == null) { null } else {MultipartBody.Part.createFormData("gallery_2", gallery2.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery2))},
                if (gallery3 == null) { null } else {MultipartBody.Part.createFormData("gallery_3", gallery3.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery3))},
                if (gallery4 == null) { null } else {MultipartBody.Part.createFormData("gallery_4", gallery4.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery4))},
                if (gallery5 == null) { null } else {MultipartBody.Part.createFormData("gallery_5", gallery5.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), gallery5))}
            )
            .enqueue(object : Callback<BaseModel> {
                override fun onResponse(
                    call: Call<BaseModel>,
                    response: Response<BaseModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                        } else {
                            state.value = State.ERROR
                            errorMassage.value = response.body()?.statusMessage.toString()
                        }
                    } else {
                        state.value = State.ERROR
                        errorMassage.value = response.body()?.statusMessage.toString()
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    state.value = State.ERROR
                    Log.e("insertLahan", t.message.toString())
                    errorMassage.value = t.message.toString()
                }
            })
    }
}