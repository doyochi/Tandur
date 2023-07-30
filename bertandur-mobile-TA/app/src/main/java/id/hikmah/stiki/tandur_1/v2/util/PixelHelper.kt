package id.hikmah.stiki.tandur_1.v2.util


import android.content.res.Resources
import android.util.DisplayMetrics


class PixelHelper {
    companion object DpHelper {
        fun convertDpToPx(dp : Int,res : Resources) : Int{
            val displayMetrics = res.displayMetrics
            return Math.round(dp * (displayMetrics.xdpi/ DisplayMetrics.DENSITY_DEFAULT))
        }
    }
}