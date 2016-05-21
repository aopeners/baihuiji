package views;

import android.app.ProgressDialog;
import android.content.Context;

import baihuiji.jkqme.baihuiji.R;

/**
 * Created by Administrator on 2016/5/21.
 */
public class MyProgerss extends ProgressDialog {
    public MyProgerss(Context context) {
        super(context, R.style.theme_customer_progress_dialog);
    }
}
