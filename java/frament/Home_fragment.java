package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.R;

import java.lang.reflect.Field;

/**
 *
 */
public class Home_fragment extends Fragment {
    private CountFragment countFragment;
    private Fragment curentFragment;
    private Home_display_fragment hDisplay_fragment;
    private LinearLayout layout;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TreadSuccess treadSuccess;

    private void loadFragment() {
        if (this.manager == null) {
            this.manager = getChildFragmentManager();
            this.transaction = this.manager.beginTransaction();
            this.transaction.add(R.id.home_page_fragment_linear, this.hDisplay_fragment);
            this.transaction.add(R.id.home_page_fragment_linear, this.countFragment).hide(this.countFragment);
            transaction.add(R.id.home_page_fragment_linear, treadSuccess).hide(treadSuccess);
            this.transaction.show(this.hDisplay_fragment);
            this.transaction.commit();
            this.curentFragment = this.hDisplay_fragment;

        }
    }

    private void setSlip(boolean paramBoolean) {
        ((HomPage) getActivity()).setSlipe(paramBoolean);
    }

    private void showFragment(Fragment paramFragment) {
        if (this.curentFragment.hashCode() != paramFragment.hashCode()) {
            this.transaction = this.manager.beginTransaction();
            this.transaction.hide(this.curentFragment);
            this.transaction.show(paramFragment);
            this.transaction.commit();
            this.curentFragment = paramFragment;
        }
        if (curentFragment.hashCode() == hDisplay_fragment.hashCode()) {
            ((HomPage) getActivity()).showButtom();
        } else {
            ((HomPage) getActivity()).hideButtom();
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.hDisplay_fragment = new Home_display_fragment();
        this.countFragment = new CountFragment();
        treadSuccess = new TreadSuccess();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.home_page_fragment, null, true);
        loadFragment();
        return localView;
    }

    /**
     * 防止suporV4bug
     */
    public void onDetach() {
        super.onDetach();
        try {
            Field localField = Fragment.class.getDeclaredField("mChildFragmentManager");
            localField.setAccessible(true);
            localField.set(this, null);
            return;
        } catch (NoSuchFieldException localNoSuchFieldException) {
            throw new RuntimeException(localNoSuchFieldException);
        } catch (IllegalAccessException localIllegalAccessException) {
            throw new RuntimeException(localIllegalAccessException);
        }
    }

    public void showFragment(int paramInt) {
        switch (paramInt) {

            case 0:
                showFragment(this.hDisplay_fragment);

                return;
            case 1:
                showFragment(this.countFragment);
                break;
            case 2:
                showFragment(treadSuccess);
                break;
            default:
                return;
        }

    }

    public void setType(int type) {
        countFragment.setPayType(type);
    }

    /**
     * 按返回时的监听
     *
     * @return
     */
    public boolean onBack() {
        if (curentFragment.hashCode() == hDisplay_fragment.hashCode()) {
            //key退出
            return false;
        } else {
            showFragment(hDisplay_fragment);
            ((HomPage) getActivity()).showButtom();
            return true;
        }
    }

    /**
     * 显示设置
     *
     * @param paytype  支付类型
     * @param signal   订单号
     * @param paytime  支付时间
     * @param payTotal 支付金额
     */
    public void setTreadSuccessDate(String paytype, String signal, String paytime, String payTotal) {
        treadSuccess.setDate(paytype, signal, paytime, payTotal);
    }
}
