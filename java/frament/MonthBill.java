package frament;

import adpter.MonthAdpter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import baihuiji.jkqme.baihuiji.MyApplaication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;

public class MonthBill extends Fragment {
    private ListView listView;
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {

                case R.id.moth_bill_back_img:
                    MonthBill.this.onClickBack();
                    break;
                default:
                    return;
            }

        }
    };
    private MonthAdpter monthAdpter;

    private void getDate() {
        new Thread(new Runnable() {
            MyApplaication applaication = (MyApplaication) MonthBill.this.getParentFragment().getActivity().getApplication();
            String json;
            String[] key = {"uId", "merchantId", "month"};
            String requst;
            String[] value =

                    {
                            applaication.getDate("operateName"), applaication.getDate("merchantId"), applaication.getDate("month")
                    };

            public void run() {
                json = BaihuijiNet.getJson(key, value, "month");
                this.requst = MonthBill.this.urlconection("http://baihuiji.weikebaba.com/aide/monthBill", this.json);
                Log.i("BillMonth", this.requst + "  \n  " + this.json);
                if (MonthBill.this.getSuccess(this.requst))
                    MonthBill.this.getDate(this.requst);
            }
        }).start();
    }

    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

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
            jsonArray = jsonObject.getJSONArray("detail");
        } catch (JSONException e) {
            e.printStackTrace();
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
                map = new HashMap<String,String>();
                try {
                    jsonObject = jsonArray.getJSONObject(j);
                    map.put("payTotal",jsonObject.getString("payTotal"));
                    map.put("backTotal",jsonObject.getString("backTotal"));
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        monthAdpter=new MonthAdpter(list,getParentFragment().getActivity());
        listView.setAdapter(monthAdpter);

    }

    private boolean getSuccess(String paramString) {
        JSONObject localJSONObject1 = null;
        try {
            localJSONObject1 = new JSONObject(paramString);

        } catch (JSONException localJSONException2) {
        }
        try {
            if (localJSONObject1.getString("errcode").equals("0"))
                return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void loadComponent(View paramView) {
        this.listView = ((ListView) paramView.findViewById(R.id.month_bill_lv));
        ((ImageView) paramView.findViewById(R.id.moth_bill_back_img)).setOnClickListener(this.listener);
    }

    /**
     * 点击后退时
     */
    private void onClickBack() {
        ((Bill) getParentFragment()).showFragment(0);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.month_bill, null, true);
        loadComponent(localView);
        return localView;
    }

    public void onHiddenChanged(boolean paramBoolean) {
        super.onHiddenChanged(paramBoolean);
        if ((!paramBoolean) && (this.monthAdpter == null))
            getDate();
    }

    public static String urlconection(String paramString1, String paramString2) {
        StringBuffer stringBuffer = new StringBuffer();
        URL urL = null;
        HttpURLConnection connection = null;
        DataOutputStream dataOutputStream = null;
        try {
            urL = new URL(paramString1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Ip格式错误";
        }

        try {
            connection = (HttpURLConnection) urL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return "网络访问失败";
        }
        try {
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            connection.setInstanceFollowRedirects(true);//设置自动重定向
            connection.setUseCaches(false);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败";
        }
        try {
            dataOutputStream.writeBytes(paramString2);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败";
        }
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(reader);
                String inputLine = null;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }
                reader.close();
                bufferedReader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "获取数据返回失败";
        }
        connection.disconnect();
        return stringBuffer.toString();
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.MonthBill
 * JD-Core Version:    0.6.2
 */