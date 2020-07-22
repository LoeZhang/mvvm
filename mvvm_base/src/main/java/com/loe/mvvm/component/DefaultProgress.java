package com.loe.mvvm.component;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.loe.mvvm.R;

/**
 * Created by zls on 2016/6/4.
 */
public class DefaultProgress
{
    private String text = "加载中...";
    private AlertDialog dialog;

    public DefaultProgress(Context context)
    {
        dialog = new AlertDialog.Builder(context).create();
    }

    /**
     * 给Dialog设置提示信息
     */
    public DefaultProgress setText(String text)
    {
        this.text = text;
        return this;
    }

    public void show()
    {
        if (!dialog.isShowing())
        {
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.default_progress);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ((TextView) window.findViewById(R.id.textView)).setText(text);
        }
    }

    public void show(String msg)
    {
        if (!dialog.isShowing())
        {
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.default_progress);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ((TextView) window.findViewById(R.id.textView)).setText(msg);
        }
    }

    public void show(DialogInterface.OnDismissListener listener)
    {
        if (!dialog.isShowing())
        {
            dialog.setOnDismissListener(listener);
            show();
        }
    }

    public void cancel()
    {
        dialog.cancel();
    }

    public boolean isShowing()
    {
        return dialog.isShowing();
    }
}
