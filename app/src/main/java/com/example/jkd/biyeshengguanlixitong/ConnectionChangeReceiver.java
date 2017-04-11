package com.example.jkd.biyeshengguanlixitong;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by ju on 16/5/22.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            Toast.makeText(context,"网络链接不可用",Toast.LENGTH_LONG).show();
            //改变背景或者 处理网络的全局变量
        }else {
            //改变背景或者 处理网络的全局变量
        }
    }
}
