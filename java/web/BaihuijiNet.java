package web;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaihuijiNet {
    public static String getJson(String[] paramArrayOfString1, String[] paramArrayOfString2, String paramString) {
        while (true) {
            int i;
            try {
                StringBuffer localStringBuffer = new StringBuffer();
                localStringBuffer.append("{");
                i = 0;
                if (i >= paramArrayOfString1.length) {
                    localStringBuffer.append("}");
                    String str = localStringBuffer.toString();
                    return str;
                }
                if (paramArrayOfString1[i].equals(paramString)) {
                    localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
                    localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"");
                } else {
                    localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
                    localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"" + ",");
                }
            } finally {
            }
            i++;
        }
    }

    /**
     * md532位加密
     */
    private synchronized static String getMd5_32(String str) {
        StringBuffer buffer = new StringBuffer();
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] b = md5.digest();
        int i;
        StringBuffer stringBuffer = new StringBuffer();
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                stringBuffer.append("0");
            stringBuffer.append(Integer.toHexString(i).toUpperCase());
        }
        return stringBuffer.toString();
    }


    public static boolean getNetState(Context paramContext) {
        ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (localConnectivityManager != null) {
            NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
            if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()) && (localNetworkInfo.getState().equals(State.CONNECTED)))
                return true;
        }
        return false;
    }

    public static String getTime(String paramString) {
        return new SimpleDateFormat(paramString).format(new Date());
    }

    /**
     * 获取网络应答
     *
     * @param paramString1 请求地址
     * @param paramString2 请求的jison
     * @return
     */
    public static String urlconection(String paramString1, String paramString2) {
        StringBuffer stringBuffer=new StringBuffer();
        URL urL = null;
        HttpURLConnection connection = null;
        DataOutputStream dataOutputStream = null;
        try {
            urL = new URL(paramString1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Ip格式错误";
        }

        try {
            connection = (HttpURLConnection) urL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return "网络访问失败";
        }
        try {
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            connection.setInstanceFollowRedirects(true);//设置自动重定向
            connection.setUseCaches(false);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return   "上传失败";
        }
        try {
            dataOutputStream.writeBytes(paramString2);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败";
        }
        try {
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStreamReader reader=new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader=new BufferedReader(reader);
                String inputLine=null;
                while((inputLine =bufferedReader.readLine())!=null){
                    stringBuffer.append(inputLine);
                }
                reader.close();
                bufferedReader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return  "获取数据返回失败";
        }
        connection.disconnect();
        return stringBuffer.toString();
    }

}