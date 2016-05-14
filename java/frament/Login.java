package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.PageView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;

public class Login extends Fragment {
    private CheckBox checkBox;
    private OnCheckedChangeListener clistener = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
        }
    };
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            Login.this.userNameEtx.getText().toString();
            Login.this.passwordEtx.getText().toString();
            Log.i("Onclick", "18716398031" + "  " + "123456");
            if ((Login.this.checkBox.isChecked()) && ("18716398031" != null) && (!"18716398031".equals("")) && ("123456" != null) && (!"123456".equals("")))
                Login.this.conect("18716398031", "123456");
        }
    };
    private TextView loginTv;
    private EditText passwordEtx;
    private String requst;
    private EditText userNameEtx;

    private void conect(String paramString1, String paramString2) {
        final String[] arrayOfString1 = {"merchantId", "ordSource", "operateId", "operatePass", "MD5"};
        final String[] arrayOfString2 = new String[5];
        arrayOfString2[0] = "0";
        arrayOfString2[1] = "app";
        arrayOfString2[2] = paramString1;
        arrayOfString2[3] = paramString2;
        arrayOfString2[4] = getMd5_32("merchantId&ordSource&operateId&operatePass&=*0*app*" + paramString1 + "*" + paramString2);
        Log.i("Md5", "merchantId&ordSource&operateId&operatePass&=*0*app*" + paramString1 + "*" + paramString2);
        new Thread() {
            public void run() {
                try {
                    Login.this.requst = BaihuijiNet.urlconection("http://baihuiji.weikebaba.com/pospay/queryShopMerchant", getJson(arrayOfString1, arrayOfString2));
                    if (Login.this.loginSuccess(Login.this.requst)) {
                        Login.this.saveDate(Login.this.requst);
                        Log.i("Login", Login.this.requst);
                        Login.this.jumptoHomepage();
                        return;
                    }
                } catch (Exception localException) {

                    localException.printStackTrace();
                    showToast();
                    Log.i("Login", Login.this.requst + "登录失败");
                }
            }
        }
                .start();
    }

    /**
     * hashMap 转jison,不可用
     *
     * @param paramHashMap
     * @return
     */
    private String getJson(HashMap<String, String> paramHashMap) {
        Iterator localIterator = paramHashMap.entrySet().iterator();
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("{");
        while (true) {
            if (!localIterator.hasNext()) {
                localStringBuffer.append("}");
                return localStringBuffer.toString();
            }
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            localStringBuffer.append("\"");
            localStringBuffer.append(localEntry.getKey());
            localStringBuffer.append("\":");
            localStringBuffer.append("\"");
            localStringBuffer.append(localEntry.getValue());
            localStringBuffer.append("\"");
            if (localIterator.hasNext())
                localStringBuffer.append(",");
        }
    }

    /**
     * 通过字符数组获取
     *
     * @param paramArrayOfString1
     * @param paramArrayOfString2
     * @return
     */
    private String getJson(String[] paramArrayOfString1, String[] paramArrayOfString2) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("{");
        int i = 0;
        while (i <= paramArrayOfString1.length) {

            if (i >= paramArrayOfString1.length) {
                localStringBuffer.append("}");
                return localStringBuffer.toString();
            }
            if (paramArrayOfString1[i].equals("MD5")) {
                localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
                localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"");
            } else {
                localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
                localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"" + ",");
            }
            i++;
        }
        return localStringBuffer.toString();
    }

    /**
     * 获取16位md5
     *
     * @param str
     * @return
     */
    private String getMd5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            // 数据放入Md5
            md5.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 字符窜转为了md5 byte
        byte[] b = md5.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {

            // 将一位byte 转为16进制字符
            if (Integer.toHexString(0xff & b[i]).length() == 1) {
                // 首位为0时
                stringBuffer.append("0").append(
                        Integer.toHexString(0xff & b[i]).toUpperCase());
            } else {
                // 首位不为0时,toUpperCase转化为答谢
                stringBuffer.append(Integer.toHexString(0xff & b[i])
                        .toUpperCase());
            }
        }
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

    public static void show(int a) {
        System.out.println(a);
    }

    private void jumptoHomepage() {
        ((PageView) getActivity()).jumptoHome();
    }

    private void loadComponent(View paramView) {
        this.userNameEtx = ((EditText) paramView.findViewById(R.id.login_urser_name_etx));
        this.passwordEtx = ((EditText) paramView.findViewById(R.id.login_urser_pass_etx));
        this.checkBox = ((CheckBox) paramView.findViewById(R.id.login_cbox));
        this.loginTv = ((TextView) paramView.findViewById(R.id.login_log_tx));
        this.loginTv.setOnClickListener(this.listener);
        this.checkBox.setOnCheckedChangeListener(this.clistener);
    }

    private boolean loginSuccess(String paramString) {
        JSONObject localJSONObject1 = null;
        try {
            localJSONObject1 = new JSONObject(paramString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String str = "";
        try {
            str = localJSONObject1.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (str.equals("登录成功")) return true;
        return false;
    }

    private void saveDate(String paramString) {
        MyApplaication localMyApplaication = (MyApplaication) getActivity().getApplication();
        int i = 0;
        String[] arrayOfString;
        arrayOfString = new String[]{"logo", "merchantId", "merName", "shopName", "time", "payTypeStatus", "company", "operateName", "operateTel"};
        JSONObject localJSONObject1 = null;
        try {
            localJSONObject1 = new JSONObject(paramString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        while (true) {
            JSONObject localJSONObject2;
            localJSONObject2 = localJSONObject1;
            if (i >= arrayOfString.length)
                return;
            try {
                localMyApplaication.putData(arrayOfString[i], localJSONObject2.getString(arrayOfString[i]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    private void showToast() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Login.this.getActivity(), "登录失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void writDb() {
        ((PageView) getActivity()).writDb();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.login, null,true);
        loadComponent(localView);
        return localView;
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Login
 * JD-Core Version:    0.6.2
 */