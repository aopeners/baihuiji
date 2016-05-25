package frament;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.PageView;
import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;
import web.Ip;

public class Login extends Fragment {

    private boolean loginclick = false;//记录是否点击登录
    private CheckBox checkBox;
    private OnCheckedChangeListener clistener = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
        }
    };
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            String user = userNameEtx.getText().toString();
            String password = passwordEtx.getText().toString();
            //   user = "18716398031";
            //  password = "123456";
            Log.i("Onclick", "" + user + password);

            if ((Login.this.checkBox.isChecked()) && user.trim().length() > 0 && password.trim().length() > 0 && !loginclick) {
                loginclick = true;
                Login.this.conect(user, password);
            } else if (user.trim().length() > 0 && password.trim().length() > 0 && !loginclick) {
                loginclick = true;
                Login.this.conect(user, password);
            }
        }
    };
    private TextView loginTv;
    private EditText passwordEtx;
    private String requst;
    private EditText userNameEtx;

    private void conect(String paramString1, final String paramString2) {
        loginclick = true;
        showProgress();
        final String[] arrayOfString1 = {"merchantId", "ordSource", "operateId", "operatePass", "MD5"};
        final String[] arrayOfString2 = new String[5];
        arrayOfString2[0] = "0";
        arrayOfString2[1] = "app";
        arrayOfString2[2] = paramString1;
        arrayOfString2[3] = paramString2;
        arrayOfString2[4] = getMd5_32("merchantId&ordSource&operateId&operatePass&=*0*app*" + paramString1 + "*" + paramString2);
        Log.i("Md5", "merchantId&ordSource&operateId&operatePass&=*0*app*" + paramString1 + "*" + paramString2);
        final String json = getJson(arrayOfString1, arrayOfString2);
        Log.i("Conect", json);
        new Thread() {
            public void run() {
                try {
                    Login.this.requst = BaihuijiNet.urlconection(Ip.logip, json);
                    Log.i("Login", Login.this.requst);
                    if (Login.this.loginSuccess(Login.this.requst)) {
                        Login.this.saveDate(Login.this.requst, paramString2);
                        if (checkBox.isChecked()) {
                            //保存数据
                            getSahedPerference(userNameEtx.getText().toString(), passwordEtx.getText().toString(), true);
                        } else {
                            getSahedPerference(userNameEtx.getText().toString(), passwordEtx.getText().toString(), false);
                        }
                        Login.this.jumptoHomepage();
                        return;
                    }
                } catch (Exception localException) {

                    localException.printStackTrace();

                    Log.i("Login", Login.this.requst + "登录失败");
                }
                loginclick = false;
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
        while (i < paramArrayOfString1.length) {
            if (paramArrayOfString1[i].equals("MD5")) {
                localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
                localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"");
                localStringBuffer.append("}");
                return localStringBuffer.toString();
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
            showToast("");
            return false;
        }

        String str = "";
        try {
            str = localJSONObject1.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (str.equals("登录成功")) {
            return true;
        } else {
            showToast(str);
        }
        return false;
    }

    private void saveDate(String paramString, String password) {
        MyApplaication localMyApplaication = (MyApplaication) getActivity().getApplication();
        localMyApplaication.putData("password", password);
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

                if (checkBox.isChecked()) {
                    //   saveUser(true);
                } else {
                    //     saveUser(false);

                    if (checkBox.isChecked()) {
                        // saveUser(true);
                    } else {
                        // saveUser(false);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;

        }
    }

    /**
     * 保存用户,保存密码或不保存密码
     */

    private void saveUser(boolean savaPassword) {
        MyApplaication localMyApplaication = (MyApplaication) getActivity().getApplication();
        SQLiteDatabase localSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(localMyApplaication.helper.dbPath, null);
        localMyApplaication.helper.isUserExct(localSQLiteDatabase, userNameEtx.getText().toString(), passwordEtx.getText().toString(), savaPassword);
    }



    private void writDb() {
        ((PageView) getActivity()).writDb();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.login, null, true);
        loadComponent(localView);
        return localView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setUser();

        }

    }

    /**
     * 向perfrence中防数据
     *
     * @param name
     * @param password
     */
    private void getSahedPerference(String name, String password, boolean savepass) {

        SharedPreferences preferences;

        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (!preferences.contains("user") || !preferences.getString("user", "").equals(name))
            editor.putString("name", name);


        if (savepass) {
            if (!preferences.contains("password") || !preferences.getString("password", "").equals(password))
                ;
            editor.putString("password", password);
        }
        editor.commit();
    }

    /**
     * 将用户信息写进手机,有信息就登录
     */
    public void setUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("name")) {
            userNameEtx.setText(sharedPreferences.getString("name", ""));
            if (sharedPreferences.contains("password")) {
                passwordEtx.setText(sharedPreferences.getString("password", ""));
               // conect(sharedPreferences.getString("name", ""), sharedPreferences.getString("password", ""));
                return;
            }
        }
    }

    private AlertDialog progerss;

    private void showProgress() {
        if (progerss == null) {
            progerss = new AlertDialog.Builder(getContext(), R.style.mydiaog).create();
            progerss.setView(LayoutInflater.from(getContext()).inflate(R.layout.progress, null, true));
            progerss.setCanceledOnTouchOutside(false);
            progerss.setCancelable(false);

        }
        progerss.show();
    }
    private void showToast(final String string) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                progerss.setCancelable(true);
                progerss.cancel();
                Toast.makeText(Login.this.getActivity(), string, Toast.LENGTH_LONG).show();
            }
        });
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Login
 * JD-Core Version:    0.6.2
 */