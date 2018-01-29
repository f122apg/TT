package com.tetsujin.tt.database;

public class Memo
{
    //データベース名
    public final static String DB_NAME = "MemoDB";
    //テーブル名
    private final static String TABLE_NAME = "Memo";
    //テーブルの列名
    final static String COLUMN_DATE = "Date";
    final static String COLUMN_CONTENT = "Content";

    //テーブルを作成するクエリ
    final static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_DATE + " TEXT PRIMARY KEY," + COLUMN_CONTENT + " TEXT NOT NULL" +
            ");";
    
    //列「Date」のすべてのレコードを取得するクエリ
    final static String GET_RECORD_ALL = "SELECT " + COLUMN_DATE + " FROM " + TABLE_NAME + ";";
    
    //列「Content」の特定の日付だけ取得するクエリ
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
