package frament;

import adpter.BillAdpter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.MyApplaication;
import org.json.JSONException;
import org.json.JSONObject;
import web.BaihuijiNet;

public class Paylist_Fragment extends Fragment
{
  private BillAdpter billAdpter;
  private OnEditorActionListener eListener = new OnEditorActionListener()
  {
    public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
    {
      return false;
    }
  };
  private ListView listView;
  private OnClickListener listener = new OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      switch (paramAnonymousView.getId())
      {
      case 2131230722:
      default:
        return;
      case 2131230724:
        Paylist_Fragment.this.onJump(1);
        return;
      case 2131230721:
        Paylist_Fragment.this.jumptoDecode();
        return;
      case 2131230723:
      }
      Paylist_Fragment.this.onSerch();
    }
  };
  private TextView textView;

  private void getDate()
  {
    new Thread(new Runnable()
    {
      MyApplaication applaication = (MyApplaication)Paylist_Fragment.this.getParentFragment().getActivity().getApplication();
      String json;
      String[] key = { "uId", "merchantId", "day" };
      String requst;
      String[] value;

      public void run()
      {
        this.requst = BaihuijiNet.urlconection("http://baihuiji.weikebaba.com/aide/bill", this.json);
        Log.i("BillRequst", this.requst + "  \n  " + this.json);
        if (Paylist_Fragment.this.getSuccess(this.requst))
          Paylist_Fragment.this.getDate(this.requst);
      }
    }).start();
  }

  // ERROR //
  private void getDate(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 73	frament/Paylist_Fragment:getParentFragment	()Landroid/support/v4/app/Fragment;
    //   4: invokevirtual 77	android/support/v4/app/Fragment:getActivity	()Landroid/support/v4/app/FragmentActivity;
    //   7: invokevirtual 83	android/support/v4/app/FragmentActivity:getApplication	()Landroid/app/Application;
    //   10: checkcast 85	baihuiji/jkqme/baihuiji/MyApplaication
    //   13: astore_2
    //   14: aconst_null
    //   15: astore_3
    //   16: new 87	org/json/JSONObject
    //   19: dup
    //   20: aload_1
    //   21: invokespecial 89	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   24: astore 4
    //   26: aload 4
    //   28: ldc 91
    //   30: invokevirtual 95	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   33: astore 21
    //   35: aload 21
    //   37: astore 6
    //   39: aload 4
    //   41: astore_3
    //   42: new 97	java/util/ArrayList
    //   45: dup
    //   46: invokespecial 98	java/util/ArrayList:<init>	()V
    //   49: astore 7
    //   51: iconst_0
    //   52: istore 8
    //   54: iload 8
    //   56: aload 6
    //   58: invokevirtual 104	org/json/JSONArray:length	()I
    //   61: if_icmplt +48 -> 109
    //   64: aload_0
    //   65: new 106	adpter/BillAdpter
    //   68: dup
    //   69: aload 7
    //   71: aload_0
    //   72: invokevirtual 73	frament/Paylist_Fragment:getParentFragment	()Landroid/support/v4/app/Fragment;
    //   75: invokevirtual 77	android/support/v4/app/Fragment:getActivity	()Landroid/support/v4/app/FragmentActivity;
    //   78: invokespecial 109	adpter/BillAdpter:<init>	(Ljava/util/ArrayList;Landroid/content/Context;)V
    //   81: putfield 111	frament/Paylist_Fragment:billAdpter	Ladpter/BillAdpter;
    //   84: aload_0
    //   85: getfield 113	frament/Paylist_Fragment:listView	Landroid/widget/ListView;
    //   88: aload_0
    //   89: getfield 111	frament/Paylist_Fragment:billAdpter	Ladpter/BillAdpter;
    //   92: invokevirtual 119	android/widget/ListView:setAdapter	(Landroid/widget/ListAdapter;)V
    //   95: return
    //   96: astore 5
    //   98: aload 5
    //   100: invokevirtual 122	org/json/JSONException:printStackTrace	()V
    //   103: aconst_null
    //   104: astore 6
    //   106: goto -64 -> 42
    //   109: aload 6
    //   111: iload 8
    //   113: invokevirtual 126	org/json/JSONArray:getJSONObject	(I)Lorg/json/JSONObject;
    //   116: astore 20
    //   118: aload 20
    //   120: astore_3
    //   121: new 128	java/util/HashMap
    //   124: dup
    //   125: invokespecial 129	java/util/HashMap:<init>	()V
    //   128: astore 10
    //   130: aload 10
    //   132: ldc 131
    //   134: aload_2
    //   135: ldc 131
    //   137: invokevirtual 134	baihuiji/jkqme/baihuiji/MyApplaication:getDate	(Ljava/lang/String;)Ljava/lang/String;
    //   140: invokevirtual 138	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   143: pop
    //   144: aload 10
    //   146: ldc 140
    //   148: aload_3
    //   149: ldc 140
    //   151: invokevirtual 143	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   154: invokevirtual 138	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   157: pop
    //   158: aload 10
    //   160: ldc 145
    //   162: aload_3
    //   163: ldc 140
    //   165: invokevirtual 143	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   168: invokevirtual 138	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   171: pop
    //   172: aload 10
    //   174: ldc 147
    //   176: aload_3
    //   177: ldc 147
    //   179: invokevirtual 143	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   182: invokevirtual 138	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   185: pop
    //   186: aload 10
    //   188: ldc 149
    //   190: aload_3
    //   191: ldc 149
    //   193: invokevirtual 143	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   196: invokevirtual 138	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   199: pop
    //   200: aload 10
    //   202: ldc 151
    //   204: aload_3
    //   205: ldc 151
    //   207: invokevirtual 143	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   210: invokevirtual 138	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   213: pop
    //   214: aload 10
    //   216: ldc 153
    //   218: aload_3
    //   219: ldc 153
    //   221: invokevirtual 143	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   224: invokevirtual 138	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   227: pop
    //   228: aload 7
    //   230: aload 10
    //   232: invokevirtual 157	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   235: pop
    //   236: iinc 8 1
    //   239: goto -185 -> 54
    //   242: astore 9
    //   244: aload 9
    //   246: invokevirtual 122	org/json/JSONException:printStackTrace	()V
    //   249: goto -128 -> 121
    //   252: astore 11
    //   254: aload 11
    //   256: invokevirtual 122	org/json/JSONException:printStackTrace	()V
    //   259: goto -23 -> 236
    //   262: astore 5
    //   264: aload 4
    //   266: astore_3
    //   267: goto -169 -> 98
    //
    // Exception table:
    //   from	to	target	type
    //   16	26	96	org/json/JSONException
    //   109	118	242	org/json/JSONException
    //   121	236	252	org/json/JSONException
    //   26	35	262	org/json/JSONException
  }

  private boolean getSuccess(String paramString)
  {
    try
    {
      JSONObject localJSONObject1 = new JSONObject(paramString);
      localJSONObject2 = localJSONObject1;
    }
    catch (JSONException localJSONException2)
    {
      try
      {
        while (true)
        {
          int j = localJSONObject2.getInt("errcode");
          i = j;
          if (i != 0)
            break;
          return true;
          localJSONException2 = localJSONException2;
          localJSONException2.printStackTrace();
          JSONObject localJSONObject2 = null;
        }
      }
      catch (JSONException localJSONException1)
      {
        while (true)
        {
          localJSONException1.printStackTrace();
          int i = -1;
        }
      }
    }
    return false;
  }

  private void jumptoDecode()
  {
    ((HomPage)getParentFragment().getActivity()).jumptoDecode();
  }

  private void loadComponent(View paramView)
  {
    ImageView localImageView1 = (ImageView)paramView.findViewById(2131230723);
    ImageView localImageView2 = (ImageView)paramView.findViewById(2131230721);
    EditText localEditText = (EditText)paramView.findViewById(2131230722);
    ((TextView)paramView.findViewById(2131230724)).setOnClickListener(this.listener);
    localImageView2.setOnClickListener(this.listener);
    localImageView1.setOnClickListener(this.listener);
    localEditText.setOnEditorActionListener(this.eListener);
  }

  private void onJump(int paramInt)
  {
    ((Bill)getParentFragment()).showFragment(1);
  }

  private void onSerch()
  {
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903041, null);
    loadComponent(localView);
    return localView;
  }

  public void onHiddenChanged(boolean paramBoolean)
  {
    super.onHiddenChanged(paramBoolean);
    if ((!paramBoolean) && (this.billAdpter == null))
      getDate();
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Paylist_Fragment
 * JD-Core Version:    0.6.2
 */