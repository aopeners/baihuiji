package frament;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import views.Sector;

public class StatisticsFragment extends Fragment
{
  private LinearLayout layout;
  private Sector sector;
  private TextView textView;
  private View view;

  @SuppressLint({"NewApi"})
  private void loadComponent(View paramView)
  {
    this.sector = ((Sector)paramView.findViewById(2131230857));
    this.layout = ((LinearLayout)paramView.findViewById(2131230856));
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    this.view = paramLayoutInflater.inflate(2130903070, null);
    loadComponent(this.view);
    return this.view;
  }

  public void onHiddenChanged(boolean paramBoolean)
  {
    super.onHiddenChanged(paramBoolean);
    if (!paramBoolean)
    {
      Log.i("OnHidden", paramBoolean);
      this.sector.setWight(0.2F, 0.3F, 0.1F, 0.4F);
    }
  }

  public void onResume()
  {
    super.onResume();
    Log.i("OnResume", "onResume");
    loadComponent(this.view);
  }

  public void onStart()
  {
    super.onStart();
    Log.i("OnStart", "onStart");
    loadComponent(this.view);
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.StatisticsFragment
 * JD-Core Version:    0.6.2
 */