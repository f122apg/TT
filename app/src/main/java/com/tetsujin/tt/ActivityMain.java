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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
            memoHelper = new MemoHelper(activityMain);
            timeTableHelper = new TimeTableHelper(activityMain);
            //DBが存在していなかったらDBの作成がされる
            memoDB = memoHelper.getWritableDatabase();
            timeTableDB = timeTableHelper.getWritableDatabase();

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

    //今日の日付をyyyy-MM-ddで取得する
    public static String getToDay()
    {
        //Calendarに現在の日付を設定
        Calendar cal = Calendar.getInstance();
        
        //現在の曜日が「土」か「日」だったら次週の月曜日にする
        int week = cal.get(Calendar.DAY_OF_WEEK);
        if(week == Calendar.SATURDAY || week == Calendar.SUNDAY)
        {
            //現在の日付を２日足し、次週の月曜日にする
            cal.add(Calendar.DAY_OF_MONTH, 2);
            cal.set(Calendar.DAY_OF_WEEK, 2);
        }
        
        return (String) DateFormat.format(activityMain.getResources().getString(R.string.format_yyyy_MM_dd), cal);

    }

    //String型の日付をpatternでparseされたDateを返す
    public static Date getParsedDate(String source, String pattern)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try
        {
            return sdf.parse(source);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTodayWeekDay(boolean getInteger)
    {
        //getTodayで取得した日付を用いて、曜日をintegerで取得
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE);

        try
        {
            cal.clear();
            //calにparseした日付を設定
            cal.setTime(sdf.parse(getToDay()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        //曜日を取得
        int week = cal.get(Calendar.DAY_OF_WEEK);

        //現在の曜日が「土」か「日」だったら次週の月曜日にする
        if(week == Calendar.SATURDAY || week == Calendar.SUNDAY)
        {
            //現在の日付を２日足し、次週の月曜日にする
            cal.add(Calendar.DAY_OF_MONTH, 2);
            cal.set(Calendar.DAY_OF_WEEK, 2);

            week = cal.get(Calendar.DAY_OF_WEEK);
        }

        //getIntegerがfalseなら、曜日を漢字で返す
        //trueならばString型で曜日を数字として返す
        if (!getInteger)
        {
            switch (week)
            {
                case Calendar.MONDAY:
                    return "月";
                case Calendar.TUESDAY:
                    return "火";
                case Calendar.WEDNESDAY:
                    return "水";
                case Calendar.THURSDAY:
                    return "木";
                case Calendar.FRIDAY:
                    return "金";
                default:
                    return null;
            }
        }
        else
            return String.valueOf(week);
    }
    
    public static Integer getWeekDay(String date)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE);
    
        try
        {
            cal.clear();
            //calにparseした日付を設定
            cal.setTime(sdf.parse(date));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
        //曜日を取得
        int week = cal.get(Calendar.DAY_OF_WEEK);
        
        //現在の曜日が「土」か「日」だったら次週の月曜日にする
        if(week == Calendar.SATURDAY || week == Calendar.SUNDAY)
        {
            //現在の日付を２日足し、次週の月曜日にする
            cal.add(Calendar.DAY_OF_MONTH, 2);
            cal.set(Calendar.DAY_OF_WEEK, 2);
            
            week = cal.get(Calendar.DAY_OF_WEEK);
        }
        
        return week;
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