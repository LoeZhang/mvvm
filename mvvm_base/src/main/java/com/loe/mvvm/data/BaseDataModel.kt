package com.loe.mvvm.data

import androidx.lifecycle.MutableLiveData
import com.loe.mvvm.BaseModel
import java.lang.reflect.ParameterizedType

/**
 * 携带默认Data的Model基类
 *
 * @author 章路顺
 * @since 2020/7/2-19:00
 */
open class BaseDataModel<DATA> : BaseModel()
{
    val pageData: MutableLiveData<DATA> = MutableLiveData()

    val data: DATA get() = pageData.value!!

    init
    {
        // 初始化数据
        val dataClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<DATA>
        pageData.postValue(dataClass.newInstance())
    }

    /**
     * 更新数据
     * @param isMain 是否强制发送到主线程
     */
    fun update(isMain: Boolean = true)
    {
        if (pageData.value != null)
        {
            if (isMain)
            {
                // 可以在主线程或者子线程调用，postValue会将任务发布到主线程以设置给定值
                pageData.postValue(data)
            } else
            {
                // 只能在主线程设置给定值，如果不在主线程调用setValue，会抛出异常
                pageData.value = data
            }
        }
    }
}