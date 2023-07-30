package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SellProductModel(
    var name_product: String,
    var category: String,
    var id_product_category: String,
    var price: String,
    var weigth_product: String,
    var stock: String,
    var condition: String,
    var desc: String,
    var note: String,
    var gallery1: String?,
    var gallery2: String?,
    var gallery3: String?,
    var gallery4: String?,
    var gallery5: String?,
): Parcelable
