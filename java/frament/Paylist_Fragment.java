package frament;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
 * 账单显示首页
 */
public class Paylist_Fragment extends Fragment {
    private BillAdpter billAdpter;
    private OnEditorActionListener eListener = new OnEditorActionListener() {
        public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent) {
            return false;
        }
    };
    private ListView listView;
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {

                case R.id.bill_month_tx:
                    Paylist_Fragment.this.onJump(1);
                    return;
                case R.id.bill_swap_img:
                    Paylist_Fragment.this.jumptoDecode();
                    return;
                case R.id.bill_serch_img:
                    Paylist_Fragment.this.onSerch();
                    break;
            }

        }
    };
    private TextView textView;

    private String getDate() {

        MyApplaication applaication = (MyApplaication) Paylist_Fragment.this.getParentFragment().getActivity().getApplication();
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
        Log.i("BillRefoundRequst", requst + "  \n  ");
        if (Paylist_Fragment.this.getSuccess(requst)) {
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

    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    private void getDate(String paramString) {
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
        for (int j = 0; j < jsonArray.length(); j++) {
            map = new HashMap<String, String>();
            try {
                jsonObject = jsonArray.getJSONObject(j);
                map.put("payTime", jsonObject.getString("payTime"));
                map.put("payType", jsonObject.getString("payType"));
                map.put("singal", jsonObject.getString("singal"));
                map.put("ordState", jsonObject.getString("ordState"));
                map.put("ordPrice", jsonObject.getString("ordPrice"));
                map.put("backTime", jsonObject.getString("backTime"));
                list.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (billAdpter == null) {
            billAdpter = new BillAdpter(list, getParentFragment().getActivity());
            listView.setAdapter(billAdpter);
        } else {
            billAdpter.setList(list);
        }
    }

    /**
     * 判断获取数据是否成功
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
            if (localJSONObject1.getString("errcode").equals("0")) {
                return true;
            }
        } catch (JSONException localJSONException1) {
            return false;
        }

        return false;
    }

    private boolean jsjump = true;//扫码跳转判断

    private void jumptoDecode() {
        if (jsjump) {
            jsjump = false;

            ((HomPage) getParentFragment().getActivity()).jumbtoDecoderForRefund();
            ((HomPage) getParentFragment().getActivity()).showProgress();
            jsjump = true;
        }
    }

    private void loadComponent(View paramView) {
        ImageView localImageView1 = (ImageView) paramView.findViewById(R.id.bill_swap_img);
        ImageView localImageView2 = (ImageView) paramView.findViewById(R.id.bill_serch_img);
        EditText localEditText = (EditText) paramView.findViewById(R.id.bill_etx);
        ((TextView) paramView.findViewById(R.id.bill_month_tx)).setOnClickListener(this.listener);
        localImageView2.setOnClickListener(this.listener);
        localImageView1.setOnClickListener(this.listener);
        localEditText.setOnEditorActionListener(this.eListener);
        listView = (ListView) paramView.findViewById(R.id.bill_lv);
        listView.setOnItemClickListener(lvLisntener);
    }

    private AdapterView.OnItemClickListener lvLisntener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            clickIten((HashMap<String, String>) listView.getAdapter().getItem(position));
        }
    };

    /**
     * 当子项被点击时调用,扫描订单成功时调用
     *
     * @param map
     */
    private void clickIten(HashMap<String, String> map) {
        ((Bill) getParentFragment()).showBillDetail(map);
    }

    /**
     * 跳转fragment 0 paylist ,1 monthbill
     *
     * @param paramInt
     */
    private void onJump(int paramInt) {
        ((Bill) getParentFragment()).showFragment(1);
    }

    private void onSerch() {
        View view = getView();
        EditText editText = (EditText) view.findViewById(R.id.bill_etx);
        ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("singal").startsWith(editText.getText().toString())) {
                list1.add(list.get(i));
            }
        }
        billAdpter.setList(list1);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.bill, null, true);
        loadComponent(localView);
        return localView;
    }

    /**
     * 设置订单
     */
    public void setSignal(String signal) {
        HashMap<String, String> map = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("singal").equals(signal)) {
                map = list.get(i);
            }
        }
        if (map != null) {
            clickIten(map);
        } else {
            ((HomPage) getParentFragment().getActivity()).showToast("不存在该订单");
        }
    }

    /**
     * 同一级fragment切换时有用
     *
     * @param paramBoolean
     */
    public void onHiddenChanged(boolean paramBoolean) {
        super.onHiddenChanged(paramBoolean);
        if ((!paramBoolean) && (this.billAdpter == null)) {
            HomPage homPage = (HomPage) getParentFragment().getActivity();
            new MyAsyn().execute("");
        }
        //  Log.i("ONhiddenChange", "paramBoolean");
    }
    public void onDateChange(){
        new MyAsyn().execute("");
    }
    /**
     * 退款时的动作
     */
    public void setOnRefound() {
        getDate();
        list = new ArrayList<HashMap<String, String>>();
    }

    private class MyAsyn extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            return getDate();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HomPage homPage = (HomPage) getParentFragment().getActivity();
            homPage.dialogCancle();
            if (s != null) {
                if (s.trim().length() > 0)
                    getDate(s);
            }
        }
    }
}
