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
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils
import java.io.File

class FacilityGridAdapter: RecyclerView.Adapter<FacilityGridAdapter.ViewHolder>() {
    var list: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_facility, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        if (!data.isNullOrEmpty()) {
            holder.imageView.visibility = View.VISIBLE
            holder.textView.visibility = View.VISIBLE
            when (data) {
                "Irigasi" -> {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.ic_irigasi)
                        .into(holder.imageView)
                }
                "Listrik" -> {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.ic_listrik)
                        .into(holder.imageView)
                }
                "Peralatan" -> {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.ic_peralatan)
                        .into(holder.imageView)
                }
                "Kanopi" -> {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.ic_kanopi)
                        .into(holder.imageView)
                }
            }

            holder.textView.text = data
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView = ItemView.findViewById<View>(R.id.imageView) as ImageView
        val textView = ItemView.findViewById<View>(R.id.textView) as TextView
    }
}