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
						"title TEXT, memo TEXT, childMemo TEXT, category TEXT, stared INTEGER, time TEXT, position TEXT);");

		Long time = System.currentTimeMillis();
		String initTitle = "저는 제목이에요 길게 눌러보세요";
		String initCategory = "탭메모";
		String initMemo = "여기를 누르면 메모를 수정할 수 있어요.\n\n수정이 끝난 후 제목을 눌러보세요.\n\n다른 메모가 있다면 다른 메모를 눌러도 좋아요.";
		db.execSQL("insert into MEMO values(null, '" + initTitle + "', '" + initMemo + "', null, '" + initCategory + "', 0, " + time.toString() + ", " + time.toString() + ");");
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

	public String newInsert(String title, String category) {
		SQLiteDatabase db = getWritableDatabase();
		Long time = System.currentTimeMillis();
		db.execSQL("insert into MEMO values(null, '" + title + "', \"\", null, '" + category + "', 0, " + time.toString() + ", " + time.toString() + ");");

		db.close();
		return time.toString();
	}

	public void updateTitle(String title, String time) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("update MEMO set title = '" + title + "' where time = " + time + ";");
		db.close();
	}

	public String updateMemo(String memo, String time) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("update MEMO set memo = '" + memo + "' where time = " + time + ";");
		db.close();
		return memo;
	}

	public void updatMemoToNull(String time)
	{
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("update MEMO set memo = null where time = " + time + ";");
		db.close();
	}

	public void updateChildMemo(String memo, String time) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("update MEMO set childMemo = '" + memo + "' where time = " + time + ";");
		db.close();
	}

	public void updateStared(boolean isStared, String time) {
		SQLiteDatabase db = getWritableDatabase();
		int stared = (isStared) ? 1 : 0;
		db.execSQL("update MEMO set stared = " + stared + " where time = " + time + ";");
		db.close();
	}

	public void updateCategory(String category, String time) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("update MEMO set category = '" + category + "' where time = " + time + ";");
		db.close();
	}

	public void updateCategoryName(String category, String newCategoryName)
	{
		SQLiteDatabase db = getReadableDatabase();
		String str = "";

		Cursor cursor = db.rawQuery("select * from MEMO", null);
		while (cursor.moveToNext()) {
			if(cursor.getString(4).equals(category))
				updateCategory(newCategoryName, cursor.getString(6));
		}

	}

	public void updatePosition(String position, String time) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("update MEMO set position = " + position + " where time = " + time + ";");
		db.close();
	}

	public void deleteByTime(String time) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from MEMO where time = " + time + ";");
		db.close();
	}

	public void deleteCategory(String _category)
	{
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from MEMO where category = '" + _category + "';");
		db.close();
	}

	public String printData() {
		SQLiteDatabase db = getReadableDatabase();
		String str = "";

		Cursor cursor = db.rawQuery("select * from MEMO", null);
		while (cursor.moveToNext()) {
			str += cursor.getInt(0)
							+ " : Title "
							+ cursor.getString(1)
							+ ", Memo = "
							+ cursor.getString(2)
							+ ", ChileMemo = "
							+ cursor.getString(3)
							+ ", Category = "
							+ cursor.getString(4)
							+ ", Stared = "
							+ cursor.getInt(5)
							+ ", Time = "
							+ cursor.getString(6)
							+ ", Position = "
							+ cursor.getString(7)
							+ "\n";
		}

		System.out.println(str);

		return str;
	}

	public String getMemo(String time) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from MEMO where time = '" + time + "'", null);
		cursor.moveToNext();
		String s = cursor.getString(2); // memo
		s = (s != null) ? s : "";
		db.close();
		return s;
	}

	public String getTitle(String time) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from MEMO where time = '" + time + "'", null);
		cursor.moveToNext();
		String s = cursor.getString(1); // title
		s = (s != null) ? s : "";
		db.close();
		return s;
	}

	public String getCategory(String time) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from MEMO where time = '" + time + "'", null);
		cursor.moveToNext();
		String s = cursor.getString(4); // category
		s = (s != null) ? s : "";
		db.close();
		return s;
	}

	public void deleteNullMemo(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from MEMO where memo = null;");
		db.close();
	}


}
