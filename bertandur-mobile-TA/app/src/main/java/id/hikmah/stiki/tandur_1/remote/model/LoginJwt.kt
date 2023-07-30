package id.hikmah.stiki.tandur_1.remote.model


import com.google.gson.annotations.SerializedName

data class LoginJwt(
    @SerializedName("jwt")
    val jwt: String
)