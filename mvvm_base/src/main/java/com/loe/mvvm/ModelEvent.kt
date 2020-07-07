package com.loe.mvvm

import org.json.JSONObject

/**
 * Model事件
 * 用来装载model发向Activity和Fragment的信息
 *
 * @author 章路顺
 * @since 2020/6/24-16:20
 */
class ModelEvent
(
    val type: String,
    val data: Any
)
{
    fun getStringData(default: String = ""): String
    {
        return if (data is String) data else default
    }

    fun getIntData(default: Int = 0): Int
    {
        return if (data is Int) data else default
    }

    fun getLongData(default: Long = 0): Long
    {
        return if (data is Long) data else default
    }

    fun getDoubleData(default: Double = 0.0): Double
    {
        return if (data is Double) data else default
    }

    fun getBooleanData(default: Boolean = false): Boolean
    {
        return if (data is Boolean) data else default
    }

    fun getJSONObjectData(default: JSONObject? = null): JSONObject
    {
        return if (data is JSONObject) data else default ?: JSONObject()
    }

    fun getCharSequenceData(default: CharSequence = ""): CharSequence
    {
        return if (data is CharSequence) data else default
    }
}