package com.ly.jsnativetest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity implements MyJSInterfaceImp.JSAndroid {


    private WebView webView;
    private ValueCallback<Uri[]> filePath;
    private ValueCallback<Uri> mUploadMessage;
    private static final int FILECHOOSER_RESULTCODE = 12343;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        webView= (WebView) findViewById(R.id.webView);
        if(webView==null){
            return;
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        MyJSInterfaceImp myJSInterfaceImp=new MyJSInterfaceImp(this);
        myJSInterfaceImp.setJsAndroid(this);
        //在JS中调用本地方法,API>17
        //调用模式为window.interfaceName.方法名()或者是javascript: interfaceName.方法名() ;
        webView.addJavascriptInterface(myJSInterfaceImp, "className");
        webView.loadUrl("file:///android_asset/js.html");
        webView.setWebChromeClient(new WebChromeClient() {
            //For Android 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (filePath != null) {
                    filePath.onReceiveValue(null);
                }
                MainActivity.this.filePath = filePathCallback;
                openFile();
                return true;
            }
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = uploadMsg;
                openFile();
            }
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = uploadMsg;
                openFile();
            }

            // For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = uploadMsg;
                openFile();
            }

        });

    }

    private void openFile() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "选择图片"), FILECHOOSER_RESULTCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILECHOOSER_RESULTCODE){
            if(mUploadMessage==null) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if(result==null){
                mUploadMessage.onReceiveValue(null);
                mUploadMessage=null;
                return;
            }
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    // 此按键监听的是返回键，能够返回到上一个网页（通过网页的hostlistery）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void changeHTML(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:fromJsMethod('"+str+"')");
            }
        });
    }
}
