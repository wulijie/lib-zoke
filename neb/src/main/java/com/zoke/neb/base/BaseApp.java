package com.zoke.neb.base;

import android.app.Application;
import android.util.Log;

import com.zoke.neb.R;
import com.zoke.neb.tool.PersistTool;
import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

/**
 * Created by wulijie on 2018/6/4.
 */
public abstract class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(isDebug()); // 是否输出debug日志, 开启debug会影响性能.
        PersistTool.init(this, getResources().getString(R.string.app_name));
        Recovery.getInstance()
                .debug(isDebug())
                .recoverInBackground(false)
                .recoverStack(isDebug())
//                .mainPage(MainActivity.class)
                .recoverEnabled(isDebug())
                .callback(new MyCrashCallback())
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .init(this);

    }

    public abstract boolean isDebug();


    static final class MyCrashCallback implements RecoveryCallback {
        @Override
        public void stackTrace(String exceptionMessage) {
            LogUtil.e("exceptionMessage:" + exceptionMessage);
        }

        @Override
        public void cause(String cause) {
            Log.e("zxy", "cause:" + cause);
        }

        @Override
        public void exception(String exceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {
            LogUtil.e("exceptionClassName:" + exceptionType);
            LogUtil.e("throwClassName:" + throwClassName);
            LogUtil.e("throwMethodName:" + throwMethodName);
            LogUtil.e("throwLineNumber:" + throwLineNumber);
        }

        @Override
        public void throwable(Throwable throwable) {

        }
    }


}
