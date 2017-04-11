package com.example.jkd.biyeshengguanlixitong;

/**
 * Created by jkd on 2015/9/16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class AllPush extends Fragment {
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.all_push, container, false);
        String pid = getArguments().getString("pid");
        String cid = getArguments().getString("cid");
        String requirements = getArguments().getString("requirements");
        WebView webView = (WebView) rootView.findViewById(R.id.webView);
        webView.loadUrl("http://projectscore.sinaapp.com/index.php/Home/Android/getjobmessage?PID="+pid +"&CID="+cid);




        return rootView;
    }
}
