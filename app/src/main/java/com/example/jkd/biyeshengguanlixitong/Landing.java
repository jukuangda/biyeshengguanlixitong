package com.example.jkd.biyeshengguanlixitong;

/**
 * Created by jkd on 2015/9/16.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Landing extends Fragment {
    private View rootView;
    private ShowView mCallback;
    private int tt=0;//防止登陆界面跳转错乱
    private SharedPreferences per;
    private SharedPreferences.Editor editor;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.landing_fragment, container, false);

//        per = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final EditText etphone = (EditText) rootView.findViewById(R.id.lphone);
        final EditText etpassword = (EditText) rootView.findViewById(R.id.lpassword);
//        editor = per.edit();

        rootView.findViewById(R.id.zc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.landingToRegister();
            }
        });

        rootView.findViewById(R.id.ToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("phone",etphone.getText().toString());
                editor.putString("password",etpassword.getText().toString());
                editor.commit();
                PY();
            }
        });

        return rootView;
    }

    public android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
                if(msg.what==0&&tt==0) {
                    tt=1;
                    mCallback.showMainFragment();
                }
                if(msg.what==1) {
                    Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
    };

    public void PY() {
        final ArrayList nameValuePairs = new ArrayList();
        EditText etphone = (EditText) rootView.findViewById(R.id.lphone);
        final Data PHONE = (Data) getActivity().getApplication();
        PHONE.SetPHONE(etphone.getText().toString());
        nameValuePairs.add(new BasicNameValuePair("phone", etphone.getText().toString()));
        (new Thread() {
            @Override
            public void run() {
                JSONArray jArray;
                String result = new String();
                InputStream is = null;
                StringBuilder sb = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://projectscore.applinzi.com/index.php/Home/Android/login2/");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection" + e.toString());
                }
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "iso-8859-1"), 8);
                    sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                    //System.out.println(result);
                    System.out.println("转字符串成功");
                } catch (Exception e) {
                    Log.e("log_tag", "Error converting result " + e.toString());
                }
                try {
                    System.out.println(result);
                    jArray = new JSONArray(result);

                    JSONObject json_data;

                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        EditText etpassword = (EditText) rootView.findViewById(R.id.lpassword);
                        Message msg = new Message();
                        msg.what=1;
                        if (json_data.getString("PASSWORD").equals(etpassword.getText().toString())) {
                            msg.what = 0;
                            System.out.println("登录成功");
                        }
                        System.out.println("zhixingle");
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e1) {
                    // Toast.makeText(getBaseContext(), "No City Found"

                    // ,Toast.LENGTH_LONG).show();

                } catch (org.apache.http.ParseException e1) {

                    e1.printStackTrace();

                }
                if(!BooleanNetwork.init().isNetworkAvailable(getActivity())){
                    Looper.prepare();
                    Toast.makeText(getActivity(),"当前网络不可用",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        }).start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

//        menu.add(0, 1, 0, "注册").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        per = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = per.edit();
        try {
            mCallback = (ShowView) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement showCompletionRegister");
        }
    }

}
