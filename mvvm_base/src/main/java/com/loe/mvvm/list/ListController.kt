package com.loe.mvvm.list

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.loe.mvvm.R

/**
 * 下拉刷新&上拉加载 列表控制类
 *
 * @author zls
 * @since 2020/7/3-9:30
 */
abstract class ListController<BEAN> : SwipeRefreshLayout.OnRefreshListener,
    BaseQuickAdapter.RequestLoadMoreListener
{
    private lateinit var activity: Activity

    private var size: Int = 10
    lateinit var adapter: BaseQuickAdapter<BEAN, *>

    private var isEnableLoad = true

    private var recyclerView: RecyclerView? = null
    private var refreshLayout: SwipeRefreshLayout? = null

    /**
     * 在activity onCreate() 执行
     */
    fun init(
        activity: Activity,
        recyclerView: RecyclerView?,
        refreshLayout: SwipeRefreshLayout? = null,
        isEnableLoad: Boolean = true
    )
    {
        this.activity = activity
        this.recyclerView = recyclerView
        this.refreshLayout = refreshLayout
        this.isEnableLoad = isEnableLoad

        size = 10

        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        refreshLayout?.setColorSchemeColors(ContextCompat.getColor(activity, R.color.colorPrimary))
        refreshLayout?.setOnRefreshListener(this)

        adapter = getListAdapter()
        recyclerView?.adapter = adapter

        if (isEnableLoad)
        {
            adapter.setEnableLoadMore(true)
            adapter.setOnLoadMoreListener(this, recyclerView)
        }

        // 空白页
        val empty = TextView(activity)
        empty.text = "暂无数据"
        empty.setTextColor(Color.parseColor("#aaaaaa"))
        empty.textSize = 13f
        empty.setPadding(0, dp_Px(125), 0, 0)
        empty.gravity = Gravity.CENTER_HORIZONTAL
        adapter.emptyView = empty
    }

    fun refresh()
    {
        refreshLayout?.measure(0, 0)
        refreshLayout?.isRefreshing = true
    }

    abstract fun loadData(isRefresh: Boolean)

    /**
     * 加载完成调用
     */
    fun loadOk(isRefresh: Boolean, aList: List<BEAN>)
    {
        if (isEnableLoad)
        {
            refreshLayout?.isRefreshing = false
            if (isRefresh)
            {
                size = 10
                adapter.data.clear()
            }
            adapter.addData(aList)
            if (isRefresh)
            {
                refreshLayout?.isRefreshing = false
                adapter.setEnableLoadMore(true)
            } else
            {
                adapter.loadMoreComplete()
                refreshLayout?.isEnabled = true
            }
            if (aList.size < size)
            {
                adapter.setEnableLoadMore(false)
            } else
            {
                size = aList.size
            }
        } else
        {
            refreshLayout?.isRefreshing = false
            adapter.data.clear()
            adapter.addData(aList)
        }
    }

    /**
     * 加载失败调用
     */
    fun loadError(isRefresh: Boolean)
    {
        if (isEnableLoad)
        {
            if (isRefresh)
            {
                refreshLayout?.isRefreshing = false
            } else
            {
                adapter.loadMoreComplete()
                refreshLayout?.isEnabled = true
            }
        } else
        {
            refreshLayout?.isRefreshing = false
        }
    }

    protected abstract fun getListAdapter(): BaseQuickAdapter<BEAN, *>

    fun setRefreshing(refreshing: Boolean)
    {
        refreshLayout?.isRefreshing = refreshing
    }

    /**
     * 刷新
     */
    override fun onRefresh()
    {
        if (isEnableLoad) adapter.setEnableLoadMore(false)

        recyclerView?.postDelayed({
            loadData(true)
        }, 500)
    }

    /**
     * 加载
     */
    override fun onLoadMoreRequested()
    {
        refreshLayout?.isEnabled = false

        recyclerView?.postDelayed({
            loadData(false)
        }, 400)
    }

    companion object
    {
        @JvmStatic
        fun dp_Px(dp: Int): Int
        {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }
    }
}