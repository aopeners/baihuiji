package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import baihuiji.jkqme.baihuiji.R;

/**
 * Created by jkqme on 2016/5/15.
 */
public class Modifi_password extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.modifi_password,null,true);
        loadComponent(view);
        return view;
    }
    private void loadComponent(View view){
        ImageView imageView= (ImageView) view.findViewById(R.id.modifi_password_back);
        imageView.setOnClickListener(listener);
        TextView textView= (TextView) view.findViewById(R.id.modifi_password_verify_tx);
        textView.setOnClickListener(listener);
    }
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.modifi_password_back:
                    showMimeFragement();
                    break;
                case R.id.modifi_password_verify_tx:
                    break;
            }
        }
    };
    private void showMimeFragement(){
        MineHome mineHome= (MineHome) getParentFragment();
        mineHome.showFragment(0);
    }

    /**
     * 修改密码的方法
     */
    private void onModify(){
        View view=getView();
        EditText editText1= (EditText) view.findViewById(R.id.modifi_password_etx);
        EditText editText2= (EditText) view.findViewById(R.id.modifi_password_new_etx);
        EditText editText3= (EditText) view.findViewById(R.id.modifi_password_verify_etx);
        if(editText2.getText().toString().trim().endsWith(editText3.toString().trim())){

        }
    }
}
