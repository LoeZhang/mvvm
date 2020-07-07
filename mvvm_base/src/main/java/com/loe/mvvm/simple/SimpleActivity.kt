package com.loe.mvvm.simple

import com.loe.mvvm.BaseActivity
import com.loe.mvvm.BaseModel

/**
 * 简易Activity，默认Model
 *
 * @author 章路顺
 * @since 2020/7/2-20:00
 */
open class SimpleActivity : BaseActivity<BaseModel>()
{
    override fun getModelClass(): Class<BaseModel>?
    {
        return BaseModel::class.java
    }
}
