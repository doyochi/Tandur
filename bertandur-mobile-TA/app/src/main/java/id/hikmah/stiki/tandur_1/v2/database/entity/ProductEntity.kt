package id.hikmah.stiki.tandur_1.v2.database.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductEntity")
data class ProductEntity (
    @PrimaryKey
    @NonNull
    var idProduct: String,
    var photoProduct: String? = null,
    var nameProduct: String? = null,
    var priceProduct: String? = null,
    var ratingProduct: Float? = 0f,
    var addressUser: String? = null,
    var nameUser: String? = null,
    var imgUser: String? = null,
    var telpUser: String? = null,
    var stockProduct: Int? = 0,
    var jmlTerjual: Int? = 0,
    var conditionProduct: String? = null,
    var namePcat: String? = null,
    var descProduct: String? = null,
    var noteProduct: String? = null,
)