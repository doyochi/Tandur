package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateRentLandModel(
    var nameLand: String? = null,
    var address: String? = null,
    var province: String? = null,
    var city: String? = null,
    var district: String? = null,
    var locationLand: String? = null,
    var noCertificateLand: String? = null,
    var desc: String? = null,
    var ownNameLand: String? = null,
    var ownKtp: String? = null,
    var ownEmail: String? = null,
    var ownTelp: String? = null,
    var widthLand: String? = null,
    var lengthLand: String? = null,
    var rule: String? = null,
    var price: String? = null,
    var rating: String? = null,
    var longitude: String? = null,
    var latitude: String? = null,
    var facility: String? = null,
    var idLand: String? = null,
): Parcelable
