package com.loe.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loe.mvvm.component.LoadingData

/**
 * Model基类
 *
 * @author 章路顺
 * @since 2020/6/15-17:03
 */
open class BaseModel : ViewModel()
{
    /** 加载框比较特殊，具有持续状态，所以不能基于event实现，单独拥有一个liveData */
    val loadingData: MutableLiveData<LoadingData> = MutableLiveData()

    val eventData: MutableLiveData<ModelEvent> = MutableLiveData()

    /** 是否已经初始化了loading等 */
    var isInit: Boolean = false

    init
    {
        // 初始化数据
        loadingData.postValue(LoadingData())
    }

    /**
     * 显示加载框
     * @param msg loading显示的数据，兼容有提示文本的loading框
     */
    fun showLoading(msg: CharSequence = "")
    {
        val lData = loadingData.value!!
        lData.msg = msg
        lData.isLoading = true
        loadingData.postValue(lData)
    }

    /**
     * 关闭加载框
     */
    fun cancelLoading()
    {
        val lData = loadingData.value!!
        lData.isLoading = false
        loadingData.postValue(lData)
    }

    /**
     * 发送事件到Activity和Fragment
     * @param type 事件类型
     * @param data 事件内容
     */
    fun postEvent(type: String, data: Any = "")
    {
        eventData.postValue(ModelEvent(type, data))
    }

    /**
     * 简易事件，反射调用Activity、Fragment方法
     * 基于postEvent事件实现
     * @param data 执行方法的参数
     */
    fun String.invoke(data: Any = "")
    {
        postEvent(this, data)
    }

    /**
     * 显示toast
     * 基于postEvent事件实现
     */
    fun toast(msg: CharSequence?)
    {
        postEvent("toast", msg ?: "")
    }

    /**
     * 关闭页面
     * 基于postEvent事件实现
     */
    fun finish()
    {
        postEvent("finish")
    }

    /** 网络全部断开 */
    open fun onNetAllLost()
    {
    }

    /** 有新网络接入 */
    open fun onNetConnect(netCount: Int)
    {
    }
}