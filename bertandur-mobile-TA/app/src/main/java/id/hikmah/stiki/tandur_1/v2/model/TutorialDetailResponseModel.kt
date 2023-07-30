package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TutorialDetailResponseModel(
    val data: List<TutorialDetailData>?
): Parcelable, BaseModel()

@Parcelize
data class TutorialDetailData(
    val DESC_TD: String?,
    val ID_TD: Int?,
    val ID_TUTORIAL: Int?,
    val TITLE_TD: String?,
    val URLTHUMBNAIL_TD: String?,
    val URLVIDEO_TD: String?
): Parcelable