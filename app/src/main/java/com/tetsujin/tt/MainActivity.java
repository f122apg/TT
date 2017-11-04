package com.tetsujin.tt;

//Android
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;
import android.text.format.DateFormat;

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

public class MainActivity extends AppCompatActivity {

    String timetabledata_url = "http://tetsujin.azurewebsites.net/api/schedules";
    private static String[][] testdata = new String[0][];

    public static MainActivity mainActivity;
    public static Boolean isVisible = false;
    private static final String TAG = "MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    public void ToastNotify(final String notificationMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, notificationMessage, Toast.LENGTH_LONG).show();
                TextView helloText = (TextView) findViewById(R.id.text_hello);
                helloText.setText(notificationMessage);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
        //ハンドルをセット
        NotificationsManager.handleNotifications(this, NotificationSettings.SenderId, NotificationHandler.class);
        //Notification Hubsにこの端末の登録作業を行う
        registerWithNotificationHubs();

        //日付取得と表示
        TextView date_tv = (TextView)findViewById(R.id.date_textview);
        //現在の日付を取得
        Date nowdate = new Date();
        //Calendarに現在の日付を設定
        Calendar cal = Calendar.getInstance();
        //現在の曜日のみを取得
        CharSequence week = DateFormat.format("E", nowdate);
        //現在の曜日が「土」か「日」だったら次週の月曜日にする
        if(week.equals(getResources().getString(R.string.week_saturday))
                || week.equals(getResources().getString(R.string.week_sunday)))
        {
            //現在の日付を２日足し、今週の月曜日にする
            cal.add(Calendar.DAY_OF_MONTH, 2);
            cal.set(Calendar.DAY_OF_WEEK, 2);
        }

        date_tv.setText(DateFormat.format("MM/dd(E)", cal.getTime()));

        ListView timetable_lv = (ListView)findViewById(R.id.timetable_listview);

        //ScrollView上に設置されたListViewをスクロールさせるようにする
        timetable_lv.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        testdata = new String[][]
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
        CustomListViewAdapter ca = new CustomListViewAdapter(this, testdata);
        timetable_lv.setAdapter(ca);

        /* onClickListeners */
        //B1 1週間の時間割に遷移する
        findViewById(R.id.B1_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, WeekActivity.class);

                intent.putExtra("datalength", testdata.length);
                for(int i = 0; i < testdata.length; i ++)
                {
                    intent.putExtra("data" + i, testdata[i]);
                }

                startActivity(intent);
            }
        });
        //B2 GetTimeTableの動作確認用 確認できたら削除する
        findViewById(R.id.B2_button).setOnClickListener(new View.OnClickListener()
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
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported by Google Play Services.");
                ToastNotify("This device is not supported by Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }

    //Google Play Servicesが利用可能ならば、FCMにこの端末を登録する処理を行うintentを開始
    public void registerWithNotificationHubs()
    {
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
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
        ListView timetable_lv = (ListView)this.mainActivity.findViewById(R.id.timetable_listview);

        CustomListViewAdapter adapter = new CustomListViewAdapter(this.mainActivity, values);
        timetable_lv.setAdapter(adapter);

        //プログレスダイアログを閉じる
        if(this.progressdialog != null && this.progressdialog.isShowing())
        {
            this.progressdialog.dismiss();
        }
    }
}