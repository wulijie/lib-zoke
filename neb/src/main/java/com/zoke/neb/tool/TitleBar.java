package com.zoke.neb.tool;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoke.neb.R;
import com.zoke.neb.base.BaseActivity;
import com.zoke.neb.base.BaseFragment;

import org.xutils.common.util.LogUtil;

/**
 * Created by wulijie on 2018/6/4.
 * 先将 include_title.xml  include 到布局中
 */
public class TitleBar {

    private BaseActivity activity;
    private BaseFragment fragment;

    private TitleBar() {
    }

    public TitleBar(BaseActivity activity) {
        this.activity = activity;
    }

    public TitleBar(BaseFragment fragment) {
        this.fragment = fragment;
    }

    //设置title
    public TitleBar setTitle(CharSequence text) {
        TextView tv_title = getActivity().findViewById(R.id.tv_title);
        tv_title.setText(text);
        return this;
    }

    public TitleBar setTitle(@StringRes int stringId) {
        String title = getActivity().getResources().getString(stringId);
        setTitle(title);
        return this;
    }

    /**
     * 设置左侧的点击按钮
     *
     * @param resId
     * @param clickListener 传null是退出当前activity
     */
    public TitleBar setLeft(@DrawableRes int resId, View.OnClickListener clickListener) {
        ImageView iv_left = getActivity().findViewById(R.id.iv_left);
        if (iv_left == null) {
            LogUtil.e("include_title.xml is not include");
            return this;
        }
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setBackgroundResource(resId);
        if (clickListener != null)
            iv_left.setOnClickListener(clickListener);
        if (clickListener == null) {
            iv_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();//执行退出
                }
            });
        }
        return this;
    }

    public TitleBar setRight(@DrawableRes int resId, View.OnClickListener clickListener) {
        ImageView iv_right = getActivity().findViewById(R.id.iv_right);
        if (iv_right == null) {
            LogUtil.e("include_title.xml is not include");
            return this;
        }
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setBackgroundResource(resId);
        if (clickListener != null)
            iv_right.setOnClickListener(clickListener);
        return this;
    }

    private Activity getActivity() {
        if (activity == null && fragment != null) {
            return fragment.context;
        }
        return activity;
    }
}
