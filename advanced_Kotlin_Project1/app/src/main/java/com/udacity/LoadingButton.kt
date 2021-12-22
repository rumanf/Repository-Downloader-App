package com.udacity

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.solver.widgets.Rectangle
import androidx.core.content.ContextCompat.getColor
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var currentPercentage=0
    private val darkColor = context.resources.getColor(R.color.colorPrimaryDark, null)

    private val darkColorPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = darkColor
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
        strokeWidth=70f
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }
    private val valueAnimator = ValueAnimator()

     var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
    }


    init {
isClickable=true
        buttonState=ButtonState.Clicked
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

       // fanSpeed = fanSpeed.next()
        //contentDescription = resources.getString(fanSpeed.label)

        invalidate()
        return true
    }
    private val textdownload="Download"
    private val textdownloading="Downloading"
    private val textcomplete="Complete"

    override fun onDraw(canvas: Canvas?) {
       // super.onDraw(canvas)
        canvas?.let {
        paint.color = Color.GREEN
        canvas?.drawRect(0.0f,0.0f,widthSize.toFloat(),heightSize.toFloat(), paint)
        //paint.color = Color.WHITE
        //  canvas?.drawText(textdownload, (widthSize/2).toFloat(), (heightSize/2).toFloat(), paint)
        progressbar(it)
            displaytext(it)
            progresscircle(it)
    }}
    fun displaytext(canvas:Canvas?) {
        if (buttonState == ButtonState.Completed) {
            paint.color = Color.WHITE
            canvas?.drawText(
                textcomplete,
                (widthSize / 2).toFloat(),
                (heightSize / 2).toFloat(),
                paint
            )
        }
        if (buttonState == ButtonState.Loading) {
            paint.color = Color.WHITE
            canvas?.drawText(
                textdownloading,
                (widthSize / 2).toFloat(),
                (heightSize / 2).toFloat(),
                paint
            )
        }

        if (buttonState == ButtonState.Clicked) {
            paint.color = Color.WHITE
            canvas?.drawText(
                textdownload,
                (widthSize / 2).toFloat(),
                (heightSize / 2).toFloat(),
                paint
            )
        }
    }
    fun progressbar (canvas:Canvas?){
        val percentageFill=currentPercentage.toFloat()
        if (buttonState == ButtonState.Clicked) {
        }
        else {
        canvas?.drawRect(0f, 0f, percentageFill,heightSize.toFloat(), darkColorPaint)
    }}
    fun progresscircle (canvas:Canvas?) {
        val circlepercentageFill = (360 * currentPercentage / widthSize).toFloat()
        if (buttonState == ButtonState.Clicked) {
        }
        else {
            canvas?.drawArc(
                ((widthSize * 0.8).toFloat()) - 50f,
                ((heightSize / 2).toFloat()) - 50f,
                ((widthSize * 0.8).toFloat()) + 50f,
                ((heightSize / 2).toFloat()) + 50f,
                0f,
                circlepercentageFill,
                true,
                circlePaint
            )
            Log.i("circle angle", "$circlepercentageFill")
        }
    }





    fun animateProgress() {

        val valueHolder = PropertyValuesHolder.ofFloat(
            "Percentage",
            0f,
            widthSize.toFloat()
        )
        buttonState = ButtonState.Clicked

         valueAnimator.apply {
            setValues(valueHolder)
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()


            addUpdateListener {
                val percentage = it.getAnimatedValue("Percentage") as Float
                currentPercentage = percentage.toInt()
                buttonState = ButtonState.Loading
                invalidate()
                Log.i("this", "$currentPercentage")
                if (currentPercentage==widthSize){
                    buttonState = ButtonState.Completed
                    buttonState = ButtonState.Clicked
                }
            }


        }
        // 7
        valueAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        Log.i("width", "$widthSize")
        heightSize = h
        setMeasuredDimension(w, h)
    }




}