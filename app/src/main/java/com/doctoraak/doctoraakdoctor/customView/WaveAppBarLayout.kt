package com.doctoraak.doctoraakdoctor.customView

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.utils.Utils

import com.google.android.material.appbar.AppBarLayout

class WaveAppBarLayout : AppBarLayout
{
    private lateinit var path_1: Path
    private lateinit var path_2: Path
    private lateinit var paint_path_1: Paint
    private lateinit var paint_path_2: Paint
    // Default Values:
    private val def_curve_height = Utils.convertDpToPixel(context, 8f)
    private val def_curve_width_percentage = 0.1f
    // Values of height for the Start and End points of the paths;
    private var start_height_path_1: Float = Utils.convertDpToPixel(context, def_curve_height)
    private var end_height_path_1: Float = Utils.convertDpToPixel(context, def_curve_height)
    // Values of height for all the curves of the 2 paths;
    private var start_height_path_2: Float = Utils.convertDpToPixel(context, def_curve_height)
    private var end_height_path_2: Float = Utils.convertDpToPixel(context, def_curve_height)
    // Values of height for all the curves of the 2 paths;
    private var curve_1_height_path_1: Float = Utils.convertDpToPixel(context, def_curve_height)
    private var curve_2_height_path_1: Float = Utils.convertDpToPixel(context, def_curve_height)
    private var curve_1_height_path_2: Float = Utils.convertDpToPixel(context, def_curve_height)
    private var curve_2_height_path_2: Float = Utils.convertDpToPixel(context, def_curve_height)
    // Values of width for curves of the 2 paths must be from 0:1 ( percentage);
    private var curve_1_width_path_1_percentage: Float = def_curve_width_percentage
    private var curve_2_width_path_1_percentage: Float = def_curve_width_percentage
    private var curve_1_width_path_2_percentage: Float = def_curve_width_percentage
    private var curve_2_width_path_2_percentage: Float = def_curve_width_percentage
    private var p = 0.5f //2.42f
    private var wave_elevation: Float = Utils.convertDpToPixel(context, 2f)
    private var background_wave_1 : Drawable? = null
    private var background_wave_2 : Drawable? = null
    private var show_first_curve :Boolean = true
    private var show_second_curve :Boolean = true
    private var auto_bottom_padding :Boolean = true

    constructor(context: Context) : super(context)
    {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        val attrArray = context.theme.obtainStyledAttributes(attrs, R.styleable.WaveAppBarLayout, 0, 0)

        try
        {
            wave_elevation = attrArray.getDimension(R.styleable.WaveAppBarLayout_wave_elevation
                , Utils.convertDpToPixel(context, 4f))

            val background_ref_1 = attrArray.getResourceId(R.styleable.WaveAppBarLayout_firstWave_background, 0)
            val background_ref_2 = attrArray.getResourceId(R.styleable.WaveAppBarLayout_secondWave_background, 0)
            if (background_ref_1 != 0)
                background_wave_1 = ResourcesCompat.getDrawable(resources, background_ref_1, null)
            if (background_ref_2 != 0)
                background_wave_2 = ResourcesCompat.getDrawable(resources, background_ref_2, null)

            curve_1_height_path_1 = attrArray.getDimension(R.styleable.WaveAppBarLayout_FirstCurveHeightOfFirstWave, def_curve_height)
            curve_2_height_path_1 = attrArray.getDimension(R.styleable.WaveAppBarLayout_SecondCurveHeightOfFirstWave, def_curve_height)
            curve_1_height_path_2 = attrArray.getDimension(R.styleable.WaveAppBarLayout_FirstCurveHeightOfSecondWave, def_curve_height)
            curve_2_height_path_2 = attrArray.getDimension(R.styleable.WaveAppBarLayout_SecondCurveHeightOfSecondWave, def_curve_height)

            // Width Percentage of Curves;
            curve_1_width_path_1_percentage = attrArray.getFloat(R.styleable.WaveAppBarLayout_FirstCurvePercentageWidthOfFirstWave
                    , def_curve_width_percentage)
            curve_2_width_path_1_percentage = attrArray.getFloat(R.styleable.WaveAppBarLayout_SecondCurvePercentageWidthOfFirstWave
                , def_curve_width_percentage)
            curve_1_width_path_2_percentage = attrArray.getFloat(R.styleable.WaveAppBarLayout_FirstCurvePercentageWidthOfSecondWave
                , def_curve_width_percentage)
            curve_2_width_path_2_percentage = attrArray.getFloat(R.styleable.WaveAppBarLayout_SecondCurvePercentageWidthOfSecondWave
                , def_curve_width_percentage)

            show_first_curve = attrArray.getBoolean(R.styleable.WaveAppBarLayout_showFirstWave, true)
            show_second_curve = attrArray.getBoolean(R.styleable.WaveAppBarLayout_showSecondWave, true)
            auto_bottom_padding = attrArray.getBoolean(R.styleable.WaveAppBarLayout_autoBottomPadding, true)

            start_height_path_1 = attrArray.getDimension(R.styleable.WaveAppBarLayout_StartHeightOfFirstWave
                , def_curve_height)
            end_height_path_1 = attrArray.getDimension(R.styleable.WaveAppBarLayout_EndHeightOfFirstWave
                , def_curve_height)
            start_height_path_2 = attrArray.getDimension(R.styleable.WaveAppBarLayout_StartHeightOfSecondWave
                , def_curve_height)
            end_height_path_2 = attrArray.getDimension(R.styleable.WaveAppBarLayout_EndHeightOfSecondWave
                , def_curve_height)
        }
        finally {
            attrArray.recycle()
        }

        init()
    }

    private fun init()
    {
        p = if (!auto_bottom_padding) 0.5f else 2.42f

        paint_path_1 = Paint()
            .apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                setShadowLayer(wave_elevation, 0f, 0f, Color.GRAY)
            }
        paint_path_2 = Paint(paint_path_1)
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint_path_1)
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint_path_2)

        path_1 = Path()
        path_2 = Path()

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        setBackgroundColor(Color.TRANSPARENT)
        // TODO but elevation = 0 in code not in xml.
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int)
    {
        if (auto_bottom_padding)
        {
            val max_height_curve = max(curve_1_height_path_1, curve_2_height_path_1, curve_1_height_path_2
                , curve_2_height_path_2, start_height_path_1, end_height_path_1, start_height_path_2, end_height_path_2)

            super.setPadding(left, top, right
                , bottom + ((max_height_curve*2)+wave_elevation +(max_height_curve*0.22)).toInt() )
        }
        else
            super.setPadding(left, top, right, bottom )

    }

    private fun setBackground(paint: Paint, background: Drawable)
    {
        if (background is ColorDrawable) {
            paint.setColor(background.color)
        }
        else if (background is GradientDrawable)
        {
            var shader: Shader? = null
            if (Build.VERSION.SDK_INT >= 24)
            {
                if (background.colors != null)
                    when (background.gradientType)
                    {
                        GradientDrawable.LINEAR_GRADIENT ->
                            shader = LinearGradient(
                            0f, 0f, 0f,
                            height.toFloat(), background.colors!!, null, Shader.TileMode.MIRROR
                        )
                        GradientDrawable.RADIAL_GRADIENT -> shader = RadialGradient(
                            width / 2f,
                            height / 2f,
                            background.gradientRadius,
                            background.colors!!,
                            null,
                            Shader.TileMode.MIRROR
                        )
                        GradientDrawable.SWEEP_GRADIENT -> shader =
                            SweepGradient(width / 2f, height / 2f, background.colors!!, null)
                    }
            }
            else
            {
                // todo handle this probably make dynamics;
                val startColor_def = Color.parseColor("#9B61FC")
                val endColor_def = Color.parseColor("#6A65BEFC")

                shader = LinearGradient(0f, 0f, 0f,
                    height.toFloat(), intArrayOf(startColor_def, endColor_def), null, Shader.TileMode.MIRROR)
            }
            paint.setShader(shader)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    {
        super.onSizeChanged(w, h, oldw, oldh)

        background_wave_1?.let { setBackground(paint_path_1, it) }
        background_wave_2?.let { setBackground(paint_path_2, it) }

        if (show_first_curve)
            makeFirstPath()
        if (show_second_curve)
            makeSecondPath()
    }

    private fun initPaintsGradient()
    {
        // Paint:
        paint_path_1.shader = LinearGradient(0f, 0f, 0f, height.toFloat()
            , intArrayOf(Color.BLUE, Color.GREEN), null, Shader.TileMode.MIRROR)
        paint_path_2.shader = LinearGradient(0f, 0f, 0f, height.toFloat()
            , intArrayOf(Color.BLUE, Color.GREEN), null, Shader.TileMode.MIRROR)
    }

    private fun makeFirstPath()
    {
        // Path 1:
        path_1.moveTo(0f, 0f)
        path_1.lineTo(0f, height - start_height_path_1 - wave_elevation)

        path_1.cubicTo(
            width * curve_1_width_path_1_percentage
            , (height - start_height_path_1 - curve_1_height_path_1 - wave_elevation) - (p * curve_1_height_path_1)
            , width - (width * curve_2_width_path_1_percentage)
            , (height - wave_elevation) + (p * curve_2_height_path_1)
            , width.toFloat()
            , height - end_height_path_1 - wave_elevation
        )

        path_1.lineTo(width.toFloat(), 0f)
        path_1.close()
    }

    private fun makeSecondPath()
    {
        // Path 2:
        path_2.moveTo(0f, 0f)
        path_2.lineTo(0f, height - start_height_path_2 - wave_elevation)

        path_2.cubicTo(
            width * curve_1_width_path_2_percentage
            , (height - wave_elevation) + (p * curve_1_height_path_2)
            , width - (width * curve_2_width_path_2_percentage)
            , (height - end_height_path_2 - curve_2_height_path_2 - wave_elevation) - (p * curve_2_height_path_2)
            , width.toFloat()
            , height - end_height_path_2 - wave_elevation)

        path_2.lineTo(width.toFloat(), 0f)
        path_2.close()
    }


    override fun onDraw(canvas: Canvas)
    {
        if (show_first_curve)
            canvas.drawPath(path_1, paint_path_1)
        if (show_second_curve)
            canvas.drawPath(path_2, paint_path_2)
    }

    fun max(vararg nums: Float) :Float
    {
        var max = 0f
        nums.forEach { if (it > max) max = it }
        return max
    }
}
