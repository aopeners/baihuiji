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

import java.math.BigDecimal;

import baihuiji.jkqme.baihuiji.R;

/**
 * 绘制扇形的类
 *
 * @author jkqme
 */
public class Sector extends View {
    private BigDecimal b, o, r, g;//金额统计参数
    private BigDecimal tb, r2;//天蓝，粉红
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

                    //   Log.i("hander", "draw");
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
    public void setWight(String b, String o, String r, String g) {
        BigDecimal a = new BigDecimal(b).add(new BigDecimal(o)).add(new BigDecimal(r)).add(new BigDecimal(g));
        try {
            this.b = new BigDecimal("360").multiply(new BigDecimal(b)).divide(a, 3);
            this.o = new BigDecimal("360").multiply(new BigDecimal(o)).divide(a, 3);
            this.r = new BigDecimal("360").multiply(new BigDecimal(r)).divide(a, 3);
            this.g = new BigDecimal("360").multiply(new BigDecimal(g)).divide(a, 3);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            Log.i("Sector", a.toString());
            return;
        }

        tb = new BigDecimal("0");
        r2 = new BigDecimal("0");
        //  candraw=true;
        currentDegree = 0;
        handler.sendEmptyMessageDelayed(1, 500);

    }

    public void setWidth(String b, String o, String r, String g, String tb, String r2) {
        BigDecimal a = new BigDecimal(b).add(new BigDecimal(o)).add(new BigDecimal(r)).
                add(new BigDecimal(g)).add(new BigDecimal(tb)).add(new BigDecimal(r2));
        this.b = new BigDecimal("360").multiply(new BigDecimal(b)).divide(a, 3);
        this.o = new BigDecimal("360").multiply(new BigDecimal(o)).divide(a, 3);
        this.r = new BigDecimal("360").multiply(new BigDecimal(r)).divide(a, 3);
        this.g = new BigDecimal("360").multiply(new BigDecimal(g)).divide(a, 3);

        this.tb = new BigDecimal("360").multiply(new BigDecimal(tb)).divide(a, 3);
        this.r2 = new BigDecimal("360").multiply(new BigDecimal(r2)).divide(a, 3);
        // candraw=true;
        currentDegree = 0;
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
            float b = this.b.floatValue(), o = this.o.floatValue(), r = this.r.floatValue(),
                    g = this.g.floatValue(), tb = this.tb.floatValue(), r2 = this.r2.floatValue();//金额统计参数
            //天蓝，粉红
            candraw = false;
            if (currentDegree <= b) {

                // 画绿
                paint.setColor(getResources().getColor(R.color.blue));
                if (startdegree + 5f > 360) {
                    canvas.drawArc(rectF, startdegree, 360f - startdegree, true, paint);
                } else
                    canvas.drawArc(rectF, 0f, startdegree + 5f, true, paint);

            } else if (b < currentDegree && currentDegree <= b + o) {
                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                if (startdegree + 5f > 360) {
                    canvas.drawArc(rectF, startdegree, 360f - startdegree, true, paint);
                } else
                    canvas.drawArc(rectF, b, startdegree - b + 5f, true, paint);
            } else if (b + o < currentDegree && currentDegree <= b + o + r) {
                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                canvas.drawArc(rectF, b, o, true, paint);
                paint.setColor(getResources().getColor(R.color.red));
                if (startdegree + 5f > 360) {
                    canvas.drawArc(rectF, startdegree, 360f - startdegree, true, paint);
                } else
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
                    canvas.drawArc(rectF, startdegree, 360f - startdegree, true, paint);
                } else
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
                    canvas.drawArc(rectF, startdegree, 360f - startdegree, true, paint);
                } else
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
                    canvas.drawArc(rectF, startdegree, 360f - startdegree, true, paint);
                } else
                    canvas.drawArc(rectF, b + o + r + g + tb, startdegree + 5f - b - o - r - g - tb, true, paint);
            }
            if (currentDegree <= 360) {
                candraw = true;
                handler.sendEmptyMessageDelayed(2, 20);
            }

        }
        super.onDraw(this.canvas);
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
