package com.loe.test

import android.content.Intent
import android.os.Bundle
import androidx.annotation.Keep
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loe.mvvm.data.BaseDataActivity
import com.loe.mvvm.ModelEvent
import kotlinx.android.synthetic.main.activity_model.*

class TestActivity : BaseDataActivity<TestModel, TestData>()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model)

        initList()
        initEvent()

        bindView()
    }

    private fun bindView()
    {
        model.singleData.bind { textSingle.text = "独立信息2222：$it" }
    }

    override fun onData(data: TestData)
    {
        textName.text = "姓名：${data.name}"
        textAge.text = "年龄：${data.age}"
    }

    private fun initList()
    {
        recyclerView.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        recyclerView.adapter = model.adapter
    }

    @Keep
    fun goHome()
    {
        toast("我是反射调用的方法！！！")
    }

    private fun initEvent()
    {
        button.setOnClickListener()
        {
            /**** 在model处理逻辑（推荐）****/
            model.add()
            model.singleData.value = "${data.age}"

            /**** 直接在activity处理逻辑 ****/
//            GlobalScope.launch()
//            {
//                showLoading("加载框文字...")
//                delay(1500)
//                data.age++
//                data.name = "name${data.age}"
//                data.money = data.age.toDouble() * 125
//                // data变动后需要调用update()更新
//                update()
//                cancelLoading()
//                toast("add成功")
//            }
        }

        buttonNew.setOnClickListener()
        {
            startActivity(Intent(activity, TestActivity::class.java))
        }
    }

    override fun onEvent(event: ModelEvent): Boolean
    {
        when (event.type)
        {
            "toNew" ->
            {
                toast(event.getStringData())
                startActivity(Intent(activity, TestActivity::class.java))
                return true
            }
        }
        return super.onEvent(event)
    }

    /** 网络全部断开 */
    override fun onNetAllLost()
    {
        toast("网络断开了")
    }
}
