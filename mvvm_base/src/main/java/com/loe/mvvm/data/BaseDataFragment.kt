package com.loe.mvvm.data

import androidx.lifecycle.Observer
import com.loe.mvvm.BaseFragment

/**
 * 携带默认Data的Fragment基类
 *
 * @author 章路顺
 * @since 2020/6/15-17:03
 */
abstract class BaseDataFragment<MODEL : BaseDataModel<DATA>, DATA> : BaseFragment<MODEL>()
{
    override fun onCreatedPre()
    {
        if (getActivity() != null)
        {
            // 页面数据
            model.pageData.observe(viewLifecycleOwner, Observer<DATA>
            {
                if (it != null) onData(model.data)
            })
        }
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