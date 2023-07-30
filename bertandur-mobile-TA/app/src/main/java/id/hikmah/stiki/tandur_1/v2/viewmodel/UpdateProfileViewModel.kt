package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.hikmah.stiki.tandur_1.remote.model.LoginResponse
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

class UpdateProfileViewModel: ViewModel() {
    var provinceData = MutableLiveData<List<ProvinceData>>()
    var cityData = MutableLiveData<List<CityData>>()
    var districtData = MutableLiveData<List<DistrictData>>()
    var state = MutableLiveData<State>()
    var stateUpdate = MutableLiveData<State>()
    var errorMassage = MutableLiveData<String>()

    fun getProvince(context: Context) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getProvince()
            .enqueue(object : Callback<ProvinceModel> {
                override fun onResponse(
                    call: Call<ProvinceModel>,
                    response: Response<ProvinceModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            if (!response.body()?.data.isNullOrEmpty()) {
                                provinceData.value = response.body()?.data ?: emptyList()
                            }
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<ProvinceModel>, t: Throwable) {
                    state.value = State.ERROR
                    Log.e("getProvince", t.message.toString())
                }
            })
    }

    fun getCity(context: Context, idProvince: Int) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getCity(
                idProvince
            )
            .enqueue(object : Callback<CityModel> {
                override fun onResponse(
                    call: Call<CityModel>,
                    response: Response<CityModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            if (!response.body()?.data.isNullOrEmpty()) {
                                cityData.value = response.body()?.data ?: emptyList()
                            }
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<CityModel>, t: Throwable) {
                    state.value = State.ERROR
                    Log.e("getCity", t.message.toString())
                }
            })
    }

    fun getDistrict(context: Context, idCity: Int) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getDistrict(
                idCity
            )
            .enqueue(object : Callback<DistrictModel> {
                override fun onResponse(
                    call: Call<DistrictModel>,
                    response: Response<DistrictModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            if (!response.body()?.data.isNullOrEmpty()) {
                                districtData.value = response.body()?.data ?: emptyList()
                            }
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<DistrictModel>, t: Throwable) {
                    state.value = State.ERROR
                    Log.e("getDistrict", t.message.toString())
                }
            })
    }

    fun updateProfile(context: Context, updateProfileModel: UpdateProfileModel, imgProfile: File?) {
        stateUpdate.value = State.LOADING
        NetworkClient().getService(context)
            .updateProfile(
                Prefs(context).jwt.toString(),
                updateProfileModel.idDistrict,
                updateProfileModel.idCity,
                updateProfileModel.idProvince,
                updateProfileModel.email,
                updateProfileModel.ktp,
                updateProfileModel.name,
                updateProfileModel.telp,
                updateProfileModel.address,
                updateProfileModel.userId,
                if (imgProfile != null) {
                    MultipartBody.Part.createFormData("img_user", imgProfile.getName(), RequestBody.create("image/*".toMediaTypeOrNull(), imgProfile))
                } else {
                    null
                }
            )
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            Prefs(context).jwt = response.body()?.data?.jwt.toString()
                            stateUpdate.value = State.COMPLETE
                        } else {
                            stateUpdate.value = State.ERROR
                            errorMassage.value = response.body()?.statusMessage.toString()
                        }
                    } else {
                        stateUpdate.value = State.ERROR
                        errorMassage.value = response.body()?.statusMessage.toString()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    stateUpdate.value = State.ERROR
                    errorMassage.value = t.message.toString()
                }
            })
    }
}