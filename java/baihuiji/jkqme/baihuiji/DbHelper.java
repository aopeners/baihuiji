package baihuiji.jkqme.baihuiji;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DbHelper extends SQLiteOpenHelper {
    private static final String dbName = "user";

    private String drict = Environment.getExternalStorageDirectory().getPath() + File.separator + "android" + File.separator + "data" + File.separator + "baihuiji";
    public String dbPath = this.drict + File.separator + "user";
    private File drictsFile = new File(this.drict);

    public DbHelper(Context paramContext) {
        super(paramContext, "user", null, 1);
        Log.i("Dbhelper", this.dbPath);
        if (!this.drictsFile.exists())
            this.drictsFile.mkdirs();
        Log.i("Dbhelper", "fdfafds" + this.drictsFile.exists());
    }

    public DbHelper(Context paramContext, String paramString, CursorFactory paramCursorFactory, int paramInt) {
        super(paramContext, paramString, paramCursorFactory, paramInt);
    }

    public void createTable(SQLiteDatabase paramSQLiteDatabase) {
        if (!paramSQLiteDatabase.rawQuery("select name from sqlite_master where type=\"table\";", null).moveToNext())
            paramSQLiteDatabase.execSQL("create tale user (name vchar(50) primary key,password vchar(50),time integer);");
        paramSQLiteDatabase.close();
    }

    public boolean isTableCreate(SQLiteDatabase paramSQLiteDatabase) {
        return paramSQLiteDatabase.rawQuery("select name from sqlite_master where type=\"table\";", null).moveToNext();
    }

    /**
     * 查询数据库中存在user表，且根据情况保存用户质料
     *
     * @param database
     * @param user
     * @param password
     * @return 返回数据库中存在user;
     */
    public boolean isUserExct(SQLiteDatabase database, String user, String password, boolean savePassword) {
        String string[] = {"name"};
        Cursor cursor = database.query("user", string, null, null, null, null, null);
        int i = 0;
        try {
            while (cursor.moveToNext()) {
                //判断是否存在  user 字段
                if (cursor.getString(0).equals(user)) {
                    //如果要保存密码，且当前密码，与保存密码不一直
                    if (savePassword && !cursor.getString(1).equals(password)) {
                        String sql = "insert into user(name,password) values(" + user + "," + password + ");";
                        database.execSQL(sql);
                    }
                    return true;
                } else {
                    String sql = "insert into user(name) values(\"+user+\");";
                    database.execSQL(sql);
                }
            }
        } catch (NullPointerException e) {
            String sql = "insert into user(name,password) values(" + user + "," + password + ");";
            database.execSQL(sql);
            return true;
        }
        //到这里 就不存在用户名了
        if (savePassword) {
            try {
                String sql = "insert into user(name,password) values(" + user + "," + password + ");";
                database.execSQL(sql);
            } catch (NullPointerException e) {
                String sql = "insert into user(name,password) values(" + user + "," + password + ");";
                database.execSQL(sql);
            }

        } else {
            String sql = "insert into user(name) values(" + user + ");";
            database.execSQL(sql);
        }
        return false;
    }

    public String[] getUserMsg(SQLiteDatabase database) {
        String string[] = new String[2];
        String slq="select * from user;";
        Cursor cursor=database.rawQuery(slq,null);
        cursor.moveToLast();
        string[0]=cursor.getString(0);
        string[1]=cursor.getString(1);
        return string;
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
    }

    public void write(SQLiteOpenHelper databse, String user, String password) {
        String sql = "";
    }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.DbHelper
 * JD-Core Version:    0.6.2
 */