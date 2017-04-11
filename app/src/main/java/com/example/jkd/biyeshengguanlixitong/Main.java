package com.example.jkd.biyeshengguanlixitong;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class Main extends ActionBarActivity implements ShowView {
    private String phone;
    private String password;
    private String ip;
    private ActionBar actionbar;
    private SharedPreferences per;
    private ViewPager mViewPager;
    private String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        actionbar = getSupportActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        per = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        System.out.println(per.getString("Phone", ""));

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_main);
            actionbar.setTitle("登陆");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new Landing())
                    .commit();
            if(per.getString("phone","null")!="null") {
                PY();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void landingToRegister() {
        actionbar.setTitle("手机注册");
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new RegisterFragment())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void registerBackToLanding() {
        actionbar.setTitle("登陆");
        actionbar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void showCompletionRegister(String st) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CompletionRegister())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        phone = st;
    }

    @Override
    public void showMainFragment() {
        setContentView(R.layout.activity_main_activity2);
        Intent i = new Intent(this, MainActivity2.class);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }

    public void go() {
        final ArrayList nameValuePairs = new ArrayList();
        nameValuePairs.add(new BasicNameValuePair("PHONE", phone));
        nameValuePairs.add(new BasicNameValuePair("PASSWORD", password));
        (new Thread() {
            @Override
            public void run() {
                JSONArray jArray;
                String result = new String();
                InputStream is = null;
                StringBuilder sb = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://projectscore.applinzi.com/index.php/Home/Android/zhuce/");
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
                    a=sb.toString();
                    a=a.substring(0,1);
                    //System.out.println(result);
                    System.out.println("转字符串成功");
                } catch (Exception e) {
                    Log.e("log_tag", "Error converting result " + e.toString());
                }
                try {
                    System.out.println(result);
                    jArray = new JSONArray(result);

                    JSONObject json_data = null;

                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
//                        a=json_data.getString("a");
                        System.out.println(json_data.getString("a"));
//                        final Data zczt = (Data) getApplication();
//                        zczt.SetZCZT(a);
                        //跳转识别没做
                    }
                } catch (JSONException e1) {

                    // Toast.makeText(getBaseContext(), "No City Found"

                    // ,Toast.LENGTH_LONG).show();

                } catch (org.apache.http.ParseException e1) {

                    e1.printStackTrace();

                }
                Message msg = mHandler.obtainMessage(Integer.parseInt(a));
                msg.sendToTarget();
                if(!BooleanNetwork.init().isNetworkAvailable(Main.this)){
                    Looper.prepare();
                    Toast.makeText(Main.this,"当前网络不可用",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //接收并处理消息
            if (msg.what == 0) {
                Toast.makeText(Main.this, "手机号以被注册", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 1){
                showMainFragment();
            }
        }
    };
    @Override
    public void CompletionRegisterCallbake(String st) {
        password = st;
        go();
//        if(a.equals("1")) {
//            showMainFragment();
//        }else {
//            Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
//        }
    }

    public void PY() {
        final ArrayList nameValuePairs = new ArrayList();
        final Data PHONE = (Data) this.getApplication();
        PHONE.SetPHONE(per.getString("phone","null"));
        nameValuePairs.add(new BasicNameValuePair("phone", per.getString("phone","null")));
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
                        Message msg = new Message();
                        msg.what=3;
                        System.out.println("jkljkljkljlkjl"+per.getString("password","null").toString());
                        if (json_data.getString("PASSWORD").equals(per.getString("password","null").toString())) {
                            msg.what = 1;
                            System.out.println("登录成功");
                        }
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e1) {
                    // Toast.makeText(getBaseContext(), "No City Found"

                    // ,Toast.LENGTH_LONG).show();

                } catch (org.apache.http.ParseException e1) {

                    e1.printStackTrace();

                }
                if(!BooleanNetwork.init().isNetworkAvailable(Main.this)){
                    Looper.prepare();
                    Toast.makeText(Main.this,"当前网络不可用",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        }).start();
    }
}
