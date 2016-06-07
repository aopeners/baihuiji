package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import baihuiji.jkqme.baihuiji.PageView;
import baihuiji.jkqme.baihuiji0.R;

public class Viewpage4 extends Fragment {
    private TextView img;
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            Viewpage4.this.onClicks();
        }
    };

    private void onClicks() {
        ((PageView) getActivity()).jumptoLogin();
    }

    @Nullable
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.viewpage4, null, true);
        this.img = ((TextView) localView.findViewById(R.id.viewpage4_img_bt));
        this.img.setOnClickListener(this.listener);
        return localView;
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Viewpage4
 * JD-Core Version:    0.6.2
 */