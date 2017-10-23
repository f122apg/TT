package com.tetsujin.tt;

//Android imports
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.os.AsyncTask;

//Java imports
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

//Other imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String timetabledata_url = "http://tetsujin.azurewebsites.net/api/schedules";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //日付取得と表示
        TextView date_tv = (TextView)findViewById(R.id.date_textview);
        date_tv.setText(android.text.format.DateFormat. format("MM/dd(E)", Calendar.getInstance()));

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


        /* onClickListeners */
        //B1 1週間の時間割に遷移する
        findViewById(R.id.B1_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, WeekActivity.class);
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

    public void GetTimeTable()
    {
        Network_Async task = new Network_Async(this);
        task.execute(timetabledata_url);
    }
}

class Network_Async extends AsyncTask<String, Void, String[][]>
{
    private Activity mainActivity;

    public Network_Async(Activity activity)
    {
        this.mainActivity = activity;
    }

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
                    retvalues = new String[datas.length()][3];
                    //JSONの配列の中にある値を返り値に挿入
                    for (int i = 0; i < datas.length(); i ++)
                    {
                        //配列内の値を取得
                        json = datas.getJSONObject(i);
                        //要素：StartDateTimeをStringで取得
                        String time = json.getString("StartDateTime");
                        //StartTimeのHH:mmだけ取得できるよう加工する
                        String convert_time = time.substring(time.lastIndexOf('T') + 1, time.lastIndexOf('T') + 6);
                        retvalues[i] = new String[]{ json.getString("Id"), json.getString("Title"), convert_time };
                    }

                    //昇順でソートを行う
                    for (int i = 0; i < retvalues.length; i ++)
                    {
                        Arrays.sort(retvalues, new Comparator<String[]>()
                        {
                            @Override
                            public int compare(String[] o1, String[] o2)
                            {
                                //時間が同じだった場合、Idを基準としてソートする
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

    @Override
    protected void onPostExecute(String[][] values) {
        ListView timetable_lv = (ListView)this.mainActivity.findViewById(R.id.timetable_listview);

        CustomAdapter adapter = new CustomAdapter(this.mainActivity, values);
        timetable_lv.setAdapter(adapter);
    }
}