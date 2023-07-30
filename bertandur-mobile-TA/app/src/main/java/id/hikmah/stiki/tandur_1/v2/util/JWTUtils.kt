package id.hikmah.stiki.tandur_1.v2.util

import android.util.Base64
import com.google.gson.Gson
import id.hikmah.stiki.tandur_1.v2.model.JWTData
import java.io.UnsupportedEncodingException

object JWTUtils {
    fun decoded(JWTEncoded: String) : JWTData {
        val split = JWTEncoded.split("\\.".toRegex()).toTypedArray()
        val gson = Gson()
        val jwtJson = gson.fromJson(getJson(split[1]), JWTData::class.java)
        return jwtJson!!
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getJson(strEncoded: String): String {
        val decodedBytes: ByteArray = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes, charset("UTF-8"))
    }
}