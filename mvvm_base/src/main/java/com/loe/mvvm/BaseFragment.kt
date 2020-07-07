package com.loe.mvvm

import android.app.Activity
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.loe.mvvm.initer.BaseLoading
import com.loe.mvvm.initer.BaseToast
import com.loe.mvvm.initer.LoadingData
import java.lang.Exception
import java.lang.reflect.ParameterizedType

/**
 * Fragment基类
 *
 * @author 章路顺
 * @since 2020/6/15-17:03
 */
abstract class BaseFragment<MODEL : BaseModel> : Fragment()
{
    lateinit var model: MODEL
        private set

    /** 加载框，不与activity共享model时初始化 */
    var loading: BaseLoading? = null
        private set

    /** toast */
    lateinit var toast: BaseToast
        private set

    /** activity引用 */
    val activity: Activity get() = getActivity()!!

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    protected lateinit var root: View

    protected lateinit var inflater: LayoutInflater

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        this.inflater = inflater
        root = inflater.inflate(getLayout(), container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        if (getActivity() != null)
        {
            // Simple类型时获取不到Model泛型，从getModelClass()获取
            model = try
            {
                val modelClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<MODEL>
                ViewModelProvider(this).get(modelClass)
            } catch (e: Exception)
            {
                ViewModelProvider(this).get(getModelClass()!!)
            }

            /* ****************************** 网络监听 ************************** */
            val isNoInit = !model.isInit
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                connectivityManager = activity.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
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
                            if (isNoInit) model.onNetAllLost()
                        }
                        super.onLost(network)
                    }

                    /** 网络连接 */
                    override fun onAvailable(network: Network?)
                    {
                        netCount++
                        onNetConnect(netCount)
                        if (isNoInit) model.onNetConnect(netCount)
                        super.onAvailable(network)
                    }
                }
                // 注册网络状态监听
                connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
            }
            // toast
            toast = BaseToast(activity)
            // event
            model.eventData.observe(viewLifecycleOwner, Observer<ModelEvent>
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
                    }
                }
            })
            // 不与activity共享model时，初始化加载框
            if (isNoInit)
            {
                // 加载框
                loading = BaseLoading(activity)
                model.loadingData.observe(viewLifecycleOwner, Observer<LoadingData>
                {
                    if (it != null)
                    {
                        if (it.isLoading)
                        {
                            if (it.msg.isEmpty())
                            {
                                loading?.show()
                            } else
                            {
                                loading?.show(it.msg)
                            }
                        } else
                        {
                            loading?.cancel()
                        }
                    }
                })
                model.isInit = true
            }
        }
        onCreatedPre()
        onCreated(savedInstanceState)
    }

    open fun onCreatedPre(){}

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

    /** 网络全部断开，只有不与activity共享model时有效 */
    open fun onNetAllLost()
    {
    }

    /** 有新网络接入，只有不与activity共享model时有效 */
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

    abstract fun getLayout(): Int

    abstract fun onCreated(savedInstanceState: Bundle?)

    /************************ 实现model功能 **********************/

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

    /**
     * 关闭页面
     */
    fun finish() = model.finish()
}