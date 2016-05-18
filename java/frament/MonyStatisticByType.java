package frament;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import adpter.MonyStatisticByTypeAdpter;
import baihuiji.jkqme.baihuiji.R;

/**
 * 统计部分的金额统计被点击后的frgment
 * Created by Administrator on 2016/5/18.
 */
public class MonyStatisticByType extends Fragment {
    private int payType=0;

    private View view;
    private ListView listView;
    private MonyStatisticByTypeAdpter monthAdpter;
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
    private String getDates(String requst) {
        Log.i("BillMonth0", "传人的地址    " + requst);
        //String json;
        requst = connection(requst);
        Log.i("BillMonth", requst);
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
            jsonArray = jsonObject.getJSONArray("o2o");
        } catch (JSONException e) {
            e.printStackTrace();
            showTost("没有当月数据");
            return;
        }
        float payTotle = 0;//收款数
        float backTotle = 0;//退款数
        int totel;
        //listview 适配数据
        HashMap<String, String> map;
        for (int j = 0; j < jsonArray.length(); j++) {
            map = new HashMap<String, String>();
            try {
                jsonObject = jsonArray.getJSONObject(j);
                map.put("payTotal", jsonObject.getString("payTotal"));
                map.put("backTotal", jsonObject.getString("backTotal"));
                map.put("totalDate", jsonObject.getString("totalDate"));

                payTotle = payTotle + getMone(jsonObject.getString("payTotal"));
                backTotle = backTotle + getMone(jsonObject.getString("backTotal"));
                list.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (monthAdpter == null) {

            monthAdpter = new MonyStatisticByTypeAdpter(payType, getParentFragment().getActivity(), list);
            listView.setAdapter(monthAdpter);
        }
        int tId[] = {R.id.month_bill_num1_tx, R.id.month_bill_get1_tx, R.id.month_bill_back1_tx};
        //设置顶部数据
        for (int i = 0; i < tId.length; i++) {
            textView = (TextView) view.findViewById(tId[i]);

            try {
                if (i == 0) {
                    textView.setText((jsonObject.getInt("payNum") + jsonObject.getInt("backNum")) + "");
                } else if (i == 1) {
                    textView.setText(jsonObject.getString(payTotle + ""));
                } else {
                    textView.setText(jsonObject.getString(backTotle + ""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                textView.setText("0");
            }
        }


    }

    /**
     * 计算单种方式获得的金额
     *
     * @param pay
     * @return
     */
    private float getMone(String pay) {
        if (pay == null) {
            return 0f;
        } else {
            return Float.parseFloat(pay);
        }
    }

    private int getNum(String pay) {
        if (pay == null) {
            return 0;
        } else {
            return Integer.parseInt(pay);
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

    public void setRequst(String requst,int payType,String time,String payTotleNumber) {
        this.requst = requst;
        Log.i("MONyStatistic_requst",requst);
        this.payType=payType;
        this.time=time;
        new MyAsyn().execute(requst);
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
            Log.i("MonthBillASy", s + "   onpost");
            if (s != null) {
                if (s.trim().length() > 0)
                    getDate(s);
            }
        }
    }

}

