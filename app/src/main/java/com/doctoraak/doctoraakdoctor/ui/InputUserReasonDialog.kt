package com.doctoraak.doctoraakdoctor.ui


import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.DialogInputUserReasonBinding
import com.doctoraak.doctoraakdoctor.databinding.DialogPreviewImageBinding

class InputUserReasonDialog(private val onDialogOkClick: (String)->Unit) : DialogFragment()
{
    private lateinit var binding: DialogInputUserReasonBinding


    companion object {
        internal fun newInstance(onDialogOkClick: (String)->Unit): InputUserReasonDialog {
            return InputUserReasonDialog(onDialogOkClick)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context)
            , R.layout.dialog_input_user_reason, null, false)

        val dialog = AlertDialog.Builder(context!!, 0)
            .setTitle(R.string.refuse_order)
            .setView(binding.root)
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.setOnShowListener {
            with(getDialog() as AlertDialog)
            {
                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { onPositiveClick() }
                getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                    getDialog()?.dismiss()
                }
            }
        }

        return dialog
    }

    private fun onPositiveClick()
    {
        val reason = binding.etReason.text.toString()

        if (reason.isBlank())
            binding.etlReason.error = getString(R.string.empty_field)
        else
        {
            onDialogOkClick(reason)
            getDialog()?.dismiss()
        }
    }


}
