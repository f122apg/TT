package com.tetsujin.tt.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tetsujin.tt.ActivityMain;
import com.tetsujin.tt.R;
import com.tetsujin.tt.database.TimeTable;
import com.tetsujin.tt.database.TimeTableHelper;
import com.tetsujin.tt.task.TaskGetTimeTable;
import com.tetsujin.tt.task.TaskLogin;

import org.json.JSONObject;

public class ActivityLogin extends AppCompatActivity
{
    private SQLiteDatabase timeTableDB;
    private TimeTableHelper timeTableHelper;
    public ActivityLogin activityLogin;
    Thread thread;
    ProgressDialog pdialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activityLogin = this;
        timeTableHelper = TimeTableHelper.getInstance(activityLogin);
        timeTableDB = timeTableHelper.getWritableDatabase();
        
        final EditText email = (EditText) findViewById(R.id.AyLogin_email_edittext);
        final EditText password = (EditText) findViewById(R.id.AyLogin_password_edittext);
        //TODO:RELEASE時にSetTextを消す
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        /* RELASE時に消して！！！！！！！！！！！！！！！！！！！！！！！！！！！！ */
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        /* DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG */
        email.setText("taro@it-neec.jp");
        password.setText("taro@it-neec.jp");
        
        //ログインボタンのクリックイベント
        findViewById(R.id.AyLogin_login_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                
                final TextView notice = (TextView) findViewById(R.id.AyLogin_notice_textview);

                //入力チェック チェックが済んだらログイン処理を実行
                if (email.getText().toString().isEmpty() &&
                        password.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_all);
                    return;
                } else if (email.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_email);
                    return;
                } else if (password.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_password);
                    return;
                }

                //ネットワークに接続されているかチェックする
                //接続されていなかったらエラーメッセージを表示し、処理を中断
                if(!networkCheck(activityLogin))
                {
                    notice.setText(R.string.error_cannot_connected_network);
                    return;
                }
                
                //ログイン中のダイアログを表示
                pdialog = new ProgressDialog(activityLogin);
                pdialog.setMessage(getResources().getString(R.string.logging_in));
                pdialog.setCancelable(false);
                pdialog.show();
    
                /* ログイン処理 */
                //TaskLoginのコールバックを設定
                TaskLogin taskLogin = new TaskLogin(activityLogin, email.getText().toString(), password.getText().toString(), new TaskLogin.Callback()
                {
                    //TaskLoginのコールバックを設定
                    @Override
                    public void onTaskFinished(String result)
                    {
                        //Task終了時の結果が正常ならば時間割取得処理を開始
                        if(!result.contains(WebAPIInfo.JSON_ERROR_DESCRIPTION))
                        {
                            //TaskGetTimeTableのコールバックを設定
                            TaskGetTimeTable taskGetTimeTable = new TaskGetTimeTable(activityLogin, result, new TaskGetTimeTable.Callback()
                            {
                                @Override
                                public void onTaskFinished(String result)
                                {
                                    //時間割を正しく取得できたら、データベースに時間割データを挿入する
                                    if(!result.contains(WebAPIInfo.JSON_ERROR_DESCRIPTION))
                                        TimeTable.timeTableJsonParser(timeTableDB, timeTableHelper, result);
                                    //正しく取得できなかったら、エラーメッセージを表示
                                    else
                                    {
                                        pdialog.dismiss();
                                        notice.setText(getErrorMessage(result));
                                        return;
                                    }
    
                                    pdialog.dismiss();
                                    //メイン画面に遷移
                                    Intent intent = new Intent(activityLogin, ActivityMain.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            
                            //時間割取得タスクを開始
                            taskGetTimeTable.execute();
                        }
                        //異常ならばエラーメッセージを表示し、Taskを終了する
                        else
                        {
                            pdialog.dismiss();
                            //エラーメッセージをTextViewに設定
                            notice.setText(getErrorMessage(result));
                        }
                    }
                });
                
                //ログインタスクを開始
                taskLogin.execute();
            }
        });
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(hasFocus)
        {
            //RootであるLinearLayoutの幅を取得して、それを元にEditTextの幅を設定する
            LinearLayout ll = (LinearLayout) findViewById(R.id.AyLogin_parent_linearlayout);
            EditText email = (EditText) findViewById(R.id.AyLogin_email_edittext);
            EditText password = (EditText) findViewById(R.id.AyLogin_password_edittext);
            
            //LinearLayoutを5で割った値をLinearLayoutのサイズで引いて設定する
            email.setWidth(ll.getWidth() - ll.getWidth() / 5);
            password.setWidth(ll.getWidth() - ll.getWidth() / 5);
        }
    }
    
    //Wi-FiまたはモバイルネットワークやWiMAXに接続されているかチェックを行う
    private boolean networkCheck(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }
    
    //json内のエラーメッセージを取得する
    private String getErrorMessage(String jsonStr)
    {
        try
        {
            //エラーメッセージを取得
            JSONObject json = new JSONObject(jsonStr);
            return json.getString("error_description");
        }
        //エラーメッセージを正しく取得できなかったら、固定のエラーメッセージを表示
        catch (Exception e)
        {
            e.printStackTrace();
            return  getString(R.string.error_something);
        }
    }
}