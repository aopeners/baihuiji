package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import baihuiji.jkqme.baihuiji.MyApplaication;
import baihuiji.jkqme.baihuiji.PageView;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import web.BaihuijiNet;

public class Login extends Fragment
{
  private CheckBox checkBox;
  private OnCheckedChangeListener clistener = new OnCheckedChangeListener()
  {
    public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
    {
    }
  };
  private OnClickListener listener = new OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      Login.this.userNameEtx.getText().toString();
      Login.this.passwordEtx.getText().toString();
      Log.i("Onclick", "18716398031" + "  " + "123456");
      if ((Login.this.checkBox.isChecked()) && ("18716398031" != null) && (!"18716398031".equals("")) && ("123456" != null) && (!"123456".equals("")))
        Login.this.conect("18716398031", "123456");
    }
  };
  private TextView loginTv;
  private EditText passwordEtx;
  private String requst;
  private EditText userNameEtx;

  private void conect(String paramString1, String paramString2)
  {
    String[] arrayOfString1 = { "merchantId", "ordSource", "operateId", "operatePass", "MD5" };
    String[] arrayOfString2 = new String[5];
    arrayOfString2[0] = "0";
    arrayOfString2[1] = "app";
    arrayOfString2[2] = paramString1;
    arrayOfString2[3] = paramString2;
    arrayOfString2[4] = getMd5_32("merchantId&ordSource&operateId&operatePass&=*0*app*" + paramString1 + "*" + paramString2);
    Log.i("Md5", "merchantId&ordSource&operateId&operatePass&=*0*app*" + paramString1 + "*" + paramString2);
    new Thread()
    {
      public void run()
      {
        try
        {
          Login.this.requst = BaihuijiNet.urlconection("http://baihuiji.weikebaba.com/pospay/queryShopMerchant", this.val$requstJson);
          if (Login.this.loginSuccess(Login.this.requst))
          {
            Login.this.saveDate(Login.this.requst);
            Log.i("Login", Login.this.requst);
            Login.this.jumptoHomepage();
            return;
          }
        }
        catch (Exception localException)
        {
          while (true)
            localException.printStackTrace();
          Login.this.showToast();
          Log.i("Login", Login.this.requst + "登录失败");
        }
      }
    }
    .start();
  }

  private String getJson(HashMap<String, String> paramHashMap)
  {
    Iterator localIterator = paramHashMap.entrySet().iterator();
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("{");
    while (true)
    {
      if (!localIterator.hasNext())
      {
        localStringBuffer.append("}");
        return localStringBuffer.toString();
      }
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localStringBuffer.append("\"");
      localStringBuffer.append(localEntry.getKey());
      localStringBuffer.append("\":");
      localStringBuffer.append("\"");
      localStringBuffer.append(localEntry.getValue());
      localStringBuffer.append("\"");
      if (localIterator.hasNext())
        localStringBuffer.append(",");
    }
  }

  private String getJson(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("{");
    int i = 0;
    if (i >= paramArrayOfString1.length)
    {
      localStringBuffer.append("}");
      return localStringBuffer.toString();
    }
    if (paramArrayOfString1[i].equals("MD5"))
    {
      localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
      localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"");
    }
    while (true)
    {
      i++;
      break;
      localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
      localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"" + ",");
    }
  }

  private String getMd5(String paramString)
  {
    MessageDigest localMessageDigest = null;
    byte[] arrayOfByte;
    StringBuffer localStringBuffer;
    int i;
    try
    {
      localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramString.getBytes("UTF-8"));
      arrayOfByte = localMessageDigest.digest();
      localStringBuffer = new StringBuffer();
      i = 0;
      if (i >= arrayOfByte.length)
        return localStringBuffer.toString();
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      while (true)
        localNoSuchAlgorithmException.printStackTrace();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
        localUnsupportedEncodingException.printStackTrace();
    }
    if (Integer.toHexString(0xFF & arrayOfByte[i]).length() == 1)
      localStringBuffer.append("0").append(Integer.toHexString(0xFF & arrayOfByte[i]).toUpperCase());
    while (true)
    {
      i++;
      break;
      localStringBuffer.append(Integer.toHexString(0xFF & arrayOfByte[i]).toUpperCase());
    }
  }

  private String getMd5_32(String paramString)
  {
    new StringBuffer();
    MessageDigest localMessageDigest = null;
    try
    {
      localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramString.getBytes("UTF-8"));
      arrayOfByte = localMessageDigest.digest();
      localStringBuffer = new StringBuffer();
      i = 0;
      if (i >= arrayOfByte.length)
        return localStringBuffer.toString();
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      while (true)
        localNoSuchAlgorithmException.printStackTrace();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
      {
        byte[] arrayOfByte;
        StringBuffer localStringBuffer;
        int i;
        localUnsupportedEncodingException.printStackTrace();
        continue;
        int j = arrayOfByte[i];
        if (j < 0)
          j += 256;
        if (j < 16)
          localStringBuffer.append("0");
        localStringBuffer.append(Integer.toHexString(j).toUpperCase());
        i++;
      }
    }
  }

  private void jumptoHomepage()
  {
    ((PageView)getActivity()).jumptoHome();
  }

  private void loadComponent(View paramView)
  {
    this.userNameEtx = ((EditText)paramView.findViewById(2131230789));
    this.passwordEtx = ((EditText)paramView.findViewById(2131230790));
    this.checkBox = ((CheckBox)paramView.findViewById(2131230791));
    this.loginTv = ((TextView)paramView.findViewById(2131230792));
    this.loginTv.setOnClickListener(this.listener);
    this.checkBox.setOnCheckedChangeListener(this.clistener);
  }

  private boolean loginSuccess(String paramString)
  {
    if (paramString == "");
    while (true)
    {
      return false;
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
            if (!str1.equals("登录成功"))
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
    }
  }

  private void saveDate(String paramString)
  {
    MyApplaication localMyApplaication = (MyApplaication)getActivity().getApplication();
    while (true)
    {
      JSONObject localJSONObject2;
      String[] arrayOfString;
      int i;
      try
      {
        JSONObject localJSONObject1 = new JSONObject(paramString);
        localJSONObject2 = localJSONObject1;
        arrayOfString = new String[] { "logo", "merchantId", "merName", "shopName", "time", "payTypeStatus", "company", "operateName", "operateTel" };
        i = 0;
        if (i >= arrayOfString.length)
          return;
      }
      catch (JSONException localJSONException2)
      {
        localJSONException2.printStackTrace();
        localJSONObject2 = null;
        continue;
      }
      try
      {
        localMyApplaication.putData(arrayOfString[i], localJSONObject2.getString(arrayOfString[i]));
        i++;
      }
      catch (JSONException localJSONException1)
      {
        while (true)
          localJSONException1.printStackTrace();
      }
    }
  }

  private void showToast()
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        Toast.makeText(Login.this.getActivity(), "登录失败", 1000).show();
      }
    });
  }

  private void writDb()
  {
    ((PageView)getActivity()).writDb();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903054, null);
    loadComponent(localView);
    return localView;
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Login
 * JD-Core Version:    0.6.2
 */