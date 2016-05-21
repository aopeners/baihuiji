package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        final MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        new Thread(new Runnable() {
            @Override
            public void run() {
                EditText editText1 = (EditText) view.findViewById(R.id.modifi_password_etx);
                EditText editText2 = (EditText) view.findViewById(R.id.modifi_password_new_etx);
                EditText editText3 = (EditText) view.findViewById(R.id.modifi_password_verify_etx);
                //初始密码正确
                if (editText1.getText().toString().trim().equals(applaication.getDate("password"))) {
                    String key[] = {"password", "uId", "MD5"};

                    String value[] = {editText2.getText().toString(),
                            applaication.getDate("operateTel"), BaihuijiNet.getMd5_32("password&uId&=*" +
                            editText2.getText().toString() + "*" + applaication.getDate("operateTel"))};
                    String json = BaihuijiNet.getJson(key, value, "MD5");
                    String requst;
                    if (editText2.getText().toString().trim().equals(editText3.toString().trim())) {
                        requst = BaihuijiNet.urlconection(Ip.upPass, json);
                        Log.i("ModifiPassword", requst);
                    } else {
                        showToast("新密码不一致");
                    }
                } else {
                    showToast("初始密码错误");
                }
            }
        }).start();

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

    private void showToast(final String toast) {
        getParentFragment().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getParentFragment().getActivity(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
