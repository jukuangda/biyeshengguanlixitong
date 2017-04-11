package com.example.jkd.biyeshengguanlixitong;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jkd on 2015/9/4.
 */
public class MainFragment extends Fragment {

    View rootView;
    private ListView listView = null;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private MyAdapter myAdapter;
    private ShowView2 mCallbacks;
    private SwipeRefreshLayout SwipeRefreshLayout;

    //消息部分
    private static final int MSG_CODE = 1001;
    //定义存放数据库信息的变量
    //  int b=1;
    int a;
    ArrayList<String> mid = new ArrayList<String>();
    ArrayList<String> content = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> mal = new ArrayList<String>();
    ArrayList<String> picture = new ArrayList<String>();

    //更新UI

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //接收并处理消息
            if (msg.what == MSG_CODE) {
                //UI更新
                System.out.println(BooleanNetwork.init().isNetworkAvailable(getActivity()));
                if(BooleanNetwork.init().isNetworkAvailable(getActivity())) {
                    initView();
                    setListener();
                    SwipeRefreshLayout.setRefreshing(false);
                }else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mHandler.sendEmptyMessage(1001);
                        }
                    }, 1500); //1500 for release
                }
            }
        }
    };

    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected(position);
            }
        });
    }

    public void selected(int position) {

        System.out.println(position + a);

        mCallbacks.showallpush1(title.get(a - position - 1), time.get(a - position - 1), content.get(a - position - 1),mid.get(a - position -1));
    }

    private void initView() {
        listView = (ListView) rootView.findViewById(R.id.PY);
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return a;
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
            convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.listview_item3, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(title.get(a - position - 1));
            name.setTextColor(Color.BLACK);
            TextView ti = (TextView) convertView.findViewById(R.id.time);
            ti.setText(time.get(a - position - 1));
            ti.setTextColor(Color.BLACK);
            TextView introduce = (TextView) convertView.findViewById(R.id.introduce);
            introduce.setText(content.get(a - position - 1));
            introduce.setTextColor(Color.BLACK);
            //无法显示的文字省略号表示
            introduce.setEllipsize(TextUtils.TruncateAt.END);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.people);
            if (picture.size()!=0) {
                Glide.with(getActivity())
                        .load(picture.get(a - position - 1))
                        .placeholder(R.drawable.loading_spinner)
                        .crossFade()
                        .into(imageView);
            }
            else {
                Glide.with(getActivity())
                        .load(R.drawable.loading_spinner)
                        .into(imageView);
            }
            return convertView;
        }
    }

    public void go() {
        ArrayList nameValuePairs = new ArrayList();
        (new Thread() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                JSONArray jArray;

                String result = new String();

                InputStream is = null;

                StringBuilder sb = null;

                //获取数据
                try {
//                    if(!BooleanNetwork.init().isNetworkAvailable(getActivity())){
//                        Looper.prepare();
//                        Toast.makeText(getActivity(),"当前网络不可用",Toast.LENGTH_LONG).show();
//                        Looper.loop();
//                    }
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://projectscore.sinaapp.com/ThinkPHP/Library/Vendor/Android/message.php");
                    HttpResponse response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    System.out.println("获取成功");
                } catch (Exception e) {
                    Log.e("log_tag", "Error:" + e.toString());
                }
                //将获取数据转成字符串
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "iso-8859-1"), 8);
                    sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                    System.out.println("转字符串成功");
                } catch (Exception e) {
                    Log.e("log_tag", "Error converting result " + e.toString());
                }
                //将字符串输出


                try {

                    jArray = new JSONArray(result);

                    JSONObject json_data = null;
                    a = jArray.length();
                    for (int i = 0; i < jArray.length(); i++) {

                        json_data = jArray.getJSONObject(i);

                        mid.add(i,json_data.getString("MID"));

                        title.add(i,json_data.getString("TITLE"));

                        time.add(i,json_data.getString("TIME"));

                        content.add(i,json_data.getString("CONTENT"));

                        mal.add(i,json_data.getString("MAIL"));

                        picture.add(i,"http://projectscore-uploads.stor.sinaapp.com"+json_data.getString("PICTURE"));

                    }

                } catch (JSONException e1) {

                    // Toast.makeText(getBaseContext(), "No City Found"

                    // ,Toast.LENGTH_LONG).show();

                } catch (ParseException e1) {

                    e1.printStackTrace();

                }

                //子线程发送信息
                Message msg = mHandler.obtainMessage(MSG_CODE);
                msg.sendToTarget();
                    if(!BooleanNetwork.init().isNetworkAvailable(getActivity())){
                        Looper.prepare();
                        Toast.makeText(getActivity(),"当前网络不可用",Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
            }
        }
        ).start();
    }


    private void test() {
        Toast.makeText(getActivity(), "lll", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.main_fragment, container, false);
        go();
        SwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        SwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        SwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
//            SwipeRefreshLayout.setProgressBackgroundColor(android.R.color.holo_green_light);
        //swipeRefreshLayout.setPadding(20, 20, 20, 20);
        //swipeRefreshLayout.setProgressViewOffset(true, 100, 200);
        //swipeRefreshLayout.setDistanceToTriggerSync(50);
        SwipeRefreshLayout.setProgressViewEndTarget(true, 100);
        SwipeRefreshLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        go();
                    }
                }).start();
            }
        });
        return rootView;
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