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
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.model.ProductData
import id.hikmah.stiki.tandur_1.v2.model.TutorialData
import id.hikmah.stiki.tandur_1.v2.model.TutorialDetailData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils

class ListVideoAdapter(private var listener: ItemClickListener<TutorialDetailData>): RecyclerView.Adapter<ListVideoAdapter.ViewHolder>() {
    var list: List<TutorialDetailData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        Glide.with(holder.itemView.context)
            .load(data.URLTHUMBNAIL_TD)
            .placeholder(R.drawable.item_default)
            .error(R.drawable.item_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)

        holder.textViewJudul.text = data.TITLE_TD.toString()
        holder.textViewNo.text = "${position+1}"

        holder.itemView.setOnClickListener {
            listener.onClickItem(data)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView = ItemView.findViewById<View>(R.id.imageView) as ImageView
        val textViewJudul = ItemView.findViewById<View>(R.id.textViewJudul) as TextView
        val textViewNo = ItemView.findViewById<View>(R.id.textViewNo) as TextView
    }
}