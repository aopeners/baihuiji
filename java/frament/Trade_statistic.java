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
import java.math.BigDecimal;
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
    private LinearLayout secorlayout1, sectorlayout2;
    private int month;//本月份
    private int year;//本年份
    private TextView timeText;//时间的Textview
    private String time;//时间
    private LayoutInflater inflater;
    private LinearLayout layoutm, layout2n;
    private Sector sector, sector1;
    private TextView textView;
    private View view;
    private TextView view1, view2;
    //总笔数显示数组  0收款，1退款
    private int totalNum[] = new int[2];
    //总金额显示数组
    private String totalMoney[] = new String[2];
    //销量数组,分4种, 第一维收款，第二维 退款
    private int statisticNum[][] = new int[2][4];
    //销售额数组
    private String statisticMoney[][] = new String[2][4];
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
        this.sector = (Sector) view.findViewById(R.id.trad_static_sector_1);
        sector1 = (Sector) view.findViewById(R.id.trad_static_sector_2);
        loadView(view);
        return view;
    }

    private void loadView(View view) {
        month = Integer.parseInt(BaihuijiNet.getTime("MM"));
        year = Integer.parseInt(BaihuijiNet.getTime("yyyy"));
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
        //时间选择
        ImageView img = (ImageView) view.findViewById(R.id.trad_statistic_lef_img);
        img.setOnClickListener(listener);
        img = (ImageView) view.findViewById(R.id.trad_statistic_right_img);
        img.setOnClickListener(listener);
        timeText = (TextView) view.findViewById(R.id.trad_statics_time_tx);
        for (int a = 0; a < totalNum.length; a++) {

            totalNum[a] = 0;
            totalMoney[a] = "0";
            for (int b = 0; b < statisticNum[0].length; b++) {
                statisticNum[a][b] = 0;
                statisticMoney[a][b] = "0";
            }
        }

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
                        swich(true);
                    }
                    break;
                case R.id.trad_statistic_sales_tx:
                    if (list1.size() > 0) {
                        swich(false);
                    }
                    break;
                case R.id.trad_statistic_right_img:
                    onCheckDate(true);
                    break;
                case R.id.trad_statistic_lef_img:
                    onCheckDate(false);
                    break;

                case R.id.trad_statistic_linear1:
                    onStatisticItemClic(1, "1");
                    break;
                case R.id.trad_statistic_linear2:
                    onStatisticItemClic(2, "2");
                    break;
                case R.id.trad_statistic_linear3:
                    onStatisticItemClic(3, "3");
                    break;
                case R.id.trad_statistic_linear4:
                    onStatisticItemClic(4, "4");
                    break;
                case R.id.trad_statistic_linear5:
                    onStatisticItemClic(5, "1");
                    break;
                case R.id.trad_statistic_linear6:
                    onStatisticItemClic(6, "2");
                    break;
                case R.id.trad_statistic_linear7:
                    onStatisticItemClic(7, "3");
                    break;
                case R.id.trad_statistic_linear8:
                    onStatisticItemClic(8, "4");
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
        //改变时间
        if (right) {
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
        } else {
            if (month == 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
        }
        //时间纠错,年份为本年，月份大于本月
        if (month > this.month && year == this.year) {
            month = this.month;
        }

        timeText.setText(year + "-" + month);
        if (month < 10) {
            time = year + "0" + month;
        } else {
            time = year + "" + month;
        }
        new MyAsy().execute(time);
    }


    /**
     * 当子项被点击时，新成访问字符串
     *
     * @param a 被点击的项
     */
    private void onStatisticItemClic(int a, String paytype) {
        StringBuffer buffer = new StringBuffer();
        MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        int payType = 0;
        int payTotle = 0;

        switch (a) {
            case 1:
                buffer.append(Ip.monthBillString + "?");//这一段是需要判断的
                payType = 1;
                payTotle = statisticNum[0][0];
                break;
            case 2:
                buffer.append(Ip.monthBillString + "?");//这一段是需要判断的
                payType = 2;
                payTotle = statisticNum[0][1];
                break;
            case 3:
                buffer.append(Ip.monthBillString + "?");//这一段是需要判断的
                payType = 3;
                payTotle = statisticNum[0][2];
                break;
            case 4:
                buffer.append(Ip.monthBillString + "?");//这一段是需要判断的
                payType = 4;
                payTotle = statisticNum[0][3];
                break;
            case 5:
                buffer.append(Ip.monthBillString + "?");
                payType = 1;
                payTotle = statisticNum[0][0];
                break;
            case 6:
                buffer.append(Ip.monthBillString + "?");
                payType = 2;
                payTotle = statisticNum[0][1];
                break;
            case 7:
                buffer.append(Ip.monthBillString + "?");
                payType = 3;
                payTotle = statisticNum[0][2];
                break;
            case 8:
                buffer.append(Ip.monthBillString + "?");
                payType = 4;
                payTotle = statisticNum[0][3];
                break;

        }
        buffer.append("uId=");
        buffer.append(applaication.getDate("operateTel") + "&");
        buffer.append("merchantId=");
        buffer.append(applaication.getDate("merchantId") + "&");
        buffer.append("month=");
        buffer.append(time + "&");
        buffer.append("payType=");
        buffer.append(paytype + "&");
        buffer.append("rule=ss");
        StatisticHome statisticHome = (StatisticHome) getParentFragment();
        if (a <= 4) {
            //
            statisticHome.showFragment(2);
            statisticHome.setMonyStatisticByType(buffer.toString(), payType, time, payTotle + "");
        } else {
            statisticHome.showFragment(3);
            statisticHome.setTradeStatisticSaleDayCount(buffer.toString(), payType, time, payTotle + "");
        }

    }

    /**
     * 状态切换时,sector传值  b o r g
     */
    private void swich(boolean money) {
        if (!money) {
            layoutm.setVisibility(View.GONE);
            layout2n.setVisibility(View.VISIBLE);
            view1.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.VISIBLE);
            sector.setVisibility(View.VISIBLE);
            sector.setWight(statisticMoney[0][2], statisticMoney[0][1], statisticMoney[0][3], statisticMoney[0][0]);
            sector1.setVisibility(View.GONE);
        } else {
            layoutm.setVisibility(View.VISIBLE);
            layout2n.setVisibility(View.GONE);
            view2.setVisibility(View.INVISIBLE);
            view1.setVisibility(View.VISIBLE);
            sector1.setVisibility(View.VISIBLE);
            sector1.setWight(String.valueOf(statisticNum[0][2]), String.valueOf(statisticNum[0][1]), String.valueOf(statisticNum[0][3]), String.valueOf(statisticNum[0][0]));
            sector.setVisibility(View.GONE);
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
    private void onSelectMerchant(String merChantId, String time) {
        if (this.time == null) {
            this.time = BaihuijiNet.getTime("yyyyMM");
        }

        MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        String key[] = {"uId", "merchantId", "month", "rule"};
        String value[] = {applaication.getDate("operateName"), merChantId, this.time, "ss"};
        //时间设置


        //String json = BaihuijiNet.getJson(key, value, "month");
        String json = Ip.static1 + BaihuijiNet.getRequst(key, value);
        String requst;


        // requst = BaihuijiNet.urlconection(Ip.static1, json);
        requst = connection(json);
        Log.i("OnSelectMerchant", requst + "\n" + json);
        if (getMerchantMonthbillSuccess(requst)) {
            getDate(requst);
        }
       /* if (timeText.getVisibility() == View.VISIBLE) {
            timeText.setText(BaihuijiNet.getTime("yyyy-MM"));
        }*/
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
                textView.setText(totalMoney[i]);
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
                //收款
                textView.setText(statisticMoney[0][a] + "");
            } else {
                textView.setText(statisticMoney[1][a - 4] + "");
            }
        }
        /**
         * 单项数量显示
         */
        for (int a = 0; a < tradMoneyId.length; a++) {
            textView = (TextView) view.findViewById(tradNumId[a]);
            if (a < 4) {//收款
                textView.setText(statisticNum[0][a] + "");

            } else {//退款
                textView.setText(statisticNum[1][a - 4] + "");
            }
        }
        if (sector.getVisibility() == View.VISIBLE) {
            sector.setWight(statisticMoney[0][0], statisticMoney[0][1], statisticMoney[0][2], statisticMoney[0][3]);
        } else {
            sector1.setWight(String.valueOf(statisticNum[1][0]), String.valueOf(statisticNum[1][1]), String.valueOf(statisticNum[1][2]), String.valueOf(statisticNum[1][3]));
        }
    }

    /**
     * 获取网络数据后调用，获取金额统计数据
     *
     * @param requst,
     */
    private void getDate(String requst) {
        //数据清空
        for (int a = 0; a < totalNum.length; a++) {

            totalNum[a] = 0;
            totalMoney[a] = "0";
            for (int b = 0; b < statisticNum[0].length; b++) {
                statisticNum[a][b] = 0;
                statisticMoney[a][b] = "0";
            }
        }
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
                    statisticMoney[0][0] = getMone(statisticMoney[0][0], jsonObject.getString("payTotal"));
                    statisticMoney[1][0] = getMone(statisticMoney[1][0], jsonObject.getString("backTotal"));
                    //数量统计
                    statisticNum[0][0] = statisticNum[0][0] + getNum(jsonObject.getString("payNum"));
                    statisticNum[1][0] = statisticNum[1][0] + getNum(jsonObject.getString("backNum"));

                } else if (jsonObject.getString("payType").equals("2")) {
                    statisticMoney[0][1] = getMone(statisticMoney[0][1], jsonObject.getString("payTotal"));
                    statisticMoney[1][1] = getMone(statisticMoney[1][1], jsonObject.getString("backTotal"));

                    statisticNum[0][1] = statisticNum[0][1] + getNum(jsonObject.getString("payNum"));
                    statisticNum[1][1] = statisticNum[1][1] + getNum(jsonObject.getString("backNum"));
                } else if (jsonObject.getString("payType").equals("3")) {
                    statisticMoney[0][2] = getMone(statisticMoney[0][2], jsonObject.getString("payTotal"));
                    statisticMoney[1][2] = getMone(statisticMoney[1][2], jsonObject.getString("backTotal"));

                    statisticNum[0][2] = statisticNum[0][2] + getNum(jsonObject.getString("payNum"));
                    statisticNum[1][2] = statisticNum[1][2] + getNum(jsonObject.getString("backNum"));
                } else if (jsonObject.getString("payType").equals("4")) {
                    statisticMoney[0][3] = getMone(statisticMoney[0][3], jsonObject.getString("payTotal"));
                    statisticMoney[1][3] = getMone(statisticMoney[1][3], jsonObject.getString("backTotal"));

                    statisticNum[0][3] = statisticNum[0][3] + getNum(jsonObject.getString("payNum"));
                    statisticNum[1][3] = statisticNum[1][3] + getNum(jsonObject.getString("backNum"));
                }
                //总金额
                totalMoney[0] = getMone(totalMoney[0], jsonObject.getString("payTotal"));
                totalMoney[1] = getMone(totalMoney[1], jsonObject.getString("backTotal"));

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
     * @param pay    新的收入值
     * @param origin 传入的原值
     * @return
     */
    private String getMone(String origin, String pay) {
        BigDecimal b1 = new BigDecimal(origin);
        BigDecimal b2;
        if (pay == null) {
            return origin;
        } else {
            b2 = new BigDecimal(pay);
            return b1.add(b2).toString();
        }
    }

    private int getNum(String pay) {
        if (pay == null) {
            return 0;
        } else {
            return Integer.parseInt(pay);
        }
    }

    private void onshow(String s) {
        MyApplaication application = (MyApplaication) getParentFragment().getActivity().getApplication();
        //传入门店名
        onSelectMerchant(application.getDate("merchantId"), s);
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
            String s = strings[0];
            onshow(s);
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