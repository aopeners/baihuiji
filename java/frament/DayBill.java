package frament;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import adpter.BillAdpter;
import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;
import web.Ip;

/**
 * Created by Administrator on 2016/5/18.
 * 点击月账单子项后进入的,需要从外部传入数据后访问网络
 */
public class DayBill extends Fragment {
    private ListView listView;
    private TextView textView;
    private ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String, String>>();
    private BillAdpter billAdpter;
    private String time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daybill, null, true);
        listView = (ListView) view.findViewById(R.id.daybill_lv);
        listView.setOnItemClickListener(lvListener);
        textView = (TextView) view.findViewById(R.id.daybill_tx);
        textView.setOnClickListener(listener);
        ImageView imageView = (ImageView) view.findViewById(R.id.count_back);
        imageView.setOnClickListener(listener);
        return view;
    }

    private AdapterView.OnItemClickListener lvListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            clickIten((HashMap<String, String>) listView.getAdapter().getItem(i));
        }
    };

    /**
     * 当子项被点击时调用
     *
     * @param map
     */
    private void clickIten(HashMap<String, String> map) {
        ((Bill) getParentFragment()).showBillDetail(map);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.daybill_tx:
                    break;
                case R.id.count_back:
                    showMonthBill();
                    break;
            }
        }
    };

    private void showMonthBill() {
        Bill bill = (Bill) getParentFragment();
        bill.showFragment(1);
    }

    /**
     * 通过外部传入的时间访问网络
     *
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
        Log.i("DayBill_setTime", time + "  ");
        ((HomPage)getParentFragment().getActivity()).showProgress();
        new MyAsyn().execute(time);
    }


    private boolean getSuccess(String paramString) {
        JSONObject localJSONObject1 = null;
        try {
            localJSONObject1 = new JSONObject(paramString);

        } catch (JSONException localJSONException2) {
            return false;
        }
        try {
            if (localJSONObject1.getString("errcode").equals("0")) {
                return true;
            }
        } catch (JSONException localJSONException1) {
            return false;
        }

        return false;
    }

    /**
     * 先获得所有账单，再分离出日账单
     *
     * @param paramString
     */
    private void getDate(String paramString) {
        list.removeAll(list);
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(paramString);
            jsonArray = jsonObject.getJSONArray("o2o");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        //listview 适配数据
        HashMap<String, String> map;
        for (
                int j = 0;
                j < jsonArray.length(); j++)

        {
            map = new HashMap<String, String>();
            try {
                jsonObject = jsonArray.getJSONObject(j);
                if (isSlecteDay(jsonObject.getString("payTime"))) {
                    map.put("payTime", jsonObject.getString("payTime"));
                    map.put("payType", jsonObject.getString("payType"));
                    map.put("singal", jsonObject.getString("singal"));
                    map.put("ordState", jsonObject.getString("ordState"));
                    map.put("ordPrice", jsonObject.getString("ordPrice"));
                    map.put("backTime", jsonObject.getString("backTime"));
                    list.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (billAdpter == null)

        {
            billAdpter = new BillAdpter(list, getParentFragment().getActivity());
            listView.setAdapter(billAdpter);
        } else

        {
            billAdpter.setList(list);
        }
    }

    /**
     * 判断是否是选择的时间
     *
     * @param day
     * @return
     */
    private boolean isSlecteDay(String day) {
        if (day == null) {
            return false;
        }
        String str = day.replace("-", "");
        return str.startsWith(time);
    }

    private String getDates(String time) {

        MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        String json;
        String[] key = {"merchantId", "page", "pagesize", "rule", "uId"};

        String requst;
        String[] value =
                {applaication.getDate("merchantId"), "1", "30", "ss", applaication.getDate("operateTel")
                };


        requst = "?" + "merchantId=" + applaication.getDate("merchantId") + "&page=1" + "&pagesize=20" + "&rule=ss" + "&uId=" + applaication.getDate("operateTel");
        json = BaihuijiNet.getJson(key, value, "uId");
        requst = Ip.thirtyDetail + BaihuijiNet.getRequst(key, value);
        Log.i("getDate", requst);
        requst = connection(requst);
        Log.i("BillRequst", requst + "  \n  ");
        if (getSuccess(requst)) {
            return requst;
        } else {
            return null;
        }

    }

    /**
     * get方法获取数据
     *
     * @param url
     * @return
     */
    private String connection(String url) {
        StringBuffer stringBuffer = new StringBuffer();
        URL urls = null;
        HttpURLConnection connection;
        try {
            urls = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "网络格式错误";
        }
        try {
            connection = (HttpURLConnection) urls.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return "链接失败";
        }
        try {
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            connection.setInstanceFollowRedirects(true);
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "未知错误";
        }
        try {
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String inputLine = null;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            streamReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return stringBuffer.toString();
    }

    private class MyAsyn extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String string = strings[0];
            return getDates(string);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ((HomPage)getParentFragment().getActivity()).dialogCancle();
            if (s != null) {
                if (s.trim().length() > 0)
                    getDate(s);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden&&billAdpter!=null){
            list.removeAll(list);
            billAdpter.setList(list);
        }
    }
}
