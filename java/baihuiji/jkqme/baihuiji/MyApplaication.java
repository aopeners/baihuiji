package baihuiji.jkqme.baihuiji;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import java.util.HashMap;

public class MyApplaication extends Application {
    public int height;
    public int width;
    public DbHelper helper;
    private HashMap<String, String> map = new HashMap();

    public String getDate(String paramString) {
        return (String) this.map.get(paramString);
    }

    public void onCreate() {
        super.onCreate();
        this.helper = new DbHelper(this);
        WindowManager wn=(WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        height=wn.getDefaultDisplay().getHeight();
        width=wn.getDefaultDisplay().getWidth();
        Log.i("MyApplaictin",height+"  "+width);
    }

    public void putData(String paramString1, String paramString2) {
        this.map.put(paramString1, paramString2);
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.MyApplaication
 * JD-Core Version:    0.6.2
 */