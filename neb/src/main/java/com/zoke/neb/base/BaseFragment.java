package com.zoke.neb.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.zoke.neb.tool.TitleBar;

import org.xutils.x;

public class BaseFragment extends Fragment {
    private LayoutInflater inflater;
    private View contentView;
    private ViewGroup container;
    public BaseActivity context;
    protected boolean mIsVisible;

    protected boolean mIsPrepare;

    protected ImmersionBar mImmersionBar;

    protected TitleBar mTitleBar;

    @Override
    public void onAttach(Context context) {
        this.context = (BaseActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        onCreateView(savedInstanceState);
        if (contentView == null)
            return super.onCreateView(inflater, container, savedInstanceState);
        return contentView;
    }

    protected void onCreateView(Bundle savedInstanceState) {
        if (isImmersionBarEnabled())
            mImmersionBar = ImmersionBar.with(this);
        mTitleBar = new TitleBar(this);
    }

    protected boolean isImmersionBarEnabled() {
        return true;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isLazyLoad()) {
            mIsPrepare = true;
            onLazyLoad();
        } else {
            initData();
        }
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return (T) getContentView().findViewById(id);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null && isImmersionBarEnabled())
            mImmersionBar.init();
    }


    public void setContentView(int layoutResID) {
        setContentView(inflater.inflate(layoutResID, container, false));
    }

    public void setContentView(View view) {
        contentView = view;
        x.view().inject(this, contentView);
    }

    public View getContentView() {
        return contentView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    protected boolean isLazyLoad() {
        return false;
    }

    protected void onVisible() {
        onLazyLoad();
    }

    private void onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false;
            initData();
        }
    }

    protected void initData() {
    }

    protected void onInvisible() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isImmersionBarEnabled())
            if (mImmersionBar != null)
                mImmersionBar.destroy();
        contentView = null;
        container = null;
        inflater = null;
    }
}
