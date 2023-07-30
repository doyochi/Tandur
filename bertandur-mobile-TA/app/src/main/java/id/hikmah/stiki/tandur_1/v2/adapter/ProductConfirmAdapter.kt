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

class ProductConfirmAdapter(private var listener: ItemClickListener<Boolean>): RecyclerView.Adapter<ProductConfirmAdapter.ViewHolder>() {
    var list: List<ProductData> = emptyList()
    var listRequest: List<RequestProductData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_confirm, parent, false)
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
        holder.textViewHarga.text = Utils.changePrice(data.priceProduct.toString())
        holder.textViewRating.text = data.ratingProduct.toString()

        holder.cardViewMinus.setOnClickListener {
            if (dataRequest.qty!! > 1) {
                dataRequest.qty = dataRequest.qty?.minus(1)
                dataRequest.totalHarga = dataRequest.qty?.times(data.priceProduct.toString().toInt())
                holder.textViewQuantity.text = dataRequest.qty.toString()
                listener.onClickItem(true)
            }
        }

        holder.cardViewPlus.setOnClickListener {
            dataRequest.qty = dataRequest.qty?.plus(1)
            dataRequest.totalHarga = dataRequest.qty?.times(data.priceProduct.toString().toInt())
            holder.textViewQuantity.text = dataRequest.qty.toString()
            listener.onClickItem(true)
        }

        holder.textViewLokasi.text = data.addressUser.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView = ItemView.findViewById<View>(R.id.imageView) as ImageView
        val textViewJudul = ItemView.findViewById<View>(R.id.textViewJudul) as TextView
        val textViewHarga = ItemView.findViewById<View>(R.id.textViewHarga) as TextView
        val textViewRating = ItemView.findViewById<View>(R.id.textViewRating) as TextView
        val textViewLokasi = ItemView.findViewById<View>(R.id.textViewLokasi) as TextView
        val cardViewMinus = ItemView.findViewById<View>(R.id.cardViewMinus) as CardView
        val cardViewPlus = ItemView.findViewById<View>(R.id.cardViewPlus) as CardView
        val textViewQuantity = ItemView.findViewById<View>(R.id.textViewQuantity) as TextView
    }
}