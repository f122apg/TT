package com.tetsujin.tt.database;

public class DBInfo
{
    public final static String DBNAME_MEMO = "MemoDB";
    public final static String DBNAME_STUDENT = "Student";
    public final static String DBMEMO_TABLENAME = "Memo";
    public final static String[] DBMEMO_COLUMN = { "MemoAt", "Content" };

    public final static String CREATETABLE_MEMO_QUERY = "create table " + DBNAME_MEMO + "(" +
            DBMEMO_COLUMN[0] + " text primary key," +
            DBMEMO_COLUMN[1] + " text not null" +
            ")";

}
