package com.ly.jsnativetest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * author: ly
 * data: 2016/6/2
 */
public class MyJSInterfaceImp  {

    private Context context;

    public MyJSInterfaceImp(Context context) {
        this.context = context;
    }

    private int count=0;

    @JavascriptInterface
    public void getSthFromJS(final String str){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                count++;
                jsAndroid.changeHTML("调用次数："+count);
                Toast.makeText(context, "getSthFromJS：" + str, Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + str));
                context.startActivity(intent);
            }
        });

    }

    private JSAndroid jsAndroid;

    /**
     * @param jsAndroid
     */
    public void setJsAndroid(JSAndroid jsAndroid){
        this.jsAndroid=jsAndroid;
    }

    public interface JSAndroid{
        void changeHTML(String str);
    }

}
