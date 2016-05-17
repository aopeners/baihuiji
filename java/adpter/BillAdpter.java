package adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import baihuiji.jkqme.baihuiji.R;

public class BillAdpter extends BaseAdapter {
    private Context context;
    private final long dayTime = 86400L;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> list;

    public BillAdpter(ArrayList<HashMap<String, String>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public BillAdpter(ArrayList<HashMap<String, String>> paramArrayList, Context paramContext) {
        this.list = paramArrayList;
        this.context = paramContext;
        this.inflater = LayoutInflater.from(paramContext);
    }

    public void setList(ArrayList<HashMap<String, String>> list) {
        this.list = list;
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
                localHolder.lv_message_pay_tx.setText("微信");
                break;
            case "2":
                localHolder.img.setImageResource(R.drawable.qqperse);
                localHolder.lv_message_pay_tx.setText("qq钱包");
                break;
            case "3":
                localHolder.img.setImageResource(R.drawable.ailipay);
                localHolder.lv_message_pay_tx.setText("支付宝");
                break;
            case "4":
                localHolder.img.setImageResource(R.drawable.baiduperse);
                localHolder.lv_message_pay_tx.setText("百度钱包");
                break;
            default:
                break;
        }
        String a[] = getTime(((HashMap<String, String>) this.list.get(paramInt)).get("payTime"));

        localHolder.lv_message_time_tx.setText(getDate(a[0]));
        localHolder.lv_message_time1_tx.setText(a[1]);
        //操作员
        localHolder.lv_message_pay1_tx.setText((CharSequence) ((HashMap) this.list.get(paramInt)).get("singal"));
        localHolder.lv_message_pay_num_tx.setText((CharSequence) ((HashMap) this.list.get(paramInt)).get("ordPrice"));
        if (!((String) ((HashMap) this.list.get(paramInt)).get("ordState")).equals("0")) {

            localHolder.lv_message_pay_num1_tx.setText("交易成功");
        } else {
            localHolder.lv_message_pay_num1_tx.setText("已退款");
            localHolder.lv_message_pay_num1_tx.setTextColor(paramView.getResources().getColor(R.color.red));
        }
        return paramView;
    }

    private class Holder {
        ImageView img;//

        TextView lv_message_pay_num1_tx;
        TextView lv_message_pay_num_tx;
        //交易类型，订单
        TextView lv_message_pay_tx;
        TextView lv_message_pay1_tx;
        //时间
        TextView lv_message_time1_tx;
        TextView lv_message_time_tx;

        private Holder() {
        }
    }

    /**
     * 分离json中的时间
     *
     * @param str
     * @return
     */
    private String[] getTime(String str) {
        String a[] = new String[2];
        a = str.split(" ");
        return a;
    }

    /**
     * 一天86400000
     *
     * @param str
     * @return
     */
    private String getDate(String str) {
        // Calendar calendar=Calendar.getInstance();
        Date date = new Date();
        Date date1;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //将当前转换为字符串
        String dates = format.format(date);
        String str1 = str.replace("-", "");
        int today = Integer.parseInt(dates);
        int time = Integer.parseInt(str1);
        if ((today - time) == 0) {
            return "今天";
        } else if ((today - time) == 1) {
            return "昨天";
        } else {
            return str;
        }


    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     adpter.BillAdpter
 * JD-Core Version:    0.6.2
 */