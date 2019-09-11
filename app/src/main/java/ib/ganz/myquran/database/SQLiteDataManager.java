package ib.ganz.myquran.database;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SQLiteDataManager 
{
	private static String dbName = "quran_qu.db";
	private static boolean isInit = false;
	
	public static SQLiteDatabase getDatabase(Context context)
	{
		SQLiteHelper helper = new SQLiteHelper(context, dbName);
		
		if (!isInit)
		{
			try 
			{
				helper.createDataBase();
				isInit = true;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return helper.getWritableDatabase();
	}
	
	public static void setDbName(String name)
	{
		dbName = name;
	}
	
	public static String[][] read(Context context, String q)
	{
		if (dbName == null)
		{
			Toast.makeText(context, "Database name unknown", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		SQLiteDatabase database = getDatabase(context);
		
		Cursor cursor = database.rawQuery(q, null);
		cursor.moveToFirst();
		
		String[][] res = new String[cursor.getCount()][];
		
		for (int a = 0; a < res.length; a++)
		{
			res[a] = new String[cursor.getColumnCount()];
			
			for (int b = 0; b < res[a].length; b++)
			{
				res[a][b] = cursor.getString(b);
			}
			
			cursor.moveToNext();
		}
		
		database.close();
		
		return res;
	}
	
	public static String[] readRow(Context context, String q)
	{
		String[][] res = read(context, q);
		
		if (res.length > 0)
		{
			return res[0];
		}
		else
		{
			return null;
		}
	}
	
	public static String readScalar(Context context, String q)
	{
		String[][] res = read(context, q);
		
		if (res.length > 0 && res[0].length > 0)
		{
			return res[0][0];
		}
		else
		{
			return null;
		}
	}
	
	public static void write(Context context, String q)
	{
		if (dbName == null)
		{
			Toast.makeText(context, "Database name unknown", Toast.LENGTH_SHORT).show();
			return;
		}
		
		SQLiteDatabase database = getDatabase(context);
		database.execSQL(q);
		database.close();
	}
}
