package com.zoke.neb.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by wulijie on 2017/12/26.
 */
public class PersistTool {
    private PersistTool() {
    }

    private static final String PREFERENCES_DEFAULT_NAME = "neb";
    private static SharedPreferences mPreference = null;
    private static boolean isInit = false;

    /**
     * 初始化用户偏好工具类
     *
     * @param context 上下文环境
     */
    public synchronized static void init(Context context) {
        if (mPreference == null) {
            mPreference = context.getSharedPreferences(
                    PREFERENCES_DEFAULT_NAME, Context.MODE_PRIVATE);
        }
        isInit = true;
    }

    /**
     * 初始化用户偏好工具类
     *
     * @param context         上下文环境
     * @param preferencesName 偏好文件名 不传则使用默认名
     */
    public synchronized static void init(Context context, String preferencesName) {
        if (mPreference == null) {
            if (TextUtils.isEmpty(preferencesName)) {
                preferencesName = PREFERENCES_DEFAULT_NAME;
            }
            mPreference = context.getSharedPreferences(preferencesName,
                    Context.MODE_PRIVATE);
        }
        isInit = true;
    }

    /**
     * 是否初始化
     *
     * @return
     */
    public static boolean IsInited() {
        return isInit;
    }

    /**
     * 保存boolean值
     *
     * @param name
     * @param value
     */
    public static void saveBoolean(String name, boolean value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    /**
     * 获取boolean值
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(String name, boolean defaultValue) {
        return mPreference.getBoolean(name, defaultValue);
    }

    /**
     * 保存long值
     *
     * @param name
     * @param value
     */
    public static void saveLong(String name, long value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    /**
     * 获取long值
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public static long getLong(String name, long defaultValue) {
        return mPreference.getLong(name, defaultValue);
    }

    /**
     * 保存int值
     *
     * @param name
     * @param value
     */
    public static void saveInt(String name, int value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    /**
     * 获取int值
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public static int getInt(String name, int defaultValue) {
        return mPreference.getInt(name, defaultValue);
    }

    /**
     * 保存float值
     *
     * @param name
     * @param value
     */
    public static void saveFloat(String name, float value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putFloat(name, value);
        editor.commit();
    }

    /**
     * 获取float值
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public static float getFloat(String name, float defaultValue) {
        return mPreference.getFloat(name, defaultValue);
    }

    /**
     * 保存字符串
     *
     * @param name
     * @param value
     * @return
     */
    public static boolean saveString(String name, String value) {
        boolean flag = false;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(name, value);
        flag = editor.commit();
        return flag;
    }

    /**
     * 获取字符串
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public static String getString(String name, String defaultValue) {
        return mPreference.getString(name, defaultValue);
    }

    /**
     * 清空所有用户偏好
     *
     * @return
     */
    public static boolean clear() {
        return mPreference.edit().clear().commit();
    }
}
