package com.loe.mvvm.component;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.loe.mvvm.R;

/**
 * App级别toast
 * （不受禁止通知影响）
 */
public class DefaultToast
{
    private Activity activity;

    private ViewGroup layout;
    private ViewGroup content;
    private TextView textView;

    private Animation startAnimation;
    private Animation centerAnimation;
    private Animation endAnimation;

    private DelayTask task;
    private boolean isShow;

    private LayoutParams params;

    /**
     * APP级别Toast
     */
    public DefaultToast(Activity activity)
    {
        this.activity = activity;

        layout = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.default_toast, null);
        content = (ViewGroup) layout.getChildAt(0);
        textView = (TextView) content.getChildAt(0);
        params = new LayoutParams();
        params.height = LayoutParams.WRAP_CONTENT;
        params.width = LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.TOP;
        params.type = LayoutParams.TYPE_APPLICATION;
        params.format = PixelFormat.TRANSLUCENT;
        params.flags =
                LayoutParams.FLAG_KEEP_SCREEN_ON | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;

        layout.setVisibility(View.GONE);

        // 开始动画
        startAnimation = new AlphaAnimation(0, 1);
        startAnimation.setDuration(500);

        // 中间动画
        centerAnimation = new AlphaAnimation(0.92f, 1);
        centerAnimation.setDuration(500);

        // 结束动画
        endAnimation = new AlphaAnimation(1, 0);
        endAnimation.setDuration(500);
        endAnimation.setInterpolator(new AccelerateInterpolator());

        // 结束动画监听
        endAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                layout.setVisibility(View.GONE);
                DefaultToast.this.activity.getWindowManager().removeView(layout);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        });
    }

    /**
     * 显示Toast
     */
    public void show(Object s)
    {
        show(s + "", 1000);
    }

    /**
     * 显示Toast
     */
    public void show(Object s, int delay)
    {
        if (s != null && !s.toString().isEmpty())
        {
            try
            {
                activity.getWindowManager().addView(layout, params);
            } catch (Exception e)
            {
            }
            textView.setText(s + "");
            start();
            if (task != null)
            {
                task.stop();
            }
            task = new DelayTask(delay)
            {
                @Override
                public void run()
                {
                    end();
                }
            };
            task.start();
        }
    }

    /**
     * 开始
     */
    private void start()
    {
        if (!isShow)
        {
            layout.setVisibility(View.VISIBLE);
            content.startAnimation(startAnimation);
            isShow = true;
        }
        else
        {
            content.startAnimation(centerAnimation);
        }
    }

    /**
     * 结束
     */
    private void end()
    {
        content.startAnimation(endAnimation);
        isShow = false;
    }

    abstract class DelayTask extends Handler
    {
        private long delay = 0L;

        public DelayTask(long delay)
        {
            super(Looper.getMainLooper());
            this.delay = delay;
        }

        public DelayTask()
        {
            super(Looper.getMainLooper());
        }

        private Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                DelayTask.this.run();
            }
        };

        public void start()
        {
            if (delay == 0L)
            {
                post(runnable);
            }
            else
            {
                postDelayed(runnable, delay);
            }
        }

        public void stop()
        {
            removeCallbacks(runnable);
        }

        protected abstract void run();
    }
}