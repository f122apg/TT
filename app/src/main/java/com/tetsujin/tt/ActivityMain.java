package com.tetsujin.tt;

//Android
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Path;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.widget.Toast;

//Java
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

//JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//FCM & Azure Notification Hubs
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.tetsujin.tt.adapter.CustomListViewAdapter;
import com.tetsujin.tt.notification.NotificationHandler;
import com.tetsujin.tt.notification.NotificationSettings;
import com.tetsujin.tt.notification.RegistrationIntentService;

public class ActivityMain extends AppCompatActivity {

    String timetabledata_url = "http://tetsujin.azurewebsites.net/api/schedules";
    private static String[][] testdata = new String[0][];

    public static ActivityMain activityMain;
    private boolean ischangeactivity = false;
    private View v2;
    private LinearLayout childlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        activityMain = this;
        //ハンドルをセット
        NotificationsManager.handleNotifications(this, NotificationSettings.SenderId, NotificationHandler.class);
        //Notification Hubsにこの端末の登録作業を行う
        registerWithNotificationHubs();

        //ActivityMainに存在するcontainerにFragmentMainを表示する
        FragmentMain frgmain = new FragmentMain();
        changeFragment(frgmain);

        /* onClickListeners */
        //B1 1週間の時間割に遷移する
        findViewById(R.id.Header_B1_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final LinearLayout parentlayout = (LinearLayout) findViewById(R.id.AyMain_parent_linearlayout);
                //overridePendingTransition(R.anim.activity_inright, R.anim.activity_outleft);

                if(!ischangeactivity) {
                    ischangeactivity = true;

                    //inflateで動的にviewを生成するテスト
                    parentlayout.setVisibility(LinearLayout.VISIBLE);

                    Animation animation = AnimationUtils.loadAnimation(activityMain, R.anim.activity_outleft);
                    animation.setDuration(250);
                    parentlayout.setAnimation(animation);
                    parentlayout.startAnimation(animation);

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Toast.makeText(activityMain, "animation ended", Toast.LENGTH_SHORT).show();
                            parentlayout.removeAllViews();
                            v2 = getLayoutInflater().inflate(R.layout.fragment_week, parentlayout);
                            childlayout = (LinearLayout) v2;

                            ListView lv = (ListView)findViewById(R.id.FrgWeek_listview);

                            String[][] value =
                                    {
                                            {"1", "卒業制作", "09:00", "10:30", "月"},
                                            {"2", "クラウドコンピューティング", "10:40", "12:10", "月"},
                                    };

                            //ListViewに現在のデータを適用
                            CustomListViewAdapter ca = new CustomListViewAdapter(v2.getContext(), value);
                            lv.setAdapter(ca);

                            ViewCompat.animate(v2)
                                    .translationX(v2.getWidth())
                                    .setDuration(1)
                                    .start();

                            ViewCompat.animate(v2)
                                    .translationX(0)
                                    .setDuration(3000)
                                    .start();

                            //animationのテスト ここでは一週間の時間割アイコンをバックアイコンに変化させている
                            final ImageButton ib = (ImageButton)findViewById(R.id.Header_B1_button);
                            ViewCompat.animate(ib)
                                    .rotationX(180)
                                    .alpha(0f)
                                    .setDuration(150)
                                    .setListener(new ViewPropertyAnimatorListenerAdapter()
                                    {
                                        @Override
                                        public void onAnimationEnd(View view)
                                        {
                                            ib.setImageResource(R.drawable.icon_arrow_back);
                                            ViewCompat.animate(view)
                                                    .alpha(1f)
                                                    .setDuration(150);
                                        }
                                    });
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                else
                {
                    ischangeactivity = false;

                    childlayout.setVisibility(LinearLayout.VISIBLE);

                    Animation animation2 = AnimationUtils.loadAnimation(v2.getContext(), R.anim.activity_outright);
                    animation2.setDuration(250);
                    childlayout.setAnimation(animation2);
                    childlayout.startAnimation(animation2);
                    animation2.setAnimationListener(new Animation.AnimationListener() {
                                                        @Override
                                                        public void onAnimationStart(Animation animation) {

                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animation animation) {
                                                            childlayout.removeAllViews();

                                                            View v3 = getLayoutInflater().inflate(R.layout.activity_main, childlayout);

                                                            ViewCompat.animate(v3)
                                                                    .translationX(0)
                                                                    .alpha(1)
                                                                    .setDuration(250)
                                                                    .start();

                                                            //animationのテスト ここでは一週間の時間割アイコンをバックアイコンに変化させている
                                                            final ImageButton ib = (ImageButton)findViewById(R.id.Header_B1_button);
                                                            ViewCompat.animate(ib)
                                                                    .rotationX(360)
                                                                    .alpha(0f)
                                                                    .setDuration(150)
                                                                    .setListener(new ViewPropertyAnimatorListenerAdapter()
                                                                    {
                                                                        @Override
                                                                        public void onAnimationEnd(View view)
                                                                        {
                                                                            ib.setImageResource(R.drawable.icon_week);
                                                                            ViewCompat.animate(view)
                                                                                    .alpha(1f)
                                                                                    .setDuration(250);
                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onAnimationRepeat(Animation animation) {

                                                        }
                                                    });
                }

//                Intent intent = new Intent(ActivityMain.this, ActivityWeek.class);
//
//                intent.putExtra("datalength", testdata.length);
//                for(int i = 0; i < testdata.length; i ++)
//                {
//                    intent.putExtra("data" + i, testdata[i]);
//                }
//
//                startActivity(intent);
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
        Network_Async task = new Network_Async(this);
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

    public void changeFragment(Fragment frg)
    {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.AyMain_container_linearlayout, frg);
        transaction.commit();
    }
}



class Network_Async extends AsyncTask<String, Void, String[][]>
{
    private Activity mainActivity;
    public ProgressDialog progressdialog;

    public Network_Async(Activity activity)
    {
        this.mainActivity = activity;
    }

    //キャンセルできないプログレスダイアログを表示する
    @Override
    protected void onPreExecute()
    {
        this.progressdialog = new ProgressDialog(this.mainActivity);
        this.progressdialog.setMessage("時間割のデータを取得しています...");
        this.progressdialog.setCancelable(false);
        this.progressdialog.show();
        return;
    }

    //非同期処理
    //...は可変長引数の意味
    @Override
    protected String[][] doInBackground(String... urlstr)
    {
        HttpURLConnection httpconnect = null;
        //返り値 時間割情報を返す
        String[][] retvalues = new String[0][];
        //JsonArrayの構文として正しいかどうか正規表現でチェックする文
        //^\{\s*"[^\s]+"\s*\:
        final String PATTERN_JSONARRAY = "^\\{\\s*\"[^\\s]+\"\\s*\\:";
        String JSON_arrayname = "data";

        try {
            //StringからURLを生成
            final URL url = new URL(urlstr[0]);
            //URL先に接続
            httpconnect = (HttpURLConnection) url.openConnection();
            httpconnect.connect();

            //接続した結果のステータスを取得
            final int httpstatus = httpconnect.getResponseCode();
            //ステータスが200 HTTP_OKの場合、JSONのparseを行う
            if (httpstatus == HttpURLConnection.HTTP_OK)
            {
                //InputStreamを取得
                InputStream inputst = httpconnect.getInputStream();
                //InputStreamReader、StringBuilderを用いて、InputStreamをStringに変換
                InputStreamReader streader = new InputStreamReader(inputst);
                StringBuilder strbuilder = new StringBuilder();

                //InputStreamReaderを読み込んで、StringBuilderに追記する
                char[] buf = new char[1024];
                int read;
                while (0 <= (read = streader.read(buf)))
                {
                    strbuilder.append(buf, 0, read);
                }
                //JSONの配列構文かどうか判断して、正しくないなら正しい構文に整形する
                if(strbuilder.toString().matches(PATTERN_JSONARRAY) == false)
                {
                    strbuilder.insert(0, "{ \"" + JSON_arrayname + "\" :");
                    strbuilder.append("}");
                }
                else
                {
                    //JSONの配列名を取得
                    String tmp = strbuilder.toString();
                    JSON_arrayname = tmp.substring(tmp.indexOf("\"") + 1, tmp.indexOf("\"", tmp.indexOf("\"") + 1));
                }

                String timetable_data = strbuilder.toString();

                try {
                    JSONObject json = new JSONObject(timetable_data);
                    //JSONの配列を取得
                    JSONArray datas = json.getJSONArray(JSON_arrayname);
                    retvalues = new String[datas.length()][4];
                    //JSONの配列の中にある値を返り値に挿入
                    for (int i = 0; i < datas.length(); i ++)
                    {
                        //配列内の値を取得
                        json = datas.getJSONObject(i);
                        //要素をStringで取得
                        String starttime = json.getString("StartDateTime");
                        String endtime = json.getString("EndDateTime");
                        //HH:mmだけ取得できるよう加工する
                        String convert_starttime = starttime.substring(starttime.lastIndexOf('T') + 1, starttime.lastIndexOf('T') + 6);
                        String convert_endtime = endtime.substring(endtime.lastIndexOf('T') + 1, endtime.lastIndexOf('T') + 6);
                        retvalues[i] = new String[]{ json.getString("Id"), json.getString("Title"), convert_starttime, convert_endtime };
                    }

                    //昇順でソートを行う
                    for (int i = 0; i < retvalues.length; i ++)
                    {
                        Arrays.sort(retvalues, new Comparator<String[]>()
                        {
                            @Override
                            public int compare(String[] o1, String[] o2)
                            {
                                //StartDateTimeが同じだった場合、Idを基準としてソートする
                                if(o1[2].compareTo(o2[2]) == 0)
                                {
                                    //Stringで比較するとUnicodeでソートしてしまうため、Integerで比較する
                                    int tmp1 = Integer.parseInt(o1[0]);
                                    int tmp2 = Integer.parseInt(o2[0]);

                                    if(tmp1 > tmp2)
                                        return 1;
                                    else if(tmp1 < tmp2)
                                        return -1;
                                    else
                                        return 0;
                                }
                                else
                                    return o1[2].compareTo(o2[2]);
                            }
                        });
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                streader.close();
                inputst.close();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (httpconnect != null)
            {
                httpconnect.disconnect();
            }
        }

        return retvalues;
    }

    //doInBackgroundの処理が終わった後に実行される
    //取得した時間割データを自作アダプタに渡し、ListViewで表示
    @Override
    protected void onPostExecute(String[][] values) {
        ListView timetable_lv = (ListView)this.mainActivity.findViewById(R.id.AyMain_timetable_listview);

        CustomListViewAdapter adapter = new CustomListViewAdapter(this.mainActivity, values);
        timetable_lv.setAdapter(adapter);

        //プログレスダイアログを閉じる
        if(this.progressdialog != null && this.progressdialog.isShowing())
        {
            this.progressdialog.dismiss();
        }
    }
}