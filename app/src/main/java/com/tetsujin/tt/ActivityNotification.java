package com.tetsujin.tt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActivityNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ListView lv = (ListView)findViewById(R.id.notificationAy_lv);

        String[] testdata = new String[]
                {
                        "台風接近による休講について",
                        "11月3日の授業について",
                        "10月28日開催のかまた祭延期について",
                };
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, testdata);
        lv.setAdapter(aa);
    }
}
