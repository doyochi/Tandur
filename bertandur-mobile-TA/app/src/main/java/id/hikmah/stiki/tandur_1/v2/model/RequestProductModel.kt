package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestProductModel(
    @SerializedName("product")
    var product: List<RequestProductData>? = null,
    @SerializedName("shipping_method")
    var shippingMethod: String? = null,
    @SerializedName("payment_method")
    var paymentMethod: String? = null,
    @SerializedName("name_user")
    var nameUser: String? = null,
    @SerializedName("email_user")
    var emailUser: String? = null,
    @SerializedName("telp_user")
    var telpUser: String? = null,
    @SerializedName("alamat_user")
    var alamatUser: String? = null,
    @SerializedName("shipping_cost")
    var shippingCost: Int? = null,
    @SerializedName("total_payment")
    var totalPayment: Int? = null
): Parcelable, BaseModel()

@Parcelize
data class RequestProductData(
    @SerializedName("id_product")
    var idProduct: String? = null,
    @SerializedName("qty")
    var qty: Int? = null,
    @SerializedName("total_harga")
    var totalHarga: Int? = null,
): Parcelable