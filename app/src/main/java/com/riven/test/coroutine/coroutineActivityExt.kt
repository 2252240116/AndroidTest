package com.riven.test.coroutine

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

/**
 * activity中使用lifecycle的封装
 */
inline fun AppCompatActivity.requestMain(
        errCode: Int = -1, errMsg: String = "", report: Boolean = false,
        noinline block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch(GlobalCoroutineExceptionHandler(errCode, errMsg, report)) {
        block.invoke(this)
    }
}

inline fun AppCompatActivity.requestIO(
        errCode: Int = -1, errMsg: String = "", report: Boolean = false,
        noinline block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch(Dispatchers.IO + GlobalCoroutineExceptionHandler(errCode, errMsg, report)) {
       block.invoke(this)
    }
}

inline fun AppCompatActivity.delayMain(
        errCode: Int = -1, errMsg: String = "", report: Boolean = false,
        delayTime: Long, noinline block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch(GlobalCoroutineExceptionHandler(errCode, errMsg, report)) {
        withContext(Dispatchers.IO) {
            delay(delayTime)
        }
         block.invoke(this)
    }
}
