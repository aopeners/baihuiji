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
import frament.StatisticsFragment;
import views.MyViewPager;


public class HomPage extends FragmentActivity {
    private Bill bill;
    private int[] btId = {2131230784, 2131230785, 2131230786, 2131230787};
    private Button[] button = new Button[4];
    private Fragment currentFragment;
    private ArrayList<Fragment> flist = new ArrayList();
    private Home_fragment home_fragment;
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {
                default:
                    return;
                case R.id.home_page_radio_bt1:
                    HomPage.this.showFragment(HomPage.this.home_fragment);
                  //  Log.i("OnChekedRedio", "home");
                    return;
                case R.id.home_page_radio_bt2:
                    HomPage.this.showFragment(HomPage.this.bill);
                    return;
                case R.id.home_page_radio_bt3:
                    HomPage.this.showFragment(HomPage.this.statisticsFragment);
                    return;
                case R.id.home_page_radio_bt4:
            }
            HomPage.this.showFragment(HomPage.this.mineFragment);
        }
    };
    private MyViewPager mViewPager;
    private FragmentManager manager = getSupportFragmentManager();
    private MineFragment mineFragment;
    private pagerAdpters padpter;
    private RadioGroup rGroup;
    private OnCheckedChangeListener rlistener = new OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup paramAnonymousRadioGroup, int paramAnonymousInt) {
            Log.i("OnChekedRedio", "home");
            switch (paramAnonymousInt) {
                default:
                    return;
                case 2131230784:
                    HomPage.this.showFragment(HomPage.this.home_fragment);
                    Log.i("OnChekedRedio", "home");
                    return;
                case 2131230785:
                    HomPage.this.showFragment(HomPage.this.bill);
                    return;
                case 2131230786:
                    HomPage.this.showFragment(HomPage.this.statisticsFragment);
                    return;
                case 2131230787:
            }
            HomPage.this.showFragment(HomPage.this.mineFragment);
        }
    };
    private StatisticsFragment statisticsFragment;
    private FragmentTransaction transaction = this.manager.beginTransaction();

    /**
     * 初始化fragment
     */
    private void getFragment() {
        this.home_fragment = new Home_fragment();
        this.bill = new Bill();
        this.statisticsFragment = new StatisticsFragment();
        this.mineFragment = new MineFragment();
        this.transaction.add(R.id.home_page_activit_linear, this.home_fragment);
        this.transaction.add(R.id.home_page_activit_linear, this.bill).hide(this.bill);
        this.transaction.add(R.id.home_page_activit_linear, this.statisticsFragment).hide(this.statisticsFragment);
        this.transaction.add(R.id.home_page_activit_linear, this.mineFragment).hide(this.mineFragment);
        this.transaction.show(this.home_fragment);
        this.transaction.commit();
        this.currentFragment = this.home_fragment;
    }

    /**
     * 显示fragment,点击底部时调用
     * @param paramFragment
     */
    private void showFragment(Fragment paramFragment) {//判断当前显示是否改变
        if (this.currentFragment.hashCode() != paramFragment.hashCode()) {
            this.transaction = this.manager.beginTransaction();
            this.transaction.hide(this.home_fragment).hide(this.mineFragment).hide(this.bill).hide(this.statisticsFragment);
            this.transaction.show(paramFragment);
            this.transaction.commit();
            this.currentFragment = paramFragment;
            if (paramFragment.hashCode() == this.home_fragment.hashCode()) {

                upDateBottomState(0);
                return;
            } else if (paramFragment.hashCode() == this.bill.hashCode()) {
                upDateBottomState(0);
                return;
            } else if (paramFragment.hashCode() == this.statisticsFragment.hashCode()) {
                upDateBottomState(0);
                return;
            } else if (paramFragment.hashCode() == this.mineFragment.hashCode()) {
                upDateBottomState(0);
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
                this.button[paramInt].setTextColor(getResources().getColor(R.color.white));
                continue;
            }
            this.button[i].setTextColor(getResources().getColor(R.color.blue));
        }
    }

    public void finish() {
        super.finish();
    }

    public void hideButtom() {
        this.rGroup.setVisibility(View.GONE);
    }

    public void jumptoDecode() {
        startActivity(new Intent(this, Decoder.class));
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setContentView(R.layout.home_page_activity);
        this.rGroup = ((RadioGroup) findViewById(R.id.home_page_radio));
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