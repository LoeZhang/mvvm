package com.loe.test

import androidx.lifecycle.MutableLiveData
import com.loe.mvvm.data.BaseDataModel
import com.loe.mvvm.createAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestData
{
    var name = ""
    var age = 0
    var sex = true
    var money = 0.5
}

class TestModel : BaseDataModel<TestData>()
{
    val singleData = MutableLiveData<String>()

    val adapter = createAdapter<String>(R.layout.list_item)
    { holder, s ->
        holder.setText(R.id.textName, s)
    }

    /** 有新网络接入 */
    override fun onNetConnect(netCount: Int)
    {
        toast("有网络接入了")
    }

    fun add()
    {
//        GlobalScope.launch()
//        {
//            showLoading("加载框文字...")
//            delay(2000)
//            data.age++
//            data.name = "name${data.age}"
//            data.money = data.age.toDouble() * 125
//            // data变动后需要调用update()更新
//            update()
//            cancelLoading()
//            toast("add成功")
//        }
        "goHome".invoke()
    }

    fun sub()
    {
        data.age--
        data.name = "money${data.age}"
        data.money = data.age.toDouble() * 125
        data.sex = !data.sex
        // data变动后需要调用update()更新
        update()
        toast("sub成功")
//        toNewPage()
    }

    /**
     * 发送ModelEvent事件
     */
    fun toNewPage()
    {
        postEvent("toNew", "我是ModelEvent的data")
    }
}

//class TestModel2 : BaseModel<TestData>()
//{
//    /** 有新网络接入 */
//    override fun onNetConnect(netCount: Int)
//    {
//        toast("有网络接入了22222222222")
//    }
//
//    fun add()
//    {
//        GlobalScope.launch()
//        {
//            showLoading("加载框文字...2222222")
//            delay(2000)
//            data.age++
//            data.name = "name${data.age}"
//            data.money = data.age.toDouble() * 125
//            // data变动后需要调用update()更新
//            update()
//            cancelLoading()
//            toast("add成功222222")
//        }
//    }
//
//    fun sub()
//    {
//        data.age--
//        data.name = "money${data.age}22222222"
//        data.money = data.age.toDouble() * 125
//        data.sex = !data.sex
//        // data变动后需要调用update()更新
//        update()
//        toast("sub成功2222222")
//    }
//}