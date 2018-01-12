package com.tetsujin.tt.database;

public class Memo
{
    //データベース名
    public final static String DB_NAME = "MemoDB";
    //テーブル名
    private final static String TABLE_NAME = "Memo";
    //テーブルの列名
    private final static String COLUMN_DATE = "Date";
    private final static String COLUMN_CONTENT = "Content";

    //テーブルを作成するクエリ
    final static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_DATE + " TEXT PRIMARY KEY," + COLUMN_CONTENT + " TEXT NOT NULL" +
            ");";
    
    //列「Content」を取得するクエリ
    final static String GET_RECORD_QUERY = "SELECT " + COLUMN_CONTENT +
            " FROM " + TABLE_NAME +
            " WHERE " + COLUMN_DATE + " = ?;";
    
    //インサートをするクエリ
    final static String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES(?, ?);";
    
    //列「Content」をアップデートするクエリ
    final static String UPDATE_QUERY = "UPDATE " + TABLE_NAME +
            " SET " + COLUMN_CONTENT + " = ?" +
            " WHERE " + COLUMN_DATE + " = ?;";
    
    //列を削除するクエリ
    final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME +
            " WHERE " + COLUMN_DATE + " = ?;";
}
