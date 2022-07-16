package com.doctoraak.doctoraakdoctor.customView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.bold
import com.doctoraak.doctoraakdoctor.R

class ITCardView : CardView
{
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    {
        init()
        val attrArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ITCardView, 0, 0)

        try
        {
            val card_background_id = attrArray.getResourceId(R.styleable.ITCardView_card_background, 0)
            // ImageView Attrs:
            val image_width = attrArray.getDimension(R.styleable.ITCardView_image_width
                , ViewGroup.LayoutParams.WRAP_CONTENT.toFloat())
            val image_height = attrArray.getDimension(R.styleable.ITCardView_image_height
                , ViewGroup.LayoutParams.WRAP_CONTENT.toFloat())
            val image_background_id = attrArray.getResourceId(R.styleable.ITCardView_image_background
                , 0)
            val image_scaleType_int = attrArray.getInt(R.styleable.ITCardView_image_scaleType
                , ImageView.ScaleType.CENTER_INSIDE.ordinal)
            var image_scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE
            when (image_scaleType_int)
            {
                ImageView.ScaleType.CENTER_INSIDE.ordinal -> image_scaleType = ImageView.ScaleType.CENTER_INSIDE
                ImageView.ScaleType.CENTER_CROP.ordinal -> image_scaleType = ImageView.ScaleType.CENTER_CROP
                ImageView.ScaleType.CENTER.ordinal -> image_scaleType = ImageView.ScaleType.CENTER
                ImageView.ScaleType.FIT_CENTER.ordinal -> image_scaleType = ImageView.ScaleType.FIT_CENTER
                ImageView.ScaleType.FIT_XY.ordinal -> image_scaleType = ImageView.ScaleType.FIT_XY
                ImageView.ScaleType.FIT_START.ordinal -> image_scaleType = ImageView.ScaleType.FIT_START
                ImageView.ScaleType.FIT_END.ordinal -> image_scaleType = ImageView.ScaleType.FIT_END
                ImageView.ScaleType.MATRIX.ordinal -> image_scaleType = ImageView.ScaleType.MATRIX
            }

            // TextView Attrs:
            val text_size = attrArray.getDimension(R.styleable.ITCardView_text_size
                , -1f)
            val text_color = attrArray.getColor(R.styleable.ITCardView_text_color
                , Color.BLACK)
            val text_text_normal = attrArray.getString(R.styleable.ITCardView_text_text_normal)
            val text_text_bold = attrArray.getString(R.styleable.ITCardView_text_text_bold)
            val show_divider = attrArray.getBoolean(R.styleable.ITCardView_show_divider, true)

            initValues(card_background_id = card_background_id, image_width = image_width, image_height = image_height
                , image_scaleType = image_scaleType, image_background_id = image_background_id
                , text_color = text_color, text_size = text_size
                , text_text_normal = text_text_normal, text_text_bold = text_text_bold, show_divider = show_divider)
        }
        finally {
            attrArray.recycle()
        }
    }

    private fun initValues(card_background_id: Int, image_width: Float, image_height: Float
                           , image_background_id: Int, image_scaleType: ImageView.ScaleType, text_size: Float
                           , text_color: Int, text_text_normal: String?, text_text_bold: String?, show_divider: Boolean)
    {
        if (card_background_id != 0)
            setBackgroundResource(card_background_id)

        // imageView:
        imageView.apply {
            scaleType = image_scaleType
            if (image_background_id != 0)
                setImageResource(image_background_id) // todo not support Vector cause of this.
            //background = ResourcesCompat.getDrawable(resources, image_background_id, null)
        }
            .layoutParams.let {
                it.width = image_width.toInt()
                it.height = image_height.toInt()
        }

        // textView:
        textView.let {
            if (text_size != -1f)
                it.textSize = text_size
            it.setTextColor(text_color)
            val span = SpannableStringBuilder()
                .append(text_text_normal?:"")
                .bold { append(text_text_bold?:"") }
            it.text = span
        }
    }

    private fun init()
    {
        LayoutInflater.from(context).inflate(R.layout.cardview_image_text, this, true)
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
    }


    fun setNormalText(text: String)
    {
        textView.text = text
    }

    fun setBoldText(text: String)
    {
        textView.text = text
    }

    fun setTextSize(text_size: Float)
    {
        textView.textSize = text_size
    }

    fun setTextColor(text_color: Int)
    {
        textView.setTextColor(text_color)
    }

    fun setText(normal_text: String, bold_text: String)
    {
        val span = SpannableStringBuilder()
            .append(normal_text)
            .bold { append(bold_text) }
        textView.text = span
    }

    fun setImageBackground(drawable: Drawable)
    {
        imageView.setImageDrawable(drawable)
    }

    fun setImageBackgroundResource(resId: Int)
    {
        imageView.background = ResourcesCompat.getDrawable(resources, resId, null)
    }

    fun setImageBackgroundBitmap(bitmap: Bitmap)
    {
        imageView.setImageBitmap(bitmap)
    }

    fun setImageBackgroundColor(color: Int)
    {
        imageView.setBackgroundColor(color)
    }



}
