package views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.math.BigDecimal;

import baihuiji.jkqme.baihuiji0.R;


/**
 * 绘制扇形的类
 *在视图不可见时切换要用handler
 * @author jkqme
 */
public class Sector1 extends View {
    private Bitmap bitmap;
    private BigDecimal b, o, r, g;// 金额统计参数
    private BigDecimal tb, r2;// 天蓝，粉红
    private int width, minWidth;
    private int height, minHeight;
    private Paint paint = new Paint();
    private boolean candraw;
    private float startdegree = 0f;// 开始角
    private float currentDegree = 0f;// 结尾角
    private Canvas canvas;
    private boolean hasDraw = false;//已画过一次
    private float endDegree;
    private Handler handler = new Handler() {
        float degree = 360f;

        public void handleMessage(android.os.Message msg) {

            if (msg.what == 1) {
                ini();
            } else {
                // 每次画的弧度
                if (candraw) {
                    startdegree = currentDegree;
                    if (currentDegree != 360) {
                        currentDegree += 5f;
                    }
                    // Log.i("hander", "draw");
                    invalidate();
                }
            }
        }

        ;
    };

    public Sector1(Context context, AttributeSet attrs) {
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
        BigDecimal a = new BigDecimal(b).add(new BigDecimal(o))
                .add(new BigDecimal(r)).add(new BigDecimal(g));
        try {
            this.b = new BigDecimal("360").multiply(new BigDecimal(b)).divide(
                    a, 4);
            this.o = new BigDecimal("360").multiply(new BigDecimal(o)).divide(
                    a, 4);
            this.r = new BigDecimal("360").multiply(new BigDecimal(r)).divide(
                    a, 4);
            this.g = new BigDecimal("360").multiply(new BigDecimal(g)).divide(
                    a, 4);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            Log.i("Sector", a.toString());
            //return;
            this.b = new BigDecimal("0");
            this.o = new BigDecimal("0");
            this.r = new BigDecimal("0");
            this.g = new BigDecimal("0");
        }

        Log.i("Sector", "wightSum " + a.toString() + this.b + "   " + this.o
                + "  " + this.r + "   " + this.g);

        tb = new BigDecimal("0");
        r2 = new BigDecimal("0");
        // candraw=true;
        currentDegree = 0;
        handler.sendEmptyMessageDelayed(1, 500);

    }

    public void setWidth(String b, String o, String r, String g, String tb,
                         String r2) {
        BigDecimal a = new BigDecimal(b).add(new BigDecimal(o))
                .add(new BigDecimal(r)).add(new BigDecimal(g))
                .add(new BigDecimal(tb)).add(new BigDecimal(r2));
        this.b = new BigDecimal("360").multiply(new BigDecimal(b)).divide(a, 3);
        this.o = new BigDecimal("360").multiply(new BigDecimal(o)).divide(a, 3);
        this.r = new BigDecimal("360").multiply(new BigDecimal(r)).divide(a, 3);
        this.g = new BigDecimal("360").multiply(new BigDecimal(g)).divide(a, 3);

        this.tb = new BigDecimal("360").multiply(new BigDecimal(tb)).divide(a,
                3);
        this.r2 = new BigDecimal("360").multiply(new BigDecimal(r2)).divide(a,
                3);
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

    RectF rectF;

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

            float b = this.b.floatValue(), o = this.o.floatValue(), r = this.r
                    .floatValue(), g = this.g.floatValue(), tb = this.tb
                    .floatValue(), r2 = this.r2.floatValue();// 金额统计参数
            // Log.i("secor onDraw", b + " " + o + "  " + r + "   " + "g" +
            // "   " + tb + "    " + r2);
            // 天蓝，粉红
            candraw = false;
            if (currentDegree <= b) {
                // 画绿

                paint.setColor(getResources().getColor(R.color.blue));
                if (startdegree + 5f >= 360) {
                    canvas.drawArc(rectF, 0f, 360f - 0f,
                            true, paint);
                    endDegree = b;
                } else {
                    canvas.drawArc(rectF, 0f, startdegree + 5f, true, paint);
                    //Log.i("Ondraw", " b" + b);
                }

            } else if (b < currentDegree && currentDegree <= b + o) {
                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                if (startdegree + 5f >= 360) {
                    canvas.drawArc(rectF, b, 360f - b,
                            true, paint);
                    endDegree = o;
                } else
                    canvas.drawArc(rectF, b, startdegree - b + 5f, true, paint);
            } else if (b + o < currentDegree && currentDegree <= b + o + r) {
                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                canvas.drawArc(rectF, b, o, true, paint);
                paint.setColor(getResources().getColor(R.color.red));
                if (startdegree + 5f >= 360) {
                    canvas.drawArc(rectF, b + o, 360f - (b + o),
                            true, paint);
                    endDegree = r;
                } else
                    canvas.drawArc(rectF, b + o, startdegree + 5f - b - o,
                            true, paint);
            } else if (b + o + r < currentDegree
                    && currentDegree <= b + o + r + g) {// 画绿
                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                canvas.drawArc(rectF, b, o, true, paint);
                paint.setColor(getResources().getColor(R.color.red));
                canvas.drawArc(rectF, b + o, r, true, paint);
                paint.setColor(getResources().getColor(R.color.green));
                if (startdegree + 5f >= 360) {
                    canvas.drawArc(rectF, b + o + r, 360f - (b + o + r),
                            true, paint);
                    endDegree = g;
                    //Log.i("Secor","g");
                } else
                    canvas.drawArc(rectF, b + o + r, startdegree + 5f - b - o
                            - r, true, paint);

            } else if (b + o + r + g < currentDegree
                    && currentDegree <= b + o + r + g + tb) {// 画tb
                // 最后一次绘制
                // currentDegree = 360f;

                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                canvas.drawArc(rectF, b, o, true, paint);
                paint.setColor(getResources().getColor(R.color.red));
                canvas.drawArc(rectF, b + o, r, true, paint);
                paint.setColor(getResources().getColor(R.color.green));// 这里可能存在偏差
                canvas.drawArc(rectF, b + o + r, g, true, paint);
                paint.setColor(getResources().getColor(R.color.tbluee));
                if (startdegree + 5f >= 360) {
                    canvas.drawArc(rectF, b + o + r + g, 360f - (b + o + r + g),
                            true, paint);
                    endDegree = tb;
                    //Log.i("Secor","tb");
                } else
                    canvas.drawArc(rectF, b + o + r + g, startdegree + 5f - b
                            - o - r - g, true, paint);
            } else if (b + o + r + g + tb < currentDegree
                    && currentDegree <= b + o + r + g + tb + r2) {

                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawArc(rectF, 0f, b, true, paint);
                paint.setColor(getResources().getColor(R.color.orange));
                canvas.drawArc(rectF, b, o, true, paint);
                paint.setColor(getResources().getColor(R.color.red));
                canvas.drawArc(rectF, b + o, r, true, paint);
                paint.setColor(getResources().getColor(R.color.green));// 这里可能存在偏差
                canvas.drawArc(rectF, b + o + r, g, true, paint);
                paint.setColor(getResources().getColor(R.color.tbluee));
                canvas.drawArc(rectF, b + o + r + g, tb, true, paint);
                paint.setColor(getResources().getColor(R.color.red1));
                if (startdegree + 5f >= 360) {
                    canvas.drawArc(rectF, b + o + r + g + tb, 360f - (b + o + r + g + tb),
                            true, paint);
                    endDegree = r2;
                    //	Log.i("Secor","r2");
                } else
                    canvas.drawArc(rectF, b + o + r + g + tb, startdegree + 5f
                            - b - o - r - g - tb, true, paint);

            }
            if (currentDegree < 360) {
                //Log.i("Secor","tohander");
                candraw = true;
                handler.sendEmptyMessageDelayed(2, 20);
            } else {
                hasDraw = true;
            }

        }

        super.onDraw(canvas);
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
        if (rectF == null) {
            rectF = new RectF(0f, 0f, width, height);
        }
        candraw = true;
        invalidate();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            //绘制当前
            if (b != null) {
                candraw = true;
                handler.sendEmptyMessageDelayed(2, 20);
            }
        }
    }

    /**
     * 切屏后重新绘制
     */
    public void reDraw() {
        if (b != null) {
            candraw = true;
            Log.i("Secotor1","reDraw");
            currentDegree = 360f;
            handler.sendEmptyMessageDelayed(2, 20);

        }
    }
}
