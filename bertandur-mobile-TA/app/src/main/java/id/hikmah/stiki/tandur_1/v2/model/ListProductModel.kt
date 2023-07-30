package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListProductModel(
    @SerializedName("data")
    var data: List<ListProductData>? = emptyList()
): Parcelable, BaseModel()

@Parcelize
data class ListProductData(
    @SerializedName("ID_PRODUCT")
    var idProduct: String? = null,
    @SerializedName("NAME_PRODUCT")
    var nameProduct: String? = null,
    @SerializedName("CATEGORY")
    var category: String? = null,
    @SerializedName("NAME_PCAT")
    var namePcat: String? = null,
    @SerializedName("PHOTO_PRODUCT")
    var photoProduct: List<String>? = null,
    @SerializedName("PRICE_PRODUCT")
    var priceProduct: String? = null,
    @SerializedName("WEIGHT_PRODUCT")
    var weightProduct: String? = null,
    @SerializedName("RATING_PRODUCT")
    var ratingProduct: Float? = 0f,
    @SerializedName("NAME_DISTRICT")
    var nameDistrict: String? = null,
): Parcelable, BaseModel()