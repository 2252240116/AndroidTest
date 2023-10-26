package com.riven.test.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ViewTestCCC extends View {
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        Log.e("touch_test", "===== ViewTestCCC.dispatchTouchEvent ====");

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("touch_test", "===== ViewTestCCC.onTouchEvent ====");


        return super.onTouchEvent(event);
//        return false; //表示不处理 会从C的向上传递值B->A-Activity的onTouchEvent
    }

    public ViewTestCCC(Context context) {
        super(context);
    }

    public ViewTestCCC(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewTestCCC(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewTestCCC(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
