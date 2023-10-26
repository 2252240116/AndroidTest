package com.riven.test.eventdispatch;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

public class AnimCardView extends CardView {


    public AnimCardView(@NonNull Context context) {
        super(context);
    }

    public AnimCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * true：拦截 传递给自己的onTouchEvent
     * false:不拦截 传递给子View的onTouchEvent
     *
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);

    }

    //点击事件到来的时候进行判断处理
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 获取事件类型
        int actionMarked = ev.getActionMasked();
        // 根据时间类型判断调用哪个方法来展示动画
        switch (actionMarked){
            case MotionEvent.ACTION_DOWN :{
                Log.e("touch_test", "ACTION_DOWN===== AnimCardView.dispatchTouchEvent ====");
                clickEvent();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                Log.e("touch_test", "ACTION_UP===== AnimCardView.dispatchTouchEvent ====");
                upEvent();
                break;
            }
            default: break;
        }

        Log.e("touch_test", "return ;===== AnimCardView.dispatchTouchEvent ====");
        // 最后回调默认的事件分发方法即可
//        return super.dispatchTouchEvent(ev); //这个默认回调回去，就执行不了该View的Action_MOVE Action_UP事件了，跟return false差不多
        return true;//表示事件被分发 自己处理自己的事件 会执行ACTION_UP事件
    }

    /**
     * true:事件被消费，进一步理解就是Action_Down被消费，只有Action_Down被消费，我们才能捕获到Action_Move等等
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("touch_test", "===== AnimCardView.onTouchEvent ====");
        return super.onTouchEvent(event);
    }

    //手指按下的时候触发的事件;大小高度变小，透明度减少
    private void clickEvent(){

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(this,"scaleX",1,0.5f),
                ObjectAnimator.ofFloat(this,"scaleY",1,0.5f),
                ObjectAnimator.ofFloat(this,"alpha",1,0.5f)
        );
        set.setDuration(100).start();
    }

    //手指抬起的时候触发的事件；大小高度恢复，透明度恢复
    private void upEvent(){

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(this,"scaleX",0.5f,1),
                ObjectAnimator.ofFloat(this,"scaleY",0.5f,1),
                ObjectAnimator.ofFloat(this,"alpha",0.5f,1)
        );
        set.setDuration(100).start();
    }
}
