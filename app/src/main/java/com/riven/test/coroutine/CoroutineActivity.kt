package com.riven.test.coroutine

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.riven.test.R
import kotlinx.android.synthetic.main.activity_coroutine.*
import kotlinx.coroutines.*

/**
 * Function:
 * Author Name: Riven.zhang
 * Date: 2023/12/7
 * Copyright © 2006-2021 高顿网校, All Rights Reserved.
 */
class CoroutineActivity :AppCompatActivity(){
    private val normalScope = NormalScope()

    init {
        //在onresume之后唤起
        lifecycleScope.launchWhenResumed {
            Log.d("init", "在类初始化位置启动协程")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("init", "onResume")

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)

        tv_launch.setOnClickListener {
            start1()
        }

        tv_launch1.setOnClickListener {
            startConcurrencyAndSynchronous()
        }

        tv_launch2.setOnClickListener {
            testCoroutineContext()
        }

        tv_launch3.setOnClickListener {
            testCoroutineStart()
        }

        tv_launch4.setOnClickListener {
            testCoroutineScope3()
        }
        //并发
        requestMain {
            delay(2000)
            Toast.makeText(this@CoroutineActivity,"haha1",Toast.LENGTH_SHORT).show()
        }
        //并发
        requestIO {
            loadNetData()
        }
        //并发
        delayMain(delayTime=500){
            Toast.makeText(this@CoroutineActivity,"haha2",Toast.LENGTH_SHORT).show()
        }

        //自定义Coroutine,用于dialog等其他地方使用
        var job : Job  = customCoroutineRequest{

        }
    }

    protected fun customCoroutineRequest(
        errCode: Int = -1, errMsg: String = "", report: Boolean = false,
        block: suspend CoroutineScope.() -> Unit): Job {
        return normalScope.launch(Dispatchers.IO + GlobalCoroutineExceptionHandler(errCode, errMsg, report)) {
            block.invoke(this)
        }
    }

    /**
     * 分析主从(监督)作用域的时候，我们需要用到supervisorScope或者SupervisorJob
     *
     * 主从(监督)作用域与协同作用域一致，区别在于该作用域下的协程取消操作的单向传播性，子协程的异常不会导致其它子协程取消。
     */
    private fun testCoroutineScope3() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("exceptionHandler", "${coroutineContext[CoroutineName]} $throwable")
        }
        GlobalScope.launch(Dispatchers.Main + CoroutineName("scope1") + exceptionHandler) {
            supervisorScope {
                Log.d("scope", "--------- 1")
                launch(CoroutineName("scope2")+exceptionHandler) {
                    Log.d("scope", "--------- 2")
                    throw  NullPointerException("空指针")
                    Log.d("scope", "--------- 3")
                    val scope3 = launch(CoroutineName("scope3")) {
                        Log.d("scope", "--------- 4")
                        delay(2000)
                        Log.d("scope", "--------- 5")
                    }
                    scope3.join()
                }
                val scope4 = launch(CoroutineName("scope4")) {
                    Log.d("scope", "--------- 6")
                    delay(2000)
                    Log.d("scope", "--------- 7")
                }
                scope4.join()
                Log.d("scope", "--------- 8")
            }
        }
    }


    private fun testCoroutineStart(){
        //默认是饿汉式启动模式 协程创建后立即调度，并不是立即执行（可以理解为在执行前可能被取消）
        val defaultJob = GlobalScope.launch{
            Log.d("defaultJob", "CoroutineStart.DEFAULT")
        }
        defaultJob.cancel()

        //懒汉式启动，只有主动.start,协程才会调度执行
        val lazyJob = GlobalScope.launch(start = CoroutineStart.LAZY){
            Log.d("lazyJob", "CoroutineStart.LAZY")
        }

        //跟default相似，会马上调度。但是 在执行到第一个挂起点时是不影响cancel操作的
        val atomicJob = GlobalScope.launch(start = CoroutineStart.ATOMIC){
            Log.d("atomicJob", "CoroutineStart.ATOMIC挂起前")
            delay(100)
            Log.d("atomicJob", "CoroutineStart.ATOMIC挂起后")
        }
        atomicJob.cancel()

        //跟atomic相似，不同的是不需要调度，会立马在当前调用线程去执行，直到运行到第一个挂起点
        //无论我们是否指定协程调度器，挂起前的执行都是在当前线程下执行。
        val undispatchedJob = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED){
            Log.d("undispatchedJob", "CoroutineStart.UNDISPATCHED挂起前")
            delay(100)
            Log.d("undispatchedJob", "CoroutineStart.UNDISPATCHED挂起后")
        }
        undispatchedJob.cancel()

// 1.可能是
//        D/defaultJob: CoroutineStart.DEFAULT
//        D/atomicJob: CoroutineStart.ATOMIC挂起前
//        D/undispatchedJob: CoroutineStart.UNDISPATCHED挂起前
// 2. 也可能是
//        D/undispatchedJob: CoroutineStart.UNDISPATCHED挂起前
//        D/atomicJob: CoroutineStart.ATOMIC挂起前
    }

    private fun testCoroutineContext(){
        val coroutineContext1 = Job() + CoroutineName("这是第一个上下文")
        Log.d("coroutineContext1", "$coroutineContext1")
        val  coroutineContext2 = coroutineContext1 + Dispatchers.Default + CoroutineName("这是第二个上下文")
        Log.d("coroutineContext2", "$coroutineContext2")
        val coroutineContext3 = coroutineContext2 + Dispatchers.Main + CoroutineName("这是第三个上下文")
        Log.d("coroutineContext3", "$coroutineContext3")
//        coroutineContext1: [JobImpl{Active}@c467a3a, CoroutineName(这是第一个上下文)]
//        coroutineContext2: [JobImpl{Active}@c467a3a, CoroutineName(这是第二个上下文), Dispatchers.Default]
//        coroutineContext3: [JobImpl{Active}@c467a3a, CoroutineName(这是第三个上下文), Dispatchers.Main]
    }


    private fun startConcurrencyAndSynchronous() {
        //添加调度器Dispatchers.Main，即切换到主线程。即同步处理
        GlobalScope.launch(Dispatchers.Main) {
            for (index in 1 until  10) {
                //同步执行
                launch {
                    Log.d("launch$index", "启动一个协程")
                }
            }
        }
    }


    private fun start(){
        val runBlockingJob = runBlocking {
            Log.d("runBlocking", "启动一个协程")
            41
        }
        Log.d("runBlockingJob", "$runBlockingJob")

        //协程的设计是并发的 并且不会阻塞当前调用的线程
        //所以协程体内的执行顺序随机
        val launchJob = GlobalScope.launch{
            Log.d("launch", "启动一个协程")
        }
//        launchJob.cancel();是个job
        Log.d("launchJob", "$launchJob")

        val asyncJob = GlobalScope.async{
            Log.d("async", "启动一个协程")
            "我是返回值"
        }
        Log.d("asyncJob", "$asyncJob")
    }

    private fun start1(){
//        MainScope().launch {
//
//        }
        GlobalScope.launch{
            val launchJob = launch{
                Log.d("launch", "启动一个协程")
            }
            Log.d("launchJob", "$launchJob")
            val asyncJob = async{
                Log.d("async", "启动一个协程")
                "我是async返回值"
            }
            Log.d("asyncJob.await", ":${asyncJob.await()}")
            //等待await结束执行这行
            Log.d("asyncJob", "$asyncJob")
        }
    }

    private suspend fun loadNetData(){
        //网络加载
    }

}