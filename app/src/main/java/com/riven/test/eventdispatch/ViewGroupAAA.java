package com.riven.test.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 默认的ViewGroup不执行onDraw方法，因为viewgroup是个透明的，有个dirtyOpaque判断
 * 添加背景或setWillNotDraw(false);可以触发viewGroup的onDraw方法
 */
public class ViewGroupAAA extends RelativeLayout {
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        Log.e("touch_test", "===== ViewGroupAAA.dispatchTouchEvent ====");

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e("touch_test", "===== ViewGroupAAA.onInterceptTouchEvent ====");

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("touch_test", "===== ViewGroupAAA.onTouchEvent ====");
        return super.onTouchEvent(event);
    }

    public ViewGroupAAA(Context context) {
        super(context);
    }

    public ViewGroupAAA(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupAAA(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewGroupAAA(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}

