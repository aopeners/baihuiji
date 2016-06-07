package adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import baihuiji.jkqme.baihuiji0.R;

/**
 * Created by Administrator on 2016/5/24.
 */
public class HelpAdpter extends BaseAdapter {
    private ArrayList<String[]> list = new ArrayList<String[]>();
    private Context context;
    private LayoutInflater inflater;

    public HelpAdpter() {
    }

    public HelpAdpter(Context context) {
        String string[] = context.getResources().getString(R.string.help).split("!");
        String str[];
        for (int i = 0; i < string.length; i++) {
            str = string[i].split("#");
            list.add(str);
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.lv_help, null, true);
            holder = new Holder();
            holder.lv_help_tx1 = (TextView) view.findViewById(R.id.lv_help_tx1);
            holder.lv_help_tx2 = (TextView) view.findViewById(R.id.lv_help_tx2);
            view.setOnClickListener(listener);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.lv_help_tx1.setText(list.get(i)[0]);
        holder.lv_help_tx2.setText(list.get(i)[1]);
        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Holder holder = (Holder) view.getTag();
            if (holder.lv_help_tx2.getVisibility() == View.VISIBLE) {
                holder.lv_help_tx1.setTextColor(view.getResources().getColor(R.color.black));
                holder.lv_help_tx2.setVisibility(View.GONE);
            } else {
                holder.lv_help_tx1.setTextColor(view.getResources().getColor(R.color.red));
                holder.lv_help_tx2.setVisibility(View.VISIBLE);
            }
        }
    };

    private class Holder {
        TextView lv_help_tx1;
        TextView lv_help_tx2;
    }
}
