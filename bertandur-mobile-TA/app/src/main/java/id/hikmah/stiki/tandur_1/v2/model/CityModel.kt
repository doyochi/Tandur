package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityModel(
    @SerializedName("data")
    var data: List<CityData>? = emptyList()
): Parcelable, BaseModel()

@Parcelize
data class CityData(
    @SerializedName("ID_CITY")
    var idCity: Int? = 0,
    @SerializedName("ID_PROVINCE")
    var idProvince: Int? = 0,
    @SerializedName("NAME_CITY")
    var nameCity: String? = null,
    @SerializedName("ISACTIVE_CITY")
    var isActiveCity: Int? = 0
): Parcelable, BaseModel()
