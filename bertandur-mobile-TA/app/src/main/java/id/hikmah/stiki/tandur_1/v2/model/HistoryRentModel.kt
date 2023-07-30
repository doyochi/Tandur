package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryRentModel(
    val data: List<HistoryRentData>?
): Parcelable, BaseModel()

@Parcelize
data class HistoryRentData(
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
    val PAYMENT_URL: String?,
    val PRICE_LAND: Int?,
    val RATING_LAND: Int?,
    val STARTDATE_RENT: String?,
    val STATUS_RENT: Int?,
    val TOTPAYMENT_RENT: Int?,
    val URLGALLERY_LAND: List<String>?,
    val WIDTH_LAND: Int?
): Parcelable