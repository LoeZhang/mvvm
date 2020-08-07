package com.loe.mvvm.component

import android.app.Activity

/**
 * Loading初始化器
 *
 * @author 章路顺
 * @since 2020/6/18-16:09
 */
object LoadingIniter
{
    var creater: LoadingCreater<*> = object : LoadingCreater<DefaultProgress>()
    {
        override fun create(activity: Activity): DefaultProgress?
        {
            return DefaultProgress(activity)
        }

        override fun onShow(loading: DefaultProgress?, msg: CharSequence)
        {
            if (msg.isEmpty())
            {
                loading?.show()
            } else
            {
                loading?.show(msg.toString())
            }
        }

        override fun onCancel(loading: DefaultProgress?)
        {
            loading?.cancel()
        }
    }
        private set

    fun init(creater: LoadingCreater<*>)
    {
        LoadingIniter.creater = creater
    }
}

/**
 * Loading构造器
 *
 * @author 章路顺
 * @since 2020/6/18-16:09
 */
abstract class LoadingCreater<T>
{
    abstract fun create(activity: Activity): T?

    fun toShow(loading: Any?, msg: CharSequence)
    {
        onShow(loading as T?, msg)
    }

    fun toCancel(loading: Any?)
    {
        onCancel(loading as T?)
    }

    abstract fun onShow(loading: T?, msg: CharSequence)

    abstract fun onCancel(loading: T?)
}

/**
 * 基础加载框
 *
 * @author 章路顺
 * @since 2020/6/18-16:09
 */
class BaseLoading(private val activity: Activity)
{
    private val loading = LoadingIniter.creater.create(activity)

    fun show(msg: CharSequence = "")
    {
        LoadingIniter.creater.toShow(loading, msg)
    }

    fun cancel()
    {
        LoadingIniter.creater.toCancel(loading)
    }
}

/**
 * 加载框数据
 *
 * @author 章路顺
 * @since 2020/6/18-16:09
 */
class LoadingData
{
    var isLoading = false
    var msg: CharSequence = ""
}