package baihuiji.jkqme.baihuiji;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import java.io.File;

public class DbHelper extends SQLiteOpenHelper
{
  private static final String dbName = "user";

  private String drict = Environment.getExternalStorageDirectory().getPath() + File.separator + "android" + File.separator + "data" + File.separator + "baihuiji";
  public String dbPath = this.drict + File.separator + "user";
  private File drictsFile = new File(this.drict);

  public DbHelper(Context paramContext)
  {
    super(paramContext, "user", null, 1);
    Log.i("Dbhelper", this.dbPath);
    if (!this.drictsFile.exists())
      this.drictsFile.mkdirs();
    Log.i("Dbhelper", "fdfafds" + this.drictsFile.exists());
  }

  public DbHelper(Context paramContext, String paramString, CursorFactory paramCursorFactory, int paramInt)
  {
    super(paramContext, paramString, paramCursorFactory, paramInt);
  }

  public void createTable(SQLiteDatabase paramSQLiteDatabase)
  {
    if (!paramSQLiteDatabase.rawQuery("select name from sqlite_master where type=\"table\";", null).moveToNext())
      paramSQLiteDatabase.execSQL("create tale user (name vchar(50) primary key,password vchar(50),time integer);");
    paramSQLiteDatabase.close();
  }

  public boolean isTableCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.rawQuery("select name from sqlite_master where type=\"table\";", null).moveToNext();
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
  }
}

/* Location:           C:\Users\jkqme\Androids\Androids\classes_dex2jar.jar
 * Qualified Name:     baihuiji.jkqme.baihuiji.DbHelper
 * JD-Core Version:    0.6.2
 */