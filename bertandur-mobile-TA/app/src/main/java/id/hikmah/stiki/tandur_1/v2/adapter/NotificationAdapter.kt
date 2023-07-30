package id.hikmah.stiki.tandur_1.v2.adapter

import android.util.Log
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
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.Utils

class NotificationAdapter(private val listener: ItemClickListener<NotificationData>): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    var list  = mutableListOf<NotificationData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        var time = "-"
        if (!data.created_at.isNullOrEmpty()) {
            val dbTime = data.created_at.split(" ")[1].split(":")
            time = "${dbTime[0]}:${dbTime[1]}"
        }
        holder.apply {
            textViewMessage.text = data.BODY_MESSAGE.toString()
            textViewTime.text = time

            itemView.setOnClickListener {
                listener.onClickItem(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun deleteByIdLog(idLog: Int) {
        list.remove(list.find { it.ID_LOG == idLog })
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewTime = ItemView.findViewById<View>(R.id.textViewTime) as TextView
        val textViewMessage = ItemView.findViewById<View>(R.id.textViewMessage) as TextView
        val cardViewNotification = ItemView.findViewById<View>(R.id.cardViewNotification) as CardView
    }
}