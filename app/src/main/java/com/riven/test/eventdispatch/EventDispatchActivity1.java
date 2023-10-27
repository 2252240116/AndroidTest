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
    public void onUserInteraction() {
        super.onUserInteraction();
        //当触摸Activity 走了Action_Down就出触发这个空方法
        Log.e("==========","调用onUserInteraction");
    }
}
