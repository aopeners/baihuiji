package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.R;

/**
 * 账单详情,payListFragment 子项
 */
public class Bil_detail extends Fragment {
    private boolean state;

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View view = paramLayoutInflater.inflate(R.layout.bill_detai, null);
        loadComponet(view);
        return view;
    }

    private void loadComponet(View view) {
        ImageView imgback = (ImageView) view.findViewById(R.id.bill_detail_back);
        TextView refund = (TextView) view.findViewById(R.id.bill_detail_refund_bt_tx);
        imgback.setOnClickListener(listener);
        refund.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bill_detail_back:
                    jumptoPaylist();
                    break;
                case R.id.bill_detail_refund_bt_tx:
                    break;
            }
        }
    };

    private void jumptoPaylist() {
        ((Bill) getParentFragment()).showFragment(0);
    }

    public void setDate(HashMap<String, String> map) {
        if (map.get("ordState").equals("1")) {
            state = true;
        } else {
            state = false;
        }
        setState(state, map); 
    }

    /**
     * 设置显示状态
     *
     * @param state true 付款
     * @param map 单个账单详情组
     */
    private void setState(boolean state,HashMap<String, String> map) {
        MyApplaication applaication= (MyApplaication) getParentFragment().getActivity().getApplication();
       View view=getView();
        //会改变状态id 0,   lenth-1,lenth-2 gone
        int tid[] = {R.id.bill_detail_mony_tx, R.id.bill_money_tx, R.id.bill_detail_marchtName_tx,
                R.id.bill_detail_marchtShop_tx, R.id.bill_detail_operater_tx, R.id.bill_detail_state_tx,
                R.id.bill_detail_pay_tx, R.id.bill_detail_trade_tx, R.id.bill_detail_trade_time_tx,
                R.id.bill_detail_refund_time_tx, R.id.bill_detail_refund_bt_tx};
        TextView textView;
        if(state){
           for(int i=0;i<tid.length;i++){
               textView= (TextView) view.findViewById(tid[i]);
               if(i==0){textView.setText("付款金额");}else if(i==5){
                   textView.setText("支付成功");
               }else if(i==tid.length-2){
                   textView.setVisibility(View.GONE);
               }else if(i==tid.length-1){
                   textView.setVisibility(View.VISIBLE);
               }
               switch (i){
                   case 1: textView.setText(map.get("ordPrice"));break;
                   case 2: textView.setText(applaication.getDate("shopName"));break;
                   case 3:textView.setText(applaication.getDate("merName"));break;
                   case 4:textView.setText(applaication.getDate("operateName"));break;
                   case 6:setePayWay(textView, map.get("payType"));break;
                   case 7:textView.setText(map.get("singal"));break;
                   case 8:textView.setText(map.get("payTime"));break;

               }

           }
        }else {
            for(int i=0;i<tid.length;i++){
                textView= (TextView) view.findViewById(tid[i]);
                if(i==0){textView.setText("退款金额");}else if(i==5){
                    textView.setText("已退款");
                }else if(i==tid.length-2){
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(map.get("backTime"));
                }else if(i==tid.length-1){
                    textView.setVisibility(View.GONE);
                }
                switch (i){
                    case 1: textView.setText(map.get("ordPrice"));break;
                    case 2: textView.setText(applaication.getDate("shopName"));break;
                    case 3:textView.setText(applaication.getDate("merName"));break;
                    case 4:textView.setText(applaication.getDate("operateName"));break;
                    case 6:setePayWay(textView, map.get("payType"));break;
                    case 7:textView.setText(map.get("singal"));break;
                    case 8:textView.setText(map.get("payTime"));break;

                }

            }
        }
    }

    /**
     * 设置支付方式
     * @param textView
     * @param way
     */
    private void setePayWay(TextView textView,String way){
        switch (way){
            case "1":textView.setText("微信支付");break;
            case "2":textView.setText("qq钱包");break;
            case "3":textView.setText("支付宝");break;
            case "4":textView.setText("百度钱包");break;
        }
    }
}

