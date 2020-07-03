package com.anwesh.uiprojects.cornersquaresview

/**
 * Created by anweshmishra on 03/07/20.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.app.Activity
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Color

val colors : Array<String> = arrayOf("#3F51B5", "#F44336", "#4CAF50", "#009688", "#FFEB3B")
val parts : Int = 4
val scGap : Float = 0.02f / parts
val delay : Long = 20
val sizeFactor : Float = 4.3f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawSquare(i : Int, scale : Float, w : Float, h : Float, paint : Paint) {
    val sf : Float = scale.sinify()
    val sfi : Float = sf.divideScale(i, parts)
    val size : Float = Math.min(w, h) / sizeFactor
    val xi : Float = 1f - 2 * (i % 2)
    val yi : Float = 1f - 2 * (i / 2)
    save()
    scale(xi, yi)
    translate(-w / 2, - h / 2)
    drawRect(RectF(0f, 0f, size * sfi, size * sfi), paint)
    restore()
}

fun Canvas.drawCornerSquare(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = Color.parseColor(colors[i])
    save()
    translate(w / 2, h / 2)
    for (j in 0..(parts - 1)) {
        drawSquare(j, scale, w, h, paint)
    }
    restore()
}

class CornerSquaresView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}
