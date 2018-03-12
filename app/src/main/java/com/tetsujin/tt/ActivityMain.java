package com.tetsujin.tt;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.tetsujin.tt.database.MemoHelper;
import com.tetsujin.tt.database.NotificationHelper;
import com.tetsujin.tt.database.TimeTable;
import com.tetsujin.tt.database.TimeTableHelper;
import com.tetsujin.tt.notification.NotificationHandler;
import com.tetsujin.tt.notification.NotificationSettings;
import com.tetsujin.tt.notification.RegistrationIntentService;

import static com.tetsujin.tt.notification.NotificationHandler.notificationDB;
import static com.tetsujin.tt.notification.NotificationHandler.notificationHelper;

public class ActivityMain extends AppCompatActivity {

    //Fragmentの画面管理
    private FragmentManager fm;
    //activitymain
    public static ActivityMain activityMain;
    //Fragmentの状態を管理
    public static ManagementFragmentState state;
    //DB関連
    public static MemoHelper memoHelper;
    public static TimeTableHelper timeTableHelper;
    public static SQLiteDatabase memoDB;
    public static SQLiteDatabase timeTableDB;
    public static TimeTable[] timeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //アプリ実行時１度だけ実行
        if(savedInstanceState == null)
        {
            fm = getSupportFragmentManager();
            activityMain = this;
            state = new ManagementFragmentState();
            //DBHelperを生成
            memoHelper = new MemoHelper(activityMain);
            timeTableHelper = TimeTableHelper.getInstance(activityMain);
            notificationHelper = new NotificationHelper(activityMain);
            //DBが存在していなかったらDBの作成がされる
            memoDB = memoHelper.getWritableDatabase();
            timeTableDB = timeTableHelper.getWritableDatabase();
            notificationDB = notificationHelper.getWritableDatabase();

            //ActivityMainに存在するcontainerにFragmentMainを表示する
            //初回のみアニメーションをさせないようにshowFragmentメソッドを使わずに表示
            FragmentHeader frgHead = new FragmentHeader();
            FragmentMain frgMain = new FragmentMain();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.FrgMain_headcontainer_linearlayout, frgHead);
            ft.replace(R.id.FrgMain_container_linearlayout, frgMain);
            ft.commit();
        }

        /*
        TODO:たまに出る
                E/RegIntentService: Failed to complete registration
                com.microsoft.windowsazure.messaging.NotificationHubUnauthorizedException: Unauthorized
               を解決する
         */
        //Notification Hubの設定処理 この設定が完了するとプッシュ通知を受け取れるようになる
        //ハンドルをセット
        NotificationsManager.handleNotifications(this, NotificationSettings.SenderId, NotificationHandler.class);
        //Notification Hubsにこの端末の登録作業を行う
        registerWithNotificationHubs();
    }

    //Google Play Servicesが利用可能かどうか確認する
    //利用可能ではないなら、finish()でアクティビティを終了させる
    private boolean checkPlayServices()
    {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (apiAvailability.isUserResolvableError(resultCode))
            {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            }
            else
            {
                finish();
            }
            return false;
        }
        return true;
    }

    //Google Play Servicesが利用可能ならば、FCMにこの端末を登録する処理を行うintentを開始
    public void registerWithNotificationHubs()
    {
        if (checkPlayServices())
        {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    //Fragmentを置き換えて表示する
    public void showFragment(Fragment frg)
    {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.activity_inright, R.anim.activity_outleft, R.anim.activity_inleft, R.anim.activity_outright);
        transaction.replace(R.id.FrgMain_container_linearlayout, frg);
        transaction.commit();
    }
}