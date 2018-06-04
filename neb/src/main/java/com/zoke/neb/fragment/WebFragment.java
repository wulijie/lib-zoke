package com.zoke.neb.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zoke.neb.R;
import com.zoke.neb.base.BaseActivity;
import com.zoke.neb.base.BaseFragment;

import org.xutils.common.util.LogUtil;

/**
 * Created by wulijie on 2018/6/4.
 * app 内浏览器
 */
public class WebFragment extends BaseFragment {

    private WebView webView;
    private ProgressBar progressBar;
    private String url;

    /**
     * 开启页面
     *
     * @param activity        加载这个页面的activity
     * @param containerViewId 要覆盖的id
     * @param url             加载的url
     * @return
     */
    public static WebFragment open(@NonNull BaseActivity activity, @IdRes int containerViewId, @NonNull String url) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(containerViewId, fragment);
        return fragment;
    }


    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(R.layout.f_web);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progress);
        url = getArguments().getString("url");
        webView.loadUrl(url);
        initWebView();
        initEvents();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setVisibility(View.VISIBLE);
    }

    protected void initEvents() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, android.net.http.SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.i("url=" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                LogUtil.e("errorCode=" + errorCode + ",description=" + description);
                //界面加载失败 需要处理下界面
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String t = view.getTitle();
                if (!TextUtils.isEmpty(t)) {
                    if (webTitleListener != null)
                        webTitleListener.onTitle(t);
                }
            }
        });
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    public interface WebTitleListener {
        void onTitle(String title);
    }

    public WebTitleListener webTitleListener;

    public void setWebTitleListener(WebTitleListener webTitleListener) {
        this.webTitleListener = webTitleListener;
    }

    //
    public ProgressBar getProgressBar() {
        return progressBar;
    }

}
