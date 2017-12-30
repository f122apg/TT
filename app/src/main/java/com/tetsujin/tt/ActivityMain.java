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
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.tetsujin.tt.database.MemoDBHelper;
import com.tetsujin.tt.notification.NotificationHandler;
import com.tetsujin.tt.notification.NotificationSettings;
import com.tetsujin.tt.notification.RegistrationIntentService;

public class ActivityMain extends AppCompatActivity {

    //TODO:時間割データの一時的な保存場所
    String timetabledata_url = "http://tetsujin.azurewebsites.net/api/schedules";

    public static ActivityMain activityMain;

    public static MemoDBHelper memoDBHelper;
    public static SQLiteDatabase memodb;
    private FragmentManager fm;
    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //アプリ実行時１度だけ実行
        if(savedInstanceState == null)
        {
            activityMain = this;
            memoDBHelper = new MemoDBHelper(activityMain);
            //DBが存在していなかったらDBの作成がされる
            memodb = memoDBHelper.getWritableDatabase();
            fm = getSupportFragmentManager();

            //ActivityMainに存在するcontainerにFragmentMainを表示する
            //初回のみアニメーションをさせないようにshowFragmentメソッドを使わずに表示
            FragmentHeader frghead = new FragmentHeader();
            FragmentMain frgmain = new FragmentMain();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.AyMain_headcontainer_linearlayout, frghead);
            ft.replace(R.id.AyMain_container_linearlayout, frgmain);
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

    //時間割データを取得する
    public void getTimeTable()
    {
        TaskGetTimeTable task = new TaskGetTimeTable(this);
        task.execute(timetabledata_url);
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
        transaction.replace(R.id.AyMain_container_linearlayout, frg);
        transaction.commit();
    }
}

//TODO:DBのインサート及び読み込み
//        try
//                {
//                mClient = new MobileServiceClient("https://tetsujin-tt.azurewebsites.net", this);
//                }
//                catch(MalformedURLException e)
//                {
//                e.printStackTrace();
//                }
//        Student st = new Student("K016C1369", "小濵大地", 2016, 2, "CD", 4);
//        mClient.getTable(Student.class).insert(st, new TableOperationCallback<Student>() {
//            @Override
//            public void onCompleted(Student entity, Exception exception, ServiceFilterResponse response) {
//                System.out.println("Completed");
//                if(exception == null)
//                    System.out.println("sucessfully");
//                else
//                {
//                    System.out.println("failed");
//                    exception.printStackTrace();
//                }
//            }
//        });

//        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    MobileServiceTable<test> s = mClient.getTable(test.class);
//                    System.out.println("GetTable");
//                    final List<test> a = s.execute().get();
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println(a.get(0).gettext());
//                        }
//                    });
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//        };
//
//        task.execute();