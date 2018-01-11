package com.tetsujin.tt.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.tetsujin.tt.R;
import com.tetsujin.tt.TodoItem;
import com.tetsujin.tt.database.Login;

import java.util.List;

public class ActivityLogin extends AppCompatActivity
{
    private MobileServiceClient mClient;
    private String loginId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
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
                final EditText studentId = (EditText)findViewById(R.id.AyLogin_studentid_edittext);
                EditText password = (EditText)findViewById(R.id.AyLogin_password_edittext);
                final TextView notice = (TextView) findViewById(R.id.AyLogin_notice_textview);

                //入力チェック
                if(studentId.getText().toString().isEmpty() &&
                        password.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_all);
                    return;
                }
                else if(studentId.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_number);
                    return;
                }
                else if(password.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_password);
                    return;
                }

                //入力欄が空ではないならログイン処理を実行
                final ProgressDialog progressDialog = new ProgressDialog(activityLogin);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getResources().getString(R.string.logging_in));
                progressDialog.show();

                AsyncTask<Void, Void, String> getEmailTask = new AsyncTask<Void, Void, String>()
                {
                    @Override
                    protected String doInBackground(Void... voids)
                    {
                        try
                        {
                            System.out.println("-----------------------------> Task Start");
                            MobileServiceTable<Login> table = mClient.getTable(Login.class);
                            //列:StudentIDと入力された学籍番号が一致された行を検索する
                            final List<Login> user = table
                                    .where()
                                    //列の指定
                                    .field("StudentID")
                                    //eq = 等しい
                                    .eq(studentId.getText().toString())
                                    .execute()
                                    .get();
                            System.out.println("-----------------------------> Get Data");

                            if(user.size() != 0)
                                return user.get(0).getMailAddress();
                            else
                                return null;
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        return null;
                    }
                };

                getEmailTask.execute();

//                if(loginId != null)
//                {
//                    //ログイン実行
//                    auth.signInWithEmailAndPassword(loginId, password.getText().toString()).addOnCompleteListener(activityLogin, new OnCompleteListener<AuthResult>()
//                    {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task)
//                        {
//                            //ログイン成功時
//                            if (task.isSuccessful())
//                            {
//                                Toast.makeText(activityLogin, "Success", Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
//                                return;
//                            }
//                            //失敗時
//                            else
//                            {
//                                notice.setText(R.string.wrong_number_or_password);
//                                Toast.makeText(activityLogin, R.string.failed_login, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//                else
//                {
//                    Toast.makeText(activityLogin, "DBからメールアドレスが取得できませんでした。", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        //新規登録ボタンのクリックイベント
        findViewById(R.id.AyLogin_signup_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent regIntent = new Intent(activityLogin, ActivityRegister.class);
                startActivity(regIntent);
            }
        });
    }
}