package com.tetsujin.tt.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.tetsujin.tt.ActivityNotification;
import com.tetsujin.tt.R;
import com.tetsujin.tt.database.NotificationHelper;
import com.tetsujin.tt.database.TimeTable;
import com.tetsujin.tt.database.TimeTableHelper;

import java.util.Map;
import java.util.TreeMap;

public class NotificationHandler extends NotificationsHandler {
    
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    Context context;
    
    public static NotificationHelper notificationHelper;
    public static SQLiteDatabase notificationDB;
    private static TimeTableHelper timetableHelper;
    private static SQLiteDatabase timetableDB;

    //通知の受信を行う
    @Override
    public void onReceive(Context context, Bundle bundle) {
        this.context = context;
        //受信したメッセージを各項目ごとに抜き出す
        String sendDate = bundle.getString(NotificationSettings.SENDDATE);
        String sender = bundle.getString(NotificationSettings.SENDER);
        String title = bundle.getString(NotificationSettings.TITLE);
        String message = bundle.getString(NotificationSettings.MESSAGE);
    
        notificationHelper = new NotificationHelper(this.context);
        notificationDB = notificationHelper.getWritableDatabase();
        timetableHelper = TimeTableHelper.getInstance(this.context);
        timetableDB = timetableHelper.getWritableDatabase();
    
        //お知らせデータをデータベースに保存
        notificationHelper.Insert(sendDate, sender, title, message);
        //お知らせに時間割が添付されていれば、時間割も保存
        String timetableId = bundle.getString(TimeTable.COLUMN_TIMETABLEID);
        if(timetableId != null)
        {
            Map<Integer, Object> data = new TreeMap<>();
    
            data.put(0, Integer.parseInt(timetableId));
            data.put(1, bundle.getString(TimeTable.COLUMN_LESSONCODE));
            data.put(2, bundle.getString(TimeTable.COLUMN_LESSONNAME));
            data.put(3, Integer.parseInt(bundle.getString(TimeTable.COLUMN_WEEKDAYNUMBER)));
            data.put(4, bundle.getString(TimeTable.COLUMN_STARTTIME));
            data.put(5, bundle.getString(TimeTable.COLUMN_ENDTIME));
            data.put(6, bundle.getString(TimeTable.COLUMN_CLASSROOMNAME));
            data.put(7, bundle.getString(TimeTable.COLUMN_TEACHERNAME));
            data.put(8, bundle.getString(TimeTable.COLUMN_DESCRIPTION));
    
            timetableHelper.Insert(timetableDB, data);
        }
        
        //通知を端末に表示する
        sendNotification(sendDate, sender, title, message);
    }
    
    //通知を端末に表示する
    private void sendNotification(String sendDate, String sender, String title, String message) {

        //通知がタップされた時のintentを生成
        //第二引数には起動したいActivityを入れる
        Intent intent = new Intent(context, ActivityNotification.class);
        //FLAG_ACTIVITY_CLEAR_TOP - 呼び出すActivity以外のActivityをクリアして起動させる
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //受信した通知を開くために特定のキーをマーカーとして設定
        intent.putExtra("key", sendDate);

        //引数内のシステムを取得する 今回は NOTIFICATION_SERVICE
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        //指定されたタイミングで発行するintentを"準備"(発行ではない)する
        //引数 - context, requestCode(現在使用されていない？), intent, FLAG_XXXX_XXXX
        //FLAG_ONE_SHOT - このPendingIntentは１回しか利用できず、再利用不可
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        //通知音をUriで取得する
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //通知を作成する
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        //アイコン
                        .setSmallIcon(R.mipmap.ic_launcher)
                        //通知タイトル
                        //後のsetBigContentTitleで上書きされてしまうが、Android 3.0未満だと
                        //setContentTitleしか見ないため、必ず設定する
                        .setContentTitle(title)
                        //デザイン
                        //BigTextStyle - 複数の通知が来た時、その通知を一つにまとめる
                        //setBigContentTitle - 通知タイトル
                        //bigText - 通知内容
                        //setSummaryText - 通知がまとめられた時に表示するテキスト(例：○件のメールが届いています)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                //通知タイトル
                                .setBigContentTitle(title)
                                //通知欄が閉じられた状態で表示される通知内容
                                .bigText(message)
                                //概要
                                .setSummaryText(context.getString(R.string.notification_sender) + sender))
                        //音
                        .setSound(defaultSoundUri)
                        //通知内容
                        .setContentText(message);

        //通知がタップされた時にActivityを起動する
        mBuilder.setContentIntent(contentIntent);
        //通知がタップされた時、通知を削除する
        mBuilder.setAutoCancel(true);
        //通知IDと予め作成されたBuilderを使用して通知させる
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}