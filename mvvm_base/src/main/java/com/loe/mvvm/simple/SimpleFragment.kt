package com.loe.mvvm.simple

import com.loe.mvvm.BaseFragment
import com.loe.mvvm.BaseModel

/**
 * 简易Fragment，默认Model
 *
 * @author 章路顺
 * @since 2020/7/2-20:00
 */
abstract class SimpleFragment : BaseFragment<BaseModel>()
{
    override fun getModelClass(): Class<BaseModel>?
    {
        return BaseModel::class.java
    }
}
