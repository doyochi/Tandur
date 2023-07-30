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
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.model.ProductData
import id.hikmah.stiki.tandur_1.v2.model.ProductModel
import id.hikmah.stiki.tandur_1.v2.model.RequestProductData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils

class ProductConfirmSubTotalAdapter(): RecyclerView.Adapter<ProductConfirmSubTotalAdapter.ViewHolder>() {
    var list: List<ProductData> = emptyList()
    var listRequest: List<RequestProductData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_price, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        val dataRequest = listRequest[position]

        holder.textViewDetail.text = "${dataRequest.qty}x - ${data.nameProduct}"
        holder.textViewPrice.text = Utils.changePrice(dataRequest.totalHarga.toString())
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun sumTotalPrice(): Int {
        var sum = 0
        listRequest.forEach {
            sum += it.totalHarga.toString().toInt()
        }
        return sum
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewDetail = ItemView.findViewById<View>(R.id.textViewDetail) as TextView
        val textViewPrice = ItemView.findViewById<View>(R.id.textViewPrice) as TextView
    }
}