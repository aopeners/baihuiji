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
import baihuiji.jkqme.baihuiji.R;

/**
 * Created by jkqme on 2016/5/15.
 */
public class StatisticHome extends Fragment {
    private Fragment curentFragment;
    private Trade_statistic trade_statistic;//交易统计，列表项
    private StatisticsFragment statisticsFragment;//交易统计主显示
    private FragmentManager manager;
    private FragmentTransaction  transaction;
    private MonyStatisticByType monyStatisticByType;
    private TradeStatisticSaleDayCount tradeStatisticSaleDayCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      trade_statistic=new Trade_statistic();
       // statisticsFragment=new StatisticsFragment();
        monyStatisticByType=new MonyStatisticByType();
        tradeStatisticSaleDayCount=new TradeStatisticSaleDayCount();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.statistic_home,null,true);
        lodFragment();
        return view;
    }
    private void lodFragment(){
        if(manager==null){
            manager=getChildFragmentManager();
            transaction=manager.beginTransaction();
            //transaction.add(R.id.statistic_home_linear,statisticsFragment);
             transaction.add(R.id.statistic_home_linear,trade_statistic);
            transaction.add(R.id.statistic_home_linear,monyStatisticByType).hide(monyStatisticByType);
            transaction.add(R.id.statistic_home_linear,tradeStatisticSaleDayCount).hide(tradeStatisticSaleDayCount);
            transaction.show(trade_statistic);
            transaction.commit();
            curentFragment=trade_statistic;
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
        if(paramFragment==null){
            return;
        }
        if (this.curentFragment.hashCode() != paramFragment.hashCode()) {
            this.transaction = this.manager.beginTransaction();
            this.transaction.hide(this.curentFragment);
            this.transaction.show(paramFragment);
            this.transaction.commit();
            this.curentFragment = paramFragment;
        }
        if (curentFragment.hashCode() == trade_statistic.hashCode()) {
            ((HomPage) getActivity()).showButtom();
        } else {
            ((HomPage) getActivity()).hideButtom();
        }
    }
    /**
     *
     * @param paramInt 0 statisticfragment  ,1 trad_statistic , 2 monyStatistic
     */
    public void showFragment(int paramInt) {
        switch (paramInt) {
            default:
                return;
            case 0:
               // showFragment(statisticsFragment);
                return;
            case 1:
                showFragment(this.trade_statistic);
                break;

            case 2:showFragment(monyStatisticByType);
                break;
            case 3:showFragment(tradeStatisticSaleDayCount);
                break;
        }

    }

    /**
     * 切换加载数据
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(trade_statistic!=null){
            trade_statistic.onHiddenChanged(hidden);
        }
    }

    /**
     * 收入统计子项设置
     * @param requant  请求地址
     * @param payType  交易类型
     * @param time   时间
     * @param payTotleNumber  总共收款笔数
     */
    public void setMonyStatisticByType(String requant,int payType,String time,String payTotleNumber){
        monyStatisticByType.setRequst(requant,payType,time,payTotleNumber);
    }
    public void setTradeStatisticSaleDayCount(String requant,int payType,String time,String payTotleNumber){
        tradeStatisticSaleDayCount.setRequst(requant,payType,time,payTotleNumber);
    }
    /**
     * 按返回时的监听
     * @return
     */
    public boolean onBack(){
        if(curentFragment.hashCode()==trade_statistic.hashCode()){
            //key退出
            return false;
        }else {
            showFragment(trade_statistic);
            ((HomPage)getActivity()).showButtom();
            return true;
        }
    }
}
