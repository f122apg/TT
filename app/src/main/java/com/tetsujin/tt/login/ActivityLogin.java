package com.tetsujin.tt.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.tetsujin.tt.R;
import com.tetsujin.tt.database.DBLogin;

import java.util.List;

public class ActivityLogin extends AppCompatActivity
{
    private MobileServiceClient mClient;
    
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
                final String loginId;
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

                AsyncTask<Void, Void, Void> getEmailTask = new AsyncTask<Void, Void, Void>()
                {
                    @Override
                    protected Void doInBackground(Void... voids)
                    {
                        try
                        {
                            System.out.println("-----------------------------> Task Start");
                            MobileServiceTable<DBLogin> table = mClient.getTable("Login", DBLogin.class);
                            List<DBLogin> user = table
//                                    .where()
//                                    .field("StudentID")
//                                    .eq(studentId.getText().toString())
                                    .execute()
                                    .get();
                            System.out.println("-----------------------------> Get Data");
                            System.out.println("-----------------------------> Data:" + user.get(0).getMailAddress());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        
                        return null;
                    }
                };
    
                getEmailTask.execute();
                progressDialog.dismiss();

                //ログイン実行
//                auth.signInWithEmailAndPassword(studentId.getText().toString(), password.getText().toString())
//                        .addOnCompleteListener(activityLogin, new OnCompleteListener<AuthResult>()
//                        {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task)
//                            {
//                                progressDialog.dismiss();
//
//                                //ログイン成功時
//                                if(task.isSuccessful())
//                                {
//                                    Toast.makeText(activityLogin, "Success", Toast.LENGTH_SHORT).show();
//                                }
//                                //失敗時
//                                else
//                                {
//                                    notice.setText(R.string.wrong_number_or_password);
//                                    Toast.makeText(activityLogin, R.string.failed_login, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
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