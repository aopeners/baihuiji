package adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class MonthAdpter extends BaseAdapter
{
  private Context context;
  private LayoutInflater inflater;
  private ArrayList<HashMap<String, String>> list;

  public MonthAdpter()
  {
  }

  public MonthAdpter(ArrayList<HashMap<String, String>> paramArrayList, Context paramContext)
  {
    this.list = paramArrayList;
    this.context = paramContext;
    this.inflater = LayoutInflater.from(paramContext);
  }

  public int getCount()
  {
    return this.list.size();
  }

  public Object getItem(int paramInt)
  {
    return this.list.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return null;
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     adpter.MonthAdpter
 * JD-Core Version:    0.6.2
 */