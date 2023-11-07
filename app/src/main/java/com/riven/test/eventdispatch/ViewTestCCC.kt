package com.riven.test.eventdispatch

import android.view.MotionEvent
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.MeasureSpec

/**
 * View并不支持多指处理
 * ViewGroup默认支持（TouchTarget）
 * View不能嵌套View
 */
class ViewTestCCC : View {
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.e("touch_test", "===== ViewTestCCC.dispatchTouchEvent ====")
        return super.dispatchTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        //如果这里设置了onClickListener事件 会返回true 表示消费事件了 所以递归不到父View的onTouchEvent
        Log.e("touch_test", "===== ViewTestCCC.onTouchEvent ====")
        return super.onTouchEvent(event)
        //        return false; //表示不处理 会从C的向上传递值B->A-Activity的onTouchEvent
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    /**
     * 这int两个字表示宽高和各自方向的测量模式合成的值
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     * UNSPECIFIED	00	默认值，父控件没有给子view任何限制，子View可以设置为任意大小。
     * EXACTLY	01	表示父控件已经确切的指定了子View的大小。
     * AT_MOST	10	表示子View具体大小没有尺寸限制，但是存在上限，上限一般为父View大小。
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //如果改变了View宽高，不要调用这行代码。需要调用setMeasuredDimension(widthsize,heightsize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthsize = MeasureSpec.getSize(widthMeasureSpec) //取出宽度的确切数值
        val widthmode = MeasureSpec.getMode(widthMeasureSpec) //取出宽度的测量模式
        val heightsize = MeasureSpec.getSize(heightMeasureSpec) //取出高度的确切数值
        val heightmode = MeasureSpec.getMode(heightMeasureSpec) //取出高度的测量模式
    }

    /**
     * 已经有onMeasured方法测试大小了，为啥还需要这个方法呢
     * 因为view的大小不仅由自身控制，还受父控件影响
     * @param w 最终大小
     * @param h 最终大小
     * @param oldw
     * @param oldh
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     *
     * @param changed
     * @param left 左侧距离父View left距离
     * @param top  顶部距离父View top的距离
     * @param right 右侧距离父View左侧的距离
     * @param bottom 底部距离父View顶部的距离
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        post {
//
//        }
    }
}