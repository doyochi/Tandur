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
import com.google.android.material.button.MaterialButton
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils

class ProductDetailAdapter(private val listener: ItemClickListener<NewPurchaseDetail>): RecyclerView.Adapter<ProductDetailAdapter.ViewHolder>() {
    var list: List<NewPurchaseDetail> = emptyList()
    var isPaid = false
    var isReview = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_purchase, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        Glide.with(holder.itemView.context)
            .load(data.PHOTO_PRODUCT?.first())
            .placeholder(R.drawable.item_default)
            .error(R.drawable.item_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)

        holder.textViewJudul.text = data.NAME_PRODUCT.toString()
        holder.textViewRating.text = data.RATING_PRODUCT.toString()
        holder.textViewDeskripsi.text = "Qty: ${data.QTY_PD} Pcs"
        holder.textViewLokasi.text = data.NAME_CITY.toString()

        if (isPaid) {
            if (isReview) {
                holder.materialButtonReview.visibility = View.GONE
            } else {
                holder.materialButtonReview.visibility = View.VISIBLE
            }
        } else {
            holder.materialButtonReview.visibility = View.GONE
        }

        holder.materialButtonReview.setOnClickListener {
            listener.onClickItem(data)
        }
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
        val materialButtonReview = ItemView.findViewById<View>(R.id.materialButtonReview) as MaterialButton
    }
}