package com.loe.test.component

import android.os.Handler
import android.os.Looper

abstract class DelayTask(var delay: Long)
{
    private val handler = Handler(Looper.getMainLooper())

    private var runnable = Runnable { this@DelayTask.run() }

    fun start()
    {
        if (delay == 0L)
        {
            handler.post(runnable)
        } else
        {
            handler.postDelayed(runnable, delay)
        }
    }

    fun stop()
    {
        handler.removeCallbacks(runnable)
    }

    protected abstract fun run()
}