package com.doctoraak.doctoraakdoctor.Adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.NotificationItemBinding
import com.doctoraak.doctoraakdoctor.model.NotificationInfo
import com.doctoraak.doctoraakdoctor.utils.Utils


class NotificationsAdapter(var notifications: ArrayList<NotificationInfo>?, val context: Context
                           , var itemClick: (Int)->Unit) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>()
{


    fun setData(data : ArrayList<NotificationInfo>)
    {
        notifications = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = NotificationViewHolder(DataBindingUtil.inflate(LayoutInflater
        .from(parent.context), R.layout.notification_item, parent, false))


    override fun getItemCount(): Int = notifications?.size ?: 0


    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int)
    {
        val notification = notifications!![position]

        holder.binding.tvTitle.text = Utils.getTextForAppLanguage(notification.title_en , notification.title_ar , notification.title_en)
        holder.binding.tvBody.text = Utils.getTextForAppLanguage(notification.message_en , notification.message_ar , notification.message_en)

        if (!notification.icon.isNullOrBlank())
            Glide.with(context).load(notification.icon).addListener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean
                {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_logo_icon)
                    return true
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?
                                             , isFirstResource: Boolean): Boolean
                {
                    holder.binding.ivImage.setImageDrawable(resource)
                    return true
                }
            })
        else
            holder.binding.ivImage.setImageResource(R.drawable.ic_logo_icon)
    }

    fun removeItem(position: Int): Int
    {
        val id = notifications!![position].id
        notifications?.removeAt(position)
        notifyItemRemoved(position)

        return id
    }

    inner class NotificationViewHolder(val binding: NotificationItemBinding) 
        : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.root.setOnClickListener { itemClick(adapterPosition) }
        }
    }
}