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

import baihuiji.jkqme.baihuiji.R;

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

    private void loadFragment() {
        if (this.manager == null) {
            this.manager = getChildFragmentManager();
            this.transaction = this.manager.beginTransaction();
            this.transaction.add(R.id.home_page_fragment_linear, this.paylist_Fragment);
            this.transaction.add(R.id.home_page_fragment_linear, this.monthBill).hide(this.monthBill);
            transaction.add(R.id.home_page_fragment_linear, bil_detail).hide(bil_detail);
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
            this.transaction.commit();
            this.curentFragment = paramFragment;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.paylist_Fragment = new Paylist_Fragment();
        this.monthBill = new MonthBill();
        bil_detail = new Bil_detail();
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
     * @param paramInt 0 paylist  ,1 monthbill , 2billDetail
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
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Bill
 * JD-Core Version:    0.6.2
 */