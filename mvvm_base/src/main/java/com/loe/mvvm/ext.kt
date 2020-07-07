package com.loe.mvvm

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnItemLongClickListener

/**
 * 绑定LiveData
 */
fun <T> FragmentActivity.bind(liveData: LiveData<T>, onBind: (data: T) -> Unit)
{
    liveData.observe(this, Observer<T>
    {
        if (it != null) onBind(it)
    })
}

/**
 * 绑定LiveData
 */
fun <T> Fragment.bind(liveData: LiveData<T>, onBind: (data: T) -> Unit)
{
    liveData.observe(viewLifecycleOwner, Observer<T>
    {
        if (it != null) onBind(it)
    })
}

/**
 * 绑定LiveData
 */
fun <T> LiveData<T>.bind(owner: LifecycleOwner, onBind: (data: T) -> Unit)
{
    observe(owner, Observer<T>
    {
        if (it != null) onBind(it)
    })
}

fun RecyclerView.addOnItemClickListener(onClick: (i: Int) -> Unit)
{
    this.addOnItemTouchListener(object : OnItemClickListener()
    {
        override fun onSimpleItemClick(baseQuickAdapter: BaseQuickAdapter<*, *>?, view: View?, i: Int)
        {
            onClick(i)
        }
    })
}

/**
 * 添加延时、防重复点击的itemClick事件
 */
fun RecyclerView.addDelayItemClickListener(delay: Long = 100, interval: Long = 1200, onClick: (i: Int) -> Unit)
{
    var t = 0L
    this.addOnItemTouchListener(object : OnItemClickListener()
    {
        override fun onSimpleItemClick(baseQuickAdapter: BaseQuickAdapter<*, *>?, view: View?, i: Int)
        {
            if (System.currentTimeMillis() - t > interval)
            {
                postDelayed({ onClick(i) }, delay)
                t = System.currentTimeMillis()
            }
        }
    })
}

fun RecyclerView.addOnItemChildClickListener(id: Int = 0, onClick: (i: Int) -> Unit)
{
    this.addOnItemTouchListener(object : OnItemChildClickListener()
    {
        override fun onSimpleItemChildClick(baseQuickAdapter: BaseQuickAdapter<*, *>?, view: View?, i: Int)
        {
            if (id == 0 || view?.id == id)
            {
                onClick(i)
            }
        }
    })
}

fun RecyclerView.addOnItemChildClickListener(id: Int = 0, interval: Long, onClick: (i: Int) -> Unit)
{
    var t = 0L
    this.addOnItemTouchListener(object : OnItemChildClickListener()
    {
        override fun onSimpleItemChildClick(baseQuickAdapter: BaseQuickAdapter<*, *>?, view: View?, i: Int)
        {
            if (id == 0 || view?.id == id && System.currentTimeMillis() - t > interval)
            {
                onClick(i)
                t = System.currentTimeMillis()
            }
        }
    })
}

fun RecyclerView.addOnItemLongClickListener(onClick: (i: Int) -> Unit)
{
    this.addOnItemTouchListener(object : OnItemLongClickListener()
    {
        override fun onSimpleItemLongClick(baseQuickAdapter: BaseQuickAdapter<*, *>?, view: View?, i: Int)
        {
            onClick(i)
        }
    })
}

fun RecyclerView.addOnItemChildLongClickListener(id: Int = 0, onClick: (i: Int) -> Unit)
{
    this.addOnItemTouchListener(object : OnItemChildLongClickListener()
    {
        override fun onSimpleItemChildLongClick(baseQuickAdapter: BaseQuickAdapter<*, *>?, view: View?, i: Int)
        {
            if (id == 0 || view?.id == id)
            {
                onClick(i)
            }
        }
    })
}

fun RecyclerView.addHeaderView(view: View)
{
    (adapter as BaseQuickAdapter<*, *>).addHeaderView(view)
}

fun RecyclerView.addHeaderView(view: View, index: Int)
{
    (adapter as BaseQuickAdapter<*, *>).addHeaderView(view, index)
}

fun RecyclerView.addFooterView(view: View)
{
    (adapter as BaseQuickAdapter<*, *>).addFooterView(view)
}

fun RecyclerView.addFooterView(view: View, index: Int)
{
    (adapter as BaseQuickAdapter<*, *>).addFooterView(view, index)
}

fun <T> createAdapter(layoutId: Int, list: List<T> = ArrayList(), convert: (holder: BaseViewHolder, bean: T) -> Unit): BaseQuickAdapter<T, BaseViewHolder>
{
    return object : BaseQuickAdapter<T, BaseViewHolder>(layoutId, list)
    {
        override fun convert(holder: BaseViewHolder, bean: T)
        {
            convert(holder, bean)
        }
    }
}

fun <T> RecyclerView.setQuickAdapter(layoutId: Int, list: List<T> = ArrayList(), convert: (holder: BaseViewHolder, bean: T) -> Unit): BaseQuickAdapter<T, BaseViewHolder>
{
    adapter = object : BaseQuickAdapter<T, BaseViewHolder>(layoutId, list)
    {
        override fun convert(holder: BaseViewHolder, bean: T)
        {
            convert(holder, bean)
        }
    }
    return adapter as BaseQuickAdapter<T, BaseViewHolder>
}