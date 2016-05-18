package frament;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import adpter.MonthAdpter;
import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;
import web.Ip;

/**
 * Created by Administrator on 2016/5/18.
 */
public class MonyStatisticByType extends Fragment {
    private View view;
    private ListView listView;
    private MonthAdpter monthAdpter;
    private String time;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {

                case R.id.moth_bill_back_img:
                    onClickBack();
                    break;
                default:
                    return;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.money_stastic_type, null, true);
        loadComponent(view);
        return view;
    }

    private void loadComponent(View paramView) {
        LinearLayout linearLayout = (LinearLayout) paramView.findViewById(R.id.moth_bill_select_time_linear);
        linearLayout.setOnClickListener(listener);
        this.listView = ((ListView) paramView.findViewById(R.id.month_bill_lv));
        ((ImageView) paramView.findViewById(R.id.moth_bill_back_img)).setOnClickListener(this.listener);
    }

    /**
     * @return 月账单json
     */
    private String getDates(String day) {
        String day1;
        if (day != null && day != "") {
            day1 = day;
        } else {
            day1 = BaihuijiNet.getTime("yyyyMM");
        }
        MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        String json = null;
        String[] key = {"merchantId", "month", "rule", "uId"};
        String requst;
        String[] value =

                {
                        applaication.getDate("merchantId"), day1, "ss", applaication.getDate("operateTel")
                };


        // json = BaihuijiNet.getJson(key, value, "month");
        requst = Ip.monthBillString + BaihuijiNet.getRequst(key, value);
        Log.i("MonthBillRequst", requst);
        //this.requst = MonthBill.this.urlconection("http://baihuiji.weikebaba.com/aide/monthBill", this.json);
        requst = connection(requst);
        Log.i("BillMonth", requst + "  \n  " + json);
        if (getSuccess(requst)) {
            // MonthBill.this.getDate(requst);
            return requst;
        } else {
            return null;
        }
    }

    private void showTost(String str) {
        Toast.makeText(getParentFragment().getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 处理返回的json数组
     *
     * @param paramString
     */
    private void getDate(String paramString) {
        View view = getView();
        TextView textView;//显示空间
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(paramString);
            jsonObject = jsonObject.getJSONObject("o2o");
            jsonArray = jsonObject.getJSONArray("detail");
        } catch (JSONException e) {
            e.printStackTrace();
            showTost("没有当月数据");
            return;
        }

        int tId[] = {R.id.month_bill_num1_tx, R.id.month_bill_get1_tx, R.id.month_bill_back1_tx};
        //设置顶部数据
        for (int i = 0; i < tId.length; i++) {
            textView = (TextView) view.findViewById(tId[i]);

            try {
                if (i == 0) {
                    textView.setText((jsonObject.getInt("payNum") + jsonObject.getInt("backNum")) + "");
                } else if (i == 1) {
                    textView.setText(jsonObject.getString("payTotal"));
                } else {
                    textView.setText(jsonObject.getString("backTotal"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                textView.setText("0");
            }
        }
        //listview 适配数据
        HashMap<String, String> map;
        for (int j = 0; j < jsonArray.length(); j++) {
            map = new HashMap<String, String>();
            try {
                jsonObject = jsonArray.getJSONObject(j);
                map.put("payTotal", jsonObject.getString("payTotal"));
                map.put("backTotal", jsonObject.getString("backTotal"));
                map.put("totalDate", jsonObject.getString("totalDate"));
                list.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (monthAdpter != null) {
            monthAdpter.onDateChage(list);
        } else {
            monthAdpter = new MonthAdpter(list, getParentFragment().getActivity());
            listView.setAdapter(monthAdpter);
        }

    }

    /**
     * 判断访问成功
     *
     * @param paramString
     * @return
     */
    private boolean getSuccess(String paramString) {
        JSONObject localJSONObject1 = null;
        try {
            localJSONObject1 = new JSONObject(paramString);

        } catch (JSONException localJSONException2) {
            return false;
        }

        try {
            if (localJSONObject1.getString("errcode").equals("0"))
                return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void onClickBack() {
        ((StatisticHome) getParentFragment()).showFragment(1);
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

            bufferedReader.close();
            streamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "读取流文件失败";
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
            return "讀取流失敗";
        }
        connection.disconnect();
        return stringBuffer.toString();
    }

    //请求地址，由外部传染
    private String requst;

    public void setRequst(String requst) {
        this.requst=requst;
    }

    private class MyAsyn extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String string = strings[0];
            if (string == null || string == "") {
                return getDates("");
            } else {
                return getDates(string);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("MonthBillASy", s + "   onpost");
            if (s != null) {
                if (s.trim().length() > 0)
                    getDate(s);
            }
        }
    }

}

