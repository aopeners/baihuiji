package baihuiji.jkqme.baihuiji;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.camera.open.CameraManager;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.Buffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import frament.Home_display_fragment;
import views.MyView;

/**
 * 扫码activity,可以扫描
 */
public class Decoder extends Activity {
    private boolean hashswich;
    //获取订单号，扫别人
    private final String ordSource = "http://baihuiji.weikebaba.com/pospay/getPosOrderNo";
    //收款，扫比人
    private final String getMony = "http://baihuiji.weikebaba.com/pospay/posPayStart";
    //收款，给别人扫
    private final String getMonny_bySwap = "http://baihuiji.weikebaba.com/pospay/erWeiCodePay";

    private String bitmapurl;
    private TextView bTextView;
    private TextView pleaseSwap;//请求烧苗的

    private ImageView backimg;
    private ImageView swichImg;//给出二维码的视图
    private RelativeLayout layout;//二位码扫描框的视图
    private int payType;
    private float money;
    private String orderNO;
    private LinearLayout layout1, layout2;//全面局，和支付方式框
    // private String authCode;//授权码
    private boolean onOrder = false;//正在收款
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {
                case R.id.count_back:
                    finish();
                    break;
                case R.id.decod1_bt_tx:
                    swich();
                    break;
                case R.id.decod1_slect_tx:
                    selectState(1, "尚未开通微信支付");
                    break;
                case R.id.decod1_slect1_tx:
                    selectState(2, "尚未开通QQ钱包");
                    break;
                case R.id.decod1_slect2_tx:
                    selectState(3, "尚未开通支付宝");
                    break;
                case R.id.decod1_slect3_tx:
                    selectState(4, "尚未开通百度钱包");

                    break;
            }

        }
    };

    private void selectState(int i, String msg) {
        MyApplaication applaication = (MyApplaication) getApplication();
        switch (i) {
            case 1: if (applaication.getDate("payTypeStatus").charAt(1) == '1') {
                payType = i;
            } else {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
                break;
            case 2: if (applaication.getDate("payTypeStatus").charAt(1) == '2') {
                payType = i;
            } else {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
                break;
            case 3: if (applaication.getDate("payTypeStatus").charAt(1) == '3') {
                payType = i;
            } else {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
                break;
            case 4: if (applaication.getDate("payTypeStatus").charAt(1) == '4') {
                payType = i;
            } else {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
                break;
        }

    }

    private MyView myView;
    private QRCodeReaderView.OnQRCodeReadListener onQRCodeReadListener = new QRCodeReaderView.OnQRCodeReadListener() {
        public void QRCodeNotFoundOnCamImage() {
        }

        public void cameraNotFound() {
        }

        public void onQRCodeRead(String paramAnonymousString, PointF[] paramAnonymousArrayOfPointF) {
            Log.i("QRcode", paramAnonymousString + "   " + onOrder);
            // Toast.makeText(Decoder.this,paramAnonymousString,Toast.LENGTH_LONG).show();
            //  authCode=paramAnonymousString;
            if (!onOrder) {
                onOrder = true;
                getOrder(paramAnonymousString);
            }

        }
    };
    private QRCodeReaderView readerView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    /**
     * @param paramBundle
     */
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.decod1);
        this.readerView = ((QRCodeReaderView) findViewById(R.id.decode1_qv));
        this.readerView.setOnQRCodeReadListener(this.onQRCodeReadListener);
        this.bTextView = ((TextView) findViewById(R.id.decod1_bt_tx));
        pleaseSwap = (TextView) findViewById(R.id.decod_plsese_swap_tx);
        this.backimg = ((ImageView) findViewById(R.id.count_back));
        swichImg = (ImageView) findViewById(R.id.decode1_scan_img);
        layout = (RelativeLayout) findViewById(R.id.decode1_relative);
        this.backimg.setOnClickListener(this.listener);
        this.bTextView.setOnClickListener(this.listener);

        layout1 = (LinearLayout) findViewById(R.id.decod1_linear);
        layout2 = (LinearLayout) findViewById(R.id.decod1_select_linear);

        int[] textId = {R.id.decod1_slect_tx, R.id.decod1_slect1_tx, R.id.decod1_slect2_tx, R.id.decod1_slect3_tx};
        TextView textView;
        for (int i = 0; i < textId.length; i++) {
            textView = (TextView) findViewById(textId[i]);
            textView.setOnClickListener(listener);
        }
        getDate();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    /**
     * 視圖
     * 視圖切换
     */
    private void swich() {
        if (readerView.getCameraManager().isOpen()) {
            hashswich = false;
        }
        if (hashswich) {
            return;
        }
        hashswich = true;
        if (layout.getVisibility() == View.VISIBLE) {
            layout1.setBackgroundColor(getResources().getColor(R.color.loginback));
            onOrder = false;
            readerView.getCameraManager().stopPreview();
            layout.setVisibility(View.GONE);
            swichImg.setVisibility(View.VISIBLE);
            pleaseSwap.setText("请顾客扫描二维码");
            bTextView.setText("切换到扫码收款");
            new MyAsy().execute("");
            hashswich = false;
        } else {
            layout1.setBackgroundColor(getResources().getColor(R.color.black));
            readerView.getCameraManager().startPreview();
            layout.setVisibility(View.VISIBLE);
            swichImg.setVisibility(View.GONE);
            pleaseSwap.setText("将二维码放入框内即可自行扫码");
            bTextView.setText("切换成二维码收款");
        }
    }

    /**
     * 设置收款方式，和收款金额
     */
    private void getDate() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("count");
        payType = bundle.getInt("payType");
        money = bundle.getFloat("money");
        boolean fukuanma = bundle.getBoolean("fukuan");
        if (payType == 0) {
            layout2.setVisibility(View.VISIBLE);
        } else {
            layout2.setVisibility(View.GONE);
        }
        if (fukuanma) {
            swich();
        }
    }

    protected void onDestroy() {
        this.readerView.getCameraManager().stopPreview();
        super.onDestroy();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == 4) {
            finish();
            return true;
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    protected void onPause() {
        super.onPause();
        this.readerView.getCameraManager().stopPreview();
    }

    protected void onResume() {
        super.onResume();
        this.readerView.getCameraManager().startPreview();
    }

    /**
     * 获得一个给别人扫的二维码
     */
    private Bitmap getOrderBitmap() {
        MyApplaication applaication = (MyApplaication) getApplication();

        String date = "operateTel";

        String key[] = {"merchantId", "ordSource", "payType", "totalFee", "operateId", "MD5"};
        String value[] = {applaication.getDate(key[0]), "pc", payType + "", (int) (money * 100) + "", applaication.getDate(date), getMd5_32("merchantId&ordSource&payType&totalFee&operateId&=*"
                + applaication.getDate(key[0]) + "*" + "pc" + "*" + payType + "*" + (int) (money * 100) + "*" + applaication.getDate(date))};
        String json = getJson(key, value, "MD5");
        String requst = urlconection(getMonny_bySwap, json);

        Log.i("OrderBitmap", json + "   " + requst);
        if (getRequstSuccess(requst)) {
            return onGetBitmapSucced("");
        } else {
            return null;
        }
    }

    /**
     * 当获取被扫描的bitmap成功时
     *
     * @param requst
     */
    private Bitmap onGetBitmapSucced(String requst) {
        JSONObject jsonObject;
        String bitUrl = null;
        String codUrl;
        try {
            jsonObject = new JSONObject(requst);
            bitUrl = jsonObject.getString("img");
            codUrl = jsonObject.getString("codeUrl");
            orderNO = jsonObject.getString("orderNo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //切换显示
        URL url = null;
        HttpURLConnection connection;
        Bitmap bitma = null;
        try {
            url = new URL(bitUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bitma = BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitma;
    }

    /**
     * "msg"=>成功
     *
     * @param requst
     * @return
     */
    private boolean getRequstSuccess(String requst) {
        try {
            JSONObject jsonObject = new JSONObject(requst);
            if (jsonObject.getString("msg").endsWith("成功")) {
                // orderNO=jsonObject.getString("orderNo");
                // bitmapurl=jsonObject.getString("img");
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 获得订单号，扫比人
     */
    private void getOrder(String authCode) {
        final String authcods = authCode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplaication applaication = (MyApplaication) getApplication();
                String key[] = {"merchantId", "ordSource", "MD5"};
                String value[] = {applaication.getDate(key[0]), "pc", getMd5_32("merchantId&ordSource&=*" + applaication.getDate(key[0]) + "*" + "pc")};
                String json = getJson(key, value, "MD5");
                //获取订单
                String orRequst = urlconection(ordSource, json);
                //   Log.i("Decoder_getOrder",orRequst);
                if (getOrderSucced(orRequst)) {
                    orderNO = getOrderNo(orRequst);
                    //收款部分
                    String key1[] = {"authCode", "merchantId", "orderNo", "ordSource", "payType", "totalFee", "operateId", "MD5"};
                    String value1[] = {authcods, applaication.getDate(key1[1]), orderNO, "pc", payType + "", (int) (money * 100) + "", applaication.getDate(key1[6]),
                            getMd5_32("authCode&merchantId&orderNo&ordSource&payType&totalFee&operateId&=*"
                                    + authcods + "*" + applaication.getDate(key1[1]) + "*" + orderNO + "*" + "pc" + "*" + payType + "*" + (int) (money * 100) + "*" + applaication.getDate(key1[6]))};
                    String json1 = getJson(key1, value1, "MD5");
                    String requst1 = urlconection(getMony, json1);
                    Log.i("Decoder_getOrder", requst1);
                    if (collectionSucced(requst1)) {
                        ShowTost("支付成功");
                        //没有返回或者没有支付成功的情况
                    } else {

                    }
                } else {
                    ShowTost("获取订单失败");
                }
                //

            }
        }).start();


    }

    /**
     * 判断 扫别人收款是否成功
     *
     * @param requsnt
     * @return
     */
    private boolean collectionSucced(String requsnt) {
        try {
            JSONObject jsonObject = new JSONObject(requsnt);
            if (jsonObject.getString("msg").endsWith("成功")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 获得订单号
     *
     * @param requst
     * @return
     */
    private String getOrderNo(String requst) {
        try {

            JSONObject jsonObject = new JSONObject(requst);
            return jsonObject.getString("orderNo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断获取订单是否成功
     *
     * @return
     */
    private boolean getOrderSucced(String requst) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(requst);
            if (jsonObject.getString("msg").endsWith("成功")) return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * md532位加密
     */
    private static String getMd5_32(String str) {
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
     * 获取网络应答
     *
     * @param paramString1 请求地址
     * @param paramString2 请求的jison
     * @return
     */
    private String urlconection(String paramString1, String paramString2) {
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
            return "上传失败";
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

    private String getJson(String[] paramArrayOfString1, String[] paramArrayOfString2, String paramString) {
        int i = 0;
        StringBuffer localStringBuffer = new StringBuffer();
        while (i <= paramArrayOfString1.length) {

            try {
                if (i == 0)//头
                    localStringBuffer.append("{");
                if (i >= paramArrayOfString1.length) {
                    localStringBuffer.append("}");
                    String str = localStringBuffer.toString();
                    break;
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
        return localStringBuffer.toString();
    }

    private void ShowTost(final String tost) {
        final String str = tost;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Decoder.this, str, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 形成二维码图片
     *
     * @param date 二维码内容
     * @return
     */
    protected Bitmap createZxing(String date) {
        String contentS = date;//
        int width = swichImg.getMeasuredWidth();
        int height = swichImg.getMeasuredHeight();
        //String format = "png";
        Map<EncodeHintType, Object> hintep = new HashMap<EncodeHintType, Object>();
        hintep.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bMatrix = null;
        try {

            // 生成图像,二维码
            bMatrix = new MultiFormatWriter().encode(contentS,
                    BarcodeFormat.QR_CODE, width, height, hintep);

        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int w = bMatrix.getWidth();
        int h = bMatrix.getHeight();
        int[] data = new int[w * h];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (bMatrix.get(x, y))
                    data[y * w + x] = 0xff000000;// 黑色
                else
                    data[y * w + x] = -1;// -1 相当于0xffffffff 白色
            }
        }

        // 创建一张bitmap图片，采用最高的图片效果ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        // 将上面的二维码颜色数组传入，生成图片颜色, RuntimeException("Stub!");
        bitmap.setPixels(data, 0, w, 0, 0, w, h);


        return bitmap;
    }


    @Override
    public void onStop() {
        readerView.getCameraManager().stopPreview();
        super.onStop();


    }

    class MyAsy extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getOrderBitmap();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            swichImg.setImageBitmap(bitmap);
        }
    }

    ;
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.Decoder
 * JD-Core Version:    0.6.2
 */