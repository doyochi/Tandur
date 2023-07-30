package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateProfileModel(
    var idDistrict: Int,
    var idCity: Int,
    var idProvince: Int,
    var email: String,
    var ktp: String,
    var name: String,
    var telp: String,
    var address: String,
    var userId: String,
): Parcelable
