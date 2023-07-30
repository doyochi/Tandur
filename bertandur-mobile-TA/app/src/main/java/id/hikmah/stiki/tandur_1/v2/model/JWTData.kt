package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
open class JWTData(
    @SerializedName("ID_USER")
    val idUser: String? = null,
    @SerializedName("KTP_USER")
    val ktpUser: String? = null,
    @SerializedName("ID_DISTRICT")
    val idDistrict: String? = null,
    @SerializedName("ID_CITY")
    val idCity: String? = null,
    @SerializedName("ID_PROVINCE")
    val idProvince: String? = null,
    @SerializedName("EMAIL_USER")
    val emailUser: String? = null,
    @SerializedName("NAME_USER")
    val nameUser: String? = null,
    @SerializedName("TELP_USER")
    val telpUser: String? = null,
    @SerializedName("ADDRESS_USER")
    val addressUser: String? = null,
    @SerializedName("LONG_USER")
    val longUser: String? = null,
    @SerializedName("LAT_USER")
    val latUser: String? = null,
    @SerializedName("IMG_USER")
    val imgUser: String? = null,
    @SerializedName("ISVERIF_USER")
    val isVerifUser: Int? = 0
) : Parcelable
