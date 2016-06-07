package views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;

import baihuiji.jkqme.baihuiji0.R;

/**
 * Created by Administrator on 2016/5/21.
 */
public class MyProgerss extends AlertDialog {

    protected MyProgerss(Context context) {
        super(context);

    }
    public static class MBuilder extends  Builder{
        public MBuilder(Context context) {
            super(context, R.style.mydiaog);
            setView(LayoutInflater.from(context).inflate(R.layout.progress,null,true));
        }
    }
}
