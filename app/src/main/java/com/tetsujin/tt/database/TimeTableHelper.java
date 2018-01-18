package com.tetsujin.tt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Map;

import static com.tetsujin.tt.FragmentMain.timeTableDB;

public class TimeTableHelper extends SQLiteOpenHelper
{
    public TimeTableHelper(Context context)
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
    private boolean HasId(int id)
    {
        Cursor result;

        //データベースが存在するが行がない場合、SQLiteExceptionがthrowされる模様
        try
        {
            result = timeTableDB.rawQuery(TimeTable.GET_RECORD_QUERY, new String[]{ TimeTable.COLUMN_TIMETABLEID, String.valueOf(id) });
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

    public TimeTable[] GetRecordAtWeekDay(int weekday)
    {
        Cursor result = timeTableDB.rawQuery(TimeTable.GET_RECORD_QUERY, new String[]{ TimeTable.COLUMN_WEEKDAY, String.valueOf(weekday) });
        
        //空(CursorIndexOutOfBoundsException)ではない場合、文字列を返す
        try
        {
            ArrayList<Object> retValue = new ArrayList<>();
            TimeTable[] rettimeTable = new TimeTable[result.getCount()];

            //返すデータを準備する
            while(result.moveToNext())
            {
                retValue.add(result.getInt(0));
                retValue.add(result.getString(1));
                retValue.add(result.getString(2));
                retValue.add(result.getString(3));
                retValue.add(result.getString(4));
                retValue.add(result.getString(5));
                retValue.add(result.getString(6));
                retValue.add(result.getString(7));
                retValue.add(result.getInt(8));
                retValue.add(result.getString(9));
                retValue.add(result.getString(10));

                rettimeTable[result.getPosition()] = new TimeTable(retValue);
                retValue.clear();
            }

            return rettimeTable;
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
    
    //レコードのインサートを行う
    public boolean Insert(Map<Integer, Object> data)
    {
        //インサートしようとしているデータが既に存在する場合はインサートしない
        if(!HasId((Integer)data.get(0)))
        {
            //インサートするデータを準備する
            ContentValues cv = new ContentValues();
            cv.put(TimeTable.COLUMN_TIMETABLEID, (Integer) data.get(0));
            cv.put(TimeTable.COLUMN_LESSONCODE, (String) data.get(1));
            cv.put(TimeTable.COLUMN_LESSONNAME, (String) data.get(2));
            cv.put(TimeTable.COLUMN_WEEKDAY, (String) data.get(3));
            cv.put(TimeTable.COLUMN_STARTTIME, (String) data.get(4));
            cv.put(TimeTable.COLUMN_ENDTIME, (String) data.get(5));
            cv.put(TimeTable.COLUMN_SEASON, (String) data.get(6));
            cv.put(TimeTable.COLUMN_CLASSROOMNAME, (String) data.get(7));
            cv.put(TimeTable.COLUMN_TEACHERID, (Integer) data.get(8));
            cv.put(TimeTable.COLUMN_TEACHERNAME, (String) data.get(9));
            cv.put(TimeTable.COLUMN_DESCRIPTION, (String) data.get(10));

            try
            {
                //エラーチェック 返り値が0でないならば、正しく処理されたとする
                if (timeTableDB.insert(TimeTable.TABLE_NAME, null, cv) > 0)
                    return true;
                else
                    return false;
            }
            catch (SQLException e)
            {
                return false;
            }
        }
        else
            return false;
    }

    public void Clear()
    {
        timeTableDB.execSQL("DELETE FROM " + TimeTable.TABLE_NAME);
    }
}
