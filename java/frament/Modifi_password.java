package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;
import web.Ip;

/**
 * Created by jkqme on 2016/5/15.
 */
public class Modifi_password extends Fragment {
    private View view;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modifi_password, null, true);
        loadComponent(view);
        return view;
    }

    private void loadComponent(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.modifi_password_back);
        imageView.setOnClickListener(listener);
        TextView textView = (TextView) view.findViewById(R.id.modifi_password_verify_tx);
        textView.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.modifi_password_back:
                    showMimeFragement();
                    break;
                case R.id.modifi_password_verify_tx:
                    onModify();
                    break;
            }
        }
    };

    private void showMimeFragement() {
        MineHome mineHome = (MineHome) getParentFragment();
        mineHome.showFragment(0);
    }

    /**
     * 修改密码的方法
     */
    private void onModify() {
        showProgress();
        final MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        new Thread(new Runnable() {
            @Override
            public void run() {
                EditText editText1 = (EditText) view.findViewById(R.id.modifi_password_etx);
                EditText editText2 = (EditText) view.findViewById(R.id.modifi_password_new_etx);
                EditText editText3 = (EditText) view.findViewById(R.id.modifi_password_verify_etx);
                //初始密码正确
                if (editText1.getText().toString().trim().equals(applaication.getDate("password"))) {
                    String key[] = {"updPass", "uId", "MD5"};

                    String value[] = {editText2.getText().toString(),
                            applaication.getDate("operateTel"), BaihuijiNet.getMd5_32("password&uId&=*" +
                            editText2.getText().toString() + "*" + applaication.getDate("operateTel"))};
                    String json = BaihuijiNet.getJson(key, value, "MD5");
                    String requst;
                    if (editText2.getText().toString().trim().equals(editText1.getText().toString().trim())) {
                        showToast("新密码与原密码一致");
                    } else if (editText2.getText().toString().trim().equals(editText3.getText().toString().trim())) {
                        requst = BaihuijiNet.urlconection(Ip.upPass, json);
                        Log.i("ModifiPassword", requst);
                        if (modifySucess(requst)) {
                            applaication.putData("password", editText1.getText().toString().trim());
                            showToast("修改成功");
                        }else {
                            showToast("修改失败");
                        }

                    } else {
                        showToast("新密码不一致");
                    }
                } else {
                    showToast("初始密码错误");
                }
            }
        }).start();

    }

    private boolean modifySucess(String requst) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(requst);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        try {
            if (jsonObject.getString("errcode").equals("100")) {
                return true;
            } else return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (view != null) {
                EditText editText1 = (EditText) view.findViewById(R.id.modifi_password_etx);
                EditText editText2 = (EditText) view.findViewById(R.id.modifi_password_new_etx);
                EditText editText3 = (EditText) view.findViewById(R.id.modifi_password_verify_etx);
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
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
                Toast.makeText(getParentFragment().getActivity(), string, Toast.LENGTH_LONG).show();
            }
        });
    }
}
