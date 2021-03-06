package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.HashMap;

import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji0.R;

/**
 * 管理账单的地一层fragment
 */
public class Bill extends Fragment {
    private Fragment curentFragment;
    private FragmentManager manager;
    private MonthBill monthBill;
    private Paylist_Fragment paylist_Fragment;
    private FragmentTransaction transaction;
    private Bil_detail bil_detail;
    private DayBill dayBill;

    private void loadFragment() {
        if (this.manager == null) {
            this.manager = getChildFragmentManager();
            this.transaction = this.manager.beginTransaction();
            this.transaction.add(R.id.home_page_fragment_linear, this.paylist_Fragment);
            this.transaction.add(R.id.home_page_fragment_linear, this.monthBill).hide(this.monthBill);
            transaction.add(R.id.home_page_fragment_linear, bil_detail).hide(bil_detail);
            transaction.add(R.id.home_page_fragment_linear,dayBill).hide(dayBill);
            this.transaction.show(this.paylist_Fragment);
            this.transaction.commit();
            this.curentFragment = this.paylist_Fragment;
        }
    }

    private void showFragment(Fragment paramFragment) {
        if (this.curentFragment.hashCode() != paramFragment.hashCode()) {
            this.transaction = this.manager.beginTransaction();
            this.transaction.hide(this.curentFragment);
            this.transaction.show(paramFragment);
            this.transaction.commitAllowingStateLoss();
            this.curentFragment = paramFragment;
        }
        if (curentFragment.hashCode() == paylist_Fragment.hashCode()) {
            ((HomPage) getActivity()).showButtom();
        } else {
            ((HomPage) getActivity()).hideButtom();
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.paylist_Fragment = new Paylist_Fragment();
        this.monthBill = new MonthBill();
        bil_detail = new Bil_detail();
        dayBill=new DayBill();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.home_page_fragment, null, true);
        loadFragment();
        return localView;
    }

    /**
     * 避免suportv4,fragment bug
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

    /**
     * @param paramInt 0 paylist  ,1 monthbill , 2billDetail 3.dayBill
     */
    public void showFragment(int paramInt) {
        switch (paramInt) {
            default:
                return;
            case 0:
                showFragment(this.paylist_Fragment);
                return;
            case 1:
                showFragment(this.monthBill);
                break;
            case 2:
                showFragment(this.bil_detail);
                break;
            case 3:
                showFragment(dayBill);
                break;
        }

    }

    public void showBillDetail(HashMap<String, String> map) {
        bil_detail.setDate(map);
        showFragment(2);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (paylist_Fragment != null) {
            paylist_Fragment.onHiddenChanged(hidden);
        }

    }
    public void setDayBill(String time){
        showFragment(dayBill);
        dayBill.setTime(time);
    }
    /**
     * 按返回时的监听
     * @return
     */
    public boolean onBack(){
        if(curentFragment.hashCode()==paylist_Fragment.hashCode()){
            //key退出
            return false;
        }else {
            showFragment(paylist_Fragment);
            ((HomPage)getActivity()).showButtom();
            return true;
        }
    }

    /**
     * 订单数据改变时
     */
    public void onBillDateChange(){
        paylist_Fragment.onDateChange();
    }
    /**
     * 设置订单
     */
    public void setSignal(String signal){
        paylist_Fragment.setSignal(signal);
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Bill
 * JD-Core Version:    0.6.2
 */