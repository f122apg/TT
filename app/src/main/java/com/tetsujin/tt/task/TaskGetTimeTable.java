package com.tetsujin.tt.task;

import android.content.Context;
import android.os.AsyncTask;

import com.tetsujin.tt.R;
import com.tetsujin.tt.login.WebAPIInfo;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

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
    private Context context;
    private final String token;
    private Callback callback;
    
    public interface Callback
    {
        void onTaskFinished(String result);
    }

    public TaskGetTimeTable(Context context, String token, Callback callback)
    {
        this.context = context;
        this.token = token;
        this.callback = callback;
    }

    //非同期処理
    //...は可変長引数の意味
    @Override
    protected String doInBackground(Void... param)
    {
        //トークンを用いて、特定ユーザーのJsonを取得
        return getTimeTableJson(token);
    }
    
    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        callback.onTaskFinished(result);
    }

    private String getTimeTableJson(String token)
    {
        HttpURLConnection con = null;
        
        try
        {
            String urlStr = WebAPIInfo.GET_TIMETABLE_URL;

            URL url = new URL(urlStr);

            //トークンを用いて認証を始める
            con = (HttpURLConnection) url.openConnection();
            //GET形式でリクエストを投げる
            con.setRequestMethod("GET");
            //リクエストヘッダに「Authorization：トークン」を追加
            con.setRequestProperty(WebAPIInfo.REQUEST_HEADER_NAME, WebAPIInfo.createRequestHeaderData(token));
            //URL先に接続
            con.connect();
    
            //レスポンスボディを受け取る
            String result = WebAPIInfo.getResponseBody(context, con);
            JSONObject json = new JSONObject(result);
            //jsonにエラーがなければ時間割を含んだjsonを返す
            if(!json.has(WebAPIInfo.JSON_ERROR_DESCRIPTION))
                return result;
            else
                return WebAPIInfo.createErrorMessage(context, context.getString(R.string.error_something));
        }
        //ネットワークが不安定または、できないときに発生
        catch (UnknownHostException unknownHostEx)
        {
            unknownHostEx.printStackTrace();
            return WebAPIInfo.createErrorMessage(context, context.getString(R.string.error_cannot_connected_network));
        }
        //何かのエラー時
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            con.disconnect();
        }
    }
}