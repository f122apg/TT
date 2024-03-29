package com.tetsujin.tt.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.Map;

import static com.tetsujin.tt.ActivityMain.memoDB;

public class MemoHelper extends SQLiteOpenHelper
{
    public MemoHelper(Context context)
    {
        super(context, Memo.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Memo.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
    }

    //指定された日付がすでに存在してるかどうかチェックする
    public boolean HasDate(String date)
    {
        Cursor result;

        //データベースが存在するが行がない場合、SQLiteExceptionがthrowされる模様
        try
        {
            result = memoDB.rawQuery(Memo.GET_RECORD_QUERY, new String[]{ date });
        }
        catch (SQLiteException e)
        {
            return false;
        }

        //カーソルの位置を初期位置「-1」から「0」にする
        result.moveToFirst();
        //指定された日付のレコードが存在していたらtrueを返す
        if(result.getCount() != 0)
        {
            result.close();
            return true;
        }
        else
        {
            result.close();
            return false;
        }
    }
    
    //データベース内にあるレコードすべて取得する
    public String[] GetRecordAll()
    {
        Cursor result = memoDB.rawQuery(Memo.GET_RECORD_ALL, null);
        
        try
        {
            String[] retValue = new String[result.getCount()];
            
            //すべてのメモを返す
            while(result.moveToNext())
            {
                retValue[result.getPosition()] = result.getString(result.getColumnIndex(Memo.COLUMN_DATE));
            }
            
            return retValue;
        }
        catch (CursorIndexOutOfBoundsException e)
        {
            return null;
        }
        finally
        {
            result.close();
        }
    }
    
    
    //特定の日付のレコードを返す
    public String GetRecord(String date)
    {
        Cursor result = memoDB.rawQuery(Memo.GET_RECORD_QUERY, new String[]{ date });
        //カーソルの位置を初期位置「-1」から「0」にする
        result.moveToFirst();
        
        //空(CursorIndexOutOfBoundsException)ではない場合、文字列を返す
        try
        {
            return result.getString(result.getColumnIndex(Memo.COLUMN_CONTENT));
        }
        catch (CursorIndexOutOfBoundsException e)
        {
            return "";
        }
        finally
        {
            result.close();
        }
    }
    
    //レコードをインサートするかアップデートを行う
    public boolean InsertorUpdate(Map<Integer, Object> data, boolean isupdate)
    {
        SQLiteStatement sqlst;
        
        if(isupdate)
            sqlst = memoDB.compileStatement(Memo.UPDATE_QUERY);
        else
            sqlst = memoDB.compileStatement(Memo.INSERT_QUERY);
    
        for(int i = 1; i <= data.size(); i ++)
        {
            //型によって値をqueryにバインドしていく
            if(data.get(i) instanceof String)
                sqlst.bindString(i, (String)data.get(i));
            else if(data.get(i) instanceof Double)
                sqlst.bindDouble(i, (double)data.get(i));
            else if(data.get(i) instanceof Long)
                sqlst.bindLong(i, (long)data.get(i));
            else if(data.get(i) instanceof byte[])
                sqlst.bindBlob(i, (byte[])data.get(i));
            else
                sqlst.bindNull(i);
        }
        
        try
        {
            //isupdateがtrueならばのアップデートを行う
            if(isupdate)
            {
                //エラーチェック アップデートされたレコード数を数え、0じゃなければ正しく処理されたとする
                if(sqlst.executeUpdateDelete() != 0)
                    return true;
                else
                    return false;
            }
            else
            {
                //エラーチェック 返り値が-1でないならば、正しく処理されたとする
                if(sqlst.executeInsert() != -1)
                    return true;
                else
                    return false;
            }
        }
        catch (SQLException e)
        {
            return false;
        }
        finally
        {
            sqlst.close();
        }
    }
    
    //レコードを削除する
    public void Delete(String date)
    {
        memoDB.execSQL(Memo.DELETE_QUERY, new Object[]{ date });
    }
}
