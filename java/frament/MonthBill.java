package frament;

import adpter.MonthAdpter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import baihuiji.jkqme.baihuiji.MyApplaication;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import web.BaihuijiNet;

public class MonthBill extends Fragment
{
  private ListView listView;
  private OnClickListener listener = new OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      switch (paramAnonymousView.getId())
      {
      default:
        return;
      case 2131230827:
      }
      MonthBill.this.onClickBack();
    }
  };
  private MonthAdpter monthAdpter;

  private void getDate()
  {
    new Thread(new Runnable()
    {
      MyApplaication applaication = (MyApplaication)MonthBill.this.getParentFragment().getActivity().getApplication();
      String json;
      String[] key = { "uId", "merchantId", "month" };
      String requst;
      String[] value;

      public void run()
      {
        this.requst = MonthBill.this.urlconection("http://baihuiji.weikebaba.com/aide/monthBill", this.json);
        Log.i("BillMonth", this.requst + "  \n  " + this.json);
        if (MonthBill.this.getSuccess(this.requst))
          MonthBill.this.getDate(this.requst);
      }
    }).start();
  }

  // ERROR //
  private void getDate(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 54	frament/MonthBill:getView	()Landroid/view/View;
    //   4: astore_2
    //   5: aload_2
    //   6: ldc 55
    //   8: invokevirtual 61	android/view/View:findViewById	(I)Landroid/view/View;
    //   11: checkcast 63	android/widget/TextView
    //   14: astore_3
    //   15: aload_2
    //   16: ldc 64
    //   18: invokevirtual 61	android/view/View:findViewById	(I)Landroid/view/View;
    //   21: checkcast 63	android/widget/TextView
    //   24: astore 4
    //   26: aload_2
    //   27: ldc 65
    //   29: invokevirtual 61	android/view/View:findViewById	(I)Landroid/view/View;
    //   32: checkcast 63	android/widget/TextView
    //   35: astore 5
    //   37: aconst_null
    //   38: astore 6
    //   40: aconst_null
    //   41: astore 7
    //   43: new 67	org/json/JSONObject
    //   46: dup
    //   47: aload_1
    //   48: invokespecial 69	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   51: astore 8
    //   53: aload 8
    //   55: ldc 71
    //   57: invokevirtual 75	org/json/JSONObject:getJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   60: astore 7
    //   62: aload 7
    //   64: ldc 77
    //   66: invokevirtual 81	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   69: astore 22
    //   71: aload 22
    //   73: astore 10
    //   75: aload 8
    //   77: astore 6
    //   79: aload_3
    //   80: new 83	java/lang/StringBuilder
    //   83: dup
    //   84: aload 7
    //   86: ldc 85
    //   88: invokevirtual 89	org/json/JSONObject:getInt	(Ljava/lang/String;)I
    //   91: aload 7
    //   93: ldc 91
    //   95: invokevirtual 89	org/json/JSONObject:getInt	(Ljava/lang/String;)I
    //   98: iadd
    //   99: invokestatic 97	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   102: invokespecial 98	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   105: invokevirtual 102	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   108: invokevirtual 106	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   111: aload 4
    //   113: aload 7
    //   115: ldc 108
    //   117: invokevirtual 112	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   120: invokevirtual 106	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   123: aload 5
    //   125: aload 7
    //   127: ldc 114
    //   129: invokevirtual 112	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   132: invokevirtual 106	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   135: new 116	java/util/ArrayList
    //   138: dup
    //   139: invokespecial 117	java/util/ArrayList:<init>	()V
    //   142: astore 12
    //   144: iconst_0
    //   145: istore 13
    //   147: iload 13
    //   149: aload 10
    //   151: invokevirtual 123	org/json/JSONArray:length	()I
    //   154: if_icmplt +62 -> 216
    //   157: new 125	adpter/MonthAdpter
    //   160: dup
    //   161: aload 12
    //   163: aload_0
    //   164: invokevirtual 129	frament/MonthBill:getParentFragment	()Landroid/support/v4/app/Fragment;
    //   167: invokevirtual 133	android/support/v4/app/Fragment:getActivity	()Landroid/support/v4/app/FragmentActivity;
    //   170: invokespecial 136	adpter/MonthAdpter:<init>	(Ljava/util/ArrayList;Landroid/content/Context;)V
    //   173: astore 14
    //   175: aload_0
    //   176: aload 14
    //   178: putfield 138	frament/MonthBill:monthAdpter	Ladpter/MonthAdpter;
    //   181: aload_0
    //   182: getfield 140	frament/MonthBill:listView	Landroid/widget/ListView;
    //   185: aload_0
    //   186: getfield 138	frament/MonthBill:monthAdpter	Ladpter/MonthAdpter;
    //   189: invokevirtual 146	android/widget/ListView:setAdapter	(Landroid/widget/ListAdapter;)V
    //   192: return
    //   193: astore 9
    //   195: aload 9
    //   197: invokevirtual 149	org/json/JSONException:printStackTrace	()V
    //   200: aconst_null
    //   201: astore 10
    //   203: goto -124 -> 79
    //   206: astore 11
    //   208: aload 11
    //   210: invokevirtual 149	org/json/JSONException:printStackTrace	()V
    //   213: goto -78 -> 135
    //   216: aload 10
    //   218: iload 13
    //   220: invokevirtual 152	org/json/JSONArray:getJSONObject	(I)Lorg/json/JSONObject;
    //   223: astore 21
    //   225: aload 21
    //   227: astore 6
    //   229: new 154	java/util/HashMap
    //   232: dup
    //   233: invokespecial 155	java/util/HashMap:<init>	()V
    //   236: astore 16
    //   238: aload 16
    //   240: ldc 108
    //   242: aload 6
    //   244: ldc 108
    //   246: invokevirtual 112	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   249: invokevirtual 159	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   252: pop
    //   253: aload 16
    //   255: ldc 114
    //   257: aload 6
    //   259: ldc 114
    //   261: invokevirtual 112	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   264: invokevirtual 159	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   267: pop
    //   268: aload 12
    //   270: aload 16
    //   272: invokevirtual 163	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   275: pop
    //   276: iinc 13 1
    //   279: goto -132 -> 147
    //   282: astore 15
    //   284: aload 15
    //   286: invokevirtual 149	org/json/JSONException:printStackTrace	()V
    //   289: goto -60 -> 229
    //   292: astore 17
    //   294: aload 17
    //   296: invokevirtual 149	org/json/JSONException:printStackTrace	()V
    //   299: goto -23 -> 276
    //   302: astore 9
    //   304: aload 8
    //   306: astore 6
    //   308: goto -113 -> 195
    //
    // Exception table:
    //   from	to	target	type
    //   43	53	193	org/json/JSONException
    //   79	135	206	org/json/JSONException
    //   216	225	282	org/json/JSONException
    //   229	276	292	org/json/JSONException
    //   53	71	302	org/json/JSONException
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

  private void loadComponent(View paramView)
  {
    this.listView = ((ListView)paramView.findViewById(2131230836));
    ((ImageView)paramView.findViewById(2131230827)).setOnClickListener(this.listener);
  }

  private void onClickBack()
  {
    ((Bill)getParentFragment()).showFragment(0);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903066, null, true);
    loadComponent(localView);
    return localView;
  }

  public void onHiddenChanged(boolean paramBoolean)
  {
    super.onHiddenChanged(paramBoolean);
    if ((!paramBoolean) && (this.monthAdpter == null))
      getDate();
  }

  public String urlconection(String paramString1, String paramString2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(paramString1).openConnection();
      localHttpURLConnection.setRequestMethod("POST");
      localHttpURLConnection.setDoInput(true);
      localHttpURLConnection.setDoOutput(true);
      localHttpURLConnection.setUseCaches(false);
      localHttpURLConnection.setInstanceFollowRedirects(true);
      localHttpURLConnection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
      while (true)
      {
        BufferedReader localBufferedReader;
        try
        {
          DataOutputStream localDataOutputStream = new DataOutputStream(localHttpURLConnection.getOutputStream());
          localDataOutputStream.writeBytes(paramString2);
          localDataOutputStream.flush();
          localDataOutputStream.close();
          if (localHttpURLConnection.getResponseCode() == 200)
          {
            localBufferedReader = new BufferedReader(new InputStreamReader(localHttpURLConnection.getInputStream()));
            localObject = localBufferedReader.readLine();
            break;
            if (localObject == "")
              break label193;
            j = 1;
            if ((j & i) != 0);
          }
          else
          {
            return localStringBuffer.toString();
          }
        }
        catch (ConnectException localConnectException)
        {
          return "";
        }
        localStringBuffer.append((String)localObject);
        String str = localBufferedReader.readLine();
        localObject = str;
        break;
        i = 0;
        continue;
        label193: int j = 0;
      }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      while (true)
        localMalformedURLException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        Object localObject;
        int i;
        localIOException.printStackTrace();
        continue;
        if (localObject != null)
          i = 1;
      }
    }
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.MonthBill
 * JD-Core Version:    0.6.2
 */