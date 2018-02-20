package com.tetsujin.tt.task;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.tetsujin.tt.database.TimeTable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

public class TaskLogin extends AsyncTask<Void, Void, JSONObject>
{
    private final String email;
    private final String password;

    public interface TaskLoginCallbacks
    {
        void onTaskFinished();
        void onTaskCancelled();
    }

    TaskLogin(String Email, String Password)
    {
        email = Email;
        password = Password;
    }

    @Override
    protected JSONObject doInBackground(Void... param)
    {
        return getToken(email, password);
    }

    @Override
    protected void onPostExecute(JSONObject json)
    {
        json.
    }

    //メールアドレスとパスワードを指定のURLに投げて、トークンを取得する
    private JSONObject getToken(String Email, String Password)
    {
        HttpURLConnection con = null;
        String urlStr = "https:/tetsujintimes.azurewebsites.net/token";
        StringBuilder postDataBuilder = new StringBuilder();

        //POSTするデータを作成
        postDataBuilder.append("grant_type=password&");
        postDataBuilder.append("UserName=" + Email + "&");
        postDataBuilder.append("Password=" + Password);

        try
        {
            URL url = new URL(urlStr);

            //トークンを取得するために、メールアドレスとパスワードを投げる
            con = (HttpURLConnection) url.openConnection();
            //POST形式でリクエストを投げる
            con.setRequestMethod("POST");
            //POSTで投げるときは必ずtrueにする
            con.setDoOutput(true);
            //フォームエンコード形式に設定
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //POSTするデータの長さを設定
            con.setRequestProperty("Content-Length", String.valueOf(postDataBuilder.length()));
            //リクエストボディにPOSTするデータを書き込む
            OutputStreamWriter requestBody = new OutputStreamWriter(con.getOutputStream());
            requestBody.write(postDataBuilder.toString());
            requestBody.flush();
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

                //取得したjsonを返す
                return new JSONObject(jsonStr);
            }
            else
            {
                //TODO:エラーメッセージを明確にする
                return new JSONObject("{ error : \"ネットワークエラーです。\" }");
            }
        }
        catch(Exception e)
        {
            //TODO:エラーメッセージを明確にする
            e.printStackTrace();
            return null;
        }
    }
}
