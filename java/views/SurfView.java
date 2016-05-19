package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import baihuiji.jkqme.baihuiji.R;

/**
 * Created by songxun.sx on 2015/8/14. 只能用继承view？ 当使用Textview
 * 时，设置字体无效，使用viewGroup时没作用
 */
public class SurfView extends View {
	private int orange=getResources().getColor(R.color.orange);
	private int orange2=getResources().getColor(R.color.orange1);
	NextFrameAction nextFrameAction;
	RectF rectF;
	Paint paint;
	Paint paint2;
	Paint paint3;
	Path path;
	Path path1;
	int width;
	int height;
	int w = 0;
	double startTime;
	int waveAmplitude;
	int waveRange;
	int highLevel;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (waveAmplitude != 0) {
				waveAmplitude--;
				this.sendEmptyMessageDelayed(1, 200);
			//	Log.i("Hander", "change wave Amplitude" + waveAmplitude);
			}
		};
	};

	public SurfView(Context context) {
		super(context);
	}

	public SurfView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SurfView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private void init() {
		nextFrameAction = new NextFrameAction();
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		waveRange = width;
		rectF = new RectF(5, 5, width - 5, height - 5);
		paint = new Paint();
		paint2 = new Paint();
		paint3 = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(orange2);
		paint3.setAntiAlias(true);
		paint3.setColor(orange);

		paint2.setAntiAlias(true);
		paint2.setStyle(Paint.Style.STROKE);
		paint2.setStrokeWidth(5);
		paint2.setColor(orange);
		path = new Path();
		path1 = new Path();
		startTime = System.currentTimeMillis();
		waveAmplitude = 15;
		highLevel = (int) (height * (0.5) + waveAmplitude);

		// 改变波形高度的handler
		handler.sendEmptyMessageDelayed(1, 1000);
	}

	protected class NextFrameAction implements Runnable {

		@Override
		public void run() {
			path.reset();
			path1.reset();
			path.addArc(rectF, 90.0f - 145.0f / 2.0f, 145.0f);
			path1.addArc(rectF, 90.0f - 145.0f / 2.0f, 145.0f);
			w += 5;
			if (w >= (width - 5) * 2) {
				w = 0;
			}
			for (int i = 5; i < width - 5; i++) {
				path.lineTo(
						i,
						(float) (highLevel + waveAmplitude
								* Math.cos((float) (i + w)
										/ (float) (width - 5) * Math.PI)));
				path1.lineTo(
						i,
						(float) (highLevel - waveAmplitude
								* Math.cos((float) (i + w)
										/ (float) (width - 5) * Math.PI)));
			}
			path.close();
			path1.close();
			invalidate();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(width / 2, height / 2, width / 2 - 5, paint2);
		// canvas.drawArc(rectF,90.0f-145.0f/2.0f,145.0f,false,paint);
		canvas.drawPath(path, paint);
		canvas.drawPath(path1, paint3);
		postDelayed(nextFrameAction, 4);

	}

	/**
	 * 外部控制使波形可控
	 * 
	 * @param a
	 *            波形高度
	 */
	public void setWaveAmplitude(int a) {
		this.waveAmplitude = a;
		handler.sendEmptyMessage(1);
	}
}
