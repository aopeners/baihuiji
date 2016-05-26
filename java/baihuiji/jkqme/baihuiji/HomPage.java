package baihuiji.jkqme.baihuiji;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import frament.Bill;
import frament.Home_fragment;
import frament.MineHome;
import frament.StatisticHome;
import views.MyViewPager;
import web.BaihuijiNet;
import web.Ip;

/**
 * 主fragment
 */

public class HomPage extends FragmentActivity {
    private Bill bill;
    private int[] btId = {R.id.buttom_radioh_bt1, R.id.buttom_radioh_bt2, R.id.buttom_radioh_bt3, R.id.buttom_radioh_bt4};
    private Button[] button = new Button[4];
    private Fragment currentFragment;
    private ArrayList<Fragment> flist = new ArrayList();
    private Home_fragment home_fragment;
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {
                case R.id.buttom_radioh_bt1:
                    HomPage.this.showFragment(HomPage.this.home_fragment);
                    //  Log.i("OnChekedRedio", "home");
                    break;
                case R.id.buttom_radioh_bt2:
                    HomPage.this.showFragment(HomPage.this.bill);
                    break;
                case R.id.buttom_radioh_bt3:
                    HomPage.this.showFragment(HomPage.this.statisticsFragment);
                    break;
                case R.id.buttom_radioh_bt4:
                    HomPage.this.showFragment(HomPage.this.mineHome);
                    break;
                case R.id.service_hone_cancel_tx:
                    dialog.setCancelable(true);
                    dialog.cancel();
                    break;
                case R.id.service_phone_call_tx:
                    dialog.setCancelable(true);
                    dialog.cancel();
                    onDownLoad();
                    break;
            }

        }
    };
    private MyViewPager mViewPager;
    private FragmentManager manager = getSupportFragmentManager();
    private MineHome mineHome;
    private pagerAdpters padpter;
    private LinearLayout rGroup;
    private OnCheckedChangeListener rlistener = new OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup paramAnonymousRadioGroup, int paramAnonymousInt) {
            Log.i("OnChekedRedio", "home");
            switch (paramAnonymousInt) {
                case R.id.buttom_radioh_bt1:
                    HomPage.this.showFragment(HomPage.this.home_fragment);
                    Log.i("OnChekedRedio", "home");
                    return;
                case R.id.buttom_radioh_bt2:
                    HomPage.this.showFragment(HomPage.this.bill);
                    return;
                case R.id.buttom_radioh_bt3:
                    HomPage.this.showFragment(HomPage.this.statisticsFragment);
                    return;
                case R.id.buttom_radioh_bt4:
                    HomPage.this.showFragment(HomPage.this.mineHome);
                    break;
            }

        }
    };
    private StatisticHome statisticsFragment;
    private FragmentTransaction transaction = this.manager.beginTransaction();

    /**
     * 初始化fragment
     */
    private void getFragment() {
        this.home_fragment = new Home_fragment();
        this.bill = new Bill();
        this.statisticsFragment = new StatisticHome();
        this.mineHome = new MineHome();
        this.transaction.add(R.id.home_page_activit_linear, this.home_fragment);
        this.transaction.add(R.id.home_page_activit_linear, this.bill).hide(this.bill);
        this.transaction.add(R.id.home_page_activit_linear, this.statisticsFragment).hide(this.statisticsFragment);
        this.transaction.add(R.id.home_page_activit_linear, this.mineHome).hide(this.mineHome);
        this.transaction.show(this.home_fragment);
        this.transaction.commit();
        this.currentFragment = this.home_fragment;

    }

    /**
     * 显示fragment,点击底部时调用
     *
     * @param paramFragment
     */
    private void showFragment(Fragment paramFragment) {//判断当前显示是否改变
        if (this.currentFragment.hashCode() != paramFragment.hashCode()) {
            this.transaction = this.manager.beginTransaction();
            this.transaction.hide(this.home_fragment).hide(this.mineHome).hide(this.bill).hide(this.statisticsFragment);
            this.transaction.show(paramFragment);
            this.transaction.commit();

            if (paramFragment.hashCode() == this.home_fragment.hashCode()) {

                upDateBottomState(0);

            } else if (paramFragment.hashCode() == this.bill.hashCode()) {
                upDateBottomState(1);

            } else if (paramFragment.hashCode() == this.statisticsFragment.hashCode()) {
                upDateBottomState(2);

            } else if (paramFragment.hashCode() == this.mineHome.hashCode()) {
                upDateBottomState(3);

            }
            this.currentFragment = paramFragment;
        }
    }

    /**
     * 显示首页
     */
    public void showHomeFragment() {
        showFragment(home_fragment);
        home_fragment.showFragment(0);
    }

    //绿图
    private int drawb1[] = {R.drawable.home, R.drawable.bill1, R.drawable.statistic1, R.drawable.mine1};
    //白图
    private int drawb[] = {R.drawable.home1, R.drawable.paylist, R.drawable.count, R.drawable.mine};

    /**
     * 更新底部栏，
     *
     * @param paramInt 被点击的按钮
     */
    private void upDateBottomState(int paramInt) {
        Button button;
        for (int i = 0; i < 4; i++) {
            button = (Button) findViewById(btId[i]);
            //如果不是选中的图片
            if (i != paramInt) {
                button.setTextColor(getResources().getColor(R.color.black));
                button.setCompoundDrawablesWithIntrinsicBounds(0, drawb[i], 0, 0);
            } else {
                button.setTextColor(getResources().getColor(R.color.bluetext));
                //  Log.i("upDateBottom", i + "  i   " + paramInt);
                button.setCompoundDrawablesWithIntrinsicBounds(0, drawb1[i], 0, 0);
            }
        }
    }

    public void finish() {
        super.finish();
    }

    public void hideButtom() {
        this.rGroup.setVisibility(View.GONE);
    }

    /**
     * 跳到扫码支付
     *
     * @param payType
     * @param moneycont
     * @param fukuanma
     */
    public void jumptoDecode(int payType, String moneycont, boolean fukuanma) {
        dialogCancle();
        Intent intent = new Intent(this, Decoder.class);
        Bundle bundle = new Bundle();
        bundle.putInt("payType", payType);
        bundle.putString("money", moneycont);
        bundle.putBoolean("fukuan", fukuanma);

        intent.putExtra("count", bundle);
        bundle.putBoolean("isRefund", false);
        startActivityForResult(intent, 1);
    }

    /**
     * 退款扫码
     */
    public void jumbtoDecoderForRefund() {
        dialogCancle();
        Intent intent = new Intent(this, Decoder.class);
        Bundle bundle = new Bundle();

        bundle.putBoolean("isRefund", true);
        intent.putExtra("count", bundle);

        startActivityForResult(intent, 1);
    }

    /**
     * 扫码退款返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //退款返回
        if (resultCode == 2) {
            String signal;
            Bundle bundle = data.getBundleExtra("signal");
            signal = bundle.getString("signal");

            showFragment(bill);
            bill.showFragment(2);

            bill.setSignal(signal);
            if (bundle.getBoolean("backSuccess", false)) {
                home_fragment.getDate();
            }
            dialogCancle();
            //收款返回
        } else if (resultCode == 3) {
            Bundle bundle = data.getBundleExtra("onTradSuccess");

            showFragment(home_fragment);
            home_fragment.showFragment(2);

            home_fragment.setTreadSuccessDate(bundle.getString("payType"),
                    bundle.getString("signal"), bundle.getString("paytime"),
                    bundle.getString("payTotal"));
            //设置日收入和月收入
            if (bundle.getBoolean("getSuccess", false)) {
                home_fragment.getDate();
                bill.onBillDateChange();
            }
            dialogCancle();
        } else {
            dialogCancle();
        }
    }

    /**
     * 退款页面调用
     */
    public void onRefound() {
        home_fragment.getDate();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setContentView(R.layout.home_page_activity);
        //最外层LinearLayout
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.home_liear);
        //视图结构变化时的监听
        linearLayout.addOnLayoutChangeListener(layoutListener);
        this.rGroup = ((LinearLayout) findViewById(R.id.buttom_radioh));
        //   this.rGroup.setOnCheckedChangeListener(this.rlistener);
        getFragment();
        for (int i = 0; i < 4; i++) {
            this.button[i] = ((Button) findViewById(this.btId[i]));
            this.button[i].setOnClickListener(this.listener);
        }
        new MyAsy().execute("");
    }

    /**
     * 视图结构变化时的监听,处理payList的bug
     */
    private View.OnLayoutChangeListener layoutListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View view, int left, int top, int right,
                                   int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            //键盘弹起,且控件高度大于0
            if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom) < 0) {
                rGroup.setAlpha(0);
                //键盘缩小
            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 0)) {
                rGroup.setAlpha(1);
            }
        }
    };

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == 4) {
            if (!onBack()) {
                return super.onKeyDown(paramInt, paramKeyEvent);
            }
            return onBack();
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    public void setSlipe(boolean paramBoolean) {
    }

    public void showButtom() {
        this.rGroup.setVisibility(View.VISIBLE);
    }

    /**
     * 按返回时的办法
     *
     * @return
     */
    private boolean onBack() {
        if (currentFragment.hashCode() == home_fragment.hashCode()) {

            return home_fragment.onBack();
        } else if (currentFragment.hashCode() == bill.hashCode()) {
            return bill.onBack();
        } else if (currentFragment.hashCode() == statisticsFragment.hashCode()) {
            return statisticsFragment.onBack();
        } else if (currentFragment.hashCode() == mineHome.hashCode()) {
            return mineHome.onBack();
        }
        return false;
    }

    public void showToast(final String string) {
        runOnUiThread(new Runnable() {
            public void run() {
                progerss.setCancelable(true);
                progerss.cancel();
                Toast.makeText(HomPage.this, string, Toast.LENGTH_LONG).show();
            }
        });
    }

    private AlertDialog progerss;

    public void showProgress() {
        if (progerss == null) {
            progerss = new AlertDialog.Builder(this, R.style.mydiaog).create();
            progerss.setView(LayoutInflater.from(this).inflate(R.layout.progress, null, true));
            progerss.setCanceledOnTouchOutside(false);
            progerss.setCancelable(false);
        }
        progerss.show();
    }

    //取消锁屏
    public void dialogCancle() {

        if (progerss != null) {
            progerss.setCancelable(true);
            progerss.cancel();
        }
    }

    //百汇集更新的异步
    private class MyAsy extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String string;
            return update();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("e")) {
                showNotification(s);
            }
        }
    }

    /**
     * 更新检查
     *
     * @return 更新号 如“1.0.1”，错误时返回"e"
     */
    private String update() {
        String requst = BaihuijiNet.connection(Ip.update);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(requst);
        } catch (JSONException e) {
            e.printStackTrace();
            return "e";
        }
        try {
            if (jsonObject.getString("errcode").equals("100")) {
                jsonObject = jsonObject.getJSONObject("respBody");
                return jsonObject.getString("wangqi");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "e";
        }
        return "e";
    }

    private AlertDialog dialog;
    private String apkName;

    /**
     * 显示更新
     *
     * @param string,更新版本，
     */
    private void showNotification(String string) {
        Log.i("Hompage", "版本号 " + string);
        MyApplaication applaication = (MyApplaication) getApplication();

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        if (!string.equals(applaication.getDate("update"))) {
            if (dialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(this).inflate(R.layout.service_phone_dialog, null, true);
                TextView textView = (TextView) view.findViewById(R.id.service_hone_cancel_tx);
                textView.setText("以后再说");
                textView.setOnClickListener(listener);
                textView = (TextView) view.findViewById(R.id.service_phone_call_tx);
                textView.setText("现在更新");
                textView.setOnClickListener(listener);
                textView = (TextView) view.findViewById(R.id.service_pone);
                textView.setText("白汇集商户版更新到" + string + "您要现在更新吗");
                builder.setView(view);
                dialog = builder.create();
                builder.setCancelable(false);
            }
            apkName = "BHJ_" + string + ".apk";
            dialog.show();
        }

    }

    /**
     * 下载跟新apk方法
     */
    private void onDownLoad() {
        new MyNotifi(apkName, this);
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.HomPage
 * JD-Core Version:    0.6.2
 */