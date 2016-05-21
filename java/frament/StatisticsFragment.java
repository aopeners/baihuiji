package frament;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.R;
import views.Sector;
import web.BaihuijiNet;
import web.Ip;

/**
 * 统计类的主视图页面，没有listview 相关写法，有显示三个栏的方法，商户版废弃此类
 */
public class StatisticsFragment extends Fragment {
    private LayoutInflater inflater;
    private LinearLayout layout;
    private Sector sector;
    private TextView textView;
    private View view;
    //金额统计数据
    private ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();

    @SuppressLint({"NewApi"})//当fragment 可视时调用
    private void loadComponent(View paramView) {
        this.sector = ((Sector) paramView.findViewById(R.id.trad_static_sector));
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        this.view = paramLayoutInflater.inflate(R.layout.trad_statistic_1, null, true);
        this.inflater = paramLayoutInflater;
        // loadComponent(this.view);
        loadView(view);
        return this.view;
    }

    /**
     * 加载布局，控件
     * 加载布局，控件
     *
     * @param view
     */
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
        //卡券核销量
        textView = (TextView) view.findViewById(R.id.trad_statistic_ticks_tx);
        textView.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.trad_statistic_select_tx:
                    showDialog();
                    break;
                case R.id.trad_statistic_money_tx:
                    if (list1.size() > 0) {
                        setDate(0, list1);
                    }
                    break;
                case R.id.trad_statistic_sales_tx:
                    if (list1.size() > 0) {
                        setDate(1, list1);
                    }
                    break;
                case R.id.trad_statistic_ticks_tx:
                    if (list1.size() > 0) {
                        setDate(2, list1);
                    }
                    break;
            }
        }
    };

    /**
     * 切换为显示时的方法
     */
    private void onshow(){
       MyApplaication application= (MyApplaication) getParentFragment().getActivity().getApplication();
        //传入门店名
        onSelectMerchant(application.getDate("merchantId"));
    }
    public void onHiddenChanged(boolean paramBoolean) {
        super.onHiddenChanged(paramBoolean);
        if (!paramBoolean) {

           // this.sector.setWidth("", 0.3F, 0.1F, 0.4F,0.1f,0.1f);
            onshow();
        }
    }

    public void onResume() {
        super.onResume();
        Log.i("OnResume", "onResume");
        //  loadComponent(this.view);
    }

    public void onStart() {
        super.onStart();
        Log.i("OnStart", "onStart");
        loadComponent(this.view);
    }

    //选择们点的dialog
    private AlertDialog dialog;

    private void showDialog() {
        MyApplaication application = (MyApplaication) getParentFragment().getActivity().getApplication();
        String merName[] = {application.getDate("merName")};
        //"merName"
        ListView listView = null;
        if (dialog == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getParentFragment().getActivity());
            View view = inflater.inflate(R.layout.dialog_select_shop, null, true);
            builder.setView(view);
            dialog = builder.create();
            listView = (ListView) view.findViewById(R.id.dialog_select_shop_lv);
            listView.setOnItemClickListener(listListner);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter
                (getParentFragment().getActivity(), R.layout.dialog_select_shop, R.id.lv_diolog_select_tx, merName);
        listView.setAdapter(arrayAdapter);
    }

    private AdapterView.OnItemClickListener listListner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String a = (String) parent.getAdapter().getItem(position);
        }
    };

    /**
     * 选择门店时,获取数据入口
     *
     * @param merChantId 门店名字
     */
    private void onSelectMerchant(String merChantId) {
        final String mer = merChantId;
        new Thread(new Runnable() {
            MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
            String key[] = {"uId", "merchantId", "month"};
            String value[] = {applaication.getDate("operateName"), mer, BaihuijiNet.getTime("yyyyMM")};

            //String json = BaihuijiNet.getJson(key, value, "month");
            String json=Ip.static1+BaihuijiNet.getRequst(key,value);
            String requst;

            @Override
            public void run() {
               // requst = BaihuijiNet.urlconection(Ip.static1, json);
                requst=BaihuijiNet.connection(json);
                Log.i("OnSelectMerchant",requst);
                if (getMerchantMonthbillSuccess(requst)) {
                    getDate(requst);
                    setDate(0, list1);
                }
                ;
            }
        }).start();

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

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            if (jsonObject.getString("errcode").endsWith("0")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 当只需要显示两栏时的数据适配
     */
    private void setDate1(int state, ArrayList<HashMap<String, String>> data){

    }
    /**
     * 显示三栏时的数据适配，未检测！
     * 设置控件显示的数据，有三个状态
     *
     * @param state 显示的状态 0金额统计，1 销量统计  2.卡券核销量
     * @param data  显示的内容
     */
    private void setDate(int state, ArrayList<HashMap<String, String>> data) {
        LinearLayout linearLayout;

        LinearLayout.LayoutParams params;
        View chaild;
        if (state == 0) {
            //布局
            //月销量
            linearLayout = (LinearLayout) view.findViewById(R.id.trad_statistic_1_linear2);
            linearLayout.setVisibility(View.GONE);
            linearLayout = (LinearLayout) view.findViewById(R.id.trad_statistic_1_linear);
            linearLayout.setVisibility(View.VISIBLE);
            //布局点
            linearLayout = (LinearLayout) view.findViewById(R.id.trad_statistic1_linear);

            linearLayout.removeAllViews();

            view = inflater.inflate(R.layout.lv_statistic_trad, null, true);
            params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.WRAP_CONTENT;
            params.topMargin = 2;
            TextView textView;
            //设值
            int textId[] = {R.id.total_get, R.id.trad_statistic_getMoney_tx,
                    R.id.total_back, R.id.trad_statistic_backMoney_tx};
            int textId2[] = {R.id.lv_statistic_trad_img, R.id.lv_statistic_trad_pay_tx,
                    R.id.trad_statistic_getMoney_tx, R.id.trad_statistic_backMoney_tx, R.id.totalget, R.id.totalback};
            String text[] = new String[4];
            text[0] = "总收款:¥";
            text[2] = "总退款:¥";
            ImageView img = (ImageView) view.findViewById(textId[0]);
            float payTotal = 0, backTotal = 0;
            for (int i = 0; i < 4; i++) {
                payTotal = payTotal + Float.parseFloat(data.get(i).get("payTotal"));
                backTotal = backTotal + Float.parseFloat(data.get(i).get("backTotal"));

                textView = (TextView) view.findViewById(textId2[1]);
                switch (data.get(i).get("payType")) {

                    case "1":
                        textView.setText("微信支付");
                        img.setImageResource(R.drawable.payweichar);
                        break;
                    case "2":
                        textView.setText("qq钱包");
                        img.setImageResource(R.drawable.qqperse);
                        break;
                    case "3":
                        textView.setText("支付宝");
                        img.setImageResource(R.drawable.ailipay);
                        break;
                    case "4":
                        textView.setText("百度钱包");
                        img.setImageResource(R.drawable.baiduperse);
                        break;
                }

                textView = (TextView) view.findViewById(textId2[2]);
                textView.setText(data.get(i).get("payTotal"));
                textView = (TextView) view.findViewById(textId2[3]);
                textView.setText(data.get(i).get("backTotal"));
                textView = (TextView) view.findViewById(textId2[4]);
                textView.setText("收款:¥");
                textView = (TextView) view.findViewById(textId2[5]);
                textView.setText("退款:¥");

                linearLayout.addView(view);
            }
            view = getView();
            for (int j = 0; j < textId.length; j++) {
                textView = (TextView) view.findViewById(textId[j]);
                textView.setText(text[j]);
            }//销量统计
        } else if (state == 1) {
            //布局
            linearLayout = (LinearLayout) view.findViewById(R.id.trad_statistic_1_linear);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout = (LinearLayout) view.findViewById(R.id.trad_statistic_1_linear2);
            linearLayout.setVisibility(View.GONE);
            linearLayout = (LinearLayout) view.findViewById(R.id.trad_statistic1_linear);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.removeAllViews();

            view = inflater.inflate(R.layout.lv_statistic_trad, null, true);
            params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.WRAP_CONTENT;
            params.topMargin = 2;
            TextView textView;
            //设值
            int textId[] = {R.id.total_get, R.id.trad_statistic_getMoney_tx,
                    R.id.total_back, R.id.trad_statistic_backMoney_tx};
            int textId2[] = {R.id.lv_statistic_trad_img, R.id.lv_statistic_trad_pay_tx,
                    R.id.trad_statistic_getMoney_tx, R.id.trad_statistic_backMoney_tx, R.id.totalget, R.id.totalback};
            String text[] = new String[4];
            text[0] = "收款:";
            text[2] = "退款:";
            ImageView img = (ImageView) view.findViewById(textId[0]);
            float payTotal = 0, backTotal = 0;
            for (int i = 0; i < 4; i++) {
                payTotal = payTotal + Float.parseFloat(data.get(i).get("payNum"));
                backTotal = backTotal + Float.parseFloat(data.get(i).get("backNum"));

                textView = (TextView) view.findViewById(textId2[1]);
                switch (data.get(i).get("payType")) {

                    case "1":
                        textView.setText("微信支付");
                        img.setImageResource(R.drawable.payweichar);
                        break;
                    case "2":
                        textView.setText("qq钱包");
                        img.setImageResource(R.drawable.qqperse);
                        break;
                    case "3":
                        textView.setText("支付宝");
                        img.setImageResource(R.drawable.ailipay);
                        break;
                    case "4":
                        textView.setText("百度钱包");
                        img.setImageResource(R.drawable.baiduperse);
                        break;
                }
                //方框内赋值
                textView = (TextView) view.findViewById(textId2[2]);
                textView.setText(data.get(i).get("payNum") + "笔");
                textView = (TextView) view.findViewById(textId2[3]);
                textView.setText(data.get(i).get("backNum" + "笔"));
                textView = (TextView) view.findViewById(textId2[4]);
                textView.setText("收款:¥");
                textView = (TextView) view.findViewById(textId2[5]);
                textView.setText("退款:¥");

                linearLayout.addView(view);
            }
            view = getView();
            for (int j = 0; j < textId.length; j++) {
                textView = (TextView) view.findViewById(textId[j]);
                textView.setText(text[j]);
            }
        } else if (state == 2) {
            linearLayout = (LinearLayout) view.findViewById(R.id.trad_statistic_1_linear);
            linearLayout.setVisibility(View.GONE);
            linearLayout = (LinearLayout) view.findViewById(R.id.trad_statistic_1_linear2);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout = (LinearLayout) view.findViewById(R.id.trad_statistic1_linear);
            linearLayout.removeAllViews();

            view = inflater.inflate(R.layout.lv_tick_sale, null, true);
            params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.WRAP_CONTENT;
            params.topMargin = 2;
            TextView textView;
            //设值
            int textId[] = {R.id.total_get, R.id.trad_statistic_getMoney_tx,
                    R.id.total_back, R.id.trad_statistic_backMoney_tx};
            int textId2[] = {R.id.lv_statistic_trad_img, R.id.lv_statistic_trad_pay_tx,
                    R.id.trad_statistic_getMoney_tx, R.id.trad_statistic_backMoney_tx, R.id.totalget, R.id.totalback};
            String text[] = new String[4];
            text[0] = "收款:";
            text[2] = "退款:";
            ImageView img = (ImageView) view.findViewById(textId[0]);
            float payTotal = 0, backTotal = 0;
            for (int i = 0; i < 4; i++) {
                payTotal = payTotal + Float.parseFloat(data.get(i).get("payNum"));
                backTotal = backTotal + Float.parseFloat(data.get(i).get("backNum"));

                textView = (TextView) view.findViewById(textId2[1]);
                switch (data.get(i).get("payType")) {

                    case "1":
                        textView.setText("微信支付");
                        img.setImageResource(R.drawable.payweichar);
                        break;
                    case "2":
                        textView.setText("qq钱包");
                        img.setImageResource(R.drawable.qqperse);
                        break;
                    case "3":
                        textView.setText("支付宝");
                        img.setImageResource(R.drawable.ailipay);
                        break;
                    case "4":
                        textView.setText("百度钱包");
                        img.setImageResource(R.drawable.baiduperse);
                        break;
                }
                //方框内赋值
                textView = (TextView) view.findViewById(textId2[2]);
                textView.setText(data.get(i).get("payNum") + "笔");
                textView = (TextView) view.findViewById(textId2[3]);
                textView.setText(data.get(i).get("backNum" + "笔"));
                textView = (TextView) view.findViewById(textId2[4]);
                textView.setText("收款:¥");
                textView = (TextView) view.findViewById(textId2[5]);
                textView.setText("退款:¥");

                linearLayout.addView(view);
            }
            view = getView();
            for (int j = 0; j < textId.length; j++) {
                textView = (TextView) view.findViewById(textId[j]);
                textView.setText(text[j]);
            }
        }

    }

}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.StatisticsFragment
 * JD-Core Version:    0.6.2
 */