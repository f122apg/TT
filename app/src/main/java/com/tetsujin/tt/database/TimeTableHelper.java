package com.tetsujin.tt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;

import static com.tetsujin.tt.database.TimeTable.COLUMN_CLASSROOMNAME;
import static com.tetsujin.tt.database.TimeTable.COLUMN_DESCRIPTION;
import static com.tetsujin.tt.database.TimeTable.COLUMN_ENDTIME;
import static com.tetsujin.tt.database.TimeTable.COLUMN_LESSONCODE;
import static com.tetsujin.tt.database.TimeTable.COLUMN_LESSONNAME;
import static com.tetsujin.tt.database.TimeTable.COLUMN_STARTTIME;
import static com.tetsujin.tt.database.TimeTable.COLUMN_TEACHERNAME;
import static com.tetsujin.tt.database.TimeTable.COLUMN_TIMETABLEID;
import static com.tetsujin.tt.database.TimeTable.COLUMN_WEEKDAYNUMBER;

public class TimeTableHelper extends SQLiteOpenHelper
{
    private static TimeTableHelper singleton = null;

    //シングルトン
    public static synchronized TimeTableHelper getInstance(Context context)
    {
        if(singleton == null)
            singleton = new TimeTableHelper(context);

        return singleton;
    }

    //コンストラクタ シングルトンによってprivateにしている
    private TimeTableHelper(Context context)
    {
        super(context, TimeTable.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TimeTable.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
    }

    //TimeTableIDの存在チェックを行う
    private boolean HasId(SQLiteDatabase db, int id)
    {
        Cursor result;

        //データベースが存在するが行がない場合、SQLiteExceptionがthrowされる模様
        try
        {
            result = db.rawQuery(TimeTable.GET_RECORD_ID_QUERY, new String[]{ String.valueOf(id) });
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
    public TimeTable[] GetRecordAll(SQLiteDatabase db)
    {
        Cursor result = db.rawQuery(TimeTable.GET_RECORD_ALL, null);

        try
        {
            //レコードをTimeTableに変換し返す
            return getTimeTableFromCursor(result);
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
    
    
    //特定の曜日の時間割データだけを取得する
    public TimeTable[] GetRecordAtWeekDay(SQLiteDatabase db, int weekday)
    {
        Cursor result = db.rawQuery(TimeTable.GET_RECORD_AT_WEEKDAY_QUERY, new String[]{ String.valueOf(weekday) });

        try
        {
            //レコードをTimeTableに変換し返す
            return getTimeTableFromCursor(result);
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

    //引数のカーソルからTimeTableのインスタンスを生成し、それらを配列で返す
    private TimeTable[] getTimeTableFromCursor(Cursor cr)
    {
        TimeTable[] retValue = new TimeTable[cr.getCount()];

        while(cr.moveToNext())
        {
            retValue[cr.getPosition()] =
                    new TimeTable.Builder(cr.getInt(cr.getColumnIndex(COLUMN_TIMETABLEID)))
                    .LessonCode(cr.getString(cr.getColumnIndex(COLUMN_LESSONCODE)))
                    .LessonName(cr.getString(cr.getColumnIndex(COLUMN_LESSONNAME)))
                    .WeekDay(cr.getInt(cr.getColumnIndex(COLUMN_WEEKDAYNUMBER)))
                    .StartTime(cr.getString(cr.getColumnIndex(COLUMN_STARTTIME)))
                    .EndTime(cr.getString(cr.getColumnIndex(COLUMN_ENDTIME)))
                    .ClassRoomName(cr.getString(cr.getColumnIndex(COLUMN_CLASSROOMNAME)))
                    .TeacherName(cr.getString(cr.getColumnIndex(COLUMN_TEACHERNAME)))
                    .Description(cr.getString(cr.getColumnIndex(COLUMN_DESCRIPTION)))
                    .build();
        }

        return retValue;
    }
    
    //レコードのインサートを行う
    public boolean Insert(SQLiteDatabase db, Map<Integer, Object> data)
    {
        //インサートしようとしているデータが既に存在する場合はインサートしない
        if(!HasId(db, (Integer)data.get(0)))
        {
            //トランザクションを開始
            db.beginTransaction();
            
            //インサートするデータを準備する
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_TIMETABLEID, (Integer) data.get(0));
            cv.put(TimeTable.COLUMN_LESSONCODE, (String) data.get(1));
            cv.put(TimeTable.COLUMN_LESSONNAME, (String) data.get(2));
            cv.put(TimeTable.COLUMN_WEEKDAYNUMBER, (Integer) data.get(3));
            cv.put(COLUMN_STARTTIME, (String) data.get(4));
            cv.put(TimeTable.COLUMN_ENDTIME, (String) data.get(5));
            cv.put(TimeTable.COLUMN_CLASSROOMNAME, (String) data.get(6));
            cv.put(TimeTable.COLUMN_TEACHERNAME, (String) data.get(7));
            cv.put(TimeTable.COLUMN_DESCRIPTION, (String) data.get(8));

            try
            {
                //エラーチェック 返り値が-1でないならば、正しく処理されたとする
                if ( db.insert(TimeTable.TABLE_NAME, null, cv) > -1)
                {
                    //トランザクション成功
                    db.setTransactionSuccessful();
                    return true;
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                return false;
            }
            finally
            {
                //トランザクションを終了
                db.endTransaction();
            }
        }
        
        return false;
    }

    public void Clear(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TimeTable.TABLE_NAME);
    }
}
