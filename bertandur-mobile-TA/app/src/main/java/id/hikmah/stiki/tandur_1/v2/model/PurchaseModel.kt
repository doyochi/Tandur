package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PurchaseModel(
    @SerializedName("data")
    var data: PurchaseData? = null
): Parcelable, BaseModel()

@Parcelize
data class PurchaseData(
    @SerializedName("ID_PURCHASE")
    var idPurchase: String? = null,
    @SerializedName("STATUS_PAYPRODUCT")
    var statusPayProduct: String? = null,
    @SerializedName("METHOD_PAYPRODUCT")
    var methodPayProduct: String? = null,
    @SerializedName("TOTPAYMENT_PAYPRODUCT")
    var totPaymentProduct: String? = null,
    @SerializedName("ORDERID_PAYPRODUCT")
    var orderIdPayProduct: String? = null,
    @SerializedName("SHIPPING_METHOD")
    var shippingMethod: String? = null,
    @SerializedName("SHIPPING_COST")
    var shippingCost: String? = null,
    @SerializedName("PAYMENT")
    var payment: String? = null,
    @SerializedName("TOTPAYMENT_PURCHASE")
    var totPaymentPurchase: String? = null,
    @SerializedName("PURCHASE_DETAIL")
    var purchaseDetail: List<PurchaseDetail>? = null,
): Parcelable

@Parcelize
data class PurchaseDetail(
    @SerializedName("ID_PRODUCT")
    var idProduct: String? = null,
    @SerializedName("NAME_PRODUCT")
    var nameProduct: String? = null,
    @SerializedName("QTY_PD")
    var qtyPd: String? = null,
    @SerializedName("TOTAL_PRICE")
    var totalPrice: String? = null,
    @SerializedName("PHOTO_PRODUCT")
    var photoProduct: String? = null,
    @SerializedName("RATING_PRODUCT")
    var ratingProduct: String? = null,
    @SerializedName("NAME_CITY")
    var nameCity: String? = null,
): Parcelable