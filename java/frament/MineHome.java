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

import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji0.R;

/**
 * Created by jkqme on 2016/5/15.
 */
public class MineHome extends Fragment {
    private Fragment curentFragment;
    private MineFragment mineFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private AboutUs aboutUs;
    private Modifi_password modifi_password;
    private Help help;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mineFragment = new MineFragment();
        modifi_password = new Modifi_password();
        aboutUs = new AboutUs();
        help = new Help();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistic_home, null, true);
        lodFragment();//
        return view;
    }

    private void lodFragment() {
        if (manager == null) {
            manager = getChildFragmentManager();
            transaction = manager.beginTransaction();
            transaction.add(R.id.statistic_home_linear, mineFragment);
            transaction.add(R.id.statistic_home_linear, modifi_password).hide(modifi_password);
            transaction.add(R.id.statistic_home_linear, aboutUs).hide(aboutUs);
            transaction.add(R.id.statistic_home_linear, help).hide(help);
            transaction.show(mineFragment);
            transaction.commit();
            curentFragment = mineFragment;
        }
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

    private void showFragment(Fragment paramFragment) {
        if (this.curentFragment.hashCode() != paramFragment.hashCode()) {
            this.transaction = this.manager.beginTransaction();
            this.transaction.hide(this.curentFragment);
            this.transaction.show(paramFragment);
            this.transaction.commit();
            this.curentFragment = paramFragment;
        }
        if (curentFragment.hashCode() == mineFragment.hashCode()) {
            ((HomPage) getActivity()).showButtom();
        } else {
            ((HomPage) getActivity()).hideButtom();
        }
    }

    /**
     * @param paramInt 0 statisticfragment  ,1 modfipassword , 2aboutUs
     */
    public void showFragment(int paramInt) {
        switch (paramInt) {
            default:
                return;
            case 0:
                showFragment(mineFragment);
                return;
            case 1:
                showFragment(this.modifi_password);
                break;
            case 2:
                showFragment(this.aboutUs);
                break;
            case 3:
                showFragment(this.help);
                break;
        }

    }

    /**
     * 按返回时的监听
     *
     * @return
     */
    public boolean onBack() {
        if (curentFragment.hashCode() == mineFragment.hashCode()) {
            //key退出
            return false;
        } else {
            showFragment(mineFragment);
            ((HomPage) getActivity()).showButtom();
            return true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mineFragment != null) {
            mineFragment.onHiddenChanged(hidden);
        }
    }
}
