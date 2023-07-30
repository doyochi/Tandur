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

class ProductPaymentAdapter(): RecyclerView.Adapter<ProductPaymentAdapter.ViewHolder>() {
    var list: List<ProductData> = emptyList()
    var listRequest: List<RequestProductData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_payment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        val dataRequest = listRequest[position]

        Glide.with(holder.itemView.context)
            .load(data.photoProduct?.first().toString())
            .placeholder(R.drawable.item_default)
            .error(R.drawable.item_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)

        holder.textViewJudul.text = data.nameProduct.toString()
        holder.textViewRating.text = data.ratingProduct.toString()
        holder.textViewDeskripsi.text = "Qty: ${dataRequest.qty} Pcs"
        holder.textViewLokasi.text = data.addressUser.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView = ItemView.findViewById<View>(R.id.imageView) as ImageView
        val textViewJudul = ItemView.findViewById<View>(R.id.textViewJudul) as TextView
        val textViewDeskripsi = ItemView.findViewById<View>(R.id.textViewDeskripsi) as TextView
        val textViewRating = ItemView.findViewById<View>(R.id.textViewRating) as TextView
        val textViewLokasi = ItemView.findViewById<View>(R.id.textViewLokasi) as TextView
    }
}