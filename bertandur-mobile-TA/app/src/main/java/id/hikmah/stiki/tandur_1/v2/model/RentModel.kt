package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RentModel(
    val data: RentData?
): Parcelable, BaseModel()

@Parcelize
data class RentData(
    val DURATION_RENT: Int?,
    val ENDDATE_RENT: String?,
    val FACILITY_LAND: String?,
    val ID_LAND: String?,
    val ID_RENT: String?,
    val ID_USER: String?,
    val LENGTH_LAND: Int?,
    val NAME_CITY: String?,
    val NAME_DISTRICT: String?,
    val NAME_LAND: String?,
    val NAME_PROVINCE: String?,
    val ORDER_ID: String?,
    val PAYMENT_DETAIL: List<RentPaymentDetail>?,
    val PAYMENT_URL: String?,
    val PRICE_LAND: Int?,
    val RATING_LAND: Int?,
    val STARTDATE_RENT: String?,
    val STATUS_REVIEW_LAND: String?,
    val STATUS_RENT: Int?,
    val TOTPAYMENT_RENT: Int?,
    val URLGALLERY_LAND: List<String>?,
    val WIDTH_LAND: Int?
): Parcelable

@Parcelize
data class RentPaymentDetail(
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