package com.riven.test.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.PictureDrawable
import android.util.AttributeSet
import android.view.View


/**
 * Path绘制：https://github.com/GcsSloop/AndroidNote/blob/master/CustomView/Advance/%5B05%5DPath_Basic.md
 */
class BaseCustomView : View {
    private val mPaint: Paint = Paint()

    // 2.初始化画笔
    private fun initPaint() {
        mPaint.color = Color.WHITE //设置画笔颜色
        mPaint.style = Paint.Style.STROKE //描边、（填充、描边+填充）
        mPaint.strokeWidth = 10f //设置画笔宽度为10px
    }

    constructor(context: Context?) : super(context) {
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initPaint()
    }

    //生命周期切换就会进来 例如home键切回来
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //保存画板的状态
//        canvas?.save()
        //回滚画板到上次保存的状态
//        canvas?.restore()
        //画板的操作都是基于原点操作，第三个参数用来控制原点的偏移
//        canvas?.scale(0.5f,0.5f,200f,100f);
//        canvas?.rotate(50f,100f,0f);

        //绘制画布底色
        canvas?.drawColor(Color.RED)
        //画点
        canvas?.drawPoint(200f, 200f, mPaint)
        //画一组点
        canvas?.drawPoints(
            floatArrayOf(
                500f, 500f,
                500f, 600f,
                500f, 700f
            ), mPaint
        )
        //画一条线（startX, startY）起始点  （stopX, stopY）终点
        canvas?.drawLine(
            300f, 300f, 400f, 400f, mPaint
        )
        //画多条线
        canvas?.drawLines(
            floatArrayOf(
                500f, 800f, 700f, 800f,
                500f, 900f, 700f, 900f
            ), mPaint
        )
        //画矩形
//        canvas?.drawRect(300f,1000f,700f,1300f,mPaint)
        //与上同样效果 RectF精度为float
        canvas?.drawRect(RectF(300f,1000f,700f,1300f),mPaint)

        //绘制带圆角的矩形
        val rectF = RectF(300f, 1400f, 700f, 1600f)
        canvas?.drawRoundRect(rectF, 100f, 100f, mPaint)

        //绘制椭圆
        val rectF1 = RectF(300f, 1700f, 700f, 1800f)
        canvas?.drawOval(rectF1, mPaint)

        val mPaint1 = Paint()
        mPaint1.color = Color.BLACK
        mPaint1.strokeWidth = 10f
        mPaint1.style = Paint.Style.FILL
        canvas?.drawCircle(600f,100f,50f,mPaint1)

        val mPaint2 = Paint()
        val rectF2 = RectF(700f, 100f, 900f, 300f)
        // 绘制背景矩形
        mPaint2.color = Color.GRAY
        canvas?.drawRect(rectF2, mPaint2)
        // 绘制圆弧
        mPaint2.color = Color.BLUE
        //startAngle  // 开始角度
        //sweepAngle  // 扫过角度
        //useCenter   // 是否使用中心 使用了中心点之后绘制出来类似于一个扇形(true)，
        // 而不使用中心点(false)则是圆弧起始点和结束点之间的连线加上圆弧围成的图形
        canvas?.drawArc(rectF2, 0f, 90f, true, mPaint2)


    }

}