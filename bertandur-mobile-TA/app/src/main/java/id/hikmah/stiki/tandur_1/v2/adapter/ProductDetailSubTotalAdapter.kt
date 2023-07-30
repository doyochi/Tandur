package id.hikmah.stiki.tandur_1.v2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils

class ProductDetailSubTotalAdapter(): RecyclerView.Adapter<ProductDetailSubTotalAdapter.ViewHolder>() {
    var list: List<NewPurchaseDetail> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_price, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.textViewDetail.text = "${data.QTY_PD}x - ${data.NAME_PRODUCT}"
        holder.textViewPrice.text = Utils.changePrice(data.TOTAL_PRICE.toString())
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewDetail = ItemView.findViewById<View>(R.id.textViewDetail) as TextView
        val textViewPrice = ItemView.findViewById<View>(R.id.textViewPrice) as TextView
    }
}