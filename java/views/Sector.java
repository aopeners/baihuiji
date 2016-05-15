package views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import baihuiji.jkqme.baihuiji.R;

/**
 * 绘制扇形的类
 *
 * @author jkqme
 */
public class Sector extends View {
    private float b, o, r, g;//金额统计参数
    private float tb, r2;//天蓝，粉红
    private int width, minWidth;
    private int height, minHeight;
    private Paint paint = new Paint();
    private boolean candraw;
    private float startdegree = 0f;// 开始角
    private float currentDegree = 0f;// 结尾角
    private Canvas canvas;
    private Handler handler = new Handler() {
        float degree = 360f;

        public void handleMessage(android.os.Message msg) {

            if (msg.what == 1) {
                ini();
            } else {
                // 每次画的弧度
                if (candraw) {
                    startdegree = currentDegree;
                    currentDegree += 5f;

                    Log.i("hander", "draw");
                    invalidate();
                }
            }
        }

        ;
    };

    public Sector(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /**
     * 设置颜色比重
     *
     * @param b 蓝
     * @param o 橙
     * @param r 红
     * @param g 绿
     */
    public void setWight(float b, float o, float r, float g) {
        this.b = 360 * b;
        this.o = 360 * o;
        this.r = 360 * r;
        this.g = 360 * g;
        handler.sendEmptyMessageDelayed(1, 500);

    }

    public void setWidth(float b, float o, float r, float g, float tb, float r2) {
        this.b = 360 * b;
        this.o = 360 * o;
        this.r = 360 * r;
        this.g = 360 * g;
        this.tb = 360 * tb;
        this.r2 = 360 * r2;
        handler.sendEmptyMessageDelayed(1, 500);
    }

    /**
     * 绘制扇形部分
     */
    private void todraw(Canvas canvas) {
        final Canvas c = canvas;

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                while (currentDegree <= 360f) {


                    invalidate();

                }
            }
        }).start();

    }

    @Override
    // 绘制
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if (this.canvas == null) {
            this.canvas = canvas;
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
        }
        if (candraw) {

            RectF rectF = new RectF(0f, 0f, width, height);

            candraw = false;

            if (currentDegree <= b) {

                // 画绿
                paint.setColor(getResources().getColor(R.color.blue));
                if (startdegree + 5f > 360) {
                    canvas.drawArc(rectF, b + o + r + g, 360f - b - o - r - g, true, paint);
                }
                canvas.drawArc(rectF, 0f, startdegree + 5f, true, paint);

            } else if (b < currentDegree && currentDegree <= b + o) {
                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                if (startdegree + 5f > 360) {
                    canvas.drawArc(rectF, b + o + r + g, 360f - b - o - r - g, true, paint);
                }
                canvas.drawArc(rectF, b, startdegree - b + 5f, true, paint);
            } else if (b + o < currentDegree && currentDegree <= b + o + r) {
                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                canvas.drawArc(rectF, b, o, true, paint);
                paint.setColor(getResources().getColor(R.color.red));
                if (startdegree + 5f > 360) {
                    canvas.drawArc(rectF, b + o + r + g, 360f - b - o - r - g, true, paint);
                }
                canvas.drawArc(rectF, b + o, startdegree + 5f - b - o, true,
                        paint);
            } else if (b + o + r < currentDegree && currentDegree <= b + o + r + g) {//画绿
                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                canvas.drawArc(rectF, b, o, true, paint);
                paint.setColor(getResources().getColor(R.color.red));
                canvas.drawArc(rectF, b + o, r, true, paint);
                paint.setColor(getResources().getColor(R.color.green));
                if (startdegree + 5f > 360) {
                    canvas.drawArc(rectF, b + o + r + g, 360f - b - o - r - g, true, paint);
                }
                canvas.drawArc(rectF, b + o + r, startdegree + 5f - b - o - r,
                        true, paint);

            } else if (b + o + r + g < currentDegree && currentDegree <= b + o + r + g + tb) {//画tb
                //最后一次绘制
                // currentDegree = 360f;

                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                canvas.drawArc(rectF, b, o, true, paint);
                paint.setColor(getResources().getColor(R.color.red));
                canvas.drawArc(rectF, b + o, r, true, paint);
                paint.setColor(getResources().getColor(R.color.green));//这里可能存在偏差
                canvas.drawArc(rectF, b + o + r, g, true, paint);
                paint.setColor(getResources().getColor(R.color.tbluee));
                if (startdegree + 5f > 360) {
                    canvas.drawArc(rectF, b + o + r + g, 360f - b - o - r - g, true, paint);
                }
                canvas.drawArc(rectF, b + o + r + g, startdegree + 5f - b - o - r - g, true, paint);
            } else if (b + o + r + g + tb < currentDegree && currentDegree <= b + o + r + g + tb + r2) {

                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                canvas.drawArc(rectF, b, o, true, paint);
                paint.setColor(getResources().getColor(R.color.red));
                canvas.drawArc(rectF, b + o, r, true, paint);
                paint.setColor(getResources().getColor(R.color.green));//这里可能存在偏差
                canvas.drawArc(rectF, b + o + r, g, true, paint);
                paint.setColor(getResources().getColor(R.color.tbluee));
                canvas.drawArc(rectF, b + o + r + g, tb, true, paint);
                paint.setColor(getResources().getColor(R.color.red1));
                if (startdegree + 5f > 360) {
                    canvas.drawArc(rectF, b + o + r + g, 360f - b - o - r - g - tb, true, paint);
                }
                canvas.drawArc(rectF, b + o + r + g + tb, startdegree + 5f - b - o - r - g - tb, true, paint);
            }
            if (currentDegree <= 360) {
                candraw = true;
                handler.sendEmptyMessageDelayed(2, 20);
            }

        }
        // super.onDraw(this.canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 测量
     */
    @SuppressLint("NewApi")
    private void ini() {

        width = getMeasuredWidth();
        Log.i("ini", width + "");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                width, 1);
        this.setLayoutParams(params);
        height = width;

        candraw = true;
        invalidate();
    }
}
