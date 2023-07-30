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
import com.denzcoskun.imageslider.models.SlideModel
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils
import java.io.File

class CustomDotIndicatorAdapter(): RecyclerView.Adapter<CustomDotIndicatorAdapter.ViewHolder>() {
    var list: List<SlideModel> = emptyList()
    var activePosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dots, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == activePosition) {
            holder.imageViewDots.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_dots_selected))
        } else {
            holder.imageViewDots.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_dots_unselected))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageViewDots = ItemView.findViewById<View>(R.id.imageViewDots) as ImageView
    }
}