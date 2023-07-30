package id.hikmah.stiki.tandur_1.v2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterModel(
    var idDistrict: Int,
    var idCity: Int,
    var idProvince: Int,
    var email: String,
    var ktp: String,
    var name: String,
    var password: String,
    var telp: String,
    var address: String
): Parcelable
