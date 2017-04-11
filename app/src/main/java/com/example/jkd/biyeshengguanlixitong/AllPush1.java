package com.example.jkd.biyeshengguanlixitong;

/**
 * Created by jkd on 2015/10/21.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class AllPush1 extends Fragment {
    TextView bt;
    TextView sj;
    TextView zw;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.all_push1, container, false);
        String title = getArguments().getString("title");
        String time = getArguments().getString("time");
        String content = getArguments().getString("content");
        String MID = getArguments().getString("MID");
        WebView webView = (WebView) rootView.findViewById(R.id.webView1);
        webView.loadUrl("http://projectscore.applinzi.com/index.php/Home/Android/getrenshimessage?MID=" + MID);
        return rootView;
    }
}

