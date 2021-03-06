package com.lilosoft.jhapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends Activity {

    private WebView mWebView;
    private String url = "http://jh.whsp.gov.cn/WeChatJH/index.html";
//    private String url = "http://jh.whsp.gov.cn/WeChatJH/app/oauth/toIndex.action?client_id=null";

    private JsInterface JSInterface2 = new JsInterface(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWebView();
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webView);

        WebSettings webViewSettings = mWebView.getSettings();
        webViewSettings.setJavaScriptEnabled(true); // enable java script
        mWebView.addJavascriptInterface(JSInterface2, JSInterface2.getInterface());
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setUseWideViewPort(true);

//        webViewSettings.setDatabaseEnabled(true);
//        webViewSettings.setGeolocationEnabled(true);
//        String dir = this.getCacheDir()+"/baidudata";
//        webViewSettings.setGeolocationDatabasePath(dir);
//        webViewSettings.setDomStorageEnabled(true);
//        webViewSettings.setJavaScriptEnabled(true);// 允许请求JS
//        webViewSettings.setBuiltInZoomControls(true);

        setlocalStorge();
        MyWebChromeClient myWebChromeClient = new MyWebChromeClient();
        mWebView.setWebChromeClient(myWebChromeClient);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        mWebView.loadUrl(url);
    }

    public void setlocalStorge(){
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);


    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog,
                                      boolean userGesture, Message resultMsg) {
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        /**
         * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);

            builder.setTitle("对话框").setMessage(message)
                    .setPositiveButton("确定", null);

            // 不需要绑定按键事件
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    Log.i("onJsAlert", "keyCode==" + keyCode + "event=" + event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            return true;
            // return super.onJsAlert(view, url, message, result);
        }

        public boolean onJsBeforeUnload(WebView view, String url,
                                        String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        /**
         * 67. * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////” 68.
         */
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    view.getContext());
            builder.setTitle("对话框").setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    Log.v("onJsConfirm", "keyCode==" + keyCode + "event="
                            + event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsConfirm(view, url, message, result);
        }

        /**
         * 108. * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////” 109. *
         * window.prompt('请输入您的域名地址', '618119.com'); 110.
         */
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, final JsPromptResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    view.getContext());

            builder.setTitle("对话框").setMessage(message);

            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            builder.setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm(et.getText().toString());
                }

            }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    Log.v("onJsPrompt", "keyCode==" + keyCode + "event="
                            + event);
                    return true;
                }
            });

            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsPrompt(view, url, message, defaultValue,
            // result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //if (System.currentTimeMillis() - exitTime > 1500) {
        if (mWebView.canGoBack()) {

            String url = mWebView.getUrl();
            if (url.contains("app/oauth/toIndex.action")){
                android.os.Process.killProcess(android.os.Process.myPid());
            }else{
                mWebView.goBack();
            }
        }/*else {
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            }
            exitTime = System.currentTimeMillis();
        } */
        else {
            this.finish();
        }
    }
}
