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

val colors : Array<String> = arrayOf("#3F51B5", "#F44336", "#4CAF50", "", "")
val parts : Int = 4
val scGap : Float = 0.02f / parts
val delay : Long = 20
val sizeFactor : Float = 4.3f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()
