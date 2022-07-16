package com.doctoraak.doctoraakdoctor.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide

import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.DialogPreviewImageBinding

class PreviewImageDialog : DialogFragment()
{

    companion object {
        private val ARG_IMAGE = "ARG_IMAGE"
        private val ARG_IMAGE_DEFAULT = "ARG_IMAGE_DEFAULT"

        internal fun newInstance(image: String, defImage: Int = R.drawable.ic_image_default): PreviewImageDialog
        {
            val fragment = PreviewImageDialog()
            fragment.setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
            val args = Bundle()
            args.putString(ARG_IMAGE, image)
            args.putInt(ARG_IMAGE_DEFAULT, defImage)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val binding: DialogPreviewImageBinding = DataBindingUtil.inflate(inflater
            , R.layout.dialog_preview_image, container, false)

        Glide.with(this).load(arguments?.getString(ARG_IMAGE, ""))
            .error(arguments?.getInt(ARG_IMAGE_DEFAULT, 0)!!)
            .placeholder(R.drawable.ic_image_default)
            .into(binding.ivImage);

        binding.ivClose.setOnClickListener { dismiss() }

        return binding.root
    }


}
