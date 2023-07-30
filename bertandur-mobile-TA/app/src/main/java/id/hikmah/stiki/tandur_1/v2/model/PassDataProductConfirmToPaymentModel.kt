package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PassDataProductConfirmToPaymentModel(
    var data: List<ProductData>? = null,
    var dataRequest: List<RequestProductData>? = null,
    var ongkir: Int? = null,
    var total: Int? = null,
): Parcelable
