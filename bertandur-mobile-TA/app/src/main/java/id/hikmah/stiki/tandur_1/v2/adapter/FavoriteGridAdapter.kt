package id.hikmah.stiki.tandur_1.v2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.v2.database.entity.FavoriteEntity
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.model.ProductData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils

class FavoriteGridAdapter(private var listener: ItemClickListener<FavoriteEntity>): RecyclerView.Adapter<FavoriteGridAdapter.ViewHolder>() {
    var list: List<FavoriteEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        Glide.with(holder.itemView.context)
            .load(data.photoProduct?.split(";")?.first().toString())
            .placeholder(R.drawable.item_default)
            .error(R.drawable.item_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)

        holder.textViewJudul.text = data.nameProduct.toString()
        holder.textViewHarga.text = Utils.changePrice(data.priceProduct.toString())
        holder.textViewRating.text = data.ratingProduct.toString()
        holder.ratingBar.rating = if (data.ratingProduct.toString().isNullOrEmpty()) {
            0f
        } else {
            data.ratingProduct!!
        }

        holder.itemView.setOnClickListener {
            listener.onClickItem(data)
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
        val ratingBar = ItemView.findViewById<View>(R.id.ratingBar) as RatingBar
        val textViewRating = ItemView.findViewById<View>(R.id.textViewRating) as TextView
        val textViewLokasi = ItemView.findViewById<View>(R.id.textViewLokasi) as TextView
    }
}