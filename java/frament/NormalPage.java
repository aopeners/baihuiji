package frament;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import baihuiji.jkqme.baihuiji.R;

public class NormalPage extends Fragment {
    private ImageView img;
    private int showId;

    public void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    @Nullable
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.nomalpage, null, true);
        this.img = ((ImageView) localView.findViewById(R.id.nomalpage_img));
        this.showId = getArguments().getInt("showId");
        switch (this.showId) {

            case 0:
                this.img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.img1));
                break;
            case 1:
                this.img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.img2));
                break;
            case 2:

                this.img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.img3));
                break;
        }
        return localView;
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.NormalPage
 * JD-Core Version:    0.6.2
 */