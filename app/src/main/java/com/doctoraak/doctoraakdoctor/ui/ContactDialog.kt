package com.doctoraak.doctoraakdoctor.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakdoctor.Adapter.ContactAdapter

import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.customView.DividerItemDecorationExceptLast
import com.doctoraak.doctoraakdoctor.databinding.ContactDialogBinding

class ContactDialog(context: Context, val data: List<String>?) : Dialog(context)
{
    companion object{
        @JvmStatic
        fun newInstance(context: Context, data: List<String>) =
            ContactDialog(context, data)
    }

    private lateinit var binding: ContactDialogBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context)
            , R.layout.contact_dialog, null, false)
        setContentView(binding.root)

        with(binding.rvContact)
        {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecorationExceptLast(context,
                    ResourcesCompat.getDrawable(resources, R.drawable.drawable_divider, null)!!, 12)
            )
            adapter = ContactAdapter(context, data)
        }

        binding.btnOk.setOnClickListener { dismiss() }
    }

    override fun onStart()
    {
        super.onStart()
        window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setGravity(Gravity.CENTER)
            it.setBackgroundDrawableResource(R.color.transparent)
            it.setWindowAnimations(R.style.dialogAnimation)
        }
    }

}
