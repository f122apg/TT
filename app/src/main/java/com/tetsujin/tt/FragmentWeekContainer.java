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
import com.tetsujin.tt.database.TimeTable;

import java.util.Calendar;

import static com.tetsujin.tt.FragmentMain.cal;

public class FragmentWeekContainer extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_weekcontainer, container, false);
        //FragmentMainから時間割データを受け取る
        Bundle args = getArguments();
    
        //ViewPagerとFragmentを用いた、タブ形式の各曜日の授業を表示する画面を生成
        ViewPager vp = (ViewPager) v.findViewById(R.id.FrgVP_viewpager);
        FragmentManager fm = getChildFragmentManager();
        CustomFragmentPagerAdapter cfpadapter = new CustomFragmentPagerAdapter(fm);
        
        if(!args.getBoolean("isNull"))
        {
            //時間割データを渡す
            cfpadapter.setdata((TimeTable[])args.getParcelableArray("timetable"));
        }
        
        /*
            日付データを渡す
        */
        Calendar callist = (Calendar)cal.clone();
        
        for (int i = 0; i < 5; i++)
        {
            callist.set(Calendar.DAY_OF_WEEK, 2 + i);
    
            cfpadapter.adddate((String) DateFormat.format("MM/dd(E)", callist));
        }
        
        //ViewPagerにCustomAdapterをセット
        vp.setAdapter(cfpadapter);
        //一週間の時間割表示時に、今日の曜日の時間割を表示できるように調整する
        vp.setCurrentItem(cal.get(Calendar.DAY_OF_WEEK) - 2);
        
        return v;
    }
}
