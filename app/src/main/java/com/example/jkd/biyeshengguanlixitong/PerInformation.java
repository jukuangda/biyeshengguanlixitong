package com.example.jkd.biyeshengguanlixitong;

/**
 * Created by jkd on 2015/9/16.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;

public class PerInformation extends Fragment {
    String name = "游客";
    private View rootView;
    private ShowView2 mCallbacks;
    private ListView list = null;
    private void initlist(){
        list = (ListView) rootView.findViewById(R.id.PerPY);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mCallbacks.showPerInformation();
            }
        });
        SimpleAdapter sa = new SimpleAdapter(
                getActivity(),
                getData(),
                R.layout.listview_item,
                new String[]{"photo", "name"},
                new int[]{R.id.cover_user_photo, R.id.user_name}
        );
        list.setAdapter(sa);
        sa.notifyDataSetChanged();
    }

    private Handler gxname = new Handler(){
        @Override
        public  void handleMessage(Message msg){
            if( msg.what ==0 ){
                initlist();
            }
        }
    };
    private void hqyonghu(){
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
                        if(json_data.getString("NAME")!="null"){
                            name=json_data.getString("NAME");
                        }
                            Message msg = new Message();
                            msg.what = 0;
                            gxname.sendMessage(msg);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.per_information, container, false);

        initlist();
        hqyonghu();

        ListView list2 = (ListView) rootView.findViewById(R.id.PerPY2);

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectItem(position);
            }
        });
        MyAdapter myAdapter = new MyAdapter();
//        SimpleAdapter saa = new SimpleAdapter(
//                getActivity(),
//                getData2(),
//                R.layout.listview_item2,
//                new String[]{"title"},
//                new int[]{R.id.textView01}
//        );

        list2.setAdapter(myAdapter);

        return rootView;
    }
    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.listview_item2,null);
            TextView title = (TextView) convertView.findViewById(R.id.textView01);
            switch(position){
                case 0:
                    title.setText("个人信息修改");
                    title.setTextColor(Color.BLACK);
                    break;
                case 1:
                    title.setText("定制信息修改");
                    title.setTextColor(Color.BLACK);
                    break;
                case 2:
                    title.setText("退出当前账号");
                    title.setTextColor(Color.BLACK);
                    break;
                case 3:
                    title.setText("退出应用程序");
                    title.setTextColor(Color.WHITE);
                    convertView.setBackgroundColor(Color.RED);
                    break;
            }
            return convertView;
        }
    }
    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("photo", R.drawable.lalala);
        map.put("name", name);
        list.add(map);

        return list;
    }

//    private ArrayList<HashMap<String, Object>> getData2() {
//        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("title", "个人信息修改");
//        list.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("title", "定制信息修改");
//        list.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("title", "退出当前账号");
//        list.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("title", "退出应用程序");
//        list.add(map);
//
//        return list;
//    }

    private void SelectItem(int position) {
        if(position==0){
            mCallbacks.showPerInformation();
        }
        if(position==1){
            mCallbacks.showdz();
        }
        if(position==2){
            SharedPreferences per = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = per.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent();
            intent.setClass(getActivity(), Main.class);
            startActivity(intent);
            getActivity().finish();
        }
        if(position==3){
            SysApplication.getInstance().exit();
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (ShowView2) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

}
