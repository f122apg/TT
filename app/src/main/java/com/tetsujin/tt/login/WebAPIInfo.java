package com.tetsujin.tt.login;

import android.content.Context;

import com.tetsujin.tt.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class WebAPIInfo
{
    //トークンが取得できるURL
    //ここにPOSTで以下のcreatePostDataを使用してPOSTデータを作成する。
    //その後、Content-Typeをapplication/x-www-form-urlencodedに指定してRequestBodyに添付後送信。
    //認証されればトークンとトークンの期限が返ってくる
    final public static String GET_TOKEN_URL = "https://tetsujintimes.azurewebsites.net/token";
    //特定ユーザーの時間割が取得できるURL
    //ここにGETでリクエストヘッダ
    final public static String GET_TIMETABLE_URL = "https://tetsujintimes.azurewebsites.net/api/schedules";
    //POSTデータのユーザー名とパスワードを後で実際のデータに置き換えるためのマーカーの役割
    final private static String[] PLEASE_REPLACE_POST_DATA = new String[]{ "[REPLACE_HERE_YOURE_EMAIL]", "[REPLACE_HERE_YOURE_PASSWORD]" };
    //POSTデータの元 トークンを取得する過程で使用する
    final private static String POST_DATA_SOURCE = "grant_type=password&" +
            "UserName=" + PLEASE_REPLACE_POST_DATA[0] + "&" +
            "Password=" + PLEASE_REPLACE_POST_DATA[1];
    
    //RequestHeaderのトークンを後で実際のデータに置き換えるためのマーカーの役割
    final private static String PLEASE_REPLACE_REQUEST_HEADER = "[REPLACE_HERE_YOURE_TOKEN]";
    //RequestHeaderの元 時間割取得時に使用
    final public static String REQUEST_HEADER_NAME = "Authorization";
    final private static String REQUEST_HEADER_VALUE_SOURCE = "Bearer " + PLEASE_REPLACE_REQUEST_HEADER;
    
    //トークン取得時のJsonの要素
    //この要素にトークンが存在している
    final public static String JSON_ACCESS_TOKEN = "access_token";
    //エラー発生時に出現する要素
    //エラーメッセージを格納している
    final public static String JSON_ERROR_DESCRIPTION = "error_description";
    
    //POSTするデータを作成
    public static String createPostData(String userName, String password)
    {
        return POST_DATA_SOURCE.replace(PLEASE_REPLACE_POST_DATA[0], userName).replace(PLEASE_REPLACE_POST_DATA[1], password);
    }
    
    //RequestHeaderの値を生成
    public static String createRequestHeaderData(String token)
    {
        return REQUEST_HEADER_VALUE_SOURCE.replace(PLEASE_REPLACE_REQUEST_HEADER, token);
    }
    
    //レスポンスボディを取得する
    public static String getResponseBody(Context context, HttpURLConnection httpURLConnection)
    {
        try
        {
            final int status = httpURLConnection.getResponseCode();
            
            InputStream streamInput = null;
            //正常なレスポンスコードが返ってきた時、レスポンスボディを受け取る
            if(status == HttpURLConnection.HTTP_OK) //200
            {
                streamInput = httpURLConnection.getInputStream();
            }
            //異常なレスポンスコードが返ってきた時(メールアドレスまたはパスワードの間違い)、エラー時のレスポンスボディを受け取る
            else if(status == HttpURLConnection.HTTP_BAD_REQUEST) //400
            {
                streamInput = httpURLConnection.getErrorStream();
            }
            //サーバーに異常があるとき(例えば負荷がかかっている時)、固定のエラーメッセージを返す
            else if(status == HttpURLConnection.HTTP_GATEWAY_TIMEOUT ||//504
                    status == HttpURLConnection.HTTP_UNAVAILABLE ||    //503
                    status == HttpURLConnection.HTTP_BAD_GATEWAY ||    //502
                    status == HttpURLConnection.HTTP_INTERNAL_ERROR)   //500
            {
                return WebAPIInfo.createErrorMessage(context, context.getString(R.string.error_server_unavailable));
            }
            
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
            
            String responseBody = strBuilder.toString();
            streamReader.close();
            streamInput.close();
            
            return responseBody;
        }
        catch (Exception e)
        {
            return createErrorMessage(context, context.getString(R.string.error_something));
        }
    }
    
    //json形式のエラーメッセージを作成する
    public static String createErrorMessage(Context context, String text)
    {
        return context.getString(R.string.error_json_template).replace(context.getString(R.string.error_json_template_replace_text), text);
    }
}
