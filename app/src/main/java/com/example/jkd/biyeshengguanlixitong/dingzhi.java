package com.example.jkd.biyeshengguanlixitong;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class dingzhi extends Fragment {
    private static final int MSG_CODE = 1001;
    View rootView;
    private String dclStr = new String();
    GridView gridView = null;
    ListView listView = null;
    private MyAdapter myAdapter;
    private MyAdapter1 myAdapter1;
    ArrayList<String> dz_g = new ArrayList<String>();
    ArrayList<String> dz_l = new ArrayList<String>();
    ArrayList<String> hq = new ArrayList<String>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //接收并处理消息
            if (msg.what == MSG_CODE) {
                //UI更新
                dz_g.remove(" ");
                dz_g.addAll(hq);
                dz_g.add(" ");
                dz_l.removeAll(hq);
                initview();
                setListener();
            }
        }
    };
    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_l(position);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dz_g.size()!=position+1) {
                    selected_g(position);
                }
            }
        });
    }

    public void selected_l(int position) {
        dz_g.remove(" ");
        dz_g.add(dz_l.get(position));
        dz_l.remove(position);
        dz_g.add(" ");
        myAdapter1.notifyDataSetChanged();
        myAdapter.notifyDataSetChanged();
    }
    public void selected_g(int position) {
        dz_l.add(dz_g.get(position));
        dz_g.remove(position);
        myAdapter1.notifyDataSetChanged();
        myAdapter.notifyDataSetChanged();
    }
    private void go() {
        dz_g.add(" ");
        dz_l.add("软件工程系");
        dz_l.add("土木工程系");
        dz_l.add("外语系");
        dz_l.add("艺术系");
        dz_l.add("建筑工程系");
        dz_l.add("经济管理系");
        dz_l.add("机械工程系");
        dz_l.add("电气工程系");
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

                        dclStr=json_data.getString("JOB1");
                    }
                    if(dclStr.length()>0&&dclStr!="null") {
                        dclStr = dclStr.substring(0, dclStr.length() - 1);
                        String[] str = dclStr.split("\\|");
                        hq.clear();
                        for (int i = 0; i < str.length; i++) {
                            hq.add(str[i]);
                        }
                    }
                } catch (JSONException e1) {

                    // Toast.makeText(getBaseContext(), "No City Found"

                    // ,Toast.LENGTH_LONG).show();

                } catch (org.apache.http.ParseException e1) {

                    e1.printStackTrace();

                }
                Message msg = mHandler.obtainMessage(MSG_CODE);
                msg.sendToTarget();
                if(!BooleanNetwork.init().isNetworkAvailable(getActivity())){
                    Looper.prepare();
                    Toast.makeText(getActivity(),"当前网络不可用",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        }).start();
    };

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dz_g.size();
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
            convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.grideview_item, null);
            TextView name = (TextView) convertView.findViewById(R.id.ItemText);
            name.setText(dz_g.get(position));
            name.setTextColor(Color.BLACK);
            return convertView;

        }
    }
    private class MyAdapter1 extends BaseAdapter {
        @Override
        public int getCount() {
            return dz_l.size();
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
            convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.dz_listview_item, null);
            TextView name = (TextView) convertView.findViewById(R.id.ItemText1);
            name.setText(dz_l.get(position));
            name.setTextColor(Color.BLACK);
            return convertView;
        }
    }
    private void initview() {
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        listView = (ListView) rootView.findViewById(R.id.listView);
        myAdapter = new MyAdapter();
        myAdapter1 = new MyAdapter1();
        gridView.setAdapter(myAdapter);
        listView.setAdapter(myAdapter1);
        myAdapter.notifyDataSetChanged();
        myAdapter1.notifyDataSetChanged();
    }

    public dingzhi() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dingzhi, container, false);
        go();
        Button tj = (Button) rootView.findViewById(R.id.tj);
        tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String job = new String();
                for(int i=0;i<dz_g.size()-1;i++){
                    if (job == null){
                        job = dz_g.get(i)+"|";
                    }else {
                        job = job + dz_g.get(i) +"|";
                    }
                }
                if (dz_g.size() != 0) {
                    Tag[] tagParam = new Tag[dz_g.size() - 1];
                    for (int i = 0; i < dz_g.size() - 1; i++) {
                        Tag t = new Tag();
                        t.setName(dz_g.get(i));
                        tagParam[i] = t;
                    }
                    int i = PushManager.getInstance().setTag(getActivity(), tagParam);
                    String text = "设置标签失败,未知异常";
                    switch (i) {
                        case PushConsts.SETTAG_SUCCESS:
                            text = "设置标签成功";
                            break;

                        case PushConsts.SETTAG_ERROR_COUNT:
                            text = "设置标签失败, tag数量过大, 最大不能超过200个";
                            break;

                        case PushConsts.SETTAG_ERROR_FREQUENCY:
                            text = "设置标签失败, 频率过快, 两次间隔应大于1s";
                            break;

                        case PushConsts.SETTAG_ERROR_REPEAT:
                            text = "设置标签失败, 标签重复";
                            break;

                        case PushConsts.SETTAG_ERROR_UNBIND:
                            text = "设置标签失败, 服务未初始化成功";
                            break;

                        case PushConsts.SETTAG_ERROR_EXCEPTION:
                            text = "设置标签失败, 未知异常";
                            break;

                        case PushConsts.SETTAG_ERROR_NULL:
                            text = "设置标签失败, tag 为空";
                            break;

                        default:
                            break;
                    }
                    System.out.println(text);
                }
                xgjob(job);
                getActivity().finish();
            }
        });
        return rootView;
    }
    public void xgjob(String job){
        final ArrayList nameValuePairs = new ArrayList();
        final Data PHONE = (Data) getActivity().getApplication();
        nameValuePairs.add(new BasicNameValuePair("PHONE", PHONE.GetPHONE()));
        nameValuePairs.add(new BasicNameValuePair("JOB1",job));
//        nameValuePairs.add(new BasicNameValuePair("JOB1","1"));
        (new Thread() {
            @Override
            public void run() {
                JSONArray jArray;
                String result = new String();
                InputStream is = null;
                StringBuilder sb = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://projectscore.applinzi.com/index.php/Home/Android/xiugaiyonghujob");
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
