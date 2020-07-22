package com.loe.mvvm.component

import android.os.Handler
import android.os.Looper

abstract class DelayTask(var delay: Long = 0): Handler(Looper.getMainLooper())
{
    private var runnable = Runnable { this@DelayTask.run() }

    fun start()
    {
        if (delay == 0L)
        {
            post(runnable)
        } else
        {
            postDelayed(runnable, delay)
        }
    }

    fun stop()
    {
        removeCallbacks(runnable)
    }

    protected abstract fun run()
}