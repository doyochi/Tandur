package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RentLandModel(
    var nameLand: String,
    var address: String,
    var province: String,
    var city: String,
    var district: String,
    var locationLand: String,
    var noCertificateLand: String,
    var foto1: String,
    var foto2: String,
    var desc: String,
    var ownNameLand: String,
    var ownKtp: String,
    var ownEmail: String,
    var ownTelp: String,
    var widthLand: String,
    var lengthLand: String,
    var rule: String,
    var price: String,
    var rating: String,
    var longtitude: String,
    var latitude: String,
    var facility: String,
    var gallery1: String?,
    var gallery2: String?,
    var gallery3: String?,
    var gallery4: String?,
    var gallery5: String?,
): Parcelable
