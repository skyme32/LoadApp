package com.udacity.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.udacity.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var textSize = 0F
    private var textColor = 0
    private var circleColor = 0
    private var fanFirstColor = 0
    private var fanLoadingColor = 0
    private var buttonColor = 0
    private var widthSize = 0
    private var heightSize = 0
    private var width = 0F
    private var withAngle = 0F
    private var txtButtonLabel = ""

    private var valueAnimator = ValueAnimator()
    private val paint = Paint()
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create( Typeface.DEFAULT, Typeface.BOLD)
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                txtButtonLabel = context.getString(R.string.button_loading)
                valueAnimator = ValueAnimator.ofFloat(0F, measuredWidth.toFloat()).apply {
                    duration = 1600
                    addUpdateListener { animation ->
                        width = animation.animatedValue as Float
                        withAngle = animation.animatedValue as Float
                        buttonColor = fanLoadingColor
                        invalidate()
                    }
                    start()
                }
            }
            ButtonState.Clicked -> {
                txtButtonLabel = context.getString(R.string.button_name)
            }
            ButtonState.Completed -> {
                txtButtonLabel = context.getString(R.string.button_name)
                valueAnimator.removeAllListeners()
                valueAnimator.end()
                invalidate()
                buttonColor = fanFirstColor
                withAngle = 0f
            }
        }

    }


    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            fanFirstColor = getColor(R.styleable.LoadingButton_fanColor1, 0)
            fanLoadingColor = getColor(R.styleable.LoadingButton_fanColor2, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
            textSize = getFloat(R.styleable.LoadingButton_textSize, 0F)
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //DRAW RECTANGLE
        canvas?.drawColor(fanFirstColor)
        paint.color = buttonColor
        canvas?.drawRect(
                0f,
                0f,
                width,
                measuredHeight.toFloat(),
                paint
        )

        //DRAW TEXT
        paintText.color = textColor
        paintText.textSize = textSize
        val textHeight: Float = paint.descent() - paintText.ascent()
        val textOffset: Float = textHeight / 2 - paintText.descent()
        canvas?.drawText(
            txtButtonLabel,
            (widthSize / 2).toFloat(),
            heightSize.toFloat() / 2 + textOffset,
            paintText
        )

        //DRAW CIRCLE
        paint.color = circleColor
        canvas?.drawArc(
            740f,
            30f,
            850f,
            130f,
            10f,
            withAngle,
            true,
            paint)


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
        heightSize = h
        setMeasuredDimension(w, h)
    }


    override fun performClick(): Boolean {
        if (super.performClick()) return true

        invalidate()
        return true
    }


    fun setBtnState(state: ButtonState) {
        buttonState = state
    }
}

