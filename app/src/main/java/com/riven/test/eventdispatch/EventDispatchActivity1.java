package com.riven.test.eventdispatch;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.riven.test.R;

public class EventDispatchActivity1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dispatch1);

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
