package baihuiji.jkqme.baihuiji;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.ArrayList;

import frament.Bill;
import frament.Home_fragment;
import frament.MineFragment;
import frament.MineHome;
import frament.StatisticHome;
import frament.StatisticsFragment;
import views.MyViewPager;

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
            }

        }
    };
    private MyViewPager mViewPager;
    private FragmentManager manager = getSupportFragmentManager();
    private MineHome mineHome;
    private pagerAdpters padpter;
    private RadioGroup rGroup;
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
            this.currentFragment = paramFragment;
            if (paramFragment.hashCode() == this.home_fragment.hashCode()) {

                upDateBottomState(0);
                return;
            } else if (paramFragment.hashCode() == this.bill.hashCode()) {
                upDateBottomState(1);
                return;
            } else if (paramFragment.hashCode() == this.statisticsFragment.hashCode()) {
                upDateBottomState(2);
                return;
            } else if (paramFragment.hashCode() == this.mineHome.hashCode()) {
                upDateBottomState(3);
                return;
            }
        }
    }

    /**
     * 更新底部栏，
     *
     * @param paramInt 被点击的按钮
     */
    private void upDateBottomState(int paramInt) {
        for (int i = 0; i < 4; i++) {
            if (i != paramInt) {
                this.button[paramInt].setTextColor(getResources().getColor(R.color.black));
                continue;
            }
            this.button[i].setTextColor(getResources().getColor(R.color.bluetext));
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
    public void jumptoDecode(int payType, float moneycont, boolean fukuanma) {
        Intent intent = new Intent(this, Decoder.class);
        Bundle bundle = new Bundle();

        bundle.putInt("payType", payType);
        bundle.putFloat("money", moneycont);
        bundle.putBoolean("fukuan", fukuanma);
        intent.putExtra("count", bundle);

        startActivity(intent);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setContentView(R.layout.home_page_activity);
        this.rGroup = ((RadioGroup) findViewById(R.id.buttom_radioh));
        this.rGroup.setOnCheckedChangeListener(this.rlistener);
        getFragment();
        for (int i = 0; ; i++) {
            if (i >= 4)
                return;
            this.button[i] = ((Button) findViewById(this.btId[i]));
            this.button[i].setOnClickListener(this.listener);
        }
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == 4)
            return true;
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    public void setSlipe(boolean paramBoolean) {
    }

    public void showButtom() {
        this.rGroup.setVisibility(View.VISIBLE);
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.HomPage
 * JD-Core Version:    0.6.2
 */