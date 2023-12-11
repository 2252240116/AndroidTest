package com.riven.test.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Suppress("FunctionName")
public fun NormalScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)
