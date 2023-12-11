package com.riven.test.coroutine

import android.util.Log
import androidx.lifecycle.ViewModel

/**
 *
 */
class MainViewModel: ViewModel() {

    init {
        requestMain {
            Log.d("MainViewModel", "主线程中启动协程")
        }
        requestIO {
            Log.d("MainViewModel", "IO线程中启动协程进行网络加载")
        }
        delayMain(delayTime = 500){
            Log.d("MainViewModel", "主线程中启动协程并延时一定时间")
        }
    }
}
