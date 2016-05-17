package adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import baihuiji.jkqme.baihuiji.R;

/**
 * 只包含一个TexView 的aDpter
 * Created by Administrator on 2016/5/17.
 */
public class TextAdpter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String text[];

    public TextAdpter(){}
    public TextAdpter(String[] text,Context context){
        this.text=text;
        this.context=context;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public Object getItem(int i) {
        return text[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            //view=inflater.inflate(R.layout.R.layout.dialog_select_shop)
        }
        return null;
    }
}
