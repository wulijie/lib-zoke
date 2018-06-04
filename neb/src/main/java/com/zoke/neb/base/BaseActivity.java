package com.zoke.neb.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.zoke.neb.tool.TitleBar;

import org.xutils.x;

public class BaseActivity extends AppCompatActivity {

    protected ImmersionBar mImmersionBar;

    protected TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mTitleBar = new TitleBar(this);
        if (isImmersionBarEnabled())
            initImmersionBar();
    }

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    protected void onDestroy() {
        if (mImmersionBar != null) mImmersionBar.destroy();
        super.onDestroy();
    }
}
