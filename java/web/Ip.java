package web;

public abstract interface Ip
{
  //登出
  public static final String logOutString = "http://baihuiji.weikebaba.com/pospay/posGoOut";
 //登录
  public static final String logip = "http://baihuiji.weikebaba.com/pospay/queryShopMerchant";
 //月账单
  public static final String monthBillString = "http://baihuiji.weikebaba.com/aide/monthBill";
//月收入
  public static final String mounthConmulative = "http://baihuiji.weikebaba.com/aide/monthCount";
  //30天账单
  public static final String thirtyDetail = "http://baihuiji.weikebaba.com/aide/bill";
  //日收入
  public static final String todyConmulative = "http://baihuiji.weikebaba.com/aide/dayCount";
  //退款
  public static String refund="http://baihuiji.weikebaba.com/pospay/posPayBack";
  //金额统计ip
  public String static1="http://baihuiji.weikebaba.com/aide/masCount";
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     web.Ip    baihuiji.weiekbaba.com
 * JD-Core Version:    0.6.2
 * 支付方式：1:微支付 2:qq钱包 3:支付宝 4:百度钱包
付款状态：1：已付款，10：已退款
 */