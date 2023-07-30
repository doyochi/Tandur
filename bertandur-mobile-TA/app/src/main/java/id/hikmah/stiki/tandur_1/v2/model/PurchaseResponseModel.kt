package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PurchaseResponseModel(
    @SerializedName("data")
    var data: PurchaseResponseData? = null
): Parcelable, BaseModel()

@Parcelize
data class PurchaseResponseData(
    @SerializedName("id_purchase")
    var idPurchase: String? = null,
    @SerializedName("payment_url")
    var paymentUrl: String? = null,
): Parcelable, BaseModel()
