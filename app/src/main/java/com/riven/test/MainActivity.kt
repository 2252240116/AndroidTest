package com.riven.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.riven.test.customview.BaseCustomViewActivity
import com.riven.test.eventdispatch.EventDispatchActivity
import com.riven.test.eventdispatch.EventDispatchActivity1
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        event_dispatch.setOnClickListener {
            startActivity(Intent(MainActivity@this,EventDispatchActivity::class.java));
        }

        event_dispatch1.setOnClickListener {
            startActivity(Intent(MainActivity@this, EventDispatchActivity1::class.java));
        }

        base_custom_view.setOnClickListener {
            startActivity(Intent(MainActivity@this, BaseCustomViewActivity::class.java));
        }

    }
}