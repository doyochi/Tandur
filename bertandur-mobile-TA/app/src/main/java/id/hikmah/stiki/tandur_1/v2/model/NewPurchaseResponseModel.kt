package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewPurchaseResponseModel(
    val data: NewPurchaseData
): Parcelable, BaseModel()

@Parcelize
data class NewPurchaseData(
    val ALAMAT_USER: String? = null,
    val EMAIL_USER: String? = null,
    val ID_PURCHASE: String? = null,
    val ID_USER: String? = null,
    val NAME_USER: String? = null,
    val ORDER_ID: String? = null,
    val PAYMENT_DETAIL: List<PaymentDetail>? = null,
    val PAYMENT_URL: String? = null,
    val PURCHASE_DETAIL: List<NewPurchaseDetail>? = null,
    val STATUS_PURCHASE: Int? = null,
    val STATUS_REVIEW_PRODUCT: String? = null,
    val SHIPPING_COST: Int? = null,
    val SHIPPING_METHOD: String? = null,
    val TELP_USER: String? = null,
    val TOTPAYMENT_PURCHASE: Int? = null,
    val TOTQTY_PURCHASE: Int? = null,
): Parcelable

@Parcelize
data class PaymentDetail(
    val bank_code: String?,
    val bill_code: String?,
    val created_at: String?,
    val order_id: String?,
    val payment_type: String?,
    val status: String?,
    val total_price: Int?,
    val updated_at: String?,
    val va_number: String?
): Parcelable

@Parcelize
data class NewPurchaseDetail(
    val ID_PRODUCT: Int? = null,
    val NAME_CITY: String? = null,
    val NAME_PRODUCT: String? = null,
    val PHOTO_PRODUCT: List<String>? = null,
    val PRICE_PRODUCT: Int? = null,
    val QTY_PD: Int? = null,
    val RATING_PRODUCT: Float? = null,
    val TOTAL_PRICE: Int? = null,
    val TOTAL_WEIGHT: String? = null,
    val WEIGHT_PRODUCT: String? = null,
): Parcelable