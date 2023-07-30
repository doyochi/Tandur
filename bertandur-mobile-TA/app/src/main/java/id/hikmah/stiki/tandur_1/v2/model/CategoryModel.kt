package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    @SerializedName("data")
    var data: List<CategoryData>? = emptyList()
): Parcelable, BaseModel()

@Parcelize
data class CategoryData(
    @SerializedName("ID_PCAT")
    var idPcat: String? = null,
    @SerializedName("CATEGORY")
    var category: String? = null,
    @SerializedName("NAME_PCAT")
    var namePcat: String? = null,
    @SerializedName("ISACTIVE_PCAT")
    var isActivePcat: String? = null,
): Parcelable, BaseModel()