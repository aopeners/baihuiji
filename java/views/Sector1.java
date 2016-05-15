package views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class Sector1 extends View
{
  private float b;
  private boolean candraw;
  private float g;
  Handler handler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      Sector1.this.ini();
    }
  };
  private int height;
  private int minHeight;
  private int minWidth;
  private float o;
  private float other;
  private float r;
  private float tb;
  private int width;

  public Sector1(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  @SuppressLint({"NewApi"})
  private void ini()
  {
    this.width = getMeasuredWidth();
    setLayoutParams(new LinearLayout.LayoutParams(this.width, this.width, 1.0F));
    this.height = this.width;
    this.height = getMeasuredHeight();
    this.candraw = true;
    invalidate();
  }

 /* protected void onDraw(Canvas paramCanvas)
  {
    if (this.candraw)
    {
      Paint localPaint = new Paint();
      localPaint.setStyle(Style.FILL);
      localPaint.setAntiAlias(true);
      RectF localRectF = new RectF(0.0F, 0.0F, this.width, this.height);
      Log.i("onDrow", this.width + "  " + this.height);
      localPaint.setColor(getResources().getColor(2130968580));
      paramCanvas.drawArc(localRectF, 0.0F, this.o, true, localPaint);
      localPaint.setColor(getResources().getColor(2130968585));
      paramCanvas.drawArc(localRectF, this.o, this.tb, true, localPaint);
      localPaint.setColor(getResources().getColor(2130968581));
      paramCanvas.drawArc(localRectF, this.o + this.tb, this.b, true, localPaint);
      localPaint.setColor(getResources().getColor(2130968583));
      paramCanvas.drawArc(localRectF, this.b + this.o + this.tb, this.g, true, localPaint);
      localPaint.setColor(getResources().getColor(2130968582));
      paramCanvas.drawArc(localRectF, this.b + this.o + this.tb + this.g, this.r, true, localPaint);
      localPaint.setColor(getResources().getColor(2130968586));
      paramCanvas.drawArc(localRectF, this.b + this.o + this.r + this.tb + this.g, this.other, true, localPaint);
      super.onDraw(paramCanvas);
    }
  }*/

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
  }

  public void setWight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    this.b = (360.0F * paramFloat3);
    this.o = (360.0F * paramFloat1);
    this.r = (360.0F * paramFloat5);
    this.g = (360.0F * paramFloat4);
    this.tb = (360.0F * paramFloat2);
    this.other = (360.0F * paramFloat6);
    this.handler.sendEmptyMessageDelayed(1, 2200L);
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     views.Sector1
 * JD-Core Version:    0.6.2
 */