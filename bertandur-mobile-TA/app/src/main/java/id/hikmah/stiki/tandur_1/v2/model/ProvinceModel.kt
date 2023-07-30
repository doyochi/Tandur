package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProvinceModel(
    @SerializedName("data")
    var data: List<ProvinceData>? = emptyList()
): Parcelable, BaseModel()

@Parcelize
data class ProvinceData(
    @SerializedName("ID_PROVINCE")
    var idProvince: Int? = 0,
    @SerializedName("NAME_PROVINCE")
    var nameProvince: String? = null,
    @SerializedName("ISACTIVE_PROVINCE")
    var isActiveProvince: Int? = 0
): Parcelable
