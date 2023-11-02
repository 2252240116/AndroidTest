package com.riven.test.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * 自定义换行控件
 */
class AutoLineLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //当所有子控件尺寸决定了，才能知道父控件尺寸
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        //获取控件的高度模式
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        //获取容器控件的宽度（即布局文件指定的宽度）
        val width = MeasureSpec.getSize(widthMeasureSpec)
        //定义容器控件的初始高度0
        var height = 0
        //当容器高度指定为精确的值
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec)
        }
        //手动计算容器控件高度
        else {
            //'容器控件当前行剩下的空间'
            var remainWidth = width
            //'遍历所有子控件并用自动换行的方式累加其高度'
            (0 until childCount).map {
                getChildAt(it)
            }.forEach { child ->
                val lp = child.layoutParams as LinearLayout.LayoutParams
                //'当前行已满，在新的一行放置子控件'
                if (isNewLine(lp, child, remainWidth)) {
                    remainWidth = width - child.measuredWidth
                    //'容器控件新增一行的高度'
                    height += (lp.topMargin + lp.bottomMargin + child.measuredHeight)
                }
                //'当前行未满，在当前行右侧放置子控件'
                else {
                    //'消耗当前行剩余宽度'
                    remainWidth -= child.measuredWidth
                    if (height == 0) height =
                        (lp.topMargin + lp.bottomMargin + child.measuredHeight)
                }
                //将子控件的左右边距和间隙也考虑在内
                remainWidth -= (lp.leftMargin + lp.rightMargin)
            }
        }
        //'控件测量的终点，即容器控件的宽高已确定'
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //'当前横坐标（相对于容器控件左边界的距离）'
        var left = 0
        //'当前纵坐标（相对于容器控件上边界的距离）'
        var top = 0
        //'上一行底部的纵坐标（相对于容器控件上边界的距离）'
        var lastBottom = 0
        var count = 0
        //'遍历所有子控件以确定它们相对于容器控件的位置'
        (0 until childCount).map {
            getChildAt(it)
        }.forEach { child ->
            val lp = child.layoutParams as LinearLayout.LayoutParams
            //'新起一行'
            if (isNewLine(lp, child, r - l - left)) {
                //这个地方有问题，如果这样就会导致从第二行开始首个childView没有leftMargin
//                left = -lp.leftMargin
                //换行left又变成了0
                left = 0
                top = lastBottom
                lastBottom = 0
            }
            //'子控件左边界'
            val childLeft = left + lp.leftMargin
            //'子控件上边界'
            val childTop = top + lp.topMargin
            //'确定子控件上下左右边界相对于父控件左上角的距离'
            //设置子View的位置
            child.layout(
                childLeft,
                childTop,
                childLeft + child.measuredWidth,
                childTop + child.measuredHeight
            )
            //'更新上一行底部纵坐标'
            if (lastBottom == 0) lastBottom = child.bottom + lp.bottomMargin
            //child.layout布局完成，更新下一个view的left了
            left += child.measuredWidth + lp.leftMargin + lp.rightMargin
            count++
        }
    }

    //'判断是否需要新起一行'
    private fun isNewLine(lp: LinearLayout.LayoutParams, child: View, remainWidth: Int) =
        lp.leftMargin + child.measuredWidth + lp.rightMargin > remainWidth

}