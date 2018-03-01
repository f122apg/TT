package com.tetsujin.tt.task;

import android.content.Context;
import android.os.AsyncTask;

import com.tetsujin.tt.R;
import com.tetsujin.tt.login.WebAPIInfo;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class TaskLogin extends AsyncTask<Void, Void, String>
{
    private Context context;
    private final String email;
    private final String password;
    private Callback callback;

    public interface Callback
    {
        void onTaskFinished(String result);
    }

    public TaskLogin(Context context, String email, String password, Callback callback)
    {
        this.context = context;
        this.email = email;
        this.password = password;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... param)
    {
        return getToken(email, password);
    }

    @Override
    protected void onPostExecute(String json)
    {
        super.onPostExecute(json);
        callback.onTaskFinished(json);
    }

    //メールアドレスとパスワードを指定のURLに投げて、トークンを取得する
    private String getToken(String Email, String Password)
    {
        HttpURLConnection con = null;
        String urlStr = WebAPIInfo.GET_TOKEN_URL;
        //POSTするデータを作成
        String postData = WebAPIInfo.createPostData(Email, Password);
    
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
            con.setRequestProperty("Content-Length", String.valueOf(postData.length()));
            //リクエストボディにPOSTするデータを書き込む
            OutputStreamWriter requestBody = new OutputStreamWriter(con.getOutputStream());
            requestBody.write(postData);
            requestBody.flush();
            System.out.println("connecting...");
            //URL先に接続
            con.connect();
            
            //レスポンスボディを受け取る
            String result = WebAPIInfo.getResponseBody(context, con);
            JSONObject json = new JSONObject(result);
            
            //jsonがトークンを持っていたらトークンを抜き取る作業をする
            if(json.has(WebAPIInfo.JSON_ACCESS_TOKEN))
                return json.getString(WebAPIInfo.JSON_ACCESS_TOKEN);
            //認証が正しく行かない場合、サーバーから渡されたエラーメッセージをそのまま返す
            else if(json.has(WebAPIInfo.JSON_ERROR_DESCRIPTION))
                return result;
            //jsonが正常でない場合、固定のエラーメッセージを返す
            else
                return WebAPIInfo.createErrorMessage(context, context.getString(R.string.error_something));
        }
        //ネットワークが不安定または、できないときに発生
        catch (UnknownHostException unknownHostEx)
        {
            unknownHostEx.printStackTrace();
            return WebAPIInfo.createErrorMessage(context, context.getString(R.string.error_cannot_connected_network));
        }
        //何かのエラー時、固定のエラーメッセージを返す
        catch (Exception e)
        {
            e.printStackTrace();
            return WebAPIInfo.createErrorMessage(context, context.getString(R.string.error_something));
        }
        finally
        {
            con.disconnect();
        }
    }
}
