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

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class CSNode(var i : Int, val state : State = State()) {

        private var next : CSNode? = null
        private var prev : CSNode? = null

        init {

        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = CSNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawCornerSquare(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : CSNode {
            var curr : CSNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            return this
        }
    }

    data class CornerSquare(var i : Int, val state : State = State()) {

        private var curr : CSNode = CSNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : CornerSquaresView) {

        private val animator : Animator = Animator(view)
        private val cs : CornerSquare = CornerSquare(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            cs.draw(canvas, paint)
            animator.animate {
                cs.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            cs.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : CornerSquaresView {
            val view : CornerSquaresView = CornerSquaresView(activity)
            activity.setContentView(view)
            return view
        }
    }
}
