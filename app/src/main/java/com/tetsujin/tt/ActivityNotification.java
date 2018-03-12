package com.tetsujin.tt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.tetsujin.tt.notification.NotificationHandler.notificationHelper;

public class ActivityNotification extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        
        Intent intent = getIntent();
        //表示するお知らせを特定する値を取得
        String key = intent.getStringExtra("key");
        //お知らせを取得
        String[] result = notificationHelper.GetRecord(key);
        
        //お知らせを表示
        TextView sendDatetv = (TextView)findViewById(R.id.AyNotification_senddate_textview);
        TextView sendertv = (TextView)findViewById(R.id.AyNotification_sender_textview);
        TextView titletv = (TextView)findViewById(R.id.AyNotification_title_textview);
        TextView messagetv = (TextView)findViewById(R.id.AyNotification_message_textview);
        
        sendDatetv.setText(result[0]);
        sendertv.setText(result[1]);
        titletv.setText(result[2]);
        messagetv.setText(result[3]);
    }
}
