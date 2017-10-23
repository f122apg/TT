package com.tetsujin.tt;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TabHost;

public class WeekActivity extends FragmentActivity implements FragmentTabHost.OnTabChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.container);

        TabHost.TabSpec tabSpec1, tabSpec2;

        tabSpec1 = tabHost.newTabSpec("tab1");
        tabSpec1.setIndicator("tab1");
        tabHost.addTab(tabSpec1, Week_MondayFragment.class, null);

        tabSpec2 = tabHost.newTabSpec("tab2");
        tabSpec2.setIndicator("tab2");
        tabHost.addTab(tabSpec2, Week_MondayFragment.class, null);

        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId)
    {
        Log.d("onTabChanged", "tabID: " + tabId);
    }
}
