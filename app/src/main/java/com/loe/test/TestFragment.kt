package com.loe.test

import android.os.Bundle
import com.loe.mvvm.data.BaseDataFragment
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment : BaseDataFragment<TestModel, TestData>()
{
    override fun getLayout() = R.layout.fragment_test

    override fun onCreated(savedInstanceState: Bundle?)
    {
        initEvent()
    }

    override fun onData(data: TestData)
    {
        textSex.text = "性别：${data.sex}"
        textMoney.text = "财富：${data.money}"
    }

    private fun initEvent()
    {
        button.setOnClickListener()
        {
            /**** 在model处理逻辑（推荐）****/
            model.sub()

            /**** 直接在fragment处理逻辑 ****/
//            data.age--
//            data.name = "money${data.age}"
//            data.money = data.age.toDouble() * 125
//            data.sex = !data.sex
//            // data变动后需要调用update()更新
//            update()
//            toast("sub成功3333333")
        }
    }

    /** 网络全部断开 */
    override fun onNetAllLost()
    {
        toast("网络断开了2222")
    }
}
