package com.tetsujin.tt.task;

import android.os.AsyncTask;
import android.util.Log;

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

public class TaskLogin extends AsyncTask<String, Void, String>
{
    @Override
    protected String doInBackground(String... userparam)
    {
        //[0] = username, [1] = password
        return getToken(userparam[0], userparam[1]);
    }


    private String getToken(String username, String password)
    {
        HttpURLConnection con = null;
        String urlStr = "https:/tetsujintimes.azurewebsites.net/token";
        StringBuilder postDataBuilder = new StringBuilder();

        //POSTするデータを作成
        postDataBuilder.append("grant_type=password&");
        postDataBuilder.append("UserName=" + username + "&");
        postDataBuilder.append("Password=" + password);

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

                //jsonの読み込み
                JSONObject json = new JSONObject(jsonStr);
                //トークンを取得する
                return json.getString("access_token");
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
