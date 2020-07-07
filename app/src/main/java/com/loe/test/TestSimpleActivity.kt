package com.loe.test

import android.os.Bundle
import com.loe.mvvm.simple.SimpleActivity
import kotlinx.android.synthetic.main.activity_simple.*

class TestSimpleActivity : SimpleActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        button.setOnClickListener()
        {
            toast("撒大声地")
        }
    }
}
