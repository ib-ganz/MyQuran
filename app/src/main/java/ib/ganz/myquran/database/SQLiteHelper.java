package ib.ganz.myquran.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper
{
	private String dbName;
	private static final int DATABASE_VERSION = 1;

	private Context context;
	
	public SQLiteHelper(Context context, String dbName) 
	{
		super(context, dbName, null, DATABASE_VERSION);
		
		this.context = context;
		this.dbName = dbName;
	}

	public void createDataBase() throws IOException 
	{
	    boolean mDataBaseExist = checkDataBase();
	    
	    if (!mDataBaseExist) 
	    {
	        this.getReadableDatabase();
	        
	        try 
	        {
	            copyDataBase();
	        } 
	        catch (IOException mIOException) 
	        {
	            mIOException.printStackTrace();
	            throw new Error("Error copying database");
	        } 
	        finally 
	        {
	            this.close();
	        }
	    }
	}

	private boolean checkDataBase() 
	{
	    try 
	    {
	        final File file = context.getDatabasePath(dbName);
	        
	        if (file.exists())
	            return true;
	        else
	            return false;
	    } 
	    catch (SQLiteException e)
	    {
	        e.printStackTrace();
	        return false;
	    }
	}

	private void copyDataBase() throws IOException 
	{
	    try 
	    {
	    	InputStream mInputStream = context.getAssets().open(dbName);
	        File outFileName = context.getDatabasePath(dbName);
	        OutputStream mOutputStream = new FileOutputStream(outFileName);
	        
	        byte[] buffer = new byte[1024];
	        int length;
	        
	        while ((length = mInputStream.read(buffer)) > 0) 
	        {
	            mOutputStream.write(buffer, 0, length);
	        }
	        
	        mOutputStream.flush();
	        mOutputStream.close();
	        mInputStream.close();
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		
	}

}
