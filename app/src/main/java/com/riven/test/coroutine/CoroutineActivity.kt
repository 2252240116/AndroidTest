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
 * Function: 协程系列文章：https://juejin.cn/post/6962921891501703175
 *                        https://juejin.cn/post/7113706345190129700
 * Author Name: Riven.zhang
 * Date: 2023/12/7
 * 在Activity中使用协程建议使用封装好的lifecycle库，自己使用协程也可以，但是得解决几个问题
 * 1.考虑协程在主线程中刷新ui
 * 2.子协程异常会打扰父协程，父协程取消会影响子其他协程，导致无法运行
 * 3.考虑生命周期的退出，这时协程还在运行，会导致crash或者泄漏
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
        //协程的启动
        tv_launch.setOnClickListener {
            start1()
        }
        //协程的同步
        tv_launch1.setOnClickListener {
            startConcurrencyAndSynchronous()
        }
        //协程的CoroutineContext
        tv_launch2.setOnClickListener {
            testCoroutineContext()
        }
        //协程的启动模式
        tv_launch3.setOnClickListener {
            testCoroutineStart()
        }
        //协程的作用域(主作用域与协同作用域）
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
        //协程原理
        tv_coroutine_principle.setOnClickListener {
            MainScope().launch {
                testCor()
            }
        }
    }

    /**
     *  suspend本质是callBack ，kotlin里叫Continuation(续体）
     *  Continuation:接下来需要执行的代码or剩下来的代码
     *  suspend方法反编译会形成一个 带形参continution 的方法
     *
     *  public interface Continuation<in T> {
        public val context: CoroutineContext
            //相当于 onSuccess     结果
            public fun resumeWith(result: Result<T>)
        }

        suspend方法返回值是个Any，标志函数有没有被挂起。(因为可能返回是枚举、"no suspend"、null所以定义为Any)
        testCor()方法内部会调用invokeSuspend()方法,invokeSuspend嵌套testCor()方法

        内部有两个字段label(状态机，可理解为每一个supsend方法是一个case状态）result(协程返回值）
        当执行一个伪supsend方法，会重新走一遍Switch切换到下一个状态机

        他们公用同一个Continuation实例(轻量级）
        协程完全由开发者管理，不涉及操作系统的调度和切换，因此高效
     */
    private suspend fun testCor() {
        // 从主线程切到子线程（协程的挂起）从子线程切回主线程（协程的恢复resume)
        // ↓主线程   ↓子线程
        val user = getUserInfo()
        val friendList = getFriendList(user)
        val feedList = getFeedList(friendList)
        feedList.forEach {
            Log.d("打印", it)
        }
    }

    //挂起函数
    suspend fun getUserInfo(): String {
        Log.d("打印","开始getUserInfo")
        //挂起函数包含其他挂起函数才叫真正的挂起
        withContext(Dispatchers.IO) {
            delay(1000L)
        }
        Log.d("打印","返回BoyCoder")

        return "BoyCoder"
    }
    //挂起函数
    suspend fun getFriendList(user: String): String {
        Log.d("打印","开始getFriendList")

        withContext(Dispatchers.IO) {
            delay(1000L)
        }
        Log.d("打印","返回Tom, Jack")

        return "Tom, Jack"
    }
    //挂起函数
    suspend fun getFeedList(list: String): ArrayList<String> {
        Log.d("打印","开始getFeedList")

        withContext(Dispatchers.IO) {
            delay(1000L)
        }
        Log.d("打印","返回在线状态")

        return arrayListOf<String>("在线","离线","繁忙","勿扰","Q我吧")
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