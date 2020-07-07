package com.loe.test

import android.app.Activity
import android.app.Application
import com.loe.mvvm.initer.LoadingCreater
import com.loe.mvvm.initer.LoadingIniter
import com.loe.mvvm.initer.ToastCreater
import com.loe.mvvm.initer.ToastIniter
import com.loe.test.component.AppProgress
import com.loe.test.component.AppToast

/**
 * App
 *
 * @author 章路顺
 * @since 2020/6/19-10:02
 */
class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        // 初始化Loading构造器（若无需实例对象，可用<Nothing>替代实例类<AppProgress>，create返回null）
        LoadingIniter.init(object : LoadingCreater<AppProgress>()
        {
            override fun create(activity: Activity): AppProgress?
            {
                return AppProgress(activity)
            }

            override fun onShow(loading: AppProgress?, msg: CharSequence)
            {
                if (msg.isEmpty())
                {
                    loading?.show()
                } else
                {
                    loading?.show(msg.toString())
                }
            }

            override fun onCancel(loading: AppProgress?)
            {
                loading?.cancel()
            }
        })

        // 初始化toast构造器（若无需实例对象，可用<Nothing>替代实例类<AppToast>，create返回null）
        ToastIniter.init(object : ToastCreater<AppToast>()
        {
            override fun create(activity: Activity): AppToast?
            {
                return AppToast(activity)
            }

            override fun onShow(toast: AppToast?, msg: CharSequence)
            {
                toast?.show(msg)
            }
        })
    }
}