package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LandModel(
    @SerializedName("data")
    var data: List<LandData>? = emptyList()
): Parcelable, BaseModel()

@Parcelize
data class LandData(
    @SerializedName("ID_LAND")
    var idLand: String? = null,
    @SerializedName("OWNER_USER")
    var ownerUser: String? = null,
    @SerializedName("NAME_LAND")
    var nameLand: String? = null,
    @SerializedName("ADDRESS_LAND")
    var addressLand: String? = null,
    @SerializedName("DISTRICT_LAND")
    var districtLand: String? = null,
    @SerializedName("NAME_DISTRICT")
    var districtName: String? = null,
    @SerializedName("CITY_LAND")
    var cityLand: String? = null,
    @SerializedName("PROVINCE_LAND")
    var provinceLand: String? = null,
    @SerializedName("LOCATION_LAND")
    var locationLand: String? = null,
    @SerializedName("NOCERTIFICATE_LAND")
    var noCertificateLand: String? = null,
    @SerializedName("URLDOC_LAND")
    var urlDocLand: List<String>? = null,
    @SerializedName("DESCDOC_LAND")
    var descDocLand: String? = null,
    @SerializedName("LONG_LAND")
    var longLand: String? = null,
    @SerializedName("LAT_LAND")
    var latLand: String? = null,
    @SerializedName("OWNNAME_LAND")
    var ownNameLand: String? = null,
    @SerializedName("OWNKTP_LAND")
    var ownKtpLand: String? = null,
    @SerializedName("OWNEMAIL_LAND")
    var ownEmailLand: String? = null,
    @SerializedName("OWNTELP_LAND")
    var ownTelpLand: String? = null,
    @SerializedName("WIDTH_LAND")
    var widthLand: Double? = 0.0,
    @SerializedName("LENGTH_LAND")
    var lengthLand: Double? = 0.0,
    @SerializedName("RULE_LAND")
    var ruleLand: String? = null,
    @SerializedName("URLGALLERY_LAND")
    var urlGalleryLand: List<String>? = null,
    @SerializedName("PRICE_LAND")
    var priceLand: String? = null,
    @SerializedName("RATING_LAND")
    var ratingLand: Float? = 0f,
    @SerializedName("REGISTERAT_LAND")
    var registerAtLand: String? = null,
    @SerializedName("IS_ACTIVE")
    var isActive: Int? = 0,
    @SerializedName("IMG_USER")
    var imgUser: String? = null,
    @SerializedName("TELP_USER")
    var telpUser: String? = null,
    @SerializedName("FACILITY_LAND")
    var facilityLand: String? = null,
    @SerializedName("GALLERY_LAND")
    var galleryLand: List<String>? = null,
): Parcelable, BaseModel()