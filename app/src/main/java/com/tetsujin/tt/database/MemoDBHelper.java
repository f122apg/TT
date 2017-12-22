package com.tetsujin.tt.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDBHelper extends SQLiteOpenHelper
{
    public MemoDBHelper(Context context)
    {
        super(context, DBInfo.DBNAME_MEMO, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DBInfo.CREATETABLE_MEMO_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {

    }
}
