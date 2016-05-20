package frament;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import baihuiji.jkqme.baihuiji.MyApplaication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;

public class MineFragment extends Fragment {
    private ImageView imageView;
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {

                case R.id.mine_loginout_tx:
                    MineFragment.this.loginOut();
                    break;
                case R.id.mine_password_linear:
                    jump(1);
                    break;
                case R.id.mine_aboutus:
                    jump(2);
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
            return false;
        }
        try {
            String str2 = localJSONObject1.getString("msg");
            if (str2.equals("登出成功"))
                return true;
        } catch (JSONException localJSONException1) {
            return false;
        }
        return false;
    }

    private void loadComponent(View paramView) {
        MyApplaication localMyApplaication = (MyApplaication) getActivity().getApplication();
        imageView = (ImageView) paramView.findViewById(R.id.mine_head_img);
        this.loginout = ((TextView) paramView.findViewById(R.id.mine_loginout_tx));
        this.loginout.setOnClickListener(this.listener);
        LinearLayout linearLayout = (LinearLayout) paramView.findViewById(R.id.mine_password_linear);
        linearLayout.setOnClickListener(listener);
        linearLayout= (LinearLayout) paramView.findViewById(R.id.mine_aboutus);
        linearLayout.setOnClickListener(listener);
        int textId[] = {R.id.mine_operater_tx, R.id.mine_operater_account_tx, R.id.mine_operater_logintime_tx, R.id.mine_merchantId_tx, R.id.mine_merchantName_tx

        };
        String text[] = {
                "操作员:" + localMyApplaication.getDate("operateName"), "账号:" + localMyApplaication.getDate("operateTel"),
                "上次登录时间:" + localMyApplaication.getDate("time"), localMyApplaication.getDate("merchantId"), localMyApplaication.getDate("merName")
        };
        TextView textView;
        for (int i = 0; i < textId.length; i++) {
            textView = (TextView) paramView.findViewById(textId[i]);
            textView.setText(text[i]);
        }
        loadImg();
    }

    private void loadImg() {
        MyApplaication localMyApplaication = (MyApplaication) getActivity().getApplication();
        new MyAsy().execute(localMyApplaication.getDate("logo"));
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
        getParentFragment().getActivity().finish();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.mine, null, true);
        loadComponent(localView);
        return localView;
    }

    private void jump(int a) {
        MineHome mineHome = (MineHome) getParentFragment();
        mineHome.showFragment(a);
    }

    /**
     * 获取用户头像
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Bitmap getHeadImg(String bitUrl) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.outHeight = 100;
        options.outWidth = 100;
        options.inSampleSize = 2;//图片变为原来1/2
        URL url = null;
        HttpURLConnection connection;
        Bitmap bitma = null;
        try {
            url = new URL(bitUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //可以实例化option的其他参数
                bitma = BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return bitma;
        }
        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitma;
    }

    class MyAsy extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String str = strings[0];
            return getHeadImg(str);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.MineFragment
 * JD-Core Version:    0.6.2
 */