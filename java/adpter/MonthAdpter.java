package adpter;

import android.content.Context;
import android.util.Log;
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

public class MonthAdpter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> list;
    private int month = -1;//记录月份

    public MonthAdpter() {
    }

    public MonthAdpter(ArrayList<HashMap<String, String>> paramArrayList, Context paramContext) {
        this.list = paramArrayList;
        this.context = paramContext;
        this.inflater = LayoutInflater.from(paramContext);
    }

    public void onDateChage(ArrayList<HashMap<String, String>> paramArrayList) {
        this.list = paramArrayList;
        month = -1;
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
        Holder holder;
        if (paramView == null) {
            paramView = inflater.inflate(R.layout.lv_month_bill, null, true);
            holder = new Holder();
            holder.lv_month_bill_tx = (TextView) paramView.findViewById(R.id.lv_month_bill_tx);

            holder.lv_message_time_tx = (TextView) paramView.findViewById(R.id.lv_message_time_tx);
            holder.lv_message_time1_tx = (TextView) paramView.findViewById(R.id.lv_message_time1_tx);

            holder.img = (ImageView) paramView.findViewById(R.id.lv_message_pay_img);

            holder.lv_message_pay_tx = (TextView) paramView.findViewById(R.id.lv_message_pay_tx);

            holder.lv_message_pay_num_tx = (TextView) paramView.findViewById(R.id.lv_message_pay_num_tx);
            paramView.setTag(holder);
        } else {
            holder = (Holder) paramView.getTag();
        }
        //设置月显示栏
        if (monthChage(list.get(paramInt).get("totalDate"))) {
            holder.lv_month_bill_tx.setText(month + "月");
            holder.lv_month_bill_tx.setVisibility(View.VISIBLE);
        }
        // holder.lv_message_time_tx.setText(list.get(paramInt).get("totalDate"));
        // holder.lv_message_time1_tx.setText(list.get(paramInt).get("totalDate"));
        setDate(list.get(paramInt).get("totalDate"), holder.lv_message_time_tx, holder.lv_message_time1_tx);

        //  holder.lv_message_pay_tx.setText(isGain(list.get(paramInt).get("payNum"),list.get(paramInt).get("backNum")));
        isGain(list.get(paramInt).get("payTotal"), list.get(paramInt).get("backNum"), holder.lv_message_pay_tx, holder.lv_message_pay_num_tx);
        return paramView;
    }

    private class Holder {
        //月份
        TextView lv_month_bill_tx;
        //日期
        TextView lv_message_time_tx;
        TextView lv_message_time1_tx;
        //交易方式图标
        ImageView img;
        //操作员
        TextView lv_message_pay_tx;
        //金额
        TextView lv_message_pay_num_tx;
    }

    /**
     * 处理收款退款的类
     *
     * @param get       赚取金额
     * @param back      退款
     * @param textView
     * @param textView1 收入数
     */
    private void isGain(String get, String back, TextView textView, TextView textView1) {
        if (get == null && back == null) {
            textView.setText("收款");
            textView1.setText("¥:0");
            return;
        } else if (get == null && back != null) {
            textView.setText("退款");
            textView1.setText("¥:" + back);
            return;
        } else if (get != null && back == null) {
            textView.setText("收款");
            textView1.setText("¥:" + get);
            return;
        }
        int a = Integer.parseInt(get) - Integer.parseInt(back);
        if (a >= 0) {
            textView.setText("收款");
            textView1.setText("¥:" + a + "");
            //textView.setTextColor(textView.getResources().getColor(R.color.b));
        } else {
            textView.setText("退款");
            textView1.setText("¥:" + -a + "");
            //  textView.setTextColor(textView.getResources().getColor(R.color.red));
        }
    }

    /**
     * @param date
     * @param textView  显示星期de view s
     * @param textView1 显示天的view
     */
    private void setDate(String date, TextView textView, TextView textView1) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format1 = new SimpleDateFormat("E");//判断星期的formate
        Date date1 = null;
        String day = "";//显示星期
        String day1;//显示一个月中的天
        try {
            date1 = format.parse(date);
            day = format1.format(date1);
            Log.i("MonthAdpter", date + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("MM-dd");
        day1 = format.format(date1);
        textView1.setText(day1);

        textView.setText(day);

    }

    /**
     * 检查月份是否改变
     *
     * @param data
     * @return 0 月份没改变
     */
    private boolean monthChage(String data) {
        int month1 = Integer.parseInt(data.substring(4, 6));
        if (month != 1) {
            //没有月份数据时 为true
            month = month1;
            return true;
        }
        return false;
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     adpter.MonthAdpter
 * JD-Core Version:    0.6.2
 */