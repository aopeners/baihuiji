package baihuiji.jkqme.baihuiji;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.client.android.camera.open.CameraManager;

import views.MyView;

/**
 * 扫码activity,可以扫描
 */
public class Decoder extends Activity {
    private TextView bTextView;
    private ImageView backimg;
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {
                case R.id.count_back:
                    break;
                case R.id.decod1_bt_tx:
                    break;
            }

        }
    };
    private MyView myView;
    private QRCodeReaderView.OnQRCodeReadListener onQRCodeReadListener = new QRCodeReaderView.OnQRCodeReadListener() {
        public void QRCodeNotFoundOnCamImage() {
        }

        public void cameraNotFound() {
        }

        public void onQRCodeRead(String paramAnonymousString, PointF[] paramAnonymousArrayOfPointF) {
            Log.i("QRcode", paramAnonymousString);
           // Toast.makeText(Decoder.this,paramAnonymousString,Toast.LENGTH_LONG).show();
        }
    };
    private QRCodeReaderView readerView;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.decod1);
        this.readerView = ((QRCodeReaderView) findViewById(R.id.decode1_qv));
        this.readerView.setOnQRCodeReadListener(this.onQRCodeReadListener);
        this.bTextView = ((TextView) findViewById(R.id.decod1_bt_tx));
        this.backimg = ((ImageView) findViewById(R.id.count_back));
        this.backimg.setOnClickListener(this.listener);
        this.bTextView.setOnClickListener(this.listener);
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
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.Decoder
 * JD-Core Version:    0.6.2
 */