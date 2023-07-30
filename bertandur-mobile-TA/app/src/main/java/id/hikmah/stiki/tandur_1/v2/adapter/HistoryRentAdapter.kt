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
import id.hikmah.stiki.tandur_1.v2.model.HistoryRentData
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils
import java.io.File

class HistoryRentAdapter(private var listener: ItemClickListener<HistoryRentData>): RecyclerView.Adapter<HistoryRentAdapter.ViewHolder>() {
    var listHistory: List<HistoryRentData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_rent, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataHistory = listHistory[position]

        Glide.with(holder.itemView.context)
            .load(dataHistory.URLGALLERY_LAND?.first().toString())
            .placeholder(R.drawable.item_default)
            .error(R.drawable.item_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)

        holder.textViewJudul.text = dataHistory.NAME_LAND.toString()
        holder.textViewHarga.text = Utils.changePrice(dataHistory.TOTPAYMENT_RENT.toString())
        holder.textViewDeskripsi.text = "Luas ${dataHistory.LENGTH_LAND}x${dataHistory.WIDTH_LAND}\nFasilitas: ${dataHistory.FACILITY_LAND.toString().replace(";",", ")}"
        holder.textViewRating.text = dataHistory.RATING_LAND.toString()
        holder.textViewLokasi.text = dataHistory.NAME_DISTRICT.toString()

        holder.itemView.setOnClickListener {
            listener.onClickItem(dataHistory)
        }
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView = ItemView.findViewById<View>(R.id.imageView) as ImageView
        val textViewJudul = ItemView.findViewById<View>(R.id.textViewJudul) as TextView
        val textViewHarga = ItemView.findViewById<View>(R.id.textViewHarga) as TextView
        val textViewDeskripsi = ItemView.findViewById<View>(R.id.textViewDeskripsi) as TextView
        val textViewRating = ItemView.findViewById<View>(R.id.textViewRating) as TextView
        val textViewLokasi = ItemView.findViewById<View>(R.id.textViewLokasi) as TextView
    }
}