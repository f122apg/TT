package com.tetsujin.tt.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.Map;

import static com.tetsujin.tt.ActivityMain.memodb;

public class MemoDBHelper extends SQLiteOpenHelper
{
    public MemoDBHelper(Context context)
    {
        super(context, DBMemo.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DBMemo.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
    }
    
    //指定された日付がすでに存在してるかどうかチェックする
    public boolean HasDate(String date)
    {
        Cursor result = memodb.rawQuery(DBMemo.GET_RECORD_QUERY, new String[]{ date });
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
    
    public String GetRecord(String id)
    {
        Cursor result = memodb.rawQuery(DBMemo.GET_RECORD_QUERY, new String[]{ id });
        //カーソルの位置を初期位置「-1」から「0」にする
        result.moveToFirst();
        
        //空(CursorIndexOutOfBoundsException)ではない場合、文字列を返す
        try
        {
            return result.getString(0);
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
            sqlst = memodb.compileStatement(DBMemo.UPDATE_QUERY);
        else
            sqlst = memodb.compileStatement(DBMemo.INSERT_QUERY);
    
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
        memodb.execSQL(DBMemo.DELETE_QUERY, new Object[]{ date });
    }
}
