package com.riven.test.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 可以移动的View
 * <p>
 * 博客：http://blog.csdn.net/airsaid
 */
@SuppressLint("AppCompatCustomView")
public class MoveView extends ImageView {

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    int lastX = 0;
    int lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //控制view的布局移动，scrollTo与scrollBy内部调用的layout.移动的是内容，点击事件不消失
                layout(getLeft() + offsetX, getTop() + offsetY,
                        getRight() + offsetX, getBottom() + offsetY);
                //有交互的移动-改变布局参数
//                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
//                lp.leftMargin = getLeft() + offsetX;
//                lp.topMargin = getTop() + offsetY;
//                setLayoutParams(lp);
                return true;
            case MotionEvent.ACTION_UP:
                Toast.makeText(getContext(), "禁掉click事件，自我模拟点击事件", Toast.LENGTH_SHORT).show();
                //禁掉click事件
                return true;
            default:
                return super.onTouchEvent(event);
        }

    }
}