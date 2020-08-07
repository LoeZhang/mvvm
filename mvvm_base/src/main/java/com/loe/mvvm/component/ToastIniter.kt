package com.loe.mvvm.component

import android.app.Activity

/**
 * Toast初始化器
 *
 * @author 章路顺
 * @since 2020/6/18-16:09
 */
object ToastIniter
{
    var creater: ToastCreater<*> = object : ToastCreater<DefaultToast>()
    {
        override fun create(activity: Activity): DefaultToast?
        {
            return DefaultToast(activity)
        }

        override fun onShow(toast: DefaultToast?, msg: CharSequence)
        {
            toast?.show(msg)
        }
    }
        private set

    fun init(creater: ToastCreater<*>)
    {
        ToastIniter.creater = creater
    }
}

/**
 * Toast构造器
 *
 * @author 章路顺
 * @since 2020/6/18-16:09
 */
abstract class ToastCreater<T>
{
    abstract fun create(activity: Activity): T?

    fun toShow(toast: Any?, msg: CharSequence)
    {
        onShow(toast as T?, msg)
    }

    abstract fun onShow(toast: T?, msg: CharSequence)
}

/**
 * 基础Toast
 *
 * @author 章路顺
 * @since 2020/6/18-16:09
 */
class BaseToast(activity: Activity)
{
    private val toast = ToastIniter.creater.create(activity)

    fun show(msg: CharSequence)
    {
        ToastIniter.creater.toShow(toast, msg)
    }
}