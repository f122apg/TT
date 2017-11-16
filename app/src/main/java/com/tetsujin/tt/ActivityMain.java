package com.tetsujin.tt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import java.net.MalformedURLException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import com.tetsujin.tt.notification.NotificationHandler;
import com.tetsujin.tt.notification.NotificationSettings;
import com.tetsujin.tt.notification.RegistrationIntentService;


import com.microsoft.windowsazure.mobileservices.*;

public class ActivityMain extends AppCompatActivity {

    String timetabledata_url = "http://tetsujin.azurewebsites.net/api/schedules";
    private static String[][] testdata = new String[0][];

    public static ActivityMain activityMain;
    private boolean isChangeFragment = false;
    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMain = this;
        try
        {
            mClient = new MobileServiceClient("https://tetsujin-tt.azurewebsites.net", this);
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }

        TodoItem item = new TodoItem();
        item.Text = "Awesome Text";
        mClient.getTable(TodoItem.class).insert(item, new TableOperationCallback<TodoItem>() {
            @Override
            public void onCompleted(TodoItem entity, Exception exception, ServiceFilterResponse response) {
                System.out.println("Completed");
                if(exception == null)
                    System.out.println("sucessfully");
                else
                    exception.printStackTrace();
            }
        });

        //ハンドルをセット
        NotificationsManager.handleNotifications(this, NotificationSettings.SenderId, NotificationHandler.class);
        //Notification Hubsにこの端末の登録作業を行う
        registerWithNotificationHubs();
    
        final String[][] testdata = new String[][]
                {
                        {"1", "卒業制作", "09:00", "10:30", "月"},
                        {"2", "クラウドコンピューティング", "10:40", "12:10", "月"},
                        {"3", "データベース応用", "09:00", "12:10", "火"},
                        {"4", "Linux実習", "13:00", "16:10", "火"},
                        {"5", "キャリアデザイン3", "09:00", "14:30", "水"},
                        {"6", "オブジェクト指向プログラミング実習1 S1", "09:00", "12:10", "木"},
                        {"7", "ソフトウェアデザイン S1", "13:00", "14:30", "木"},
                        {"8", "合同資格対策講座", "09:00", "14:30", "金"},
                };

        //ActivityMainに存在するcontainerにFragmentMainを表示する
        //初回のみアニメーションをさせないようにshowFragmentメソッドを使わずに表示
        FragmentMain frgmain = new FragmentMain();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.AyMain_container_linearlayout, frgmain);
        ft.commit();

        /* onClickListeners */
        //B1 1週間の時間割に遷移する
        findViewById(R.id.Header_B1_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final int resourceid;
                
                //FragmentWeekContainerの表示
                if(!isChangeFragment)
                {
                    resourceid = R.drawable.icon_arrow_back;
                    
                    //FragmentWeekContainerに時間割データを渡す
                    Bundle bundle = new Bundle();
                    bundle.putInt("datalength", testdata.length);
                    for (int i = 0; i < testdata.length; i++)
                    {
                        bundle.putStringArray("data" + i, testdata[i]);
                    }
                    
                    //FragmentWeekContainerに値を渡し、表示する
                    FragmentWeekContainer frgwc = new FragmentWeekContainer();
                    frgwc.setArguments(bundle);
                    showFragment(frgwc);
                }
                //FragmentMainの表示
                else
                {
                    resourceid = R.drawable.icon_week;
                    FragmentMain frgmain = new FragmentMain();
                    showFragment(frgmain);
                }
    
                /* アニメーション処理 */
                //Fragmentの表示と共に、B1ボタンを矢印に変更するアニメーションを開始させる
                final ImageButton ib = (ImageButton)findViewById(R.id.Header_B1_button);
                //アニメーションの読み込みとアニメーションにかける時間を設定
                Animation fadeout_anim = AnimationUtils.loadAnimation(activityMain, R.anim.icon_fadeout);
                fadeout_anim.setDuration(250);
                //リスナーを設定
                fadeout_anim.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation){}
        
                    //アニメーション終了時、ボタンの画像を変更しアニメーションをかける
                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        ib.setImageResource(resourceid);
                        Animation fadein_anim = AnimationUtils.loadAnimation(activityMain, R.anim.icon_fadein);
                        fadein_anim.setDuration(250);
                        ib.startAnimation(fadein_anim);
                    }
        
                    @Override
                    public void onAnimationRepeat(Animation animation){}
                });
                //アニメーションを開始
                ib.startAnimation(fadeout_anim);
                
                //フラグを反転
                isChangeFragment = !isChangeFragment;
            }
        });
        //B2 GetTimeTableの動作確認用 確認できたら削除する
        findViewById(R.id.Header_B2_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GetTimeTable();
            }
        });
    }

    //時間割データを取得する
    public void GetTimeTable()
    {
        NetWorkTask task = new NetWorkTask(this);
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.activity_inright, R.anim.activity_outleft, R.anim.activity_inleft, R.anim.activity_outright);
        transaction.replace(R.id.AyMain_container_linearlayout, frg);
        transaction.commit();
    }
}