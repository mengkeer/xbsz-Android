package cc.slotus.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.sqlite.SQLiteException;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/cc.slotus.xuebasizheng/databases/";
	private static String DB_NAME = "tiku.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public DataBaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();
		if (dbExist) {
			deleteDateBase();
			Log.e("mess", "应用更新，删除已存在的数据库");
		}
		this.getReadableDatabase();
		try {
			copyDataBase();
			Log.e("mess", "复制数据库文件到指定目录下");
		} catch (IOException e) {
			throw new Error("Error copying database");

		}

	}

	private void deleteDateBase() {

		File dbFile = new File(DB_PATH, DB_NAME);
		dbFile.delete();
		Log.e("mess", "第一次启动时执行的删除数据库文件");

	}

	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {

		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	// public void openDataBase() throws SQLException {
	// String myPath = DB_PATH + DB_NAME;
	// myDataBase = SQLiteDatabase.openDatabase(myPath, null,
	// SQLiteDatabase.OPEN_READONLY);
	// }

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}