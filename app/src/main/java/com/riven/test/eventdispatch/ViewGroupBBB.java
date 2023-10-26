package com.riven.test.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class ViewGroupBBB extends RelativeLayout {
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("touch_test", "===== ViewGroupBBB.dispatchTouchEvent ====");

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e("touch_test", "===== ViewGroupBBB.onInterceptTouchEvent ====");

        return super.onInterceptTouchEvent(event);
//        return true;//拦截 向上传递
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("touch_test", "===== ViewGroupBBB.onTouchEvent ====");

        return super.onTouchEvent(event);
//        return  true;//如果onInterceptTouchEvent=true拦截，会先执行自己是否处理这个事件，这里为true则不再向上传递
    }

    public ViewGroupBBB(Context context) {
        super(context);
    }

    public ViewGroupBBB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupBBB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewGroupBBB(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}

