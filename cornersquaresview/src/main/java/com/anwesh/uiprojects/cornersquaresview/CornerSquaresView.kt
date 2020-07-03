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
