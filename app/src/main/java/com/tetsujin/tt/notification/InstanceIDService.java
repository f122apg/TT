package com.tetsujin.tt.notification;

import android.content.Intent;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class InstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        Log.i("FCM", "トークンの更新を開始");
        //トークンの更新が来たときに、自動的にサーバに登録作業を行う
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
};