package com.riven.test.eventdispatch;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.riven.test.R;

public class EventDispatchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dispatch);

        View viewCCC = findViewById(R.id.view_ccc);
        //onTouchListener优先级比onTouchEvent高
        //先调用onTouchListener，再调用onClickListener和onLongClickListener
//        viewCCC.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("touch_test", "===== ViewTestCCC.[[OnTouchListener]].onTouch() ====");
//                //true 表示消耗事件 不再调用onTouchEvent
//                return false;
//            }
//        });
//        viewCCC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("touch_test", "===== ViewTestCCC.<<OnClickListener>>.onClick() ====");
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("touch_test", "ACTION_DOWN===== Activity.dispatchTouchEvent ====");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("touch_test", "ACTION_MOVE===== Activity.dispatchTouchEvent ====");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("touch_test", "ACTION_UP===== Activity.dispatchTouchEvent ====");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("touch_test", "ACTION_DOWN===== Activity.onTouchEvent ====");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("touch_test", "ACTION_MOVE===== Activity.onTouchEvent ====");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("touch_test", "ACTION_UP===== Activity.onTouchEvent ====");
                break;
        }

        return super.onTouchEvent(event);
    }
}
