package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import baihuiji.jkqme.baihuiji.MyApplaication;
import org.json.JSONException;
import org.json.JSONObject;
import web.BaihuijiNet;

public class MineFragment extends Fragment
{
  private OnClickListener listener = new OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      switch (paramAnonymousView.getId())
      {
      default:
        return;
      case 2131230818:
      }
      MineFragment.this.loginOut();
    }
  };
  private String loginOutRequst;
  private TextView loginout;
  private TextView textView;

  private boolean isloginout(String paramString)
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
          String str2 = localJSONObject2.getString("msg");
          str1 = str2;
          if (!str1.equals("登出成功"))
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
          String str1 = null;
        }
      }
    }
    return false;
  }

  private void loadComponent(View paramView)
  {
    this.loginout = ((TextView)paramView.findViewById(2131230818));
    this.loginout.setOnClickListener(this.listener);
  }

  private void loginOut()
  {
    MyApplaication localMyApplaication = (MyApplaication)getActivity().getApplication();
    String[] arrayOfString1 = { "posCode", "merchantId", "operateId", "MD5" };
    String str1 = localMyApplaication.getDate("posCode");
    String str2 = localMyApplaication.getDate("merchantId");
    String str3 = localMyApplaication.getDate("operateId");
    String str4 = "posCode&merchantId&operateId&=*" + str1 + "*" + str2 + "*" + str3;
    String[] arrayOfString2 = new String[4];
    arrayOfString2[0] = str1;
    arrayOfString2[1] = str2;
    arrayOfString2[2] = str3;
    arrayOfString2[3] = BaihuijiNet.getMd5_32(str4);
    final String str5 = BaihuijiNet.getJson(arrayOfString1, arrayOfString2, "MD5");
    Log.i("logingout", str5);
    new Thread(new Runnable()
    {
      public void run()
      {
        MineFragment.this.loginOutRequst = BaihuijiNet.urlconection("http://baihuiji.weikebaba.com/pospay/posGoOut", str5);
        Log.i("onLoginOUt", MineFragment.this.loginOutRequst);
        if (MineFragment.this.isloginout(MineFragment.this.loginOutRequst))
          MineFragment.this.toLoginout();
      }
    }).start();
  }

  private void toLoginout()
  {
    getActivity().finish();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903063, null);
    loadComponent(localView);
    return localView;
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.MineFragment
 * JD-Core Version:    0.6.2
 */