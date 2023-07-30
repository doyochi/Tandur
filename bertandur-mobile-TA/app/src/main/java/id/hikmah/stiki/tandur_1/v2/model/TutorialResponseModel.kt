package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TutorialResponseModel(
    val data: List<TutorialData>?
): Parcelable, BaseModel()

@Parcelize
data class TutorialData(
    val DESC_TUTORIAL: String?,
    val ID_TUTORIAL: Int?,
    val ID_USER: String?,
    val TITLE_TUTORIAL: String?,
    val URLIMG_TUTORIAL: String?
): Parcelable