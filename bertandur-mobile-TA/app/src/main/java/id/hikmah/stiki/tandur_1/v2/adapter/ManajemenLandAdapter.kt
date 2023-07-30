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
import com.google.android.material.button.MaterialButton
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils
import java.io.File

class ManajemenLandAdapter(
    private var listenerDetail: ItemClickListener<LandData>,
): RecyclerView.Adapter<ManajemenLandAdapter.ViewHolder>() {
    var list: List<LandData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manajemen_lahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        Glide.with(holder.itemView.context)
            .load(data.urlGalleryLand?.first().toString())
            .placeholder(R.drawable.item_default)
            .error(R.drawable.item_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)

        holder.textViewJudul.text = data.nameLand.toString()
        holder.textViewHarga.text = Utils.changePrice(data.priceLand.toString()) + File.separator + "bulan"
        holder.textViewDeskripsi.text = "Luas ${data.lengthLand}x${data.widthLand}\nFasilitas: ${data.facilityLand.toString().replace(";",", ")}"
        holder.textViewRating.text = data.ratingLand.toString()
        holder.textViewLokasi.text = data.districtName.toString()

        holder.materialButtonSewaSekarang.setOnClickListener {
            listenerDetail.onClickItem(data)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView = ItemView.findViewById<View>(R.id.imageView) as ImageView
        val textViewJudul = ItemView.findViewById<View>(R.id.textViewJudul) as TextView
        val textViewHarga = ItemView.findViewById<View>(R.id.textViewHarga) as TextView
        val textViewDeskripsi = ItemView.findViewById<View>(R.id.textViewDeskripsi) as TextView
        val textViewRating = ItemView.findViewById<View>(R.id.textViewRating) as TextView
        val textViewLokasi = ItemView.findViewById<View>(R.id.textViewLokasi) as TextView
        val materialButtonSewaSekarang = ItemView.findViewById<View>(R.id.materialButtonSewaSekarang) as MaterialButton
    }
}