package frament;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.R;
import views.Sector;
import web.BaihuijiNet;
import web.Ip;

/**
 * 统计主类
 */
public class Trade_statistic extends Fragment {
    private TextView timeText;//时间的Textview
    private String time;//时间
    private LayoutInflater inflater;
    private LinearLayout layoutm, layout2n;
    private Sector sector;
    private TextView textView;
    private View view;
    private TextView view1, view2;
    //总笔数显示数组  0收款，1退款
    private int totalNum[] = new int[2];
    //总金额显示数组
    private float totalMoney[] = new float[2];
    //销量数组,分4种, 第一维收款，第二维 退款
    private int statisticNum[][] = new int[2][4];
    //销售额数组
    private float statisticMoney[][] = new float[2][4];
    //金额统计数据
    private ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
    /**
     * 交易笔数id,前四位为收款，后四位退款
     */
    private int tradNumId[] = {
            R.id.trad_statistic_getMoney11_tx2_0, R.id.trad_statistic_getMoney1_tx2_1,
            R.id.trad_statistic_getMoney2_tx2_2, R.id.trad_statistic_getMoney3_tx2_2,
            R.id.trad_statistic_backMoney1_tx2_0,
            R.id.trad_statistic_backMoney_tx2_1,
            R.id.trad_statistic_backMoney13_tx2_2,
            R.id.trad_statistic_backMoney14_tx2_3
    };
    /**
     * 交易金额id
     */
    private int tradMoneyId[] = {
            R.id.trad_statistic_getMoney11_tx, R.id.trad_statistic_getMoney1_tx,
            R.id.trad_statistic_getMoney2_tx, R.id.trad_statistic_getMoney3_tx,
            R.id.trad_statistic_backMoney1_tx,
            R.id.trad_statistic_backMoney12_tx,
            R.id.trad_statistic_backMoney13_tx,
            R.id.trad_statistic_backMoney14_tx
    };

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        view = paramLayoutInflater.inflate(R.layout.trad_statistic_2, null, true);
        this.inflater = paramLayoutInflater;
        this.sector = ((Sector) view.findViewById(R.id.trad_static_sector_1));
        loadView(view);
        return view;
    }

    private void loadView(View view) {
        //选择门店
        TextView textView = (TextView) view.findViewById(R.id.trad_statistic_select_tx);
        textView.setOnClickListener(listener);
        //收入统计
        textView = (TextView) view.findViewById(R.id.trad_statistic_money_tx);
        textView.setOnClickListener(listener);
        //交易笔数
        textView = (TextView) view.findViewById(R.id.trad_statistic_sales_tx);
        textView.setOnClickListener(listener);
        //收款数
        layoutm = (LinearLayout) view.findViewById(R.id.trad_statistic2_linear1);
        //交易笔数
        layout2n = (LinearLayout) view.findViewById(R.id.trad_statistic2_linear2);
        view1 = (TextView) view.findViewById(R.id.trad_statistic_view);
        view2 = (TextView) view.findViewById(R.id.trad_statistic_view1);

        int[] linerId = {R.id.trad_statistic_linear1, R.id.trad_statistic_linear2,
                R.id.trad_statistic_linear3, R.id.trad_statistic_linear4,
                R.id.trad_statistic_linear5, R.id.trad_statistic_linear6,
                R.id.trad_statistic_linear7, R.id.trad_statistic_linear8,
        };
        LinearLayout linearLayout;
        for (int i = 0; i < linerId.length; i++) {
            linearLayout = (LinearLayout) view.findViewById(linerId[i]);
            linearLayout.setOnClickListener(listener);
        }
        ImageView img = (ImageView) view.findViewById(R.id.trad_statistic_lef_img);
        img.setOnClickListener(listener);
        img = (ImageView) view.findViewById(R.id.trad_statistic_right_img);
        img.setOnClickListener(listener);
        timeText = (TextView) view.findViewById(R.id.trad_statics_time_tx);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.trad_statistic_select_tx:
                    // showDialog();
                    break;
                case R.id.trad_statistic_money_tx:
                    if (list1.size() > 0) {
                        swich();
                    }
                    break;
                case R.id.trad_statistic_sales_tx:
                    if (list1.size() > 0) {
                        swich();
                    }
                    break;
                case R.id.trad_statistic_right_img:
                    onCheckDate(true);
                    break;
                case R.id.trad_statistic_lef_img:
                    onCheckDate(false);
                    break;

                case R.id.trad_statistic_linear1:
                    onStatisticItemClic(1);
                    break;
                case R.id.trad_statistic_linear2:
                    onStatisticItemClic(2);
                    break;
                case R.id.trad_statistic_linear3:
                    onStatisticItemClic(3);
                    break;
                case R.id.trad_statistic_linear4:
                    onStatisticItemClic(4);
                    break;
                case R.id.trad_statistic_linear5:
                    onStatisticItemClic(5);
                    break;
                case R.id.trad_statistic_linear6:
                    onStatisticItemClic(6);
                    break;
                case R.id.trad_statistic_linear7:
                    onStatisticItemClic(7);
                    break;
                case R.id.trad_statistic_linear8:
                    onStatisticItemClic(8);
                    break;

            }
        }
    };

    /**
     * 选择时间的时候
     */
    private void onCheckDate(boolean right) {
        String string[] = timeText.getText().toString().split("-");
        int month = Integer.parseInt(string[1]);
        int year = Integer.parseInt(string[0]);
        if (right) {
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
        } else {
           if(month==1){
               month=12;
               year--;
           }else {
               month--;
           }
        }

    }


    /**
     * @param a 被点击的项
     */
    private void onStatisticItemClic(int a) {
        StringBuffer buffer = new StringBuffer();
        MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();

        buffer.append(Ip.monthstatistic + "?");
        buffer.append("uId=");
        buffer.append(applaication.getDate("operateTel") + "&");
        buffer.append("merchantId");
        buffer.append(applaication.getDate("merchantId") + "&");
        switch (a) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;

        }

    }

    /**
     * 状态切换时,sector传值  b o r g
     */
    private void swich() {
        if (layoutm.getVisibility() == View.VISIBLE) {
            layoutm.setVisibility(View.GONE);
            layout2n.setVisibility(View.VISIBLE);
            view1.setBackgroundColor(getResources().getColor(R.color.white));
            view2.setBackgroundColor(getResources().getColor(R.color.blue));
            sector.setWight(statisticMoney[0][2], statisticMoney[0][1], statisticMoney[0][3], statisticMoney[0][0]);
        } else if (layoutm.getVisibility() == View.GONE) {
            layoutm.setVisibility(View.VISIBLE);
            layout2n.setVisibility(View.GONE);
            view2.setBackgroundColor(getResources().getColor(R.color.white));
            view1.setBackgroundColor(getResources().getColor(R.color.blue));
            sector.setWight(statisticNum[0][2], statisticNum[0][1], statisticNum[0][3], statisticNum[0][0]);
        }
    }

    /**
     * 金额统计数据返回成功判断
     *
     * @param reqst
     * @return
     */
    private boolean getMerchantMonthbillSuccess(String reqst) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(reqst);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        try {
            if (jsonObject.getString("errcode").equals("0")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 选择门店时,获取数据入口,
     *
     * @param merChantId 门店名字
     */
    private void onSelectMerchant(String merChantId) {


        MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        String key[] = {"uId", "merchantId", "month", "rule"};
        String value[] = {applaication.getDate("operateName"), merChantId, BaihuijiNet.getTime("yyyyMM"), "ss"};

        //String json = BaihuijiNet.getJson(key, value, "month");
        String json = Ip.static1 + BaihuijiNet.getRequst(key, value);
        String requst;


        // requst = BaihuijiNet.urlconection(Ip.static1, json);
        requst = connection(json);
        Log.i("OnSelectMerchant", requst + "\n" + json);
        if (getMerchantMonthbillSuccess(requst)) {
            getDate(requst);
        }

    }

    /**
     * 显示三栏时的数据适配，未检测！
     * 设置控件显示的数据，有三个状态
     */
    private void setDate() {
        //显示总额的id,前两位为 金额，后两位为数量
        TextView textView;
        int totalId[] = {R.id.trad_statistic_getMoney_tx, R.id.trad_statistic_backMoney_tx, R.id.trad_statistic_getMoney_tx2, R.id.trad_statistic_backMoney2_tx};
        //交易总数设置
        for (int i = 0; i < totalId.length; i++) {
            textView = (TextView) view.findViewById(totalId[i]);
            if (i < 2) {
                textView.setText(totalMoney[i] + "");
                continue;
            } else {
                textView.setText(totalNum[i - 2] + "笔");
            }
        }
        /**
         * 单项金额显示
         */
        for (int a = 0; a < tradMoneyId.length; a++) {
            textView = (TextView) view.findViewById(tradMoneyId[a]);
            if (a < 4) {
                textView.setText(statisticMoney[0][a] + "");

            } else {
                textView.setText(statisticMoney[0][a - 4] + "");
            }
        }
        /**
         * 单项数量显示
         */
        for (int a = 0; a < tradMoneyId.length; a++) {
            textView = (TextView) view.findViewById(tradNumId[a]);
            if (a < 4) {
                textView.setText(statisticNum[0][a] + "");

            } else {
                textView.setText(statisticNum[0][a - 4] + "");
            }
        }
        sector.setWight(statisticMoney[0][0], statisticMoney[0][1], statisticMoney[0][2], statisticMoney[0][3]);
    }

    /**
     * 获取网络数据后调用，获取金额统计数据
     *
     * @param requst,
     */
    private void getDate(String requst) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(requst);
            jsonArray = jsonObject.getJSONArray("o2o");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        HashMap<String, String> map;
        for (int i = 0; i < jsonArray.length(); i++) {
            map = new HashMap<String, String>();
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                map.put("payType", jsonObject.getString("payType"));
                map.put("payTotal", jsonObject.getString("payTotal"));
                map.put("payNum", jsonObject.getString("payNum"));
                map.put("backTotal", jsonObject.getString("backTotal"));
                map.put("backNum", jsonObject.getString("backNum"));
                list1.add(map);

                if (jsonObject.getString("payType").equals("1")) {
                    //金额获取
                    statisticMoney[0][0] = statisticMoney[0][0] + getMone(jsonObject.getString("payTotal"));
                    statisticMoney[1][0] = statisticMoney[1][0] + getMone(jsonObject.getString("backTotal"));
                    //数量统计
                    statisticNum[0][0] = statisticNum[0][0] + getNum(jsonObject.getString("payNum"));
                    statisticNum[1][0] = statisticNum[1][0] + getNum(jsonObject.getString("backNum"));

                } else if (jsonObject.getString("payType").equals("2")) {
                    statisticMoney[0][1] = statisticMoney[0][1] + getMone(jsonObject.getString("payTotal"));
                    statisticMoney[1][1] = statisticMoney[1][1] + getMone(jsonObject.getString("backTotal"));

                    statisticNum[0][1] = statisticNum[0][1] + getNum(jsonObject.getString("payNum"));
                    statisticNum[1][1] = statisticNum[1][1] + getNum(jsonObject.getString("backNum"));
                } else if (jsonObject.getString("payType").equals("3")) {
                    statisticMoney[0][2] = statisticMoney[0][2] + getMone(jsonObject.getString("payTotal"));
                    statisticMoney[1][2] = statisticMoney[1][2] + getMone(jsonObject.getString("backTotal"));

                    statisticNum[0][2] = statisticNum[0][2] + getNum(jsonObject.getString("payNum"));
                    statisticNum[1][2] = statisticNum[1][2] + getNum(jsonObject.getString("backNum"));
                } else if (jsonObject.getString("payType").equals("4")) {
                    statisticMoney[0][3] = statisticMoney[0][3] + getMone(jsonObject.getString("payTotal"));
                    statisticMoney[1][3] = statisticMoney[1][3] + getMone(jsonObject.getString("backTotal"));

                    statisticNum[0][3] = statisticNum[0][3] + getNum(jsonObject.getString("payNum"));
                    statisticNum[1][3] = statisticNum[1][3] + getNum(jsonObject.getString("backNum"));
                }
                //总金额
                totalMoney[0] = totalMoney[0] + getMone(jsonObject.getString("payTotal"));
                totalMoney[1] = totalMoney[1] + getMone(jsonObject.getString("backTotal"));

                totalNum[0] = totalNum[0] + getNum(jsonObject.getString("payNum"));
                totalNum[1] = totalNum[1] + getNum(jsonObject.getString("backNum"));
            } catch (JSONException e) {
                e.printStackTrace();
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

    private void onshow() {
        MyApplaication application = (MyApplaication) getParentFragment().getActivity().getApplication();
        //传入门店名
        onSelectMerchant(application.getDate("merchantId"));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            new MyAsy().execute("");
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
            return "读取流文件失败";
        }
        connection.disconnect();
        return stringBuffer.toString();
    }

    /**
     * 获取到数据后存到全局不用下传
     */
    private class MyAsy extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            onshow();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (list1.size() > 0) {
                setDate();
            }
        }
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Trade_statistic
 * JD-Core Version:    0.6.2
 */