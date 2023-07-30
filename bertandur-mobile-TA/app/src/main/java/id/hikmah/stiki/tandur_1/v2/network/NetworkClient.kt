package id.hikmah.stiki.tandur_1.v2.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import id.hikmah.stiki.tandur_1.remote.service.APIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class NetworkClient {

    //set function interceptor
    private fun setInterceptor(context: Context): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.MINUTES)
            .connectTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .addInterceptor(ChuckerInterceptor(context))
            .build()
    }

    //set retrofit
    private fun setRetrofit(context: Context): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .client(setInterceptor(context))
            .baseUrl("https://bertandur.yntkts.my.id/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    //get service
    fun getService(context: Context): id.hikmah.stiki.tandur_1.v2.network.APIService {
        return setRetrofit(context).create(id.hikmah.stiki.tandur_1.v2.network.APIService::class.java)
    }

}