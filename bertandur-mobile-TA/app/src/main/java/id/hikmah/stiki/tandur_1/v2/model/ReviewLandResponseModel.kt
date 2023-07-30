package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewLandResponseModel(
    val data: List<ReviewLandData>?,
): Parcelable, BaseModel()

@Parcelize
data class ReviewLandData(
    val DATE_REVIEW: String?,
    val ID_LAND: String?,
    val ID_REVIEW_LAND: Int?,
    val ID_USER: String?,
    val IMG_USER: String?,
    val NAME_USER: String?,
    val RATING: String?,
    val REVIEW_CONTENT: String?,
    val REVIEW_TITLE: String?,
): Parcelable