package com.tetsujin.tt.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.tetsujin.tt.R;

public class ActivityRegister extends AppCompatActivity
{
    private MobileServiceClient mClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    
        if(!getDatabasePath("collegelist").exists())
        {
            try
            {
                mClient = new MobileServiceClient("https://tetsujin-tt.azurewebsites.net", this);
                
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
