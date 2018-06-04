package com.zoke.neb.tool;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by wulijie on 2018/1/8.
 */
public class UToast {
    public static void show(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, @StringRes int strRes) {
        String str = context.getResources().getString(strRes);
        show(context, str);
    }
}
