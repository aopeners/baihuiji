package frament;

import adpter.BillAdpter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import adpter.MonthAdpter;
import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.MyApplaication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;
import web.Ip;

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

    private void getDate() {
        new Thread(new Runnable() {
            MyApplaication applaication = (MyApplaication) Paylist_Fragment.this.getParentFragment().getActivity().getApplication();
            String json;
            String[] key = {"uId", "merchantId", "day"};
            String requst;
            String[] value =
                    {
                            applaication.getDate("operateName"), applaication.getDate("merchantId"),BaihuijiNet.getTime("yyyyMMdd")
                    };


            public void run() {
                json=BaihuijiNet.getJson(key, value, "day");
                this.requst = BaihuijiNet.urlconection(Ip.monthBillString, this.json);
                Log.i("BillRequst", this.requst + "  \n  " + this.json);
                if (Paylist_Fragment.this.getSuccess(this.requst))
                    Paylist_Fragment.this.getDate(this.requst);
            }
        }).start();
    }
    private ArrayList<HashMap<String,String>>list=new ArrayList<HashMap<String, String>>();
    // ERROR //
    private void getDate(String paramString) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(paramString);
            jsonArray = jsonObject.getJSONArray("o2o");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //listview 适配数据
        HashMap<String, String> map;
        for (int j = 0; j < jsonArray.length(); j++) {
            map = new HashMap<String,String>();
            try {
                jsonObject = jsonArray.getJSONObject(j);
                map.put("payTime",jsonObject.getString("payTime"));
                map.put("payType",jsonObject.getString("payType"));
                map.put("singal",jsonObject.getString("singal"));
                map.put("ordState",jsonObject.getString("ordState"));
                map.put("ordPrice",jsonObject.getString("ordPrice"));
                map.put("backTime",jsonObject.getString("backTime"));
                list.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        billAdpter=new BillAdpter(list,getParentFragment().getActivity());
        listView.setAdapter(billAdpter);
    }

    /**
     * 判断获取数据是否成功
     * @param paramString
     * @return
     */
    private boolean getSuccess(String paramString) {
        JSONObject localJSONObject1 = null;
        try {
            localJSONObject1 = new JSONObject(paramString);

        } catch (JSONException localJSONException2) { return false;}
            try {
               if(localJSONObject1.getString("errcode").equals("0")){return true;}
            } catch (JSONException localJSONException1) {
            return false;
            }

        return false;
    }

    private void jumptoDecode() {
        ((HomPage) getParentFragment().getActivity()).jumptoDecode();
    }

    private void loadComponent(View paramView) {
        ImageView localImageView1 = (ImageView) paramView.findViewById(R.id.bill_swap_img);
        ImageView localImageView2 = (ImageView) paramView.findViewById(R.id.bill_serch_img);
        EditText localEditText = (EditText) paramView.findViewById(R.id.bill_etx);
        ((TextView) paramView.findViewById(R.id.bill_month_tx)).setOnClickListener(this.listener);
        localImageView2.setOnClickListener(this.listener);
        localImageView1.setOnClickListener(this.listener);
        localEditText.setOnEditorActionListener(this.eListener);
        listView= (ListView) paramView.findViewById(R.id.bill_lv);
        listView.setOnItemClickListener(lvLisntener);
    }
private AdapterView.OnItemClickListener lvLisntener=new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickIten((HashMap<String, String>) listView.getAdapter().getItem(position));
    }
};

    /**
     * 当子项被点击时调用
     * @param map
     */
    private void clickIten(HashMap<String,String>map){
        ( (Bill)getParentFragment()).showBillDetail(map);
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

    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.bill, null,true);
        loadComponent(localView);
        return localView;
    }

    public void onHiddenChanged(boolean paramBoolean) {
        super.onHiddenChanged(paramBoolean);
        if ((!paramBoolean) && (this.billAdpter == null))
            getDate();
    }
}
