package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationResponseModel(
    val data: List<NotificationData>?
): Parcelable, BaseModel()

@Parcelize
data class NotificationData(
    val BODY_MESSAGE: String?,
    val ID_LOG: Int?,
    val STATUS_MESSAGE: String?,
    val TITLE_MESSAGE: String?,
    val USER_TOKEN: String?,
    val created_at: String?
): Parcelable