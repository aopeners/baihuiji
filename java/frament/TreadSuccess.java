package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.R;

/**
 * 支付成功后显示的页面
 * Created by Administrator on 2016/5/20.需要外部传入的数据：支付方式，订单号，支付时间,交易金额
 */
public class TreadSuccess extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.decod_sucess, null, true);
        TextView textView = (TextView) view.findViewById(R.id.bill_detail_refund_bt_tx);
        textView.setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //返回首页
                case R.id.bill_detail_refund_bt_tx:
                    onGoHome();
                    break;
            }
        }
    };

    /**
     * 显示首页
     */
    private void onGoHome() {
        Home_fragment home_fragment = (Home_fragment) getParentFragment();
        home_fragment.showFragment(0);
    }

    /**
     * 显示设置
     *
     * @param paytype  支付类型
     * @param signal   订单号
     * @param paytime  支付时间
     * @param payTotal 支付金额
     */
    public void setDate(String paytype, String signal, String paytime, String payTotal) {
        MyApplaication applaication = (MyApplaication) getParentFragment().getActivity().getApplication();
        //控件Id
        int textId[] = {R.id.bill_detail_mony_tx, R.id.bill_money_tx, R.id.bill_detail_marchtName_tx, R.id.bill_detail_marchtShop_tx, R.id.bill_detail_operater_tx,
                R.id.bill_detail_pay_tx, R.id.bill_detail_trade_tx, R.id.bill_detail_trade_time_tx};
        TextView textView;
        if (paytype.equals("1")) {
            paytype = "微信支付";
        } else if (paytype.equals("2")) {
            paytype = "QQ钱包";
        } else if (paytype.equals("3")) {
            paytype = "支付宝";
        } else if (paytype.equals("4")) {
            paytype = "百度钱包";
        }
        //控件值
        String text[] = {applaication.getDate("merName"), "¥" + payTotal, applaication.getDate("company"),
                applaication.getDate("merName"), applaication.getDate("operateTel"), paytype, signal, paytime
        };
        for (int i = 0; i < textId.length; i++) {
            textView = (TextView) view.findViewById(textId[i]);
            textView.setText(text[i]);
        }
    }
}
