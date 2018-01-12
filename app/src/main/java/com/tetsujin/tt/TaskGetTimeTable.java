package com.tetsujin.tt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import com.tetsujin.tt.adapter.CustomListViewAdapter;
import com.tetsujin.tt.database.TimeTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/***********************************************************************/
//時間割データをネットワーク経由で取得し、ListViewアダプタにセットする非同期のクラス
/***********************************************************************/

//引数の意味
//String = Activityからスレッドへ渡す変数の型
//    Activityからスレッドを呼び出すexecuteの引数
//    doInBackgroundの引数
//Void = 進捗度合を表示するときに利用する型
//    onProgressUpdateの引数
//TimeTable[] = バックグラウンド処理完了時に受け取る型
//    doInBackgroundの戻り値
//    onPostExecuteの引数
public class TaskGetTimeTable extends AsyncTask<String, Void, TimeTable[]>
{
    private Activity activityMain;
    public ProgressDialog progressdialog;

    public TaskGetTimeTable(Activity activity)
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
    }

    //非同期処理
    //...は可変長引数の意味
    @Override
    protected TimeTable[] doInBackground(String... urlStr)
    {
        HttpURLConnection httpConnect = null;
        //返り値 時間割情報を返す
        TimeTable[] retValues = new TimeTable[0];
        //JsonArrayの構文として正しいかどうか正規表現でチェックする文
        final String PATTERN_JSONARRAY = "^\\{\\s*\"\\S+\"\\s*:";
        String json_ArrayName = "data";

        try {
            //StringからURLを生成
            final URL url = new URL(urlStr[0]);
            //URL先に接続
            httpConnect = (HttpURLConnection) url.openConnection();
            httpConnect.connect();

            //接続した結果のステータスを取得
            final int httpStatus = httpConnect.getResponseCode();
            //ステータスが200 HTTP_OKの場合、JSONのparseを行う
            if (httpStatus == HttpURLConnection.HTTP_OK)
            {
                //InputStreamを取得
                InputStream inputSt = httpConnect.getInputStream();
                //InputStreamReader、StringBuilderを用いて、InputStreamをStringに変換
                InputStreamReader stReader = new InputStreamReader(inputSt);
                StringBuilder strBuilder = new StringBuilder();

                //InputStreamReaderを読み込んで、StringBuilderに追記する
                char[] buf = new char[1024];
                int read;
                while (0 <= (read = stReader.read(buf)))
                {
                    strBuilder.append(buf, 0, read);
                }
                
                //JSONの配列構文かどうか判断して、正しくないなら正しい構文に整形する
                if(!strBuilder.toString().matches(PATTERN_JSONARRAY))
                {
                    strBuilder.insert(0, "{ \"" + json_ArrayName + "\" :");
                    strBuilder.append("}");
                }
                else
                {
                    //JSONの配列名を取得
                    String array = strBuilder.toString();
                    json_ArrayName = array.substring(array.indexOf("\"") + 1, array.indexOf("\"", array.indexOf("\"") + 1));
                }

                String timeTableJson = strBuilder.toString();

                try {
                    JSONObject json = new JSONObject(timeTableJson);
                    //JSONの配列を取得
                    JSONArray datas = json.getJSONArray(json_ArrayName);
                    retValues = new TimeTable[datas.length()];
                    //JSONの配列の中にある値を返り値に挿入
                    for (int i = 0; i < datas.length(); i ++)
                    {
                        //配列内の値を取得
                        json = datas.getJSONObject(i);
                        System.out.println("get");

                        int timeTableId = json.getInt("TimeTableId");
                        String lessonCode = json.getString("LessonCode");
                        String lessonName = json.getString("LessonName");
                        int weekDay = json.getInt("WeekDay");
                        String startTime = json.getString("StartTime");
                        String endTime = json.getString("EndTime");
                        int season = 0;
                        String description = json.getString("Description");
                        String classRoomName = json.getString("ClassRoomName");
                        int teacherId = json.getInt("TeacherId");
                        String teacherName = json.getString("TeacherName");

                        retValues[i] = new TimeTable(timeTableId, lessonCode, lessonName, weekDay,
                                startTime, endTime, season, classRoomName,
                                teacherId, teacherName, description);
                        
                        //TODO:jsonの読み込み処理を書く
//                        //要素をStringで取得
//                        String starttime = json.getString("StartDateTime");
//                        String endtime = json.getString("EndDateTime");
//                        //HH:mmだけ取得できるよう加工する
//                        String convert_starttime = starttime.substring(starttime.lastIndexOf('T') + 1, starttime.lastIndexOf('T') + 6);
//                        String convert_endtime = endtime.substring(endtime.lastIndexOf('T') + 1, endtime.lastIndexOf('T') + 6);
//                        retValues[i] = new String[]{ json.getString("Id"), json.getString("Title"), convert_starttime, convert_endtime };
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                stReader.close();
                inputSt.close();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (httpConnect != null)
            {
                httpConnect.disconnect();
            }
        }

        return retValues;
    }

    //doInBackgroundの処理が終わった後に実行される
    //取得した時間割データを自作アダプタに渡し、ListViewで表示
    @Override
    protected void onPostExecute(TimeTable[] values) {
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