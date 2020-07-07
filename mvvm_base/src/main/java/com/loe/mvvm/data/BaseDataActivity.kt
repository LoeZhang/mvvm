package com.loe.mvvm.data

import android.os.Bundle
import androidx.lifecycle.Observer
import com.loe.mvvm.BaseActivity

/**
 * 携带默认Data的Activity基类
 *
 * @author 章路顺
 * @since 2020/6/15-17:03
 */
abstract class BaseDataActivity<MODEL : BaseDataModel<DATA>, DATA> : BaseActivity<MODEL>()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // 页面数据
        model.pageData.observe(this, Observer<DATA>
        {
            if (it != null) onData(model.data)
        })
    }

    /**
     * 页面data变化监听
     */
    abstract fun onData(data: DATA)

    val data: DATA get() = model.data

    /**
     * 更新数据
     * @param isMain 是否强制发送到主线程
     */
    fun update(isMain: Boolean = true)
    {
        model.update(isMain)
    }
}