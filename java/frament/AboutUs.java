package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import baihuiji.jkqme.baihuiji.R;

/**
 * Created by Administrator on 2016/5/20.
 */
public class AboutUs extends Fragment {

    public void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    @Nullable
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.mine_about_us, null, true);
        ImageView img;
        img = ((ImageView) localView.findViewById(R.id.count_back));
        img.setOnClickListener(listener);
        return localView;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showMine();
        }
    };

    private void showMine() {
        MineHome mineHome = (MineHome) getParentFragment();
        mineHome.showFragment(0);
    }
}
