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

import adpter.DayCoutAdpter;
import adpter.MonyStatisticByTypeAdpter;
import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.R;

/**
 * 交易笔数的，子项点击进入
 * Created by Administrator on 2016/5/18.
 */
public class TradeStatisticSaleDayCount extends Fragment {
    private int payType = 0;

    private View view;
    private ListView listView;
    private DayCoutAdpter dayCoutAdpter;
    private String time;
    private TextView year;
    private TextView month;
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
        view = inflater.inflate(R.layout.num_statistic_type, null, true);
        loadComponent(view);
        return view;
    }

    private void loadComponent(View paramView) {
        LinearLayout linearLayout = (LinearLayout) paramView.findViewById(R.id.moth_bill_select_time_linear);
        linearLayout.setOnClickListener(listener);
        this.listView = ((ListView) paramView.findViewById(R.id.month_bill_lv));
        ((ImageView) paramView.findViewById(R.id.moth_bill_back_img)).setOnClickListener(this.listener);
        year = (TextView) paramView.findViewById(R.id.month_bill_year_tx);
        month = (TextView) paramView.findViewById(R.id.month_bill_moth_tx);
    }

    /**
     * @return 月账单json
     */
    private String getDates(String requst) {
        Log.i("BillMonth0", "传人的地址    " + requst);
        //String json;
        requst = connection(requst + "&payType=" + payType);
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
        if (list != null) {
            list.removeAll(list);
        }

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
        int payTotle = 0;//收款数
        int backTotle = 0;//退款数
        int totel;

        //listview 适配数据
        HashMap<String, String> map;
        for (int j = 0; j < jsonArray.length(); j++) {
            map = new HashMap<String, String>();
            try {
                jsonObject = jsonArray.getJSONObject(j);
                map.put("payTotal", jsonObject.getString("payNum"));
                map.put("backTotal", jsonObject.getString("backNum"));
                map.put("totalDate", jsonObject.getString("totalDate"));

                payTotle = payTotle + getNum(jsonObject.getString("payNum"));
                backTotle = backTotle + getNum(jsonObject.getString("backNum"));
                list.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
        if (dayCoutAdpter == null) {

            dayCoutAdpter = new DayCoutAdpter(payType, getParentFragment().getActivity(), list);
            listView.setAdapter(dayCoutAdpter);
        }
        int tId[] = {R.id.month_bill_num1_tx, R.id.month_bill_get1_tx, R.id.month_bill_back1_tx};
        //设置顶部数据
        if (time != null) {
            year.setText(time.substring(0, 4) + "年");
            month.setText(time.substring(4, 6) + "月");
        }
        for (int i = 0; i < tId.length; i++) {
            textView = (TextView) view.findViewById(tId[i]);

            if (i == 0) {
                textView.setText(payTotleNum);
            } else if (i == 1) {
                textView.setText(payTotle + "");
            } else {
                textView.setText(backTotle + "");
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
    private String payTotleNum;

    public void setRequst(String requst, int payType, String time, String payTotleNumber) {
        this.requst = requst;
        Log.i("NumStatistic_requst", requst);
        this.payType = payType;
        this.time = time;
        this.payTotleNum = payTotleNumber;
        HomPage homPage= (HomPage) getParentFragment().getActivity();
        homPage.showProgress();
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
            HomPage homPage= (HomPage) getParentFragment().getActivity();
            homPage.dialogCancle();
            if (s != null) {
                if (s.trim().length() > 0)
                    getDate(s);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && dayCoutAdpter != null) {

            list.removeAll(list);
            dayCoutAdpter.setDate(payType, list);
        }
    }
}
