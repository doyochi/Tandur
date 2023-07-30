package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    @SerializedName("data")
    var data: List<ProductData>? = emptyList()
): Parcelable, BaseModel()

@Parcelize
data class ProductData(
    @SerializedName("ID_PRODUCT")
    var idProduct: String? = null,
    @SerializedName("ID_PCAT")
    var idPcat: String? = null,
    @SerializedName("PHOTO_PRODUCT")
    var photoProduct: List<String>? = null,
    @SerializedName("NAME_PRODUCT")
    var nameProduct: String? = null,
    @SerializedName("PRICE_PRODUCT")
    var priceProduct: String? = null,
    @SerializedName("RATING_PRODUCT")
    var ratingProduct: Float? = 0f,
    @SerializedName("ADDRESS_USER")
    var addressUser: String? = null,
    @SerializedName("NAME_USER")
    var nameUser: String? = null,
    @SerializedName("IMG_USER")
    var imgUser: String? = null,
    @SerializedName("TELP_USER")
    var telpUser: String? = null,
    @SerializedName("STOCK_PRODUCT")
    var stockProduct: Int? = 0,
    @SerializedName("JML_TERJUAL")
    var jmlTerjual: Int? = 0,
    @SerializedName("CONDITION_PRODUCT")
    var conditionProduct: String? = null,
    @SerializedName("NAME_PCAT")
    var namePcat: String? = null,
    @SerializedName("DESC_PRODUCT")
    var descProduct: String? = null,
    @SerializedName("NOTE_PRODUCT")
    var noteProduct: String? = null,
    @SerializedName("WEIGHT_PRODUCT")
    var weightProduct: String? = null,
): Parcelable, BaseModel()