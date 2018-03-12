package com.tetsujin.tt.notification;

public class NotificationSettings
{
    //NotificationHubを使う上で使用されるId等
    final public static String SenderId = "393335228957";
    final static String HubName = "test_notificationhub";
    final static String HubListenConnectionString = "Endpoint=sb://testtesttetst.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=3ZgnR+7y01oB9/qFRuFbtmjcx+AO1dcQ3bMkKgsvnvc=";
    
    //送信されたお知らせの要素名
    final public static String SENDDATE = "SendDate"; //送信日時
    final public static String SENDER = "Sender";     //送信者
    final public static String TITLE = "Title";       //タイトル
    final public static String MESSAGE = "Message";   //内容
}
