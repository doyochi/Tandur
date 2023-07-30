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
import id.hikmah.stiki.tandur_1.v2.database.entity.ProductEntity
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils

class CartAdapter(private var listenerHapus: ItemClickListener<ProductEntity>, private var listenerBeli: ItemClickListener<ProductEntity>): RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    var list: List<ProductEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        Glide.with(holder.itemView.context)
            .load(data.photoProduct.toString())
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

        holder.textViewLokasi.text = data.addressUser.toString()

        holder.materialButtonHapus.setOnClickListener {
            listenerHapus.onClickItem(data)
        }

        holder.materialButtonBeliSekarang.setOnClickListener {
            listenerBeli.onClickItem(data)
        }
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
        val ratingBar = ItemView.findViewById<View>(R.id.ratingBar) as RatingBar
        val materialButtonHapus = ItemView.findViewById<View>(R.id.materialButtonHapus) as MaterialButton
        val materialButtonBeliSekarang = ItemView.findViewById<View>(R.id.materialButtonBeliSekarang) as MaterialButton
    }

    fun convertToProductDataList(): ArrayList<ProductData> {
        val datas = ArrayList<ProductData>()
        list.forEach {
            val image = mutableListOf<String>()
            image.add(it.photoProduct.toString())
            val data = ProductData(
                it.idProduct,
                "0",
                image,
                it.nameProduct,
                it.priceProduct,
                it.ratingProduct,
                it.addressUser,
                it.nameUser,
                it.imgUser,
                it.telpUser,
                it.stockProduct,
                it.jmlTerjual,
                it.conditionProduct,
                it.namePcat,
                it.descProduct,
                it.noteProduct,
                "0",
            )
            datas.add(data)
        }
        return datas
    }
}