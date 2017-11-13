package com.tetsujin.tt;

//Android

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.tetsujin.tt.adapter.CustomListViewAdapter;
import com.tetsujin.tt.notification.NotificationHandler;
import com.tetsujin.tt.notification.NotificationSettings;
import com.tetsujin.tt.notification.RegistrationIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

//Java
//JSON
//FCM & Azure Notification Hubs

public class ActivityMain extends AppCompatActivity {

    String timetabledata_url = "http://tetsujin.azurewebsites.net/api/schedules";
    private static String[][] testdata = new String[0][];

    public static ActivityMain activityMain;
    private boolean isChangeFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMain = this;
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

    //Fragmentを置き換えて表示する
    public void showFragment(Fragment frg)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.activity_inright, R.anim.activity_outleft, R.anim.activity_inleft, R.anim.activity_outright);
        transaction.replace(R.id.AyMain_container_linearlayout, frg);
        transaction.commit();
    }
}


/****************************************************/
//時間割データを取得する非同期クラス
//***************************************************/
class Network_Async extends AsyncTask<String, Void, String[][]>
{
    private Activity activityMain;
    public ProgressDialog progressdialog;

    public Network_Async(Activity activity)
    {
        this.activityMain = activity;
    }

    //キャンセルできないプログレスダイアログを表示する
    @Override
    protected void onPreExecute()
    {
        this.progressdialog = new ProgressDialog(this.activityMain);
        this.progressdialog.setMessage(activityMain.getResources().getString(R.string.getdata));
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
        ListView timetable_lv = (ListView)this.activityMain.findViewById(R.id.AyMain_timetable_listview);

        CustomListViewAdapter adapter = new CustomListViewAdapter(this.activityMain, values);
        timetable_lv.setAdapter(adapter);

        //プログレスダイアログを閉じる
        if(this.progressdialog != null && this.progressdialog.isShowing())
        {
            this.progressdialog.dismiss();
        }
    }
}