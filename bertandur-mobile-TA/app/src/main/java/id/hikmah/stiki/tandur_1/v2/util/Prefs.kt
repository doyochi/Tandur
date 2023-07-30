package id.hikmah.stiki.tandur_1.v2.util

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val KEY_JWT = "KEY_JWT"

    private val preferencesJWT = context.getSharedPreferences(KEY_JWT, Context.MODE_PRIVATE)

    var jwt: String?
        get() = preferencesJWT.getString(KEY_JWT, null)
        set(value) = preferencesJWT.edit().putString(KEY_JWT, value).apply()
}