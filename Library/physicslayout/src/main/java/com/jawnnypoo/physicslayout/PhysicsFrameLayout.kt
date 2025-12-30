package com.jawnnypoo.physicslayout

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.OverScroller
import kotlin.jvm.internal.Intrinsics

/**
 * Typical FrameLayout with some physics added on. Call [physics] to get the
 * physics component.
 */
@Suppress("unused")
open class PhysicsFrameLayout : FrameLayout {

    lateinit var physics: Physics
    private var e: OverScroller? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    @TargetApi(21)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)
        physics = Physics(this, attrs)
        this.e = OverScroller(context)
    }

    private fun d(i: Int, i2: Int) {
        val scrollX = scrollX
        val scrollY = scrollY
        val i3 = i - scrollX
        val i4 = i2 - scrollY
        this.e?.startScroll(scrollX, scrollY, i3, i4)
        postInvalidateOnAnimation()
    }

    fun c() {
        scrollTo(width / 2, height / 2)
    }

    override fun computeScroll() {
        val overScroller = this.e ?: return
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.currX, overScroller.currY)
            postInvalidateOnAnimation()
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    fun e() {
        d(width / 2, height / 2)
    }

    fun f() {
        d(width / 2, 0)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        physics.onSizeChanged(w, h)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        physics.onLayout(changed)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        physics.onDraw(canvas)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return physics.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return physics.onTouchEvent(event)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    class LayoutParams(c: Context, attrs: AttributeSet?) : FrameLayout.LayoutParams(c, attrs),
        PhysicsLayoutParams {
        override var config: PhysicsConfig = PhysicsLayoutParamsProcessor.process(c, attrs)
    }
}
