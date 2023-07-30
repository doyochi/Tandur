package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryProductModel(
    val data: List<HistoryProductData>?
): Parcelable, BaseModel()

@Parcelize
data class HistoryProductData(
    val ALAMAT_USER: String?,
    val EMAIL_USER: String?,
    val ID_PURCHASE: String?,
    val ID_USER: String?,
    val NAME_USER: String?,
    val ORDER_ID: String?,
    val PAYMENT_URL: String?,
    val PURCHASE_DETAIL: List<HistoryPurchaseDetail>?,
    val RESITRACK_PURCHASE: String?,
    val SHIPPING_COST: Int?,
    val SHIPPING_METHOD: String?,
    val STATUS_PURCHASE: Int?,
    val TELP_USER: String?,
    val TOTPAYMENT_PURCHASE: Int?,
    val TOTQTY_PURCHASE: Int?
): Parcelable

@Parcelize
data class HistoryPurchaseDetail(
    val ID_PRODUCT: Int?,
    val NAME_CITY: String?,
    val NAME_PRODUCT: String?,
    val PHOTO_PRODUCT: String?,
    val PRICE_PRODUCT: Int?,
    val QTY_PD: Int?,
    val RATING_PRODUCT: String?,
    val TOTAL_PRICE: Int?,
    val TOTAL_WEIGHT: String?,
    val WEIGHT_PRODUCT: String?
): Parcelable