package views;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import baihuiji.jkqme.baihuiji.R;

/**
 * 关于matrix 应用的类，matrix 使用前要set一下，再用pre 和post
 *
 * @author jkqme
 */
public class MyView extends View {
    private int width = 0;//扫描框高度
    private int height = 0;//扫描线高度
    private int start = 0;
    private boolean toshow = true;
    private Handler handler = new Handler() {
        // 自定义类的局部变量
        int i = 70;

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            // Log.i("MyviewHander", heights + "");
            //复位
            if (i == 70) {
                matrix.setTranslate(0, 0);
                height = 0;
                i = 1;
            }
            if (toshow) {
                i++;

                height = height + 4;
                invalidate();
                sendEmptyMessageDelayed(1, 60);
            }
        }
    };
    private float heights;
    private float Y = 0;
    Paint paint = new Paint();
    Matrix matrix = new Matrix();

    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
            R.drawable.scan_name);
    private RectF f;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        // matrix.setTranslate(0, 0);
        // Log.i("Myview", "" + heights);

        handler.sendEmptyMessage(1);
    }

    private Canvas canva;

    @Override
    public void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if (width == 0) {
            width = getMeasuredWidth();
            if (canva == null) {
                canva = canvas;
                paint.setColor(getResources().getColor(R.color.green));
                paint.setStrokeWidth(3);

                canvas.save();

            }

        }

        if (canva != null) {
            //canva.restore();
            canva=canvas;
            //左上
           // Log.i("MyView", height + "");
            f = new RectF(0, 0, 10, 3);
            canva.drawRect(f, paint);
            f = new RectF(0, 0, 3, 10);
            canva.drawRect(f, paint);
            //左下
            f = new RectF(0, width - 10, 3, width);
            canva.drawRect(f, paint);
            f = new RectF(0, width - 3, 10, width);
            canva.drawRect(f, paint);
            //右下
            f = new RectF(width - 3, width - 10, width, width);
            canva.drawRect(f, paint);
            f = new RectF(width - 10, width - 3, width, width);
            canva.drawRect(f, paint);
            //右上
            f = new RectF(width - 10, 3, width, 0);
            canva.drawRect(f, paint);
            f = new RectF(width - 3, 10, width, 0);
            canva.drawRect(f, paint);

            canva.drawLine(0, height, width, height, paint);
        }
        // matrix.postTranslate(0, heights);
        float b[] = new float[9];
        // 获取3个矢量
       /* matrix.getValues(b);

        for (int i = 0; i < 6; ) {

            Log.i("matrix", b[i] + "  " + b[i++] + "  " + b[i++]);

        }*/
        // matrix.postTranslate(0, 4);
        //   canvas.drawBitmap(bitmap, matrix, paint);
        super.onDraw(canva);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub

        heights = MeasureSpec.getMode(heightMeasureSpec) / 5;
        Log.i("ONMeasure", heights + "    " + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    // 视图可见 变化时调用
    protected void onVisibilityChanged(View changedView, int visibility) {
        // TODO Auto-generated method stub
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != VISIBLE) {
            toshow = false;
        } else if (visibility == VISIBLE) {
            toshow = true;
            handler.sendEmptyMessageDelayed(1, 60);
        }
        Log.i("onVisible", "fdsf");
    }

    @Override
    // 视图被销毁时调用
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        toshow = false;
    }
}
