package com.tetsujin.tt.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import com.tetsujin.tt.R;
import com.tetsujin.tt.adapter.LessonListAdapter;
import com.tetsujin.tt.database.TimeTable;
import com.tetsujin.tt.database.TimeTableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import static com.tetsujin.tt.ActivityMain.timeTableDB;

/***********************************************************************/
//トークンを用いて、Jsonを取得しそのまま返す非同期クラス
/***********************************************************************/

//引数の意味
//param1 = Activityからスレッドへ渡す変数の型
//    Activityからスレッドを呼び出すexecuteの引数
//    doInBackgroundの引数
//param2 = 進捗度合を表示するときに利用する型
//    onProgressUpdateの引数
//param3 = バックグラウンド処理完了時に受け取る型
//    doInBackgroundの戻り値
//    onPostExecuteの引数
//                                              param1 param2 param3
public class TaskGetTimeTable extends AsyncTask<Void, Void, String>
{
    private final JSONObject json;

    TaskGetTimeTable(JSONObject Json)
    {
        json = Json;
    }

    //非同期処理
    //...は可変長引数の意味
    @Override
    protected String doInBackground(Void... param)
    {
        //トークンを用いて、特定ユーザーのJsonを取得
        return getTimeTableJson(json);
    }

    @Override
    protected void onPostExecute(String result) {
    }

    private String getTimeTableJson(JSONObject json)
    {
        try
        {
            //トークンを取得
            final String token = json.getString("access_token");

            HttpURLConnection con = null;
            String urlStr = "https:/tetsujintimes.azurewebsites.net/api/schedules";

            //リクエストするデータを準備する
            String requestHeaderName = "Authorization";
            String requestHeaderValue = "Bearer " + token;

            URL url = new URL(urlStr);

            //トークンを用いて認証を始める
            con = (HttpURLConnection) url.openConnection();
            //GET形式でリクエストを投げる
            con.setRequestMethod("GET");
            //リクエストヘッダにAuthorization：トークンを追加
            con.setRequestProperty(requestHeaderName, requestHeaderValue);
            //URL先に接続
            con.connect();

            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK)
            {
                //InputStreamを取得
                InputStream streamInput = con.getInputStream();
                //InputStreamReader、StringBuilderを用いて、InputStreamをStringに変換
                InputStreamReader streamReader = new InputStreamReader(streamInput);
                StringBuilder strBuilder = new StringBuilder();

                //InputStreamReaderを読み込んで、StringBuilderに追記する
                char[] buf = new char[1024];
                int read;
                while (0 <= (read = streamReader.read(buf)))
                {
                    strBuilder.append(buf, 0, read);
                }

                String jsonStr = strBuilder.toString();
                streamReader.close();
                streamInput.close();

                return jsonStr;
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }
}