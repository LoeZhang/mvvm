package com.loe.mvvm

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object ResultUtil
{
    private const val TAG = "ResultUtil"

    @JvmStatic
    fun startResult(activity: Activity, intent: Intent, onResult: ((ResultBean) -> Unit)? = null)
    {
        if (activity is FragmentActivity)
        {
            val manager = activity.supportFragmentManager
            var fragment = manager.findFragmentByTag(TAG) as? ResultFragment
            if (fragment == null)
            {
                fragment = ResultFragment()
                manager.beginTransaction()
                    .add(fragment, TAG)
                    .commitNowAllowingStateLoss()
            }
            fragment.onResult = onResult
            fragment.startActivityForResult(intent, 14)
        } else
        {
            Log.e("Runtime-$TAG", "请传入FragmentActivity")
        }
    }

    @JvmStatic
    fun startResult(activity: Activity, cls: Class<*>, onResult: ((ResultBean) -> Unit)? = null)
    {
        startResult(activity, Intent(activity, cls), onResult)
    }

    class ResultFragment : Fragment()
    {
        var onResult: ((ResultBean) -> Unit)? = null

        override fun onCreate(savedInstanceState: Bundle?)
        {
            super.onCreate(savedInstanceState)
            retainInstance = true
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode != 14) return
            onResult?.invoke(ResultBean(resultCode, data))
            onResult = null
        }
    }

    class ResultBean(
            var code: Int,
            var data: Intent?
    )
    {
        fun isOk(): Boolean
        {
            return code == RESULT_OK
        }

        fun getUri(): Uri?
        {
            return data?.data
        }

        fun getString(key: String, default: String = ""): String
        {
            return data?.getStringExtra(key) ?: default
        }

        fun getInt(key: String, default: Int = 0): Int
        {
            return data?.getIntExtra(key, default) ?: default
        }

        fun getLong(key: String, default: Long = 0): Long
        {
            return data?.getLongExtra(key, default) ?: default
        }

        fun getDouble(key: String, default: Double = 0.0): Double
        {
            return data?.getDoubleExtra(key, default) ?: default
        }

        fun getFloat(key: String, default: Float = 0f): Float
        {
            return data?.getFloatExtra(key, default) ?: default
        }

        fun getBoolean(key: String, default: Boolean = false): Boolean
        {
            return data?.getBooleanExtra(key, default) ?: default
        }
    }
}