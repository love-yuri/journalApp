package com.yuri.journal.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.yuri.journal.R
import com.yuri.journal.utils.MessageUtils.createToast


class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
    private val outColor: Int
    private val innerColor: Int
    private val paint = Paint().apply {
        context.obtainStyledAttributes(attrs, R.styleable.CircleView).apply {
            outColor = getColor(R.styleable.CircleView_outColor, Color.TRANSPARENT)
            innerColor = getColor(R.styleable.CircleView_innerColor, Color.TRANSPARENT)
            recycle()
        }
        strokeWidth = 5f
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val redis = width / 2f
        canvas.drawCircle(redis, redis, redis, paint.apply {
            color = innerColor
        })
        canvas.drawCircle(redis, redis, redis - 10, paint.apply {
            color = outColor
        })
        canvas.drawLine(redis, redis * 2, redis, height.toFloat(), paint.apply {
            color = Color.GRAY
        })
    }
}