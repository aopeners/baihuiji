package frament;

import adpter.MonthAdpter;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;
import web.Ip;

public class MonthBill extends Fragment {
    private ListView listView;
    private MonthAdpter monthAdpter;
    private AlertDialog dialog;
    private String time;
    private TextView year;
    private TextView month;
    private LayoutInflater inflater;
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {

                case R.id.moth_bill_back_img:
                    MonthBill.this.onClickBack();
                    break;
                case R.id.moth_bill_select_time_linear:
                    showDatePick();
                    break;
                default:
                    return;
            }

        }
    };

    /**
     * 显示date pick
     */
    private void showDatePick() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentFragment().getActivity());
            View view = inflater.inflate(R.layout.dialog_data, null, true);
            DatePicker picker = (DatePicker) view.findViewById(R.id.dialog_date);
            picker.init(2016, 05, 4, dateChangedListener);
            builder.setView(view);
            dialog = builder.create();
            dialog.setOnDismissListener(dismissListener);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date());
            //设置最大时间
            picker.setMaxDate(calendar.getTimeInMillis());
        }
        dialog.show();
    }

    /**
     * dialog消失时
     */
    private DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            if (time != null) {
                Log.i("OnDismis", time);
                new MyAsyn().execute(time);
            }
        }
    };
    private DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
            Log.i("  Date picker", "" + i + "   " + i1 + "   " + i2);
            if (i1 == 12) {
                i1 = 1;
            }
            if (i1 < 10) {
                time = i + "0" + (i1 + 1);
            } else {
                time = i + "" + i1;
            }
        }
    };

    /**
     * @return 月账单json
     */
    private String getDates(String day) {
        String day1;
        if (day != null && day != "") {
            day1 = day;
        } else {
            day1 = BaihuijiNet.getTime("yyyyMM");
            time = day1;
        }
        MyApplaication applaication = (MyApplaication) MonthBill.this.getParentFragment().getActivity().getApplication();
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
        if (MonthBill.this.getSuccess(requst)) {
            // MonthBill.this.getDate(requst);
            return requst;
        } else {
            return null;
        }

    }

    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    private void showTost(String str) {
        Toast.makeText(getParentFragment().getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 处理返回的json数组
     *
     * @param paramString
     */
    private void getDate(String paramString) {
        if (list.size() > 0) {
            list.removeAll(list);
        }
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
        if (time != null) {
            year.setText(time.substring(0, 4));
            month.setText(time.substring(4, 6));
        }
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

    private void loadComponent(View paramView) {
        LinearLayout linearLayout = (LinearLayout) paramView.findViewById(R.id.moth_bill_select_time_linear);
        linearLayout.setOnClickListener(listener);
        this.listView = ((ListView) paramView.findViewById(R.id.month_bill_lv));
        listView.setOnItemClickListener(lvlistener);
        ((ImageView) paramView.findViewById(R.id.moth_bill_back_img)).setOnClickListener(this.listener);
        year = (TextView) paramView.findViewById(R.id.month_bill_year_tx);
        month = (TextView) paramView.findViewById(R.id.month_bill_moth_tx);
    }

    private AdapterView.OnItemClickListener lvlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            onItemClic(((HashMap<String, String>) listView.getAdapter().getItem(i)).get("totalDate"));
        }
    };

    private void onItemClic(String time) {
        Bill bill = (Bill) getParentFragment();
        bill.setDayBill(time);
    }

    /**
     * 点击后退时
     */
    private void onClickBack() {
        ((Bill) getParentFragment()).showFragment(0);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.month_bill, null, true);
        this.inflater = paramLayoutInflater;
        loadComponent(localView);
        return localView;
    }

    public void onHiddenChanged(boolean paramBoolean) {
        super.onHiddenChanged(paramBoolean);
        if ((!paramBoolean) && (this.monthAdpter == null)) {
            Log.i("MOtnBill", "ONHIDENCHSNGED");
            new MyAsyn().execute("");
        }
    }

    private String urlconection(String paramString1, String paramString2) {
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

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.MonthBill
 * JD-Core Version:    0.6.2
 */