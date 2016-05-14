package adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import baihuiji.jkqme.baihuiji.R;

public class BillAdpter extends BaseAdapter {
    private Context context;
    private final long dayTime = 86400L;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> list;

    public BillAdpter(ArrayList<HashMap<String, String>> list) {
        this.list=list;
        notifyDataSetChanged();
    }

    public BillAdpter(ArrayList<HashMap<String, String>> paramArrayList, Context paramContext) {
        this.list = paramArrayList;
        this.context = paramContext;
        this.inflater = LayoutInflater.from(paramContext);
    }
    public void setList(ArrayList<HashMap<String, String>> list){
        this.list=list;
        notifyDataSetChanged();
    }
    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int paramInt) {
        return this.list.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        Holder localHolder = null;
        String str;
        if (paramView == null) {
            paramView = this.inflater.inflate(R.layout.lv_bill, null);
            localHolder = new Holder();
            localHolder.img = ((ImageView) paramView.findViewById(R.id.lv_message_pay_img));
            localHolder.lv_message_time_tx = ((TextView) paramView.findViewById(R.id.lv_message_time_tx));
            localHolder.lv_message_time1_tx = ((TextView) paramView.findViewById(R.id.lv_message_time1_tx));
            localHolder.lv_message_pay_tx = ((TextView) paramView.findViewById(R.id.lv_message_pay_tx));
            localHolder.lv_message_pay1_tx = ((TextView) paramView.findViewById(R.id.lv_message_pay1_tx));
            localHolder.lv_message_pay_num_tx = ((TextView) paramView.findViewById(R.id.lv_message_pay_num_tx));
            localHolder.lv_message_pay_num1_tx = ((TextView) paramView.findViewById(R.id.lv_message_pay_num1_tx));
            paramView.setTag(localHolder);

        } else {
            localHolder = (Holder) paramView.getTag();

        }
        str = (String) ((HashMap) this.list.get(paramInt)).get("payType");
        switch (str) {
            //ascall值对比，支付方式
            case "1":
                localHolder.img.setImageResource(R.drawable.payweichar);
                break;
            case "2":
                localHolder.img.setImageResource(R.drawable.qqperse);
                break;
            case "3":
                localHolder.img.setImageResource(R.drawable.ailipay);
                break;
            case "4":
                localHolder.img.setImageResource(R.drawable.baiduperse);
                break;
            default:
                break;
        }

        localHolder.lv_message_time_tx.setText((CharSequence) ((HashMap) this.list.get(paramInt)).get("payTime"));
        localHolder.lv_message_time1_tx.setText((CharSequence) ((HashMap) this.list.get(paramInt)).get("payTime"));
        localHolder.lv_message_pay1_tx.setText((CharSequence) ((HashMap) this.list.get(paramInt)).get("operateName"));
        localHolder.lv_message_pay_num_tx.setText((CharSequence) ((HashMap) this.list.get(paramInt)).get("ordPrice"));
        if (!((String) ((HashMap) this.list.get(paramInt)).get("ordState")).equals("0"))

            localHolder.lv_message_pay_num1_tx.setText("交易成功");
        else
            localHolder.lv_message_pay_num1_tx.setText("已退款");
        return paramView;
    }

    private class Holder {
        ImageView img;
        TextView lv_message_pay1_tx;
        TextView lv_message_pay_num1_tx;
        TextView lv_message_pay_num_tx;
        TextView lv_message_pay_tx;
        TextView lv_message_time1_tx;
        TextView lv_message_time_tx;

        private Holder() {
        }
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     adpter.BillAdpter
 * JD-Core Version:    0.6.2
 */