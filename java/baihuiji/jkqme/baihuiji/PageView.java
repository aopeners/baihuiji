package baihuiji.jkqme.baihuiji;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;

import frament.Login;
import frament.NormalPage;
import frament.Viewpage4;
import java.util.ArrayList;

/**
 * 开始页面，采用viewPage与logFragment分开显示
 */
public class PageView extends FragmentActivity
{
  private int currentdisplay = 0;
  private ArrayList<Fragment> flist = new ArrayList();
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      Log.i("handler", PageView.this.currentdisplay+"");
      if (PageView.this.currentdisplay < 4)
      {
        PageView localPageView = PageView.this;
        localPageView.currentdisplay = (1 + localPageView.currentdisplay);
        PageView.this.vpager.setCurrentItem(-1 + PageView.this.currentdisplay);
        sendEmptyMessageDelayed(1, 2000L);
      }
    }
  };
  private Login login;
  private FragmentManager manager = getSupportFragmentManager();
  private OnPageChangeListener pListener = new OnPageChangeListener()
  {
    public void onPageScrollStateChanged(int paramAnonymousInt)
    {
    }

    public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2)
    {
    }

    public void onPageSelected(int paramAnonymousInt)
    {
    }
  };
  private pagerAdpter padpter;
  private FragmentTransaction transaction = this.manager.beginTransaction();
  private ViewPager vpager;

  private void getFragment()
  {
    for (int i = 0;i<4 ; i++)
    {
      //设置最后一张显示的内容，和配置viewpager 的adpter
      if (i >= 3)
      {
        Viewpage4 localViewpage4 = new Viewpage4();
        this.flist.add(localViewpage4);
        this.padpter = new pagerAdpter(getSupportFragmentManager(), this.flist);
        this.vpager.setAdapter(this.padpter);
        this.vpager.setOnPageChangeListener(this.pListener);
        return;
      }
      Bundle localBundle = new Bundle();
      localBundle.putInt("showId", i);
      NormalPage localNormalPage = new NormalPage();
      //设置normalpage显示的图片
      localNormalPage.setArguments(localBundle);
      this.flist.add(localNormalPage);
    }
  }

  private void jugShowViewPager()
  {
    MyApplaication localMyApplaication = (MyApplaication)getApplication();
    SQLiteDatabase localSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(localMyApplaication.helper.dbPath, null);
    if (localMyApplaication.helper.isTableCreate(localSQLiteDatabase))
    {
      jumptoLogin();
      return;
    }
    getFragment();
  }

  public void jumptoHome()
  {
    startActivity(new Intent(this, HomPage.class));
    finish();
  }

  public void jumptoLogin()
  {
    if (this.vpager != null)
      this.vpager.setVisibility(View.GONE);
    this.transaction.show(this.login);
    this.transaction.commit();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(R.layout.viewpage);
    this.vpager = ((ViewPager)findViewById(R.id.viewpage_vp));
    this.login = new Login();
    this.transaction.add(R.id.home_page_fragment_linear, this.login);
    jugShowViewPager();
  }

  public void writDb()
  {
    MyApplaication localMyApplaication = (MyApplaication)getApplication();
    SQLiteDatabase localSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(localMyApplaication.helper.dbPath, null);
    localMyApplaication.helper.createTable(localSQLiteDatabase);
    Log.i("viewPage", "WriteDb");
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.PageView
 * JD-Core Version:    0.6.2
 */