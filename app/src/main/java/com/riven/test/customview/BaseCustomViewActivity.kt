package com.riven.test.customview

import android.app.Activity
import android.os.Bundle
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
        pieview.setData(arrayListOf(PieData("aaaaa",50f),PieData("aaaaa",10f),PieData("aaaaa",25f),PieData("aaaaa",15f)))
        pieview.setStartAngle(270)
    }

}