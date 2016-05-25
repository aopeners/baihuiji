package baihuiji.jkqme.baihuiji;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.camera.open.CameraManager;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import views.MyView;


/**
 * 扫码activity,可以扫描,返回的result code 2,为收款， 3为退款
 * 接收的 requst  不用判断
 */
public class Decoder extends Activity {
    private boolean toDecor = true;
    private boolean isRefund;//是否退款
    private boolean tradSucess = false;//交易成功判断
    private boolean hashswich;//是否切换方式判断
    //获取订单号，扫别人
    private final String ordSource = "http://baihuiji.weikebaba.com/pospay/getPosOrderNo";
    //收款，扫比人
    private final String getMony = "http://baihuiji.weikebaba.com/pospay/posPayStart";
    //收款，给别人扫
    private final String getMonny_bySwap = "http://baihuiji.weikebaba.com/pospay/erWeiCodePay";
    //检查收款是否成功
    private final String tradQuerry = "http://baihuiji.weikebaba.com/pospay/queryPayState";
    private View view1, view2;//切换视图时
    private String bitmapurl;
    private TextView bTextView;
    private TextView pleaseSwap;//请求烧苗的
    //显示账目的
    private TextView merchantId;
    private TextView merchatMoney;

    private ImageView backimg;
    private ImageView swichImg;//给出二维码的视图
    private RelativeLayout layout;//二位码扫描框的视图
    private int payType;
    private String money;
    private String orderNO;
    private LinearLayout layout1, layout2;//全面局，和支付方式框
    // private String authCode;//授权码
    private boolean onOrder = false;//正在收款

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!tradSucess) {
                tradeSuccess();
            }
            super.handleMessage(msg);
        }
    };
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {
                case R.id.count_back:
                  /*  readerView.getCameraManager().stopPreview();
                    readerView.getCameraManager().closeDriver();*/
                    finish();
                    break;
                case R.id.decod1_bt_tx:
                    swich();
                    break;
                case R.id.decod1_slect_tx:

                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.INVISIBLE);
                    selectState(1, "尚未开通微信支付");

                    break;
                case R.id.decod1_slect1_tx:
                    view2.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.INVISIBLE);
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
        showProgress();
        switch (i) {
            case 1:
                if (applaication.getDate("payTypeStatus").charAt(0) == '1') {
                    payType = i;
                    new MyAsy().execute("");
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                if (applaication.getDate("payTypeStatus").charAt(2) == '1') {
                    payType = i;
                    new MyAsy().execute("");
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (applaication.getDate("payTypeStatus").charAt(4) == '1') {
                    payType = i;
                    new MyAsy().execute("");
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if (applaication.getDate("payTypeStatus").charAt(6) == '1') {
                    payType = i;
                    new MyAsy().execute("");
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
            //是否退款
            if (!isRefund) {
                //是否已获得订单号
                if (!onOrder) {
                    onOrder = true;
                    getOrder(paramAnonymousString);
                }
            } else {
                if (!onOrder) {
                    onOrder = true;
                    onReFound(paramAnonymousString);
                }

            }
        }
    };

    /**
     * 收款成功时
     */
    private void onOrderSuccess(String time) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("payType", payType + "");
        bundle.putString("signal", orderNO);
        bundle.putString("paytime", time);
        bundle.putString("payTotal", money);
        //收款成功位
        bundle.putBoolean("getSuccess", true);
        intent.putExtra("onTradSuccess", bundle);
        setResult(3, intent);
        if (readerView != null) {
            if (readerView.getCameraManager() != null) {
                readerView.getCameraManager().stopPreview();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    //扫描退款成功时,回到账单详情退款
    private void onReFound(String sigal) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("signal", sigal);
        bundle.putBoolean("refondSuccess", true);
        bundle.putBoolean("backSuccess", true);
        intent.putExtra("signal", bundle);

        setResult(2, intent);
        if (readerView != null) {
            if (readerView.getCameraManager() != null) {
                readerView.getCameraManager().stopPreview();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        finish();

    }

    //
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

        view1 = findViewById(R.id.decod1_v1);
        view2 = findViewById(R.id.decod1_v2);

        layout1 = (LinearLayout) findViewById(R.id.decod1_linear);
        //付款方式选择框
        layout2 = (LinearLayout) findViewById(R.id.decod1_select_linear);
        // decod1_select_linear
        merchantId = (TextView) findViewById(R.id.decod1_mercant_tx);
        merchatMoney = (TextView) findViewById(R.id.decod1_mercan_money_tx);
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

        //判断相机是否开启，如果是

       /* if (readerView.getCameraManager().isOpen()&&layout.getVisibility() == View.VISIBLE) {
            hashswich = false;//当相机开启时设置为可切换
        } else if (readerView.getCameraManager().isOpen()&&layout.getVisibility() == View.VISIBLE) {
        //如果相机开启，返回,hashswich 记录相机是否切换
        if (hashswich) {
            return;

        }*/
        //切换到了相机但相机还没开启，不能切换
        if(hashswich&&!readerView.getCameraManager().isOpen()){
           return;
        }
        Log.i("DecoderSwich", "" + toDecor);
        if (layout.getVisibility() == View.VISIBLE) {
            layout1.setBackgroundColor(getResources().getColor(R.color.loginback));
            layout2.setVisibility(View.VISIBLE);
            onOrder = false;
            readerView.setVisibility(View.INVISIBLE);
            if (readerView != null) {
                readerView.getCameraManager().stopPreview();
            }
            layout.setVisibility(View.GONE);
            swichImg.setVisibility(View.VISIBLE);
            pleaseSwap.setText("请顾客扫描二维码");
            pleaseSwap.setTextColor(getResources().getColor(R.color.black));

            bTextView.setText("切换到扫码收款");
            merchantId.setVisibility(View.VISIBLE);
            merchatMoney.setVisibility(View.VISIBLE);
            showProgress();
            new MyAsy().execute("");
            hashswich = false;
        } else {
            layout1.setBackgroundColor(getResources().getColor(R.color.black));
            readerView.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            if (readerView != null) {
                readerView.getCameraManager().startPreview();
            }
            layout.setVisibility(View.VISIBLE);
            swichImg.setVisibility(View.GONE);

            merchatMoney.setVisibility(View.INVISIBLE);
            merchantId.setVisibility(View.INVISIBLE);

            pleaseSwap.setText("将二维码放入框内即可自行扫码");
            pleaseSwap.setTextColor(getResources().getColor(R.color.white));
            bTextView.setText("切换成二维码收款");
            hashswich=true;//切换到了相机
        }
    }

    /**
     * 收款时进入的方法，设置收款方式，和收款金额
     */
    private void getDate() {
        MyApplaication applaication = (MyApplaication) getApplication();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("count");
        //判断是否为退款
        if (!bundle.getBoolean("isRefund")) {
            isRefund = false;
            payType = bundle.getInt("payType");
            if(payType!=0){
                LinearLayout linearLayout= (LinearLayout) findViewById(R.id.select_paytype_linear);
                linearLayout.setVisibility(View.INVISIBLE);
            }
            money = bundle.getString("money");
            boolean fukuanma = bundle.getBoolean("fukuan", false);
            Log.i("Decoder", " fukuanma " + fukuanma);
            merchantId.setText(applaication.getDate("merName"));
            merchatMoney.setText("¥" + money);
            if (fukuanma) {
                //toDecor = false;
                layout1.setBackgroundColor(getResources().getColor(R.color.loginback));
                layout2.setVisibility(View.VISIBLE);
                onOrder = false;
                try {
                    //延时
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readerView.setVisibility(View.INVISIBLE);
               // readerView.getCameraManager().closeDriver();
                readerView.getCameraManager().stopPreview();
                layout.setVisibility(View.GONE);
                swichImg.setVisibility(View.VISIBLE);
                pleaseSwap.setText("请顾客扫描二维码");
                pleaseSwap.setTextColor(getResources().getColor(R.color.black));

                bTextView.setText("切换到扫码收款");
                merchantId.setVisibility(View.VISIBLE);
                merchatMoney.setVisibility(View.VISIBLE);
                showProgress();

                new MyAsy().execute("");
                hashswich = false;
            } else {
                layout2.setVisibility(View.INVISIBLE);
            }
        } else {//退款
            layout2.setVisibility(View.INVISIBLE);
            isRefund = true;
            TextView textView = (TextView) findViewById(R.id.count_title_tx);
            textView.setText("退款扫描");
            bTextView.setVisibility(View.INVISIBLE);
        }
    }

    protected void onDestroy() {
        if (readerView != null) {
            Log.i("Decoder", "Destroy");
            this.readerView.getCameraManager().stopPreview();
        /*    CameraManager manager = readerView.getCameraManager();
            manager.getCamera().release();*/
        }
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
        if (readerView != null) {
            this.readerView.getCameraManager().stopPreview();
           /* CameraManager manager = readerView.getCameraManager();
            manager.getCamera().release();*/
        }
    }

    protected void onResume() {
        Log.i("DecoderonResume", "Dcoder");
        super.onResume();
        if (readerView == null) {
            readerView = ((QRCodeReaderView) findViewById(R.id.decode1_qv));
        }
        CameraManager manager = readerView.getCameraManager();
        if (readerView.getCameraManager() != null) {
           this.readerView.getCameraManager().startPreview();
        } else {
            readerView.getCameramanager();
        }
    //    getDate();
    }

    /**
     * 获得一个给别人扫的二维码
     */
    private Bitmap getOrderBitmap() {
        MyApplaication applaication = (MyApplaication) getApplication();
        if (payType == 0) {
            ShowTost("请选择支付类型");
            return null;
        }
        String date = "operateTel";

        String key[] = {"merchantId", "ordSource", "payType", "totalFee", "operateId", "MD5"};
        String value[] = {applaication.getDate(key[0]), "app", payType + "", money.replace(".", "") + "", applaication.getDate(date), getMd5_32("merchantId&ordSource&payType&totalFee&operateId&=*"
                + applaication.getDate(key[0]) + "*" + "app" + "*" + payType + "*" + money.replace(".", "") + "*" + applaication.getDate(date))};
        String json = getJson(key, value, "MD5");
        String requst = urlconection(getMonny_bySwap, json);

        Log.i("OrderBitmap", json + "   " + requst);
        if (getRequstSuccess(requst)) {
            return onGetBitmapSucced(requst);
        } else {
            showToast(getMsg(requst));
            return null;
        }
    }

    private String getMsg(String requst) {
        try {
            JSONObject jsonObject = new JSONObject(requst);
            return jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "访问失败";
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
            return null;
        }
        //切换显示
        URL url = null;
        HttpURLConnection connection;
        Bitmap bitma = null;
        try {
            url = new URL(bitUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
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
            return null;
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
            if (jsonObject.getString("code").equals("100")) {
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
     * 检验交易是否成功
     *
     * @return
     */
    private void tradeSuccess() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String key[] = {"orderNo", "MD5"};
                String value[] = {orderNO, getMd5_32("orderNo&=*" + orderNO)};
                String json = getJson(key, value, "MD5");
                String requst = urlconection(tradQuerry, json);
                if (getRequstSuccess(requst)) {
                    // ShowTost("交易成功");
                    onOrderSuccess(getTime(requst));
                    tradSucess = true;
                } else {
                    handler.sendEmptyMessageDelayed(1, 8000);
                }
            }
        }).start();

    }

    /**
     * 获得交易时间
     *
     * @param requst
     * @return
     */
    private String getTime(String requst) {
        try {
            JSONObject jsonObject = new JSONObject(requst);
            return jsonObject.getString("time");
        } catch (JSONException e) {
            e.printStackTrace();
            return "没有获取到交易时间";
        }
    }

    /**
     * 获得订单号，扫比人
     */
    private void getOrder(String authCode) {
        showProgress();
        final String authcods = authCode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplaication applaication = (MyApplaication) getApplication();
                String key[] = {"merchantId", "ordSource", "MD5"};
                String value[] = {applaication.getDate(key[0]), "app", getMd5_32("merchantId&ordSource&=*" + applaication.getDate(key[0]) + "*" + "app")};
                String json = getJson(key, value, "MD5");
                //获取订单
                String orRequst = urlconection(ordSource, json);
                //   Log.i("Decoder_getOrder",orRequst);
                if (getOrderSucced(orRequst)) {
                    orderNO = getOrderNo(orRequst);
                    //收款部分
                    String key1[] = {"authCode", "merchantId", "orderNo", "ordSource", "payType", "totalFee", "operateId", "MD5"};
                    String value1[] = {authcods, applaication.getDate(key1[1]), orderNO, "app", payType + "", money.replace(".", "") + "", applaication.getDate(key1[6]),
                            getMd5_32("authCode&merchantId&orderNo&ordSource&payType&totalFee&operateId&=*"
                                    + authcods + "*" + applaication.getDate(key1[1]) + "*" + orderNO + "*" + "app" + "*" + payType + "*" + money.replace(".", "") + "*" + applaication.getDate(key1[6]))};
                    String json1 = getJson(key1, value1, "MD5");
                    String requst1 = urlconection(getMony, json1);
                    Log.i("Decoder_getOrder", requst1);
                    if (collectionSucced(requst1)) {
                        // ShowTost("交易成功");
                        //没有返回或者没有支付成功的情况
                        showToast("收款成功");
                        onOrderSuccess(getTime(requst1));
                    } else {

                    }
                } else {
                    showToast("获取订单失败");
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
        if (readerView != null) {
            // CameraManager manager = readerView.getCameraManager();
            readerView.getCameraManager().stopPreview();
            //   manager.getCamera().release();
        }
        super.onStop();


    }
    private SurfaceHolder holder;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        holder=readerView.getHolder();
    }

    class MyAsy extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getOrderBitmap();
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialogCancle();
            if (bitmap != null) {

                swichImg.setImageBitmap(bitmap);
                handler.sendEmptyMessageAtTime(1, 8000);
            }
        }
    }

    private void showToast(final String string) {
        runOnUiThread(new Runnable() {
            public void run() {
                progerss.setCancelable(true);
                progerss.cancel();
                Toast.makeText(Decoder.this, string, Toast.LENGTH_LONG).show();
            }
        });
    }

    //progress+dialog
    private android.support.v7.app.AlertDialog progerss;

    private void showProgress() {
        if (progerss == null) {

            progerss = new android.support.v7.app.AlertDialog.Builder(this, R.style.mydiaog).create();
            progerss.setView(LayoutInflater.from(this).inflate(R.layout.progress, null, true));
            progerss.setCanceledOnTouchOutside(false);
            progerss.setCancelable(false);

        }
        progerss.show();
    }

    private void dialogCancle() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progerss.setCancelable(true);
                progerss.cancel();
            }
        });
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.Decoder
 * JD-Core Version:    0.6.2
 */