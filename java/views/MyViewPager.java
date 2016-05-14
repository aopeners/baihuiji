package views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager
{
  private boolean slip = false;

  public MyViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean getSlip()
  {
    return this.slip;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.slip)
      return super.onInterceptTouchEvent(paramMotionEvent);
    return false;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.slip)
      return super.onTouchEvent(paramMotionEvent);
    return false;
  }

  public void setSlip(boolean paramBoolean)
  {
    this.slip = paramBoolean;
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     views.MyViewPager
 * JD-Core Version:    0.6.2
 */