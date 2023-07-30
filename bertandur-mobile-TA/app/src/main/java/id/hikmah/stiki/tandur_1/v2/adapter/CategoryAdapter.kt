package id.hikmah.stiki.tandur_1.v2.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.v2.model.CategoryData
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.PixelHelper
import id.hikmah.stiki.tandur_1.v2.util.Utils
import java.io.File

class CategoryAdapter(private var listener: ItemClickListener<CategoryData>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var list = mutableListOf<CategoryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.textViewKategori.text = data.namePcat

        holder.itemView.setOnClickListener {
            listener.onClickItem(data)
        }

        (holder.itemView.layoutParams as ViewGroup.MarginLayoutParams).apply {
            if (position != list.lastIndex) {
                rightMargin = PixelHelper.convertDpToPx(8, holder.itemView.context.resources)
            }
        }
//        Glide.with(holder.itemView.context)
//            .load(list[position])
//            .placeholder(R.drawable.item_default)
//            .error(R.drawable.item_default)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView = ItemView.findViewById<View>(R.id.imageView) as ImageView
        val textViewKategori = ItemView.findViewById<View>(R.id.textViewKategori) as TextView
    }
}