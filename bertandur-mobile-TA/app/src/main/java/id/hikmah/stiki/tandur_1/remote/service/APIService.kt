package id.hikmah.stiki.tandur_1.remote.service

import id.hikmah.stiki.tandur_1.remote.model.LoginInfo
import id.hikmah.stiki.tandur_1.remote.model.LoginJwt
import id.hikmah.stiki.tandur_1.remote.model.LoginResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {

    //Register
    @POST("auth/register")
    suspend fun registerAccount(@Body body: RequestBody)

    //Login
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun loginAccount(@Body loginInfo: LoginInfo): LoginResponse
}