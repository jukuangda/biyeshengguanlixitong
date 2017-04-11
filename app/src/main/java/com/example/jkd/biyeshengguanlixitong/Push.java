package com.example.jkd.biyeshengguanlixitong;

/**
 * Created by jkd on 2015/9/16.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Push extends Fragment {
    View rootView;
    private ListView listView = null;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private MyAdapter myAdapter;
    private ShowView2 mCallbacks;
    private SwipeRefreshLayout SwipeRefreshLayout;
    private SwipeRefreshLayout EmptySwipeRefreshLayout;
    //消息部分
    private static final int MSG_CODE = 1001;
    //定义存放数据库信息的变量
    //  int b=1;
    int a=0;
    ArrayList<String> pid = new ArrayList<String>();
    ArrayList<String> cid = new ArrayList<String>();
    ArrayList<String> jobname = new ArrayList<String>();
    ArrayList<String> duty = new ArrayList<String>();
    ArrayList<String> requirements = new ArrayList<String>();
    ArrayList<String> keyword = new ArrayList<String>();
    ArrayList<String> salary = new ArrayList<String>();
    ArrayList<String> degree = new ArrayList<String>();
    ArrayList<String> experience = new ArrayList<String>();
    ArrayList<String> dateline = new ArrayList<String>();
    ArrayList<String> mail = new ArrayList<String>();
    ArrayList<String> school = new ArrayList<String>();
    ArrayList<String> workplace = new ArrayList<String>();
    ArrayList<String> logo = new ArrayList<String>();

    //更新UI
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //接收并处理消息
            if (msg.what == 1001) {
                //UI更新
                System.out.println(BooleanNetwork.init().isNetworkAvailable(getActivity()));
                if(BooleanNetwork.init().isNetworkAvailable(getActivity())) {
                    myAdapter.notifyDataSetChanged();
                    if (a != 0) {
                        SwipeRefreshLayout.setVisibility(View.VISIBLE);
                        SwipeRefreshLayout.setRefreshing(false);
                        EmptySwipeRefreshLayout.setVisibility(View.GONE);
                        EmptySwipeRefreshLayout.setRefreshing(false);
                    } else {
                        SwipeRefreshLayout.setVisibility(View.GONE);
                        SwipeRefreshLayout.setRefreshing(false);
                        EmptySwipeRefreshLayout.setVisibility(View.VISIBLE);
                        EmptySwipeRefreshLayout.setRefreshing(false);
                    }
                }else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mHandler.sendEmptyMessage(1001);
                        }
                    }, 10000); //1500 for release

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

        mCallbacks.showPush(pid.get(a - position - 1), cid.get(a - position - 1));
    }

    private void initView() {
        listView = (ListView) rootView.findViewById(R.id.listView);
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
            name.setText(jobname.get(a - position - 1));
            name.setTextColor(Color.BLACK);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            time.setText(dateline.get(a - position - 1));
            time.setTextColor(Color.BLACK);
            TextView introduce = (TextView) convertView.findViewById(R.id.introduce);
            introduce.setText("我们需要有" + experience.get(a - position - 1) + "工作经验的人。" + "在" + workplace.get(a - position - 1) + "工作," + "月薪" + salary.get(a - position - 1) + "。");
            introduce.setTextColor(Color.BLACK);
            introduce.setEllipsize(TextUtils.TruncateAt.END);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.people);
            if(logo.size()!=0) {
                Glide.with(getActivity())
                        .load(logo.get(a - position - 1))
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
      public  void go(){
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
//                      if(!BooleanNetwork.init().isNetworkAvailable(getActivity())){
//                          Looper.prepare();
//                          Toast.makeText(getActivity(),"当前网络不可用",Toast.LENGTH_LONG).show();
//                          Looper.loop();
//                      }
                      HttpClient httpclient = new DefaultHttpClient();
                      HttpPost httppost = new HttpPost("http://projectscore.applinzi.com/index.php/Home/Android/findjobmessageforposition/");
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
                      System.out.println("转字符串成功");
                  } catch (Exception e) {
                      Log.e("log_tag", "Error converting result " + e.toString());
                  }
                  try {

                      jArray = new JSONArray(result);

                      JSONObject json_data = null;
                      a = jArray.length();
                      for (int i = 0; i < jArray.length(); i++) {
                          json_data = jArray.getJSONObject(i);
                        pid.add(i,json_data.getString("PID"));
                        cid.add(i,json_data.getString("CID"));
                        jobname.add(i,json_data.getString("JOBNAME"));
                        requirements.add(i,json_data.getString("REQUIREMENTS"));
                        keyword.add(i,json_data.getString("KEYWORD"));
                        salary.add(i,json_data.getString("SALARY"));
                        degree.add(i,json_data.getString("DEGREE"));
                        experience.add(i,json_data.getString("EXPERIENCE"));
                        dateline.add(i,json_data.getString("DATELINE"));
                        mail.add(i,json_data.getString("MAIL"));
                        school.add(i,json_data.getString("SCHOOL"));
                        workplace.add(i,json_data.getString("WORKPLACE"));
                        logo.add(i,json_data.getString("LOGO"));
                      }
                  } catch (JSONException e1) {

                      // Toast.makeText(getBaseContext(), "No City Found"

                      // ,Toast.LENGTH_LONG).show();

                  } catch (org.apache.http.ParseException e1) {

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
          }).start();
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.push_fragment, container, false);
        go();
        initView();
        setListener();
        SwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        SwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        SwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
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

        EmptySwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayoutEmptyView);
        EmptySwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        EmptySwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        EmptySwipeRefreshLayout.setProgressViewEndTarget(true, 100);
        EmptySwipeRefreshLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
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

        TextView emView = (TextView) rootView.findViewById(R.id.empty);
        emView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.showdz();
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
