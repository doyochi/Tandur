package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentRentLandModel(
    @SerializedName("id_land")
    var idLand: String,
    @SerializedName("duration_rent")
    var durationRent: String,
    @SerializedName("start_date")
    var startDate: String,
    @SerializedName("end_date")
    var endDate: String,
    @SerializedName("payment_method")
    var paymentMethod: String,
    @SerializedName("total_payment")
    var totalPayment: String,
): Parcelable
