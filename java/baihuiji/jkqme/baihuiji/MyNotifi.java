package baihuiji.jkqme.baihuiji;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import web.Ip;

/**
 * 负责app 跟新的类
 * Created by Administrator on 2016/5/25.
 */
public class MyNotifi {
    private final String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "Download" + File.separator + "baihuiji" + File.separator;
    private Notification notification;
    private RemoteViews views;
    private String fileName;
    private int filesize;
    private NotificationManager nManager;
    PendingIntent contentIntent;
    private Context context;
    private boolean candraw = true;//可下载控制
    private int hasDown = 0;//已下载
    int a = 0;//记录已下载的百分比

    //传入文件名
    public MyNotifi(String Filename, Context context) {
        this.fileName = Filename;
        // fileName = "BHJ_1.0.0.apk";
        this.context = context;
        CreateNotification(context);
        connecion();
        IntentFilter filter = new IntentFilter("notify");
        context.registerReceiver(receiver, filter);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    a += 5;
                    Log.i("Notify", "Hander  a:" + a);
                    if (a == 100) {
                        views.setTextViewText(R.id.notify_tx, "下载完毕");
                        notification.contentView.setTextViewText(R.id.notify_tx, "下载完毕");

                        nManager.notify(null, 1, notification);

                    } else {
                        views.setProgressBar(R.id.notify_progress, 100, a, false);
                        notification.contentView.setProgressBar(R.id.notify_progress, 100, a, false);
                        notification.contentView.setTextViewText(R.id.notify_tx, "已下载" + a + "%");
                        nManager.notify(null, 1, notification);
                    }
                    break;
                case 2://没有文件的情况
                    notification.contentView.setTextViewText(R.id.notify_tx, "没有找到资源文件");
                    nManager.notify(null, 1, notification);
            }
        }
    };

    /**
     * 显示notification
     *
     * @param context
     */
    private void CreateNotification(Context context) {
        //链接notificahion与acitivity
        contentIntent = PendingIntent.getBroadcast(context,
                R.string.app_name, new Intent("notify"),
                PendingIntent.FLAG_UPDATE_CURRENT);
        nManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        views = new RemoteViews(context.getPackageName(),
                R.layout.notify);
        views.setOnClickPendingIntent(R.id.notify_down_img, contentIntent);//RemotView 监听,broadCastReciver处理
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mBuilder.setWhen(System.currentTimeMillis());
        // mBuilder.setContent(view);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setTicker("百汇集下载中");
        mBuilder.setNumber(1);
        views.setProgressBar(R.id.notify_progress, 100, 0, false);
        views.setTextViewText(R.id.notify_baihuiji_name, "白汇集" + fileName);
        mBuilder.setContent(views);
        mBuilder.setContentIntent(contentIntent);
        notification = mBuilder.build();
        // nManager.notify(1, notification);
        nManager.notify(null, 1, notification);
    }

    //下载方法
    private void connecion() {
        new Thread(new Runnable() {
            @Override
            public void run() {


                URL url = null;
                try {
                    url = new URL(Ip.downlod + fileName);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                connection.setDoOutput(false);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(true);
                //设置 user _agent
                connection.setRequestProperty("User-Agent", "NetFox");
                //设置下载位置
                connection.setRequestProperty("RANGE", "bytes=" + hasDown);
                try {
                    connection.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                BufferedInputStream bufferedInputStream;
                BufferedOutputStream writer;
                FileOutputStream fileOutputStream = null;
                try {
                    //路径
                    File file = new File(path);
                    if (!file.exists()) {//路径存在判断
                        //  file.mkdirs();
                        file.mkdir();
                        //路径存在
                    } else {
                    }
                    //在指定路径下创建
                    file = new File(path + fileName);
                    if (file.exists()) {//如果文件已经存在就删除再创建
                        file.delete();
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            file.createNewFile();//文件不纯正就直接创建
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    fileOutputStream = new FileOutputStream(file, true);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {

                    bufferedInputStream = new BufferedInputStream(connection.getInputStream());
                    writer = new BufferedOutputStream(fileOutputStream);

                    int b = 0;
                    filesize = connection.getContentLength();//获取文件大小
                    Log.i("MYNotifi", "  " + filesize);

                    Looper.prepare();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                        while ((b = bufferedInputStream.read()) != -1 && candraw) {
                            //  b=bufferedInputStream.read();
                            hasDown++;
                            writer.write(b);
                            // Log.i("MyNotifi", "onDown" + hasDown + "  " + filesize);
                            //百分比提醒设置
                            if (hasDown * 100 / filesize > a + 5) {
                                //hasDown = 0;
                                Log.i("Notify", "sendHandelr  hasDown" + hasDown);
                                handler.sendEmptyMessage(1);
                            }
                            //最后要执行一次结尾
                            if ((b = bufferedInputStream.read()) == -1) {
                                writer.write(b);
                                handler.sendEmptyMessage(1);
                                Instanll();
                            }
                        }
                    //  handler.sendEmptyMessage(1);
                    fileOutputStream.flush();
                    writer.flush();
                    fileOutputStream.close();
                    writer.close();

                    bufferedInputStream.close();
                    Log.i("Notify", "hasDown" + hasDown);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
                connection.disconnect();

            }
        }).start();

    }

    //安装下载后的apk文件
    private void Instanll() {
        context.unregisterReceiver(receiver);
        File file = new File(path + fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 处理文件停止下载时的动作
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("notify")) {
                Log.i("hasnotify", "notifycation broadCast");
                candraw = !candraw;
                if (candraw) {
                    notification.contentView.setImageViewResource(R.id.notify_down_img, R.drawable.stop);
                    connecion();
                } else {
                    notification.contentView.setImageViewResource(R.id.notify_down_img, R.drawable.start);
                }
                nManager.notify(null, 1, notification);
            }
        }
    };
}
