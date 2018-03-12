package com.tetsujin.tt.database;

import com.tetsujin.tt.notification.NotificationSettings;

public class Notification
{
    //データベース名
    public final static String DB_NAME = "NotificationDB";
    //テーブル名
    public final static String TABLE_NAME = "Notification";
    //テーブルの列名
    final static String COLUMN_SENDDATE = NotificationSettings.SENDDATE;
    final static String COLUMN_SENDER = NotificationSettings.SENDER;
    final static String COLUMN_TITLE = NotificationSettings.TITLE;
    final static String COLUMN_MESSAGE = NotificationSettings.MESSAGE;

    //テーブルを作成するクエリ
    final static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_SENDDATE + " TEXT PRIMARY KEY," +
            COLUMN_SENDER + " TEXT NOT NULL," +
            COLUMN_TITLE + " TEXT NOT NULL," +
            COLUMN_MESSAGE + " TEXT NOT NULL" +
            ");";
    
    //すべてのレコードを取得するクエリ
    final static String GET_RECORD_ALL = "SELECT * FROM " + TABLE_NAME + ";";
    
    //特定のレコードを取得するクエリ
    final static String GET_RECORD = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_SENDDATE + " = ?";
    
    //インサートをするクエリ
    final static String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES(?, ?, ?, ?);";
}
