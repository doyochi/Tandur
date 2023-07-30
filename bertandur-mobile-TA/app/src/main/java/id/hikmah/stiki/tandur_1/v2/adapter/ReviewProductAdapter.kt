package id.hikmah.stiki.tandur_1.v2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.v2.model.ReviewProductData
import id.hikmah.stiki.tandur_1.v2.util.Utils

class ReviewProductAdapter(): RecyclerView.Adapter<ReviewProductAdapter.ViewHolder>() {
    var listReview: List<ReviewProductData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataReview = listReview[position]

        Glide.with(holder.itemView.context)
            .load(dataReview.IMG_USER.toString())
            .placeholder(R.drawable.item_default)
            .error(R.drawable.item_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.shapeableImageViewUser)

        holder.textViewName.text = dataReview.NAME_USER.toString()
        holder.textViewTitle.text = dataReview.REVIEW_TITLE.toString()
        holder.textViewDate.text = dataReview.DATE_REVIEW.toString()
        holder.textViewReview.text = dataReview.REVIEW_CONTENT.toString()
        holder.ratingBar.rating = if (dataReview.RATING.isNullOrEmpty()) {
            0f
        } else {
            dataReview.RATING.toString().toFloat()
        }
    }

    override fun getItemCount(): Int {
        return listReview.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val shapeableImageViewUser = ItemView.findViewById<View>(R.id.shapeableImageViewUser) as ShapeableImageView
        val textViewName = ItemView.findViewById<View>(R.id.textViewName) as TextView
        val textViewTitle = ItemView.findViewById<View>(R.id.textViewTitle) as TextView
        val textViewDate = ItemView.findViewById<View>(R.id.textViewDate) as TextView
        val textViewReview = ItemView.findViewById<View>(R.id.textViewReview) as TextView
        val ratingBar = ItemView.findViewById<View>(R.id.ratingBar) as RatingBar
    }
}