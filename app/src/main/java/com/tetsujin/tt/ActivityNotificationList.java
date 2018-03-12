package com.tetsujin.tt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tetsujin.tt.adapter.NotificationListAdapter;

import static com.tetsujin.tt.notification.NotificationHandler.notificationHelper;

public class ActivityNotificationList extends AppCompatActivity
{
    private ActivityNotificationList activityNotificationList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationlist);
        
        activityNotificationList = this;
        String[][] nfData = notificationHelper.GetRecordAll();
        
        if(nfData.length == 0)
        {
            new AlertDialog.Builder(activityNotificationList)
                    .setTitle("警告")
                    .setMessage("お知らせはありません。")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }
                    })
                    .show();
        }
        
        ListView nflv = (ListView)findViewById(R.id.AyNotificationList_nflist_listview);
        NotificationListAdapter nla = new NotificationListAdapter(this, nfData);
        nflv.setAdapter(nla);
    
        //ListViewのクリックイベント
        nflv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            //ListView内にあるアイテムをタップするとAlertDiaglogを用いて、
            //授業の詳細を表示するようにしている
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id)
            {
                ListView lv = (ListView)adapterView;
                //ListView内に存在するアイテムを取得
                String[] data = (String[]) lv.getItemAtPosition(pos);
    
                Intent intent = new Intent(activityNotificationList, ActivityNotification.class);
                intent.putExtra("key", data[0]);
                startActivity(intent);
                //詳細画面を表示
//                new AlertDialog.Builder(activityNotificationList)
//                        .setTitle("お知らせの詳細")
//                        .setMessage(
//                                "受信日時：" + data[0] + "\n" +
//                                "送信者：" + data[1] + "\n" +
//                                "タイトル：" + data[2] + "\n" +
//                                "内容：" + data[3])
//                        .setPositiveButton(R.string.ok, null)
//                        .show();
            }
        });
    }
}
