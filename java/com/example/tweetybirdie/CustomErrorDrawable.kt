//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT


package com.example.tweetybirdie

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat


class CustomErrorDrawable(context: Context) : Drawable() {
    private val drawable: Drawable

    init {

        drawable = ContextCompat.getDrawable(context, R.drawable.custom_error)!!
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

        val desiredWidth = 65
        val desiredHeight = 65
        val scaleFactor = (desiredWidth.toFloat() / drawable.intrinsicWidth).coerceAtMost(desiredHeight.toFloat() / drawable.intrinsicHeight)
        val scaledWidth = (drawable.intrinsicWidth * scaleFactor).toInt()
        val scaledHeight = (drawable.intrinsicHeight * scaleFactor).toInt()

        drawable.setBounds(-130, -50, -130 + scaledWidth, scaledHeight - 50)
    }

    override fun draw(canvas: Canvas) {
        drawable.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        drawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        drawable.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}


