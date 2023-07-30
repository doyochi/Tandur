package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseModel(
    @SerializedName("status_code")
    var statusCode: Int? = 0,
    @SerializedName("status_message")
    var statusMessage: String? = null
): Parcelable
