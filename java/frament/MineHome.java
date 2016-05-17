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

import baihuiji.jkqme.baihuiji.R;

/**
 * Created by jkqme on 2016/5/15.
 */
public class MineHome extends Fragment {
    private Fragment curentFragment;
    private MineFragment mineFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private Modifi_password modifi_password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       mineFragment=new MineFragment();
        modifi_password=new Modifi_password();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.statistic_home,null,true);
        lodFragment();//
        return view;
    }
    private void lodFragment(){
        if(manager==null){
            manager=getChildFragmentManager();
            transaction=manager.beginTransaction();
            transaction.add(R.id.statistic_home_linear,mineFragment);
            transaction.add(R.id.statistic_home_linear,modifi_password).hide(modifi_password);
            transaction.show(mineFragment);
            transaction.commit();
            curentFragment=mineFragment;
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
    }
    /**
     *
     * @param paramInt 0 statisticfragment  ,1 modfipassword , 2billDetail
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
            case 2://showFragment(this.bil_detail);
                break;
        }

    }
}
