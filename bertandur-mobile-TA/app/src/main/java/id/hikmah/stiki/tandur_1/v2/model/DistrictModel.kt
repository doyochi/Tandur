package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DistrictModel(
    @SerializedName("data")
    var data: List<DistrictData>? = emptyList()
): Parcelable, BaseModel()

@Parcelize
data class DistrictData(
    @SerializedName("ID_DISTRICT")
    var idDistrict: Int? = 0,
    @SerializedName("ID_CITY")
    var idCity: Int? = 0,
    @SerializedName("NAME_DISTRICT")
    var nameDistrict: String? = null,
    @SerializedName("ISACTIVE_DISTRICT")
    var isActiveDistrict: Int? = 0
): Parcelable
