package frament;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.MyApplaication;

import java.util.ArrayList;

import baihuiji.jkqme.baihuiji.R;
import web.BaihuijiNet;

public class Home_display_fragment extends Fragment {
    /**
     * view pageadpter
     */
    private Adpter adpter;
    private Handler handler = new Handler() {
        Bundle bundle;

        /**
         * x用于显示月金额，和日金额的hanlder
         * @param paramAnonymousMessage
         */
        public void handleMessage(Message paramAnonymousMessage) {
            this.bundle = paramAnonymousMessage.getData();
            if (this.bundle.getIntArray("requst") != null) {
                int[] arrayOfInt = this.bundle.getIntArray("requst");
                ((TextView) Home_display_fragment.this.tody.findViewById(R.id.home_page_totle_tx)).setText(arrayOfInt[0] + "元");
                ((TextView) Home_display_fragment.this.month.findViewById(R.id.home_page_totle1_tx)).setText(arrayOfInt[1] + "元");
            }
        }
    };
    //viewpage view list
    private ArrayList<View> list = new ArrayList();
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            switch (paramAnonymousView.getId()) {
                case 2131230777:
                default:
                    return;
                case R.id.home_page_pay_tx:
                    Home_display_fragment.this.showFragment(1,0);
                    return;
                case R.id.home_page_radio_bt1:
                    Home_display_fragment.this.showFragment(1,1);
                    return;
                case R.id.home_page_radio_bt2:
                    Home_display_fragment.this.showFragment(1,2);
                    return;
                case R.id.home_page_radio_bt3:
                    Home_display_fragment.this.showFragment(1,3);
                    return;
                case R.id.home_page_radio_bt4:
                    Home_display_fragment.this.showFragment(1,4);
            }

        }
    };
    private View month;
    private OnPageChangeListener pListener = new OnPageChangeListener() {
        public void onPageScrollStateChanged(int paramAnonymousInt) {
        }

        public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {
        }

        public void onPageSelected(int paramAnonymousInt) {
            if (paramAnonymousInt == 1) {
                ((TextView) Home_display_fragment.this.view.findViewById(R.id.home_page_today_tx)).setBackgroundDrawable(Home_display_fragment.this.view.getResources().getDrawable(R.drawable.circle1));
                ((TextView) Home_display_fragment.this.view.findViewById(R.id.home_page_moth_tx)).setBackgroundDrawable(Home_display_fragment.this.view.getResources().getDrawable(R.drawable.circle));
                return;
            }
            ((TextView) Home_display_fragment.this.view.findViewById(R.id.home_page_today_tx)).setBackgroundDrawable(Home_display_fragment.this.view.getResources().getDrawable(R.drawable.circle));
            ((TextView) Home_display_fragment.this.view.findViewById(R.id.home_page_moth_tx)).setBackgroundDrawable(Home_display_fragment.this.view.getResources().getDrawable(R.drawable.circle1));
        }
    };
    private ViewPager pager;
    private TextView proceed;
    private View tody;
    private View view;

    /**
     * 返回用于显示日收入和月收入的数组
     * @param paramString1 今日 json
     * @param paramString2 月   json
     * @return
     */
    private int[] getDate(String paramString1, String paramString2) {
        JSONObject object;

            int a[]=new int[2];
        try {
            object=new JSONObject(paramString1);
            a[0]=object.getInt("o2o");
            object=new JSONObject(paramString2);
            a[1]=object.getInt("o2o");
        } catch (JSONException e) {
            e.printStackTrace();
            a[0]=0;
            a[1]=0;
        }
        return a;
    }

    /**
     * 获取今日收入，和月收入
     */
    private void getDayDate() {
        new Thread(new Runnable() {
            MyApplaication applaication = (MyApplaication) Home_display_fragment.this.getParentFragment().getActivity().getApplication();
            Bundle bundle;
            String json;
            String[] key = {"uId", "merchantId", "day", "rule"};
            Message message;
            String mjson;
            String[] mkey;
            String[] mvalue;
            String requstDay;
            String requstMonth;
            String[] value;

            public void run() {
                Log.i("Home_setDate", this.json);
                this.requstDay = BaihuijiNet.urlconection("http://baihuiji.weikebaba.com/aide/dayCount", this.json);
                this.requstMonth = BaihuijiNet.urlconection("http://baihuiji.weikebaba.com/aide/monthCount", this.json);
                Log.i("home_setDate", this.requstDay + this.requstMonth + 333);
                this.bundle.putIntArray("requst", Home_display_fragment.this.getDate(this.requstDay, this.requstMonth));
                this.message.setData(this.bundle);
                Home_display_fragment.this.handler.sendMessage(this.message);
            }
        }).start();
    }

    private void hideButtom() {
      HomPage homPage=((HomPage) getParentFragment().getActivity());
        homPage.hideButtom();
    }

    private void loadButton(View paramView) {
        int[] arrayOfInt = {R.id.home_page_radio_bt1, R.id.home_page_radio_bt2, R.id.home_page_radio_bt3, R.id.home_page_radio_bt4};
        for (int i = 0; ; i++) {
            if (i >= arrayOfInt.length)
                return;
            ((Button) paramView.findViewById(arrayOfInt[i])).setOnClickListener(this.listener);
        }
    }

    private void setMonthDate() {
    }

    private void showFragment(int paramInt,int type) {
        ((Home_fragment) getParentFragment()).showFragment(paramInt);
        //设置支付类型
        ((Home_fragment) getParentFragment()).setType(type);
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        this.view = paramLayoutInflater.inflate(R.layout.home_page, null, true);
        this.tody = paramLayoutInflater.inflate(R.layout.tody, null, true);
        this.month = paramLayoutInflater.inflate(R.layout.month, null, true);
        this.list.add(this.tody);
        this.list.add(this.month);
        this.pager = ((ViewPager) this.view.findViewById(R.id.home_page_vp));
        this.adpter = new Adpter();
        this.pager.setAdapter(this.adpter);
        //收款按钮
        this.proceed = ((TextView) this.view.findViewById(R.id.home_page_pay_tx));
        this.proceed.setOnClickListener(this.listener);
        this.pager.setOnPageChangeListener(this.pListener);
        loadButton(this.view);
        getDayDate();
        return this.view;
    }

    class Adpter extends PagerAdapter {
        Adpter() {
        }

        public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
            paramViewGroup.removeView((View) Home_display_fragment.this.list.get(paramInt));
        }

        public int getCount() {
            return Home_display_fragment.this.list.size();
        }

        public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
            paramViewGroup.addView((View) Home_display_fragment.this.list.get(paramInt));
            return Home_display_fragment.this.list.get(paramInt);
        }

        public boolean isViewFromObject(View paramView, Object paramObject) {
            return paramView == paramObject;
        }
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.Home_display_fragment
 * JD-Core Version:    0.6.2
 */