package frament;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.MyApplaication;
import java.util.ArrayList;
import web.BaihuijiNet;

public class Home_display_fragment extends Fragment
{
  private Adpter adpter;
  private Handler handler = new Handler()
  {
    Bundle bundle;

    public void handleMessage(Message paramAnonymousMessage)
    {
      this.bundle = paramAnonymousMessage.getData();
      if (this.bundle.getIntArray("requst") != null)
      {
        int[] arrayOfInt = this.bundle.getIntArray("requst");
        ((TextView)Home_display_fragment.this.tody.findViewById(2131230850)).setText(arrayOfInt[0] + "元");
        ((TextView)Home_display_fragment.this.month.findViewById(2131230826)).setText(arrayOfInt[1] + "元");
      }
    }
  };
  private ArrayList<View> list = new ArrayList();
  private OnClickListener listener = new OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      switch (paramAnonymousView.getId())
      {
      case 2131230777:
      default:
        return;
      case 2131230776:
        Home_display_fragment.this.showFragment(1);
        return;
      case 2131230778:
        Home_display_fragment.this.showFragment(1);
        return;
      case 2131230779:
        Home_display_fragment.this.showFragment(1);
        return;
      case 2131230780:
        Home_display_fragment.this.showFragment(1);
        return;
      case 2131230781:
      }
      Home_display_fragment.this.showFragment(1);
    }
  };
  private View month;
  private OnPageChangeListener pListener = new OnPageChangeListener()
  {
    public void onPageScrollStateChanged(int paramAnonymousInt)
    {
    }

    public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2)
    {
    }

    public void onPageSelected(int paramAnonymousInt)
    {
      if (paramAnonymousInt == 1)
      {
        ((TextView)Home_display_fragment.this.view.findViewById(2131230774)).setBackgroundDrawable(Home_display_fragment.this.view.getResources().getDrawable(2130837514));
        ((TextView)Home_display_fragment.this.view.findViewById(2131230775)).setBackgroundDrawable(Home_display_fragment.this.view.getResources().getDrawable(2130837515));
        return;
      }
      ((TextView)Home_display_fragment.this.view.findViewById(2131230774)).setBackgroundDrawable(Home_display_fragment.this.view.getResources().getDrawable(2130837515));
      ((TextView)Home_display_fragment.this.view.findViewById(2131230775)).setBackgroundDrawable(Home_display_fragment.this.view.getResources().getDrawable(2130837514));
    }
  };
  private ViewPager pager;
  private TextView proceed;
  private View tody;
  private View view;

  // ERROR //
  private int[] getDate(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: iconst_2
    //   1: newarray int
    //   3: astore_3
    //   4: new 79	org/json/JSONObject
    //   7: dup
    //   8: aload_1
    //   9: invokespecial 82	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   12: astore 4
    //   14: aload 4
    //   16: ldc 84
    //   18: invokevirtual 88	org/json/JSONObject:getInt	(Ljava/lang/String;)I
    //   21: ifne +58 -> 79
    //   24: aload_3
    //   25: iconst_0
    //   26: aload 4
    //   28: ldc 90
    //   30: invokevirtual 88	org/json/JSONObject:getInt	(Ljava/lang/String;)I
    //   33: iastore
    //   34: new 79	org/json/JSONObject
    //   37: dup
    //   38: aload_2
    //   39: invokespecial 82	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   42: astore 6
    //   44: aload 6
    //   46: ldc 84
    //   48: invokevirtual 88	org/json/JSONObject:getInt	(Ljava/lang/String;)I
    //   51: ifne +59 -> 110
    //   54: aload_3
    //   55: iconst_1
    //   56: aload 6
    //   58: ldc 90
    //   60: invokevirtual 88	org/json/JSONObject:getInt	(Ljava/lang/String;)I
    //   63: iastore
    //   64: aload_3
    //   65: areturn
    //   66: astore 9
    //   68: aload 9
    //   70: invokevirtual 93	org/json/JSONException:printStackTrace	()V
    //   73: aconst_null
    //   74: astore 4
    //   76: goto -62 -> 14
    //   79: aload_3
    //   80: iconst_0
    //   81: iconst_0
    //   82: iastore
    //   83: goto -49 -> 34
    //   86: astore 5
    //   88: aload 5
    //   90: invokevirtual 93	org/json/JSONException:printStackTrace	()V
    //   93: goto -59 -> 34
    //   96: astore 8
    //   98: aload 8
    //   100: invokevirtual 93	org/json/JSONException:printStackTrace	()V
    //   103: aload 4
    //   105: astore 6
    //   107: goto -63 -> 44
    //   110: aload_3
    //   111: iconst_1
    //   112: iconst_0
    //   113: iastore
    //   114: aload_3
    //   115: areturn
    //   116: astore 7
    //   118: aload 7
    //   120: invokevirtual 93	org/json/JSONException:printStackTrace	()V
    //   123: aload_3
    //   124: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   4	14	66	org/json/JSONException
    //   14	34	86	org/json/JSONException
    //   79	83	86	org/json/JSONException
    //   34	44	96	org/json/JSONException
    //   44	64	116	org/json/JSONException
    //   110	114	116	org/json/JSONException
  }

  private void getDayDate()
  {
    new Thread(new Runnable()
    {
      MyApplaication applaication = (MyApplaication)Home_display_fragment.this.getParentFragment().getActivity().getApplication();
      Bundle bundle;
      String json;
      String[] key = { "uId", "merchantId", "day", "rule" };
      Message message;
      String mjson;
      String[] mkey;
      String[] mvalue;
      String requstDay;
      String requstMonth;
      String[] value;

      public void run()
      {
        Log.i("Home_setDate", this.json);
        this.requstDay = BaihuijiNet.urlconection("http://baihuiji.weikebaba.com/aide/dayCount", this.json);
        this.requstMonth = BaihuijiNet.urlconection("http://baihuiji.weikebaba.com/aide/monthCount", this.json);
        Log.i("home_setDate", this.requstDay + this.requstMonth + 333);
        this.bundle.putIntArray("requst", Home_display_fragment.this.getDate(this.requstDay, this.requstMonth));
        this.message.setData(this.bundle);
        Home_display_fragment.this.handler.sendMessage(this.message);
      }
    }).start();
  }

  private void hideButtom()
  {
    ((HomPage)getParentFragment().getActivity());
  }

  private void loadButton(View paramView)
  {
    int[] arrayOfInt = { 2131230778, 2131230779, 2131230780, 2131230781 };
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfInt.length)
        return;
      ((Button)paramView.findViewById(arrayOfInt[i])).setOnClickListener(this.listener);
    }
  }

  private void setMonthDate()
  {
  }

  private void showFragment(int paramInt)
  {
    ((Home_fragment)getParentFragment()).showFragment(paramInt);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    this.view = paramLayoutInflater.inflate(2130903051, null);
    this.tody = paramLayoutInflater.inflate(2130903069, null);
    this.month = paramLayoutInflater.inflate(2130903065, null);
    this.list.add(this.tody);
    this.list.add(this.month);
    this.pager = ((ViewPager)this.view.findViewById(2131230773));
    this.adpter = new Adpter();
    this.pager.setAdapter(this.adpter);
    this.proceed = ((TextView)this.view.findViewById(2131230776));
    this.proceed.setOnClickListener(this.listener);
    this.pager.setOnPageChangeListener(this.pListener);
    loadButton(this.view);
    getDayDate();
    return this.view;
  }

  class Adpter extends PagerAdapter
  {
    Adpter()
    {
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramViewGroup.removeView((View)Home_display_fragment.this.list.get(paramInt));
    }

    public int getCount()
    {
      return Home_display_fragment.this.list.size();
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      paramViewGroup.addView((View)Home_display_fragment.this.list.get(paramInt));
      return Home_display_fragment.this.list.get(paramInt);
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Home_display_fragment
 * JD-Core Version:    0.6.2
 */