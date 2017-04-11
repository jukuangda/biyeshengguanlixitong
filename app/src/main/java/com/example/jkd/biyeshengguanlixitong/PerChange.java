package com.example.jkd.biyeshengguanlixitong;

/**
 * Created by jkd on 2015/9/16.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class PerChange extends Fragment {
    String hname = new String();
    String hxb = new String();
    String hIdcard = new String();
    String hschool = new String();
    String hgrade = new String();
    private View rootView;
    private Button xgFinsh;
    private EditText name;
    private EditText  xb;
    private EditText  Idcard;
    private EditText school;
    private EditText grade;
    private Handler mHandler = new Handler(){
      @Override
      public void handleMessage(Message msg){
          if(msg.what==1){
              if(hname!="null") {
                  name = (EditText) rootView.findViewById(R.id.xgName);
                  name.setText(hname);
              }
              if (hxb!="null") {
                  xb = (EditText) rootView.findViewById(R.id.xgStudent);
                  xb.setText(hxb);
              }
              if (hIdcard!="null") {
                  Idcard = (EditText) rootView.findViewById(R.id.xgIdcard);
                  Idcard.setText(hIdcard);
              }
              if (hschool!="null") {
                  school = (EditText) rootView.findViewById(R.id.xgSchool);
                  school.setText(hschool);
              }
              if(hgrade!="null") {
                  grade = (EditText) rootView.findViewById(R.id.xgGrade);
                  grade.setText(hgrade);
              }
              textChange tc1 = new textChange();
              name.addTextChangedListener(tc1);
              grade.addTextChangedListener(tc1);
              school.addTextChangedListener(tc1);
              xb.addTextChangedListener(tc1);
              Idcard.addTextChangedListener(tc1);
          }
      }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.per_change, container, false);
        xgFinsh = (Button) rootView.findViewById(R.id.xgFinsh);
        name =  (EditText) rootView.findViewById(R.id.xgName);
        xb=  (EditText) rootView.findViewById(R.id.xgStudent);
        Idcard =  (EditText) rootView.findViewById(R.id.xgIdcard);
        school =  (EditText) rootView.findViewById(R.id.xgSchool);
        grade = (EditText) rootView.findViewById(R.id.xgGrade);
        hq();
        rootView.findViewById(R.id.xgFinsh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xg();
                getActivity().finish();
            }
        });

        return rootView;
    }
    //EditText监听器

    class textChange implements TextWatcher {

        @Override

        public void afterTextChanged(Editable arg0) {

        }

        @Override

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,

                                      int arg3) {

        }

        @Override

        public void onTextChanged(CharSequence cs, int start, int before,

                                  int count) {

            boolean Sign1 = name.getText().length() > 0;

            boolean Sign2 = grade.getText().length() > 0;

            boolean Sign3 = xb.getText().length() > 0;

            boolean Sign4 = school.getText().length() > 0;

            boolean Sign5 = Idcard.getText().length() > 0;

            if (Sign1 & Sign2 & Sign3 & Sign4 & Sign5) {

                xgFinsh.setText("修改完成");

                xgFinsh.setEnabled(true);

            }

//在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的

            else {

                xgFinsh.setText("修改信息不允许留空");

                xgFinsh.setEnabled(false);

            }
        }
    }
            @Override
    public void onResume(){
        super.onResume();
        if(name.getText().toString().length()!=0&&xb.getText().toString().length()!=0&&Idcard.getText().toString().length()!=0&&school.getText().toString().length()!=0&&grade.getText().toString().length()!=0){
            rootView.findViewById(R.id.xgFinsh).setEnabled(true);
        }
    }
    public void hq(){
        final ArrayList nameValuePairs = new ArrayList();
        final Data PHONE = (Data) getActivity().getApplication();
        nameValuePairs.add(new BasicNameValuePair("PHONE", PHONE.GetPHONE()));
        (new Thread() {
            @Override
            public void run() {
                JSONArray jArray;
                String result = new String();
                InputStream is = null;
                StringBuilder sb = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://projectscore.applinzi.com/index.php/Home/Android/fanhuiyonghuxinxi");
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

                    JSONObject json_data = null;

                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        hname=json_data.getString("NAME");
                        hxb=json_data.getString("STUDENTINFO");
                        hIdcard=json_data.getString("IDCARD");
                        hschool=json_data.getString("SCHOOL");
                        hgrade=json_data.getString("GRADE");
                    }

                } catch (JSONException e1) {

                    // Toast.makeText(getBaseContext(), "No City Found"

                    // ,Toast.LENGTH_LONG).show();

                } catch (org.apache.http.ParseException e1) {

                    e1.printStackTrace();

                }
                Message msg = mHandler.obtainMessage(1);
                msg.sendToTarget();
                if(!BooleanNetwork.init().isNetworkAvailable(getActivity())){
                    Looper.prepare();
                    Toast.makeText(getActivity(),"当前网络不可用",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }

        }).start();
    }
    public void xg(){
        final ArrayList nameValuePairs = new ArrayList();
        final Data PHONE = (Data) getActivity().getApplication();
        nameValuePairs.add(new BasicNameValuePair("PHONE", PHONE.GetPHONE()));
        EditText name =  (EditText) rootView.findViewById(R.id.xgName);
        nameValuePairs.add(new BasicNameValuePair("NAME",name.getText().toString()));
        EditText  xb=  (EditText) rootView.findViewById(R.id.xgStudent);
        nameValuePairs.add(new BasicNameValuePair("STUDENTINFO",xb.getText().toString()));
        EditText Idcard =  (EditText) rootView.findViewById(R.id.xgIdcard);
        nameValuePairs.add(new BasicNameValuePair("IDCARD",Idcard.getText().toString()));
        EditText school =  (EditText) rootView.findViewById(R.id.xgSchool);
        nameValuePairs.add(new BasicNameValuePair("SCHOOL",school.getText().toString()));
        EditText grade = (EditText) rootView.findViewById(R.id.xgGrade);
        nameValuePairs.add(new BasicNameValuePair("GRADE",grade.getText().toString()));
        (new Thread() {
            @Override
            public void run() {
                JSONArray jArray;
                String result = new String();
                InputStream is = null;
                StringBuilder sb = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://projectscore.applinzi.com/index.php/Home/Android/xiugaiyonghuxinxi");
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

                    JSONObject json_data = null;

//                    for (int i = 0; i < jArray.length(); i++) {
//                        json_data = jArray.getJSONObject(i);
//
//                    }
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
}