package com.example.jkd.biyeshengguanlixitong;

/**
 * Created by jkd on 2015/9/16.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private ShowView mCallback;
    private View rootView;
    private String phone;
    private int yzjd=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.register, container, false);
        rootView.findViewById(R.id.next).setOnClickListener(this);
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        final TextView description=(TextView) rootView.findViewById(R.id.seektext);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                description.setText("拖动停止");
                if (seekBar.getProgress()==100){
                    description.setText("验证成功!");
                    yzjd=seekBar.getProgress();
                    rootView.findViewById(R.id.next).setEnabled(true);
                }else if(seekBar.getProgress()<yzjd){
                    seekBar.setProgress(100);
                    description.setText("验证成功!");
                    rootView.findViewById(R.id.next).setEnabled(true);
                }else{
                    seekBar.setProgress(0);
                    yzjd=0;
                    description.setText("验证失败!");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                description.setText("开始拖动");
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress==100){
                    rootView.findViewById(R.id.next).setEnabled(true);
                }else if(progress<yzjd){

                }else {
                    description.setText("已验证：" + progress + "%");
                }
            }
        });
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                EditText eet = (EditText) rootView.findViewById(R.id.phone);
                phone = eet.getText().toString();
                final Data PHONE = (Data) getActivity().getApplication();
                PHONE.SetPHONE(phone);
                if(eet.getText().toString().length()>0) {
                    mCallback.showCompletionRegister(eet.getText().toString());
                }else {
                    Toast.makeText(getActivity(), "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (ShowView) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement showCompletionRegister");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCallback.registerBackToLanding();
    }
}