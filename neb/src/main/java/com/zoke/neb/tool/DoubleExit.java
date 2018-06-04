package com.zoke.neb.tool;

import android.app.Activity;

import com.zoke.neb.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wulijie on 2018/1/20.
 */
public class DoubleExit {
    private Activity activity;
    private boolean mDoubleExit;

    private static DoubleExit doubleExit;


    private DoubleExit(Activity activity) {
        this.activity = activity;

    }


    /**
     * 绑定要操作的界面
     *
     * @param activity
     * @return
     */
    public static DoubleExit getInstance(Activity activity) {
        if (doubleExit == null) doubleExit = new DoubleExit(activity);
        return doubleExit;
    }


    public void start() {
        if (mDoubleExit) {
            activity.finish();
            //执行双击退出app操作
            activity = null;
        } else {
            mDoubleExit = true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mDoubleExit = false;
                }
            }, 2000);
            // 提示的内容让子类去实现
            if (activity != null)
                UToast.show(activity, R.string.double_exit);
        }
    }


}
