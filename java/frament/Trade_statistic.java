package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import baihuiji.jkqme.baihuiji.R;

/**
 * 交易统计有列表的fragment
 */
public class Trade_statistic extends Fragment {
    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View view=paramLayoutInflater.inflate(R.layout.trad_statistic_1, null, true);
        return view;
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Trade_statistic
 * JD-Core Version:    0.6.2
 */