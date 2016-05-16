package frament;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import baihuiji.jkqme.baihuiji.HomPage;
import baihuiji.jkqme.baihuiji.R;


/**
 * 计算器类
 */
public class CountFragment extends Fragment {
    private int currentState = 0;
    private ImageView delet;
    private AlertDialog dialog;
    private TextView disView;
    private TextView disView2;
    private ImageView img;
    private LinearLayout linearLayout;
    private int payTaype = -1;
    private OnClickListener listener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {
            String string, string0;
            TextView textView;
            switch (paramAnonymousView.getId()) {
                case R.id.count_back:
                    showFragment(0);
                    break;
                case R.id.count_cancel_img:
                    string = disView2.getText().toString().trim();
                    if (string.length() > 1) {//有一位以上时
                        disView2.setText(string.substring(0, string.length() - 1));
                    } else {
                        disView2.setText("");
                    }
                    break;
                case R.id.count_clear_tx:
                    disView.setText("");
                    Log.i("count", "clear");
                    disView2.setText("");
                    break;
                case R.id.count0_tx:
                    disView2.append("0");
                    break;
                case R.id.count1_tx:
                    disView2.append("1");
                    break;
                case R.id.count2_tx:
                    disView2.append("2");
                    break;
                case R.id.count3_tx:
                    disView2.append("3");
                    break;
                case R.id.count4_tx:
                    disView2.append("4");
                    break;
                case R.id.count5_tx:
                    disView2.append("5");
                    break;
                case R.id.count6_tx:
                    disView2.append("6");
                    break;
                case R.id.count7_tx:
                    disView2.append("7");
                    break;
                case R.id.count8_tx:
                    disView2.append("8");
                    break;
                case R.id.count9_tx:
                    disView2.append("9");
                    break;
                case R.id.count_point_tx:
                    string = disView2.getText().toString().trim();
                    if (!string.contains(".") && string.length() > 0)
                        disView2.append(".");
                    break;
                case R.id.countx_tx:
                    string = disView2.getText().toString().trim();
                    string0 = disView.getText().toString().trim();
                    if (string.length() > 0 && string0.length() == 0) {
                        disView.setText(string);
                        disView2.setText("");
                        disView.append("*");
                    } else if (string.length() > 0 && string0.length() > 0) {
                        disView.setText(count(string0, string));
                        disView2.setText("");
                        disView.append("*");
                    } else if (string.length() == 0 && string0.length() > 0) {
                        if (!string0.endsWith("*")) {
                            string0 = string0.substring(0, string0.length() - 2);
                            disView.setText(string0);
                            disView.append("*");
                        }
                    }
                    setState(0);
                    break;
                case R.id.count_add_tx:
                    string = disView2.getText().toString().trim();
                    string0 = disView.getText().toString().trim();
                    if (string.length() > 0 && string0.length() == 0) {
                        disView.setText(string);
                        disView2.setText("");

                        disView.append("+");
                    } else if (string.length() > 0 && string0.length() > 0) {
                        disView.setText(count(string0, string));
                        disView2.setText("");
                        disView.append("+");
                    } else if (string.length() == 0 && string0.length() > 0) {
                        if (!string0.endsWith("*")) {
                            string0 = string0.substring(0, string0.length() - 2);
                            disView.setText(string0);
                            disView.append("+");
                        }
                    }
                    setState(0);
                    break;
                case R.id.count_2row_tx:
                    onClicRight(rowView.getText().toString());

                    break;
                case R.id.cont_2row_1:
                    textView = (TextView) linearLayout.findViewById(R.id.cont_2row_1);

                    onClicRight(textView.getText().toString());
                    break;
                case R.id.cont_2row_2:
                    textView = (TextView) linearLayout.findViewById(R.id.cont_2row_2);
                    onClicRight(textView.getText().toString());
                    break;


            }

        }
    };
    private TextView rowView;
    private TextView[] t = new TextView[18];
    //最后三个定义的为右下键
    private int[] tId = {R.id.count0_tx, R.id.count1_tx, R.id.count2_tx, R.id.count3_tx, R.id.count4_tx, R.id.count5_tx, R.id.count6_tx, R.id.count7_tx, R.id.count8_tx, R.id.count9_tx, R.id.count_add_tx, R.id.count_point_tx,
            R.id.count_clear_tx, R.id.countx_tx, R.id.cont_2row_1, R.id.cont_2row_2, R.id.count_2row_tx};

    /**
     * 计算的方法，需要传人的值不为全空格
     *
     * @param paramString1
     * @param paramString2
     * @return
     */
    private String count(String paramString1, String paramString2) {
        float f1;
        float f2;
        if (paramString1.endsWith("+")) {
            try {
                f1 = Float.parseFloat(paramString1.substring(0, paramString1.length() - 2));
            } catch (NumberFormatException e) {
               return "";
            }

            if ((paramString2.length() > 0)) {
                f2 = Float.parseFloat(paramString2);
                Log.i("countf2", "f2=0");

                return f1 + f2 + "";
            }
        }
        if (paramString1.endsWith("*")) {
            try {
                f1 = Float.parseFloat(paramString1.substring(0, paramString1.length() - 2));
            } catch (NumberFormatException e) {
                return "";
            }
            if ((paramString2.length() > 0)) {
                f2 = Float.valueOf(paramString2);
                Log.i("countf2", "f2=0");
                return paramString2 = f1 * f2 + "";
            }
        }
        return paramString1;
    }

    private void getDialog(LayoutInflater paramLayoutInflater) {
        Builder localBuilder = new Builder(getParentFragment().getActivity());
        localBuilder.setMessage("还没有输入数据");
        this.dialog = localBuilder.create();
    }

    private void hideButtom() {
        ((HomPage) getParentFragment().getActivity()).hideButtom();
    }

    private void jumptoDecoder(int type, float money) {
        ((HomPage) getParentFragment().getActivity()).jumptoDecode(type, money);
    }

    private void loadComponent(View paramView) {
        this.img = ((ImageView) paramView.findViewById(R.id.count_back));
        this.img.setOnClickListener(this.listener);
        this.delet = ((ImageView) paramView.findViewById(R.id.count_cancel_img));
        this.delet.setOnClickListener(this.listener);
        this.disView = ((TextView) paramView.findViewById(R.id.comt_tx));
        this.disView2 = ((TextView) paramView.findViewById(R.id.comt_1_tx));
        this.linearLayout = ((LinearLayout) paramView.findViewById(R.id.count_2row_linear));
        TextView localTextView = null;
        for (int i = 0; i < tId.length; i++) {
            //给键盘加监听
            localTextView = (TextView) paramView.findViewById(this.tId[i]);
            localTextView.setOnClickListener(this.listener);
        }
        this.rowView = localTextView;
        setState(1);
    }

    /**
     * 当点击计算机键盘右下三个键中任意一个时调用
     *
     * @param paramString 传人的键值
     */
    private void onClicRight(String paramString) {
        String moneyCount = disView2.getText().toString().trim();

        if (paramString.equals("=")) {
            //当v1有数据时
            String string = disView2.getText().toString().trim();
            String string0 = disView.getText().toString().trim();
            //2 不空0为空
            if (string.length() > 0 && string0.length() == 0) {
                disView.setText("");

            } else if (string.length() > 0 && string0.length() > 0) {
                disView2.setText(count(string0, string));
                disView.setText("");
                //当disview2为空时
            } else if (string.length() == 0 && string0.length() > 0) {
                disView2.setText(string0.substring(0, string0.length() - 2));
                disView.setText("");
            }

            setState(1);
        } else if (paramString.equals("扫一扫") && moneyCount.length() > 0) {
            jumptoDecoder(payTaype, Float.parseFloat(moneyCount));
        }
    }

    private void showButtom() {
        ((HomPage) getParentFragment().getActivity()).showButtom();
    }

    /**
     * 显示fragment
     *
     * @param paramInt 0 调用Homepage方法
     */
    @SuppressLint({"NewApi"})
    private void showFragment(int paramInt) {
        ((Home_fragment) getParentFragment()).showFragment(paramInt);
        HomPage homPage = (HomPage) getParentFragment().getActivity();
        homPage.showButtom();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.count, null, true);
        loadComponent(localView);
        getDialog(paramLayoutInflater);
        return localView;
    }

    public void onHiddenChanged(boolean paramBoolean) {
        super.onHiddenChanged(paramBoolean);
        if (!paramBoolean)
            hideButtom();
    }

    /**
     * 相机状态定义
     *
     * @param paramInt 0，全=  1 上扫下付   2，上等下扫   3.全扫
     */
    public void setState(int paramInt) {
        this.currentState = paramInt;
        switch (paramInt) {
            default:
                return;
            case 0:
                this.linearLayout.setVisibility(View.GONE);
                this.rowView.setVisibility(View.VISIBLE);
                this.rowView.setText("=");
                return;
            case 1:
                this.linearLayout.setVisibility(View.VISIBLE);
                this.rowView.setVisibility(View.GONE);
                ((TextView) this.linearLayout.findViewById(R.id.cont_2row_1)).setText("扫一扫");
                ((TextView) this.linearLayout.findViewById(R.id.cont_2row_2)).setText("付款码");
                return;
            case 2:
                this.linearLayout.setVisibility(View.VISIBLE);
                this.rowView.setVisibility(View.GONE);
                ((TextView) this.linearLayout.findViewById(R.id.cont_2row_1)).setText("=");
                ((TextView) this.linearLayout.findViewById(R.id.cont_2row_2)).setText("扫一扫");
                return;
            case 3:
        }
        this.linearLayout.setVisibility(View.GONE);
        this.rowView.setVisibility(View.VISIBLE);
        this.rowView.setText("扫一扫");
    }

    public void setPayType(int type) {
        payTaype = type;
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     frament.CountFragment
 * JD-Core Version:    0.6.2
 */