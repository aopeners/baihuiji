package frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import baihuiji.jkqme.baihuiji.HomPage;
import java.lang.reflect.Field;

public class Home_fragment extends Fragment
{
  private CountFragment countFragment;
  private Fragment curentFragment;
  private Home_display_fragment hDisplay_fragment;
  private LinearLayout layout;
  private FragmentManager manager;
  private FragmentTransaction transaction;

  private void loadFragment()
  {
    if (this.manager == null)
    {
      this.manager = getChildFragmentManager();
      this.transaction = this.manager.beginTransaction();
      this.transaction.add(2131230788, this.hDisplay_fragment);
      this.transaction.add(2131230788, this.countFragment).hide(this.countFragment);
      this.transaction.show(this.hDisplay_fragment);
      this.transaction.commit();
      this.curentFragment = this.hDisplay_fragment;
    }
  }

  private void setSlip(boolean paramBoolean)
  {
    ((HomPage)getActivity()).setSlipe(paramBoolean);
  }

  private void showFragment(Fragment paramFragment)
  {
    if (this.curentFragment.hashCode() != paramFragment.hashCode())
    {
      this.transaction = this.manager.beginTransaction();
      this.transaction.hide(this.curentFragment);
      this.transaction.show(paramFragment);
      this.transaction.commit();
      this.curentFragment = paramFragment;
    }
    if (paramFragment.hashCode() == this.countFragment.hashCode())
    {
      setSlip(false);
      return;
    }
    setSlip(true);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.hDisplay_fragment = new Home_display_fragment();
    this.countFragment = new CountFragment();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903053, null, true);
    loadFragment();
    return localView;
  }

  public void onDetach()
  {
    super.onDetach();
    try
    {
      Field localField = Fragment.class.getDeclaredField("mChildFragmentManager");
      localField.setAccessible(true);
      localField.set(this, null);
      return;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      throw new RuntimeException(localNoSuchFieldException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new RuntimeException(localIllegalAccessException);
    }
  }

  public void showFragment(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return;
    case 0:
      showFragment(this.hDisplay_fragment);
      return;
    case 1:
    }
    showFragment(this.countFragment);
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Home_fragment
 * JD-Core Version:    0.6.2
 */