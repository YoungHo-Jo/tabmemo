package team2.apptive.tabmemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by solar on 2016-11-19.
 */

public class DBHelper extends SQLiteOpenHelper {

  public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  // db 생성
  @Override
  public void onCreate(SQLiteDatabase db) {
    // new table
    db.execSQL("CREATE TABLE MEMO(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
      "title TEXT, memo TEXT, childMemo TEXT, category TEXT, stared INTEGER, time INTEGER, position INTEGER);");

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }

  public void insert(String _query) {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL(_query);
    db.close();
  }

  public void update(String _query) {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL(_query);
    db.close();
  }

  public void delete(String _query) {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL(_query);
    db.close();
  }

  public void newInsert(String title, String category) {
    SQLiteDatabase db = getWritableDatabase();
    long time = System.currentTimeMillis();
    db.execSQL("insert into MEMO values(null, '" + title + "', null, '" + category + "', 0, " + time + ", " + time + ");");
    db.close();
  }

  public void updateTitle(String title, long time) {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("update MEMO set title = '" + title + "' where time = " + time + ";");
    db.close();
  }

  public void updateMemo(String memo, long time) {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("update MEMO set memo = '" + memo + "' where time = " + time + ";");
    db.close();
  }

  public void updateChileMemo(String memo, long time) {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("update MEMO set childMemo = '" + memo + "' where time = " + time + ";");
    db.close();
  }

  public void updateStared(boolean isStared, long time) {
    SQLiteDatabase db = getWritableDatabase();
    int stared = (isStared) ? 1 : 0;
    db.execSQL("update MEMO set stared = " + stared + " where time = " + time + ";");
    db.close();
  }

  public void updateCategory(String category, long time) {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("update MEMO set category = '" + category + "' where time = " + time + ";");
    db.close();
  }

  public void updateCategory(int position, long time) {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("update MEMO set position = '" + position + "' where time = " + time + ";");
    db.close();
  }

  public void deleteByTime(long time) {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("delete form MEMO where time = " + time + ";");
    db.close();
  }

}
