package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RentResponseModel(
    @SerializedName("data")
    var data: RentResponseData? = null
): Parcelable, BaseModel()

@Parcelize
data class RentResponseData(
    @SerializedName("id_rent")
    var idRent: String? = null,
    @SerializedName("payment_url")
    var paymentUrl: String? = null,
): Parcelable, BaseModel()
