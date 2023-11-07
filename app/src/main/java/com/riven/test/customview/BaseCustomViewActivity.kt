package com.riven.test.customview

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.riven.test.R
import kotlinx.android.synthetic.main.activity_base_custom_view.*

/**
 * Function:
 * Author Name: Riven.zhang
 * Date: 2023/11/1
 * Copyright © 2006-2021 高顿网校, All Rights Reserved.
 */
class BaseCustomViewActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_custom_view)
        pieview.setData(
            arrayListOf(
                PieData("aaaaa", 50f),
                PieData("aaaaa", 10f),
                PieData("aaaaa", 25f),
                PieData("aaaaa", 15f)
            )
        )
        pieview.setStartAngle(270)

        moveview.setOnClickListener {
            Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show()
        }

        moveview.setOnLongClickListener {
            Toast.makeText(this, "长按", Toast.LENGTH_SHORT).show();
            false
        }
        var index = 0
        pieview.setOnClickListener {
            TextView(this).apply {
                text = "Tag ${index}"
                textSize = 20f
                setBackgroundColor(Color.parseColor("#888888"))
                gravity = Gravity.CENTER
                setPadding(8, 3, 8, 3)
                setTextColor(Color.parseColor("#FFFFFF"))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    leftMargin = 10
                    rightMargin = 10
                    bottomMargin = 40
                }
            }.also {
                // addView会触发
                // requestLayout();
                // invalidate(true);
                container?.addView(it)
            }
            index++
        }
    }

    /**
     * 让activity窗口快速变暗
     */
    fun dimBackGround(from: Float, to: Float) {
        ValueAnimator.ofFloat(from, to).apply {
            duration = 500
            addUpdateListener {
                var params: WindowManager.LayoutParams = window.attributes
                params.alpha = it.animatedValue as Float
                window.attributes = params
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        pieview.setOnClickListener {
            dimBackGround(1.0f,0.5f)
        }
    }

}