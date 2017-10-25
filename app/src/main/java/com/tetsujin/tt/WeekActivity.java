package com.tetsujin.tt;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class WeekActivity extends FragmentActivity implements TabHost.OnTabChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.content);

        TabHost.TabSpec[] tab = new TabHost.TabSpec[5];
        Bundle[] bundle = new Bundle[5];
        String[] weekname = new String[]
                {
                        getResources().getString(R.string.week_monday),
                        getResources().getString(R.string.week_tuesday),
                        getResources().getString(R.string.week_wednesday),
                        getResources().getString(R.string.week_thursday),
                        getResources().getString(R.string.week_friday)
                };

        //曜日ごとのタブを生成
        for(int i = 0; i < tab.length; i ++)
        {
            //タブのインスタンス生成
            tab[i] = tabHost.newTabSpec(weekname[i]);
            //タブに識別子を付ける
            tab[i].setIndicator(weekname[i]);
            //タブ毎に値を渡す
            bundle[i] = new Bundle();
            bundle[i].putString("tab", weekname[i]);
            tabHost.addTab(tab[i], Week_TabFragment.class, bundle[i]);
        }

        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d("onTabChanged", "tabId: " + tabId);
    }
}
