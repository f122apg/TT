package com.tetsujin.tt;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.tetsujin.tt.database.MemoHelper;
import com.tetsujin.tt.database.TimeTable;
import com.tetsujin.tt.database.TimeTableHelper;
import com.tetsujin.tt.notification.NotificationHandler;
import com.tetsujin.tt.notification.NotificationSettings;
import com.tetsujin.tt.notification.RegistrationIntentService;

import java.util.Calendar;
import java.util.Date;

public class ActivityMain extends AppCompatActivity {

    private FragmentManager fm;
    public static ActivityMain activityMain;
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
            activityMain = this;
            memoHelper = new MemoHelper(activityMain);
            timeTableHelper = new TimeTableHelper(activityMain);
            //DBが存在していなかったらDBの作成がされる
            memoDB = memoHelper.getWritableDatabase();
            timeTableDB = timeTableHelper.getWritableDatabase();
            //時間割データをTimeTableDBから取得し、static変数に入れる
            timeTable = timeTableHelper.GetRecordAtWeekDay(Integer.parseInt(getToDayWeekDay(true, getToDayWeekDay(false, null))));
            fm = getSupportFragmentManager();

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

    //文字列から数値へ変換する場合はtrue
    public static String getToDayWeekDay(boolean getStrToInteger, String weekDay)
    {
        //falseなら当日の曜日を返し、trueでありかつnullではないなら指定された曜日を数値へ変換し返す
        if(!getStrToInteger)
        {
            //現在の日付を取得
            Date nowdate = new Date();
            //Calendarに現在の日付を設定
            Calendar cal = Calendar.getInstance();
            //現在の曜日のみを取得
            CharSequence week = DateFormat.format("E", nowdate);
            //現在の曜日が「土」か「日」だったら次週の月曜日にする
            if (week.equals("土") || week.equals("日"))
            {
                //現在の日付を２日足し、次週の月曜日にする
                cal.add(Calendar.DAY_OF_MONTH, 2);
                cal.set(Calendar.DAY_OF_WEEK, 2);
            }

            return (String) week;
        }
        else if(!(weekDay == null))
        {
            //現在の日付を取得
            Date nowdate = new Date();
            //Calendarに現在の日付を設定
            Calendar cal = Calendar.getInstance();
            //現在の曜日のみを取得
            CharSequence week = DateFormat.format("E", nowdate);
            //現在の曜日が「土」か「日」だったら次週の月曜日にする
            if (week.equals("土") || week.equals("日"))
            {
                //現在の日付を２日足し、次週の月曜日にする
                cal.add(Calendar.DAY_OF_MONTH, 2);
                cal.set(Calendar.DAY_OF_WEEK, 2);
            }

            switch ((String)week)
            {
                case "月":
                    return String.valueOf(Calendar.MONDAY);
                case "火":
                    return String.valueOf(Calendar.TUESDAY);
                case "水":
                    return String.valueOf(Calendar.WEDNESDAY);
                case "木":
                    return String.valueOf(Calendar.THURSDAY);
                case "金":
                    return String.valueOf(Calendar.FRIDAY);
                default:
                    return "-1";
            }
        }

        return "-1";
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