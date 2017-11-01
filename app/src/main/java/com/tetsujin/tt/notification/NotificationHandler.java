package com.tetsujin.tt.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.tetsujin.tt.MainActivity;
import com.tetsujin.tt.NotificationActivity;
import com.tetsujin.tt.R;

public class NotificationHandler extends NotificationsHandler {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    //通知の受信を行う
    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String nhMessage = bundle.getString("message");
        sendNotification(nhMessage);
        if (MainActivity.isVisible) {
            MainActivity.mainActivity.ToastNotify(nhMessage);
        }
    }

    //通知を送信する
    private void sendNotification(String msg) {

        //通知がタップされた時のintentを生成
        //第二引数には起動したいActivityを入れる
        Intent intent = new Intent(ctx, NotificationActivity.class);
        //FLAG_ACTIVITY_CLEAR_TOP - 呼び出すActivity以外のActivityをクリアして起動させる
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //引数内のシステムを取得する 今回は NOTIFICATION_SERVICE
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        //指定されたタイミングで発行するintentを"準備"(発行ではない)する
        //引数 - context, requestCode(現在使用されていない？), intent, FLAG_XXXX_XXXX
        //FLAG_ONE_SHOT - このPendingIntentは１回しか利用できず、再利用不可
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        //通知音をUriで取得する
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //通知を作成する
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        //アイコン
                        .setSmallIcon(R.mipmap.ic_launcher)
                        //通知タイトル
                        .setContentTitle(ctx.getResources().getString(R.string.notification_title))
                        //デザイン
                        //BigTextStyle - 複数の通知が来た時、その通知を一つにまとめる
                        //setBigContentTitle - 通知タイトル
                        //bigText - 通知内容
                        //setSummaryText - 通知がまとめられた時に表示するテキスト(例：○件のメールが届いています)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .setBigContentTitle(ctx.getResources().getString(R.string.notification_title))
                                .bigText(msg)
                                .setSummaryText(ctx.getResources().getString(R.string.notification_receive_num)))
                        //音
                        .setSound(defaultSoundUri)
                        //通知内容
                        .setContentText(msg);

        //通知がタップされた時にActivityを起動する
        mBuilder.setContentIntent(contentIntent);
        //通知がタップされた時、通知を削除する
        mBuilder.setAutoCancel(true);
        //通知IDと予め作成されたBuilderを使用して通知させる
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}