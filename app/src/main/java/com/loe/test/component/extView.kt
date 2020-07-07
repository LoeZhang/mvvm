package com.zls.ext.view

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnItemLongClickListener

var View.visible: Boolean
    get() = this.visibility == View.VISIBLE
    set(value)
    {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }

fun View.setText(o: Any)
{
    (this as TextView).text = o.toString()
}

val View.string: String
    get() = try
    {
        (this as TextView).text.toString()
    } catch (e: Exception)
    {
        ""
    }

var View.autoText: String
    get() = try
    {
        (this as TextView).text.toString()
    } catch (e: Exception)
    {
        ""
    }
    set(value)
    {
        if(value == null || value.isEmpty())
        {
            visibility = View.GONE
        }else
        {
            visibility = View.VISIBLE
        }
        (this as TextView).text = value
    }

val View.int: Int
    get() = try
    {
        string.toInt()
    } catch (e: Exception)
    {
        0
    }

val View.double: Double
    get() = try
    {
        string.toDouble()
    } catch (e: Exception)
    {
        0.0
    }

val TextView.isEmpty get() = this.string.trim().isEmpty()
val TextView.isNotEmpty get() = this.string.trim().isNotEmpty()

fun EditText.putText(s: String)
{
    this.setText(s)
    this.setSelection(s.length)
}

fun View.setMaxLength(i: Int)
{
    (this as TextView).filters = arrayOf(InputFilter.LengthFilter(i))
}

fun View.setFilterChars(s: String)
{
    (this as TextView).keyListener = DigitsKeyListener.getInstance(s)
}

/**
 * 判断是否点击了某个View
 */
fun View.inRangeOfView(ev: MotionEvent?): Boolean
{
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    val x = location[0]
    val y = location[1]
    return !(ev!!.x < x || ev.x > x + this.width || ev.y < y || ev.y > y + this.height)
}

/**
 * 控制重复点击
 * @param delay 延迟响应时间（单位：毫秒）
 * @param interval 间隔响应时间（单位：毫秒）
 */
fun View.setDelayClickListener(delay: Long = 50, interval: Long = 1200, onClick: (v: View) -> Unit)
{
    var t = 0L
    setOnClickListener()
    {
        if (System.currentTimeMillis() - t > interval)
        {
            postDelayed({ onClick(it) }, delay)
            t = System.currentTimeMillis()
        }
    }
}

fun View.setOnDownListener(onDown: (v: View) -> Unit)
{
    setOnTouchListener()
    { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) onDown(v)
        true
    }
}

fun View.setOnUpListener(onDown: (v: View) -> Unit)
{
    setOnTouchListener()
    { v, event ->
        if (event.action == MotionEvent.ACTION_UP) onDown(v)
        true
    }
}

fun TextView.setOnEditorSearchListener(listener: () -> Unit)
{
    setOnEditorActionListener { _, id, _ ->
        if (id == EditorInfo.IME_ACTION_SEARCH)
        {
            listener()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun TextView.setOnEditorOkListener(ok: () -> Unit)
{
    setOnEditorActionListener()
    { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE)
        {
            ok()
        }
        false
    }
}

/**
 * 文本改变监听
 */
fun TextView.addTextAfterListener(after: (s: String) -> Unit)
{
    this.addTextChangedListener(object : TextWatcher
    {
        override fun afterTextChanged(s: Editable?)
        {
            after(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        {
        }
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

fun RecyclerView.init(context: Context)
{
    layoutManager = LinearLayoutManager(context)
    itemAnimator = null
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

fun ViewPager.addOnChangeListener(onChange: (i: Int) -> Unit)
{
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener
    {
        override fun onPageScrollStateChanged(state: Int)
        {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
        {
        }

        override fun onPageSelected(position: Int)
        {
            onChange(position)
        }
    })
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

fun <T> RecyclerView.setQuickAdapter(layoutId: Int, list: List<T>, convert: (holder: BaseViewHolder, bean: T) -> Unit): BaseQuickAdapter<T, BaseViewHolder>
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