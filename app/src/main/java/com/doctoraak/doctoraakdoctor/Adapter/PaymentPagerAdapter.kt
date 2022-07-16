package com.doctoraak.doctoraakdoctor.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.PaymentItemBinding

class PaymentPagerAdapter : PagerAdapter()
{
    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        val binding = DataBindingUtil.inflate<PaymentItemBinding>(LayoutInflater.from(container.context)
            , R.layout.payment_item, null, false)

        binding.ivImage.setImageResource(when(position)
        {
            0 -> R.drawable.ic_vodafone
            1 -> R.drawable.ic_fawry
            else -> R.drawable.ic_visa
        })

        container.addView(binding.root)

        return binding.root
    }

    override fun getPageWidth(position: Int): Float {
        return 0.8f
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == (`object` as View)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount() = 3

}