package com.tetsujin.tt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.tetsujin.tt.notification.NotificationHandler.notificationDB;

public class NotificationHelper extends SQLiteOpenHelper
{
    public NotificationHelper(Context context)
    {
        super(context, Notification.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Notification.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
    }
    
    //特定のレコードを返す
    public String[] GetRecord(String sendDate)
    {
        Cursor result = notificationDB.rawQuery(Notification.GET_RECORD, new String[]{ sendDate });
        //カーソルの位置を初期位置「-1」から「0」にする
        result.moveToFirst();
        
        //空(CursorIndexOutOfBoundsException)ではない場合、文字列を返す
        try
        {
            String[] retValue = new String[4];
            
            retValue[0] = result.getString(result.getColumnIndex(Notification.COLUMN_SENDDATE));
            retValue[1] = result.getString(result.getColumnIndex(Notification.COLUMN_SENDER));
            retValue[2] = result.getString(result.getColumnIndex(Notification.COLUMN_TITLE));
            retValue[3] = result.getString(result.getColumnIndex(Notification.COLUMN_MESSAGE));
            
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
    
    //すべてのレコードを返す
    public String[][] GetRecordAll()
    {
        Cursor result = notificationDB.rawQuery(Notification.GET_RECORD_ALL, null);

        //空(CursorIndexOutOfBoundsException)ではない場合、文字列を返す
        try
        {
            String[][] retValue = new String[result.getCount()][4];
    
            while(result.moveToNext())
            {
                retValue[result.getPosition()][0] = result.getString(result.getColumnIndex(Notification.COLUMN_SENDDATE));
                retValue[result.getPosition()][1] = result.getString(result.getColumnIndex(Notification.COLUMN_SENDER));
                retValue[result.getPosition()][2] = result.getString(result.getColumnIndex(Notification.COLUMN_TITLE));
                retValue[result.getPosition()][3] = result.getString(result.getColumnIndex(Notification.COLUMN_MESSAGE));
            }
            
            return retValue;
        }
        catch (CursorIndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            result.close();
        }
    }
    
    //レコードをインサートする
    public boolean Insert(String sendDate, String sender, String title, String message)
    {
        //トランザクションを開始
        notificationDB.beginTransaction();
    
        //インサートするデータを準備する
        ContentValues cv = new ContentValues();
        cv.put(Notification.COLUMN_SENDDATE, sendDate);
        cv.put(Notification.COLUMN_SENDER, sender);
        cv.put(Notification.COLUMN_TITLE, title);
        cv.put(Notification.COLUMN_MESSAGE, message);
    
        try
        {
            //エラーチェック 返り値が-1でないならば、正しく処理されたとする
            if ( notificationDB.insert(Notification.TABLE_NAME, null, cv) > -1)
            {
                //トランザクション成功
                notificationDB.setTransactionSuccessful();
                return true;
            }
            else
                return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            //トランザクションを終了
            notificationDB.endTransaction();
        }
    }
}
