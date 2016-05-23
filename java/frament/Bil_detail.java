package frament;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;
import web.Ip;

/**
 * 账单详情,payListFragment 子项
 */
public class Bil_detail extends Fragment {
    private boolean state;//支付状态,ture 支付成功，false 退款状态
    private LayoutInflater inflater;
    private EditText editText;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (state) {
                setState(state, map);
                dialog.cancel();

            } else {

                dialog.cancel();
            }
        }
    };

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View view = paramLayoutInflater.inflate(R.layout.bill_detai, null);
        this.inflater = paramLayoutInflater;
        loadComponet(view);
        return view;
    }

    private void loadComponet(View view) {
        ImageView imgback = (ImageView) view.findViewById(R.id.bill_detail_back);
        TextView refund = (TextView) view.findViewById(R.id.bill_detail_refund_bt_tx);
        imgback.setOnClickListener(listener);
        refund.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bill_detail_back:
                    jumptoPaylist();
                    break;
                case R.id.bill_detail_refund_bt_tx:
                    showDialog();
                    break;
                case R.id.dialog_refund_close:
                    dialog.cancel();
                    break;
                case R.id.dialog_refund_tx1:
                    onRefund(editText.getText().toString());
                    break;
            }
        }
    };

    /**
     * 点击退款时
     */
    private void onClicRefund() {
        showDialog();
    }

    private AlertDialog dialog;

    private void showDialog() {
        View view = inflater.inflate(R.layout.dialog_refund, null, true);
        TextView textView;
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentFragment().getContext());

            ImageView imageView = (ImageView) view.findViewById(R.id.dialog_refund_close);
            imageView.setOnClickListener(listener);
            editText = (EditText) view.findViewById(R.id.dialog_refund_etx);
            editText.setOnEditorActionListener(elstener);


            builder.setView(view);
            textView = (TextView) view.findViewById(R.id.dialog_refund_tx1);
            textView.setOnClickListener(listener);
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnDismissListener(dismissListener);
        }
        textView = (TextView) view.findViewById(R.id.dialog_refund_tx);
        textView.setText(map.get("ordPrice"));
        dialog.show();


    }

    private DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            editText.setText("");
        }
    };
    /**
     * 点击退款监听
     */
    private TextView.OnEditorActionListener elstener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
                // onRefund(v.getText().toString());
            }
            return false;
        }
    };

    private void jumptoPaylist() {
        ((Bill) getParentFragment()).showFragment(0);
    }

    private HashMap<String, String> map;

    public void setDate(HashMap<String, String> map) {
        this.map = map;
        if (map.get("ordState").equals("1")) {
            state = true;
        } else {
            state = false;
        }
        setState(state, map);
    }

    /**
     * 设置显示状态
     *
     * @param state true 付款
     * @param map   单个账单详情组
     */
    private void setState(boolean state, HashMap<String, String> map) {
        MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        View view = getView();
        //会改变状态id 0,   lenth-1,lenth-2 gone
        int tid[] = {R.id.bill_detail_mony_tx, R.id.bill_money_tx, R.id.bill_detail_marchtName_tx,
                R.id.bill_detail_marchtShop_tx, R.id.bill_detail_operater_tx, R.id.bill_detail_state_tx,
                R.id.bill_detail_pay_tx, R.id.bill_detail_trade_tx, R.id.bill_detail_trade_time_tx,
                R.id.bill_detail_refund_time_tx, R.id.bill_detail_refund_bt_tx};
        TextView textView;
        if (state) {
            for (int i = 0; i < tid.length; i++) {
                textView = (TextView) view.findViewById(tid[i]);
                if (i == 0) {
                    textView.setText("付款金额");
                } else if (i == 5) {
                    textView.setText("支付成功");
                } else if (i == tid.length - 2) {
                    textView.setVisibility(View.GONE);
                } else if (i == tid.length - 1) {
                    textView.setVisibility(View.VISIBLE);
                }
                switch (i) {
                    case 1:
                        textView.setText(map.get("ordPrice"));
                        break;
                    case 2:
                        textView.setText(applaication.getDate("shopName"));
                        break;
                    case 3:
                        textView.setText(applaication.getDate("merName"));
                        break;
                    case 4:
                        textView.setText(applaication.getDate("operateName"));
                        break;
                    case 6:
                        setePayWay(textView, map.get("payType"));
                        break;
                    case 7:
                        textView.setText(map.get("singal"));
                        break;
                    case 8:
                        textView.setText(map.get("payTime"));
                        break;

                }

            }
        } else {
            for (int i = 0; i < tid.length; i++) {
                textView = (TextView) view.findViewById(tid[i]);
                if (i == 0) {
                    textView.setText("退款金额");
                } else if (i == 5) {
                    textView.setText("已退款");
                } else if (i == tid.length - 2) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(map.get("backTime"));
                } else if (i == tid.length - 1) {
                    textView.setVisibility(View.GONE);
                }
                switch (i) {
                    case 1:
                        textView.setText(map.get("ordPrice"));
                        break;
                    case 2:
                        textView.setText(applaication.getDate("shopName"));
                        break;
                    case 3:
                        textView.setText(applaication.getDate("merName"));
                        break;
                    case 4:
                        textView.setText(applaication.getDate("operateName"));
                        break;
                    case 6:
                        setePayWay(textView, map.get("payType"));
                        break;
                    case 7:
                        textView.setText(map.get("singal"));
                        break;
                    case 8:
                        textView.setText(map.get("payTime"));
                        break;

                }

            }
        }
    }

    /**
     * 设置支付方式
     *
     * @param textView
     * @param way
     */
    private void setePayWay(TextView textView, String way) {
        switch (way) {
            case "1":
                textView.setText("微信支付");
                break;
            case "2":
                textView.setText("qq钱包");
                break;
            case "3":
                textView.setText("支付宝");
                break;
            case "4":
                textView.setText("百度钱包");
                break;
        }
    }

    /**
     * 获取json窜中一个值
     *
     * @param json
     * @param key
     * @return
     */
    private String getJsonValu(String json, String key) {
        JSONObject object;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        try {
            return object.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    //退款
    private void onRefund(final String password) {
        final MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        final HomPage homPage = (HomPage) getParentFragment().getActivity();
        new Thread(new Runnable() {

            String key[] = {"merchantId", "orderNo",
                    "ordSource", "operateId", "backPsw",
                    "ver", "MD5"};
            String valu[] = {applaication.getDate("merchantId")
                    , map.get("singal"), "pc"
                    ,
                    applaication.getDate("operateId"),
                    password,
                    "2.6",
                    getMd5_32("merchantId&orderNo&ordSource&operateId&backPsw&ver&=*"
                            + applaication.getDate("merchantId") + "*" + map.get("singal") + "*" + "pc" + "*" + applaication.getDate("operateId") + "*" + password + "*" + "2.6")
            };
            String json = getJson(key, valu, "MD5");
            String requst;

            @Override
            public void run() {
                requst = urlconection(Ip.refund, json);
                Log.i("OnRefund", requst);
                Looper.prepare();
                homPage.dialogCancle();
                if (refundSuccess(requst)) {
                    map.put("backTime", getJsonValu(requst, "time"));
                    state = false;
                    showToast("已退款");
                    handler.sendEmptyMessage(1);
                } else {
                    state = true;
                    handler.sendEmptyMessage(1);
                    showToast("超过最大退款日期，不能进行退款。");
                }
            }
        }).start();

    }

    /**
     * 判断退款成功
     *
     * @param requst
     * @return
     */
    private boolean refundSuccess(String requst) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(requst);
        } catch (JSONException e) {
            return false;
        }
        try {
            if (jsonObject.getString("msg").equals("成功"))
                return true;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
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

    private String getMd5_32(String str) {
        StringBuffer buffer = new StringBuffer();
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] b = md5.digest();
        int i;
        StringBuffer stringBuffer = new StringBuffer();
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                stringBuffer.append("0");
            stringBuffer.append(Integer.toHexString(i).toUpperCase());
        }
        return stringBuffer.toString();
    }

    private String getJson(String[] paramArrayOfString1, String[] paramArrayOfString2, String paramString) {
        int i = 0;
        StringBuffer localStringBuffer = new StringBuffer();
        while (i <= paramArrayOfString1.length) {

            try {
                if (i == 0)//头
                    localStringBuffer.append("{");
                if (i >= paramArrayOfString1.length) {
                    localStringBuffer.append("}");
                    String str = localStringBuffer.toString();
                    break;
                }
                if (paramArrayOfString1[i].equals(paramString)) {
                    localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
                    localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"");
                } else {
                    localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
                    localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"" + ",");
                }
            } finally {
            }
            i++;
        }
        return localStringBuffer.toString();
    }

    private void showToast(final String tost) {
        getParentFragment().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getParentFragment().getActivity(), tost, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

