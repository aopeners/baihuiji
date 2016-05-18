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

/**
 * Created by Administrator on 2016/5/18.
 */
public class MonyStatisticByTypeAdpter extends BaseAdapter {
    private int payType;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> list;

    public MonyStatisticByTypeAdpter(int payType,
                                     Context context, ArrayList<HashMap<String, String>> list) {
        this.payType = payType;
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.lv_statistic_trad_1, null, true);
            holder = new Holder();
            holder.lv_statistic_trad1_day_tx = (TextView) view.findViewById(R.id.lv_statistic_trad1_day_tx);
            holder.trad_statistic_getMoney_tx = (TextView) view.findViewById(R.id.trad_statistic_getMoney_tx);
            holder.img = (ImageView) view.findViewById(R.id.lv_statistic_trad_img);
            holder.lv_statistic_trad_pay_tx = (TextView) view.findViewById(R.id.lv_statistic_trad_pay_tx);
            holder.trad_statistic1_getMoney_tx = (TextView) view.findViewById(R.id.trad_statistic1_getMoney_tx);
            holder.trad_statistic_backMoney_tx = (TextView) view.findViewById(R.id.trad_statistic_backMoney_tx);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        //支付图标设置
        if (payType == 1) {
            holder.img.setImageResource(R.drawable.payweichar);
            holder.lv_statistic_trad_pay_tx.setText("微信支付");
        } else if (payType == 2) {
            holder.img.setImageResource(R.drawable.qqperse);
            holder.lv_statistic_trad_pay_tx.setText("qq钱包");
        } else if (payType == 3) {
            holder.img.setImageResource(R.drawable.ailipay);
            holder.lv_statistic_trad_pay_tx.setText("支付宝");
        } else if (payType == 4) {
            holder.img.setImageResource(R.drawable.baiduperse);
            holder.lv_statistic_trad_pay_tx.setText("百度钱包");
        }
        setDate(list.get(i).get("totalDate"), holder.lv_statistic_trad1_day_tx, holder.trad_statistic_getMoney_tx);
        holder.trad_statistic1_getMoney_tx.setText(getText(list.get(i).get("payTotal")));
        holder.trad_statistic_backMoney_tx.setText(getText(list.get(i).get("backTotal")));
        return view;
    }

    private class Holder {
        //月
        TextView lv_statistic_trad1_day_tx;
        //日
        TextView trad_statistic_getMoney_tx;
        //支付图标
        ImageView img;
        //支付方式
        TextView lv_statistic_trad_pay_tx;
        //收款
        TextView trad_statistic1_getMoney_tx;
        //退款
        TextView trad_statistic_backMoney_tx;
    }

    /**
     * @param date
     * @param textView  显示星期de view s
     * @param textView1 显示天的view
     */
    private void setDate(String date, TextView textView, TextView textView1) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date1 = null;
        int day = 0;//显示星期
        String day1;//显示一个月中的天
        try {
            date1 = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            day = calendar.DAY_OF_WEEK;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("MM-dd");
        day1 = format.format(date1);
        textView1.setText(day1);
        switch (day) {
            case Calendar.SATURDAY:
                textView.setText("周六");
                break;
            case Calendar.MONDAY:
                textView.setText("周一");
                break;
            case Calendar.TUESDAY:
                textView.setText("周二");
                break;
            case Calendar.WEDNESDAY:
                textView.setText("周三");
                break;
            case Calendar.THURSDAY:
                textView.setText("周四");
                break;
            case Calendar.FRIDAY:
                textView.setText("周五");
                break;
            case Calendar.SUNDAY:
                textView.setText("星期天");
                break;
        }
    }

    private String getText(String str) {
        if (str == null) {
            return "0";
        } else return str;
    }

}
