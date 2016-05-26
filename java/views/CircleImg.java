package views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import baihuiji.jkqme.baihuiji.R;

/**
 * 绘制圆形头像的类
 * Created by Administrator on 2016/5/26.
 */
public class CircleImg extends View {
    private Bitmap bitmap;//头像
    private Paint paint;
    private int width;
    private Canvas canvas;

    public CircleImg(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ini();
            }
        }
    };

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = delBitmaps(bitmap);
        handler.sendEmptyMessageDelayed(1, 500);
    }

    @SuppressLint("NewApi")
    private void ini() {
        width = getMeasuredWidth();
        Log.i("CircleImg_ine", "   " + width);
        width = 100;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width != 0) {
           /* Log.i("CircleImg_draw", "  " + width);
            RectF rectF = new RectF(0, 0, width, width);
            this.canvas.drawRoundRect(rectF, width / 2, width / 2, paint);
            this.canvas.drawBitmap(bitmap, null, rectF, paint);*/
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Log.i("CircleImg", "  " + widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 生成圆形bitmap
     *
     * @param bitmap
     * @return
     */
    private Bitmap delBitmaps(Bitmap bitmap) {
        int bwidth = bitmap.getWidth();
        int bheight = bitmap.getHeight();
        width = 100;
        Paint paint;
        Canvas canvas;
        paint = new Paint();
        //设置选择相交的部分 IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setAntiAlias(true);
        Bitmap backGround = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);

        canvas = new Canvas(backGround);//这样可以直接将画的东西作为bit
        paint.setColor(getResources().getColor(R.color.red));
        RectF rectF = new RectF(0, 0, width, width);
        //画圆，第二、三个参数为半径
        canvas.drawRoundRect(rectF, width / 2, width / 2, paint);
        Matrix matrix = new Matrix();
        //缩放处理
        if (bwidth > bheight) {
            matrix.reset();
            matrix.setScale(1, bwidth / bheight);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bwidth, bheight, matrix, true);
        } else if (bwidth < bheight) {
            matrix.reset();
            matrix.setScale(bheight / bwidth, 1);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bwidth, bheight, matrix, true);
        }
        bwidth = bitmap.getWidth();
        bheight = bitmap.getHeight();

        if (bwidth != width) {
            matrix.reset();
            matrix.setScale(width / bheight, width / bheight);
            //亚索输出？,图片压缩
            Log.i("DeilBitmap", bwidth + "  " + bheight);
            //不可用的缩放方式
            // bitmap = Bitmap.createBitmap(bitmap, 1, 1, bheight-1, bwidth-1, matrix, true);

        }
        canvas.drawBitmap(bitmap, matrix, paint);
        return backGround;
    }
}
