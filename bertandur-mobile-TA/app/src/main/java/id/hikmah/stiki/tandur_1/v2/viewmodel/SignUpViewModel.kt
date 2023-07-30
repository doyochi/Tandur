package id.hikmah.stiki.tandur_1.v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.network.NetworkClient
import id.hikmah.stiki.tandur_1.v2.util.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class SignUpViewModel: ViewModel() {
    var provinceData = MutableLiveData<List<ProvinceData>>()
    var cityData = MutableLiveData<List<CityData>>()
    var districtData = MutableLiveData<List<DistrictData>>()
    var state = MutableLiveData<State>()

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
}