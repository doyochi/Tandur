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

class UpdateLandViewModel: ViewModel() {
    var land = MutableLiveData<LandData>()
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

    fun getDetailLand(context: Context, idLand: String) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .getDetailLand(
                Prefs(context).jwt.toString(),
                idLand.toString()
            )
            .enqueue(object : Callback<LandModel>{
                override fun onResponse(call: Call<LandModel>, response: Response<LandModel>) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == HttpsURLConnection.HTTP_OK) {
                            state.value = State.COMPLETE
                            land.value = response.body()?.data?.first()
                        } else {
                            state.value = State.ERROR
                        }
                    } else {
                        state.value = State.ERROR
                    }
                }

                override fun onFailure(call: Call<LandModel>, t: Throwable) {
                    Log.e("getDetailLand", t.message.toString())
                    state.value = State.ERROR
                }
            })
    }

    fun updateLahan(context: Context, updateRentLandModel: UpdateRentLandModel) {
        state.value = State.LOADING
        NetworkClient().getService(context)
            .updateLahan(
                Prefs(context).jwt.toString(),
                updateRentLandModel.nameLand.toString(),
                updateRentLandModel.address.toString(),
                updateRentLandModel.province.toString(),
                updateRentLandModel.city.toString(),
                updateRentLandModel.district.toString(),
                updateRentLandModel.locationLand.toString(),
                updateRentLandModel.noCertificateLand.toString(),
                updateRentLandModel.desc.toString(),
                updateRentLandModel.ownNameLand.toString(),
                updateRentLandModel.ownKtp.toString(),
                updateRentLandModel.ownEmail.toString(),
                updateRentLandModel.ownTelp.toString(),
                updateRentLandModel.widthLand.toString(),
                updateRentLandModel.lengthLand.toString(),
                updateRentLandModel.rule.toString(),
                updateRentLandModel.price.toString(),
                updateRentLandModel.rating.toString(),
                updateRentLandModel.longitude.toString(),
                updateRentLandModel.latitude.toString(),
                updateRentLandModel.facility.toString(),
                updateRentLandModel.idLand.toString(),
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
                    Log.e("updateLahan", t.message.toString())
                    errorMassage.value = t.message.toString()
                }
            })
    }
}