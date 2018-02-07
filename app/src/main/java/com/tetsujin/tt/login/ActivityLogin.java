package com.tetsujin.tt.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.tetsujin.tt.R;
import com.tetsujin.tt.database.Login;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ActivityLogin extends AppCompatActivity
{
    private MobileServiceClient mClient;
    private String loginId;
    private Activity activitylogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activitylogin = this;

        final ActivityLogin activityLogin = this;
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        try
        {
            mClient = new MobileServiceClient("https://tetsujin-tt.azurewebsites.net", this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        //ログインボタンのクリックイベント
        findViewById(R.id.AyLogin_login_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final EditText studentId = (EditText) findViewById(R.id.AyLogin_studentid_edittext);
                EditText password = (EditText) findViewById(R.id.AyLogin_password_edittext);
                final TextView notice = (TextView) findViewById(R.id.AyLogin_notice_textview);

                //入力チェック
                if (studentId.getText().toString().isEmpty() && password.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_all);
                    return;
                } else if (studentId.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_number);
                    return;
                } else if (password.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_password);
                    return;
                }

//                //入力欄が空ではないならログイン処理を実行
//                final ProgressDialog progressDialog = new ProgressDialog(activityLogin);
//                progressDialog.setCancelable(false);
//                progressDialog.setMessage(getResources().getString(R.string.logging_in));
//                progressDialog.show();

                AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
                {
                    @Override
                    protected String doInBackground(Void... voids)
                    {
                        login();
                        return null;
                    }
                };

                task.execute();
            }
        });
    }

    private void login()
    {
        HttpURLConnection con = null;
        String urlstr = "https:/tetsujintimes.azurewebsites.net/token";
        StringBuilder postDataBuilder = new StringBuilder();
        EditText mailaddressEd = (EditText) findViewById(R.id.AyLogin_studentid_edittext);
        EditText passwordEd = (EditText) findViewById(R.id.AyLogin_password_edittext);
        String mailaddress = mailaddressEd.getText().toString();
        String password = passwordEd.getText().toString();
        String token = "";

        //POSTするデータを作成
        postDataBuilder.append("grant_type=password&");
        postDataBuilder.append("UserName=taro@it-neec.jp&");
        postDataBuilder.append("Password=taro@it-neec.jp");

        try
        {
            URL url = new URL(urlstr);

            //トークンを取得するために、メールアドレスとパスワードを投げる
            con = (HttpURLConnection)url.openConnection();
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
            Log.d("NETWORK_TT", "接続...");
            //URL先に接続
            con.connect();

            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK)
            {
                //InputStreamを取得
                InputStream inputSt = con.getInputStream();
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

                String strJson = strBuilder.toString();

                Log.d("NETWORK_TT", "result");
                Log.d("NETWORK_TT", strBuilder.toString());

                JSONObject json = new JSONObject(strJson);
                token = json.getString("access_token");
                String data = "Bearer " + token;

                url = new URL("https:/tetsujintimes.azurewebsites.net/api/schedules");

                //トークンを取得するために、メールアドレスとパスワードを投げる
                con = (HttpURLConnection)url.openConnection();
                //GET形式でリクエストを投げる
                con.setRequestMethod("GET");
                //フォームエンコード形式に設定
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //POSTするデータの長さを設定
                con.setRequestProperty("Content-Length", String.valueOf(data.length()));
                //リクエストボディにPOSTするデータを書き込む
                con.setRequestProperty("Authorization", "Bearer " + token);
                Log.d("NETWORK_TT", "Bearer " + token);
                Log.d("NETWORK_TT", "接続...");
                //URL先に接続
                con.connect();

                status = con.getResponseCode();
                if (status == HttpURLConnection.HTTP_OK)
                {
                    Log.d("NETWORK_TT", "HTTP_OK");
                    //InputStreamを取得
                    inputSt = con.getInputStream();
                    //InputStreamReader、StringBuilderを用いて、InputStreamをStringに変換
                    stReader = new InputStreamReader(inputSt);
                    strBuilder = new StringBuilder();

                    //InputStreamReaderを読み込んで、StringBuilderに追記する
                    buf = new char[1024];
                    while (0 <= (read = stReader.read(buf)))
                    {
                        strBuilder.append(buf, 0, read);
                    }

                    Log.d("NETWORK_TT", strBuilder.toString());
                }
                else
                    Log.d("NETWORK_TT", "Bad ResCode:" + con.getResponseCode() + "\nResM:" + con.getResponseMessage());

                stReader.close();
                inputSt.close();
            }
            else
                Log.d("NETWORK_TT", "Bad ResCode:" + con.getResponseCode() + "\nResM:" + con.getResponseMessage());
        }
        catch (Exception e)
        {
            Log.d("NETWORK_TT", "Exception");
            Log.d("NETWORK_TT", e.getMessage());
        }
    }
}