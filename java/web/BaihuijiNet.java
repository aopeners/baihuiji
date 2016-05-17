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
    /**
     * 合成json
     *
     * @param paramArrayOfString1
     * @param paramArrayOfString2
     * @param param
     * @return
     */
    public synchronized static String getJson(String[] paramArrayOfString1, String[] paramArrayOfString2, String param) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("{");
        int i = 0;
        while (i < paramArrayOfString1.length) {
            if (paramArrayOfString1[i].equals(param)) {
                localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
                localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"");
                localStringBuffer.append("}");
                return localStringBuffer.toString();
            } else {
                localStringBuffer.append("\"" + paramArrayOfString1[i] + "\"" + ":");
                localStringBuffer.append("\"" + paramArrayOfString2[i] + "\"" + ",");
            }
            i++;
        }
        return localStringBuffer.toString();
    }

    /**
     * md532位加密
     */
    public synchronized static String getMd5_32(String str) {
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

    /**
     * 网络状态
     *
     * @param paramContext
     * @return
     */
    public synchronized static boolean getNetState(Context paramContext) {
        ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (localConnectivityManager != null) {
            NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
            if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()) && (localNetworkInfo.getState().equals(State.CONNECTED)))
                return true;
        }
        return false;
    }

    public synchronized static String getTime(String paramString) {
        return new SimpleDateFormat(paramString).format(new Date());
    }

    /**
     * 获取网络应答
     *
     * @param paramString1 请求地址
     * @param paramString2 请求的jison
     * @return
     */
    public synchronized static String urlconection(String paramString1, String paramString2) {
        StringBuffer stringBuffer = new StringBuffer();
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
            return "上传失败0";
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
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(reader);
                String inputLine = null;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }
                
                reader.close();
                bufferedReader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "获取数据返回失败";
        }
        connection.disconnect();
        return stringBuffer.toString();
    }

    /**
     * 获取get参数
     * @param key
     * @param value
     * @return
     */
    public synchronized static String getRequst(String key[], String value[]) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");
        for (int i = 0; i < key.length; i++) {
            stringBuffer.append(key[i] + "=" + value[i]);
            if (i < key.length - 1) {
                stringBuffer.append("&");
            }

        }
        return stringBuffer.toString();
    }
    /**
     * get方法获取数据
     * @param url
     * @return
     */
    public synchronized static String  connection(String url){
        StringBuffer stringBuffer = new StringBuffer();
        URL urls = null;
        HttpURLConnection connection;
        try {
            urls=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "网络格式错误";
        }
        try {
            connection= (HttpURLConnection) urls.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return "链接失败";
        }
        try {
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            connection.setInstanceFollowRedirects(true);
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "未知错误";
        }
        try {
            InputStreamReader streamReader=new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String inputLine = null;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            streamReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "读取流文件失败";
        }
        connection.disconnect();
        return stringBuffer.toString();
    }
}
