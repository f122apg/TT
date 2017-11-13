package com.tetsujin.tt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tetsujin.tt.adapter.CustomFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentWeekContainer extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_weekcontainer, container, false);
    
        //ActivityMainから値を受け取る
        Bundle args = getArguments();
        //FragmentPagerAdapterに渡すためのデータを一元管理するArrayList
        ArrayList<ArrayList<String[]>> allweek_values = new ArrayList<>();
        //各曜日の時間割データ
        ArrayList<String[]> monday_value = new ArrayList<>();
        ArrayList<String[]> tuesday_value = new ArrayList<>();
        ArrayList<String[]> wednesday_value = new ArrayList<>();
        ArrayList<String[]> thursday_value = new ArrayList<>();
        ArrayList<String[]> friday_value = new ArrayList<>();
    
        for (int i = 0; i < args.getInt("datalength"); i++)
        {
            String[] tmpvalue = args.getStringArray("data" + i);
            //月
            if (tmpvalue[4].equals(getResources().getString(R.string.week_monday)))
                monday_value.add(tmpvalue);
                //火
            else if (tmpvalue[4].equals(getResources().getString(R.string.week_tuesday)))
                tuesday_value.add(tmpvalue);
                //水
            else if (tmpvalue[4].equals(getResources().getString(R.string.week_wednesday)))
                wednesday_value.add(tmpvalue);
                //木
            else if (tmpvalue[4].equals(getResources().getString(R.string.week_thursday)))
                thursday_value.add(tmpvalue);
                //金
            else if (tmpvalue[4].equals(getResources().getString(R.string.week_friday)))
                friday_value.add(tmpvalue);
        }
    
        //各曜日の時間割データを一元管理する
        allweek_values.add(monday_value);
        allweek_values.add(tuesday_value);
        allweek_values.add(wednesday_value);
        allweek_values.add(thursday_value);
        allweek_values.add(friday_value);
    
        //ViewPagerとFragmentを用いた、タブ形式の各曜日の授業を表示する画面を生成
        ViewPager vp = (ViewPager) v.findViewById(R.id.FrgVP_viewpager);
        FragmentManager fm = getChildFragmentManager();
        CustomFragmentPagerAdapter cfpadapter = new CustomFragmentPagerAdapter(fm);
        //時間割データを渡す
        cfpadapter.setdatas(allweek_values);
        /* 日付データを渡す */
        //現在の日付を取得
        Date nowdate = new Date();
        //Calendarに現在の日付を設定
        Calendar cal = Calendar.getInstance();
        //現在の曜日のみを取得
        CharSequence week = DateFormat.format("E", nowdate);
        //現在の曜日が「土」か「日」だったら次週の月曜日にする
        if (week.equals(getResources().getString(R.string.week_saturday)) || week.equals(getResources().getString(R.string.week_sunday)))
        {
            //現在の日付を２日足す
            cal.add(Calendar.DAY_OF_MONTH, 2);
            //月曜日としてセット
            cal.set(Calendar.DAY_OF_WEEK, 2);
        }
    
        for (int i = 0; i < 5; i++)
        {
            cal.set(Calendar.DAY_OF_WEEK, 2 + i);
    
            cfpadapter.adddate((String) DateFormat.format("MM/dd(E)", cal));
        }
        
        //ViewPagerにCustomAdapterをセット
        vp.setAdapter(cfpadapter);
        
        return v;
    }
}
