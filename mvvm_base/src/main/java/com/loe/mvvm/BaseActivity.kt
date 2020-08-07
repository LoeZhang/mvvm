package com.loe.mvvm

import android.net.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.loe.mvvm.component.BaseLoading
import com.loe.mvvm.component.BaseToast
import com.loe.mvvm.component.LoadingData
import java.lang.Exception
import java.lang.reflect.ParameterizedType

/**
 * Activity基类
 *
 * @author 章路顺
 * @since 2020/6/15-17:03
 */
open class BaseActivity<MODEL : BaseModel> : AppCompatActivity()
{
    lateinit var model: MODEL
        private set

    /** 加载框 */
    lateinit var loading: BaseLoading
        private set

    /** toast */
    lateinit var toast: BaseToast
        private set

    /** activity引用，减少this@ */
    val activity: AppCompatActivity get() = this

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Simple类型时获取不到Model泛型，从getModelClass()获取
        model = try
        {
            val modelClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<MODEL>
            ViewModelProvider(this).get(modelClass)
        } catch (e: Exception)
        {
            ViewModelProvider(this).get(getModelClass()!!)
        }

        // 加载框
        loading = BaseLoading(this)
        model.loadingData.observe(this, Observer<LoadingData>
        {
            if (it != null)
            {
                if (it.isLoading)
                {
                    if (it.msg.isEmpty())
                    {
                        loading.show()
                    } else
                    {
                        loading.show(it.msg)
                    }
                } else
                {
                    loading.cancel()
                }
            }
        })
        // toast
        toast = BaseToast(this)
        // event
        model.eventData.observe(this, Observer<ModelEvent>
        {
            if (it != null)
            {
                // 预设事件处理
                when (it.type)
                {
                    "toast" ->
                    {
                        toast.show(it.getCharSequenceData())
                        model.eventData.value = null
                        return@Observer
                    }
                    "finish" ->
                    {
                        activity.finish()
                        model.eventData.value = null
                        return@Observer
                    }
                }
                // 事件处理完后置为null，event将不再传递
                if (onEvent(it))
                {
                    model.eventData.value = null
                }else
                // 反射调用遗落事件
                {
                    if(it.data == "")
                    {
                        invoke(it.type)
                    }else
                    {
                        invoke(it.type, it.data)
                    }
                }
            }
        })

        /* ****************************** 网络监听 ************************** */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            var netCount = 0
            networkCallback = object : ConnectivityManager.NetworkCallback()
            {
                /** 网络连接断开 */
                override fun onLost(network: Network?)
                {
                    netCount--
                    if (netCount <= 0)
                    {
                        onNetAllLost()
                        model.onNetAllLost()
                    }
                    super.onLost(network)
                }

                /** 网络连接 */
                override fun onAvailable(network: Network?)
                {
                    netCount++
                    onNetConnect(netCount)
                    model.onNetConnect(netCount)
                    super.onAvailable(network)
                }
            }
            // 注册网络状态监听
            connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
        }
        model.isInit = true
    }

    override fun onDestroy()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            // 取消注册网络状态监听
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
        super.onDestroy()
    }

    /** Simple获取Model类型 */
    open fun getModelClass(): Class<MODEL>?
    {
        return null
    }

    /** 网络全部断开 */
    open fun onNetAllLost()
    {
    }

    /** 有新网络接入 */
    open fun onNetConnect(netCount: Int)
    {
    }

    /**
     * 接收model发来的事件
     * @return 是否已处理事件，若处理了事件，则返回true
     */
    open fun onEvent(event: ModelEvent): Boolean
    {
        return false
    }

    /**
     * LiveData绑定View
     */
    fun <T> LiveData<T>.bind(onBind: (data: T) -> Unit)
    {
        observe(this@BaseActivity, Observer<T>
        {
            if (it != null) onBind(it)
        })
    }

    /**
     * 反射调用方法
     */
    fun invoke(name: String, vararg params: Any)
    {
        this::class.java.declaredMethods.forEach()
        {
            if (it.name == name)
            {
                try
                {
                    it.isAccessible = true
                    it.invoke(this, *params)
                    // 反射执行结束消耗掉事件，使其不再传递
                    model.eventData.value = null
                } catch (e: Exception)
                {
                }
            }
        }
    }

    /************************ 实现model功能**********************/

    /**
     * 显示加载框
     * @param msg loading显示的数据，兼容有提示文本的loading框
     */
    fun showLoading(msg: CharSequence = "") = model.showLoading(msg)

    /**
     * 关闭加载框
     */
    fun cancelLoading() = model.cancelLoading()

    /**
     * 显示toast
     */
    fun toast(msg: CharSequence?) = model.toast(msg)
}