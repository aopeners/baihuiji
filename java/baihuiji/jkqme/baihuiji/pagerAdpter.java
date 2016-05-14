package baihuiji.jkqme.baihuiji;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

class pagerAdpter extends FragmentStatePagerAdapter
{
  private ArrayList<Fragment> fList;
  private FragmentTransaction transaction;

  public pagerAdpter(FragmentManager paramFragmentManager, ArrayList<Fragment> paramArrayList)
  {
    super(paramFragmentManager);
    this.transaction = paramFragmentManager.beginTransaction();
    this.fList = paramArrayList;
  }

  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    this.transaction.remove((Fragment)this.fList.get(paramInt));
  }

  public int getCount()
  {
    return this.fList.size();
  }

  public Fragment getItem(int paramInt)
  {
    return (Fragment)this.fList.get(paramInt);
  }

  public Object instantiateItem(View paramView, int paramInt)
  {
    return getItem(paramInt);
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.pagerAdpter
 * JD-Core Version:    0.6.2
 */