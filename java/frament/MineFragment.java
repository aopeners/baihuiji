package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import baihuiji.jkqme.baihuiji.MyApplaication;

import org.json.JSONException;
import org.json.JSONObject;

import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;

public class MineFragment extends Fragment {
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {

                case R.id.mine_loginout_tx:
                    MineFragment.this.loginOut();
                    break;
                default:
                    return;
            }

        }
    };
    private String loginOutRequst;
    private TextView loginout;
    private TextView textView;

    private boolean isloginout(String paramString) {
        JSONObject localJSONObject1 = null;
        try {
            localJSONObject1 = new JSONObject(paramString);
        } catch (JSONException localJSONException2) {
        }
        try {
            String str2 = localJSONObject1.getString("msg");
            if (!str2.equals("登出成功"))
                return true;
        } catch (JSONException localJSONException1) {
        }
        return false;
    }

    private void loadComponent(View paramView) {
        this.loginout = ((TextView) paramView.findViewById(R.id.mine_loginout_tx));
        this.loginout.setOnClickListener(this.listener);
    }

    private void loginOut() {
        MyApplaication localMyApplaication = (MyApplaication) getActivity().getApplication();
        String[] arrayOfString1 = {"posCode", "merchantId", "operateId", "MD5"};
        String str1 = localMyApplaication.getDate("posCode");
        String str2 = localMyApplaication.getDate("merchantId");
        String str3 = localMyApplaication.getDate("operateId");
        String str4 = "posCode&merchantId&operateId&=*" + str1 + "*" + str2 + "*" + str3;
        String[] arrayOfString2 = new String[4];
        arrayOfString2[0] = str1;
        arrayOfString2[1] = str2;
        arrayOfString2[2] = str3;
        arrayOfString2[3] = BaihuijiNet.getMd5_32(str4);
        final String str5 = BaihuijiNet.getJson(arrayOfString1, arrayOfString2, "MD5");
        Log.i("logingout", str5);
        new Thread(new Runnable() {
            public void run() {
                MineFragment.this.loginOutRequst = BaihuijiNet.urlconection("http://baihuiji.weikebaba.com/pospay/posGoOut", str5);
                Log.i("onLoginOUt", MineFragment.this.loginOutRequst);
                if (MineFragment.this.isloginout(MineFragment.this.loginOutRequst))
                    MineFragment.this.toLoginout();
            }
        }).start();
    }

    private void toLoginout() {
        getActivity().finish();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.mine, null, true);
        loadComponent(localView);
        return localView;
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.MineFragment
 * JD-Core Version:    0.6.2
 */