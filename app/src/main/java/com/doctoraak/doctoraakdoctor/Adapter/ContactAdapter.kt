package com.doctoraak.doctoraakdoctor.Adapter

import android.app.TimePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.ContactItemBinding
import com.doctoraak.doctoraakdoctor.utils.Utils
import com.doctoraak.doctoraakdoctor.utils.convertToHours12
import java.util.*
import java.util.regex.Pattern

class ContactAdapter(val context: Context, val data: List<String>?)
    : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater
                    .from(context), R.layout.contact_item, parent, false
            )
        )

    override fun getItemCount(): Int  = data?.size?:0

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int)
    {
        data!![position].trim().let {
            holder.binding.tvData.text = context.getString(R.string.rect_bullet)+" "+it

            holder.binding.tvData.setOnClickListener {_->

                if (Patterns.WEB_URL.matcher(it).matches())
                {
                    val url = if (!it.startsWith("http://") && !it.startsWith("https://"))
                        Uri.parse("http://"+it) else Uri.parse(it)

                    context.startActivity(Intent.createChooser(Intent(Intent.ACTION_VIEW, url)
                        , context.getString(R.string.select)))
                }
                else if (Patterns.PHONE.matcher(it).matches())
                {
                    context.startActivity(Intent.createChooser(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))
                        , context.getString(R.string.select)))
                }
            }

            holder.binding.ivCopy.setOnClickListener {_->
                val clipboardManager =  context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(ClipData.newPlainText("Source Text", it))
                Toast.makeText(context, context.getString(R.string.text_copied), Toast.LENGTH_SHORT).show()
            }
        }

    }


    class ContactViewHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root)
}