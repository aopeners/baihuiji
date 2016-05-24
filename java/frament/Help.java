package frament;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import adpter.HelpAdpter;
import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.R;

/**
 * 我的————帮助中心
 * Created by Administrator on 2016/5/24.
 */
public class Help extends Fragment {
    private View view;
    private ListView listView;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.help, null, true);
        imageView = (ImageView) view.findViewById(R.id.count_back);
        imageView.setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((MineHome) getParentFragment()).showFragment(0);
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && listView == null) {

            listView = (ListView) view.findViewById(R.id.help_lv);
            HelpAdpter adpter = new HelpAdpter(getParentFragment().getContext());
            listView.setAdapter(adpter);
            getAttributs(imageView, listView);
        }
    }

    private boolean isMesurd = false;

    private void getAttributs(View views, final View view2) {
        final View view = views;
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {
                        // 设置margin
                        if (!isMesurd) {
                            int height;
                            height = view.getHeight();
                            int heights = ((MyApplaication) getParentFragment().getActivity().getApplication()).getHeight();
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view2.getLayoutParams();
                            params.height = heights - height;
                            isMesurd = true;
                        }
                    }
                });
    }
}
