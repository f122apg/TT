package com.tetsujin.tt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.tetsujin.tt.adapter.CustomFragmentPagerAdapter;
import com.tetsujin.tt.adapter.LessonTableAdapter;
import com.tetsujin.tt.database.TimeTable;

import java.util.Calendar;

import static com.tetsujin.tt.ActivityMain.state;

public class FragmentWeekContainer extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_weekcontainer, container, false);
        state.setState(ManagementFragmentState.stateList.WEEK);
        //FragmentMainから時間割データを受け取る
        Bundle args = getArguments();
        TimeTable[] timeTable = new TimeTable[0];
    
        //ViewPagerとFragmentを用いた、タブ形式の各曜日の授業を表示する画面を生成
        ViewPager vp = (ViewPager) v.findViewById(R.id.FrgWeekContainer_viewpager);
        FragmentManager fm = getChildFragmentManager();
        CustomFragmentPagerAdapter cfpadapter = new CustomFragmentPagerAdapter(fm);
        
        if(!args.getBoolean("isNull"))
        {
            timeTable = (TimeTable[])args.getParcelableArray("timetable");

            //時間割データを渡す
            cfpadapter.setdata(timeTable);
        }
        
        /*
            日付の月曜日から曜日の値を取得して、それをインクリメントしつつ渡す
            例：日付が1/25(木)だったら、まず月曜日である1/22を渡し、次に1/23を渡す...
            これを金曜日まで行う
        */
        Calendar weekDate = Calendar.getInstance();
        
        for (int i = 0; i < 5; i++)
        {
            //日付をインクリメントしてセット
            weekDate.set(Calendar.DAY_OF_WEEK, 2 + i);

            //インクリメントされた日付をアダプタに挿入
            cfpadapter.adddate((String) DateFormat.format(getResources().getString(R.string.format_MM_dd_E), weekDate));
        }
        
        //ViewPagerにCustomAdapterをセット
        vp.setAdapter(cfpadapter);
        //一週間の時間割表示時に、今日の曜日の時間割を表示できるように調整する
        weekDate = Calendar.getInstance();
        vp.setCurrentItem(cfpadapter.getPosFromDate((String) DateFormat.format(getResources().getString(R.string.format_MM_dd_E), weekDate)));
        
        //タブ画面の下部に常に一週間の時間割を簡易的に表示するGridViewを設定する
        GridView gv = (GridView)v.findViewById(R.id.FrgWeekContainer_lessontable_gridview);
        //時間割データを表形式に変換
        String[][] table = TimeTable.createTableValue(TimeTable.createWeekDayList((timeTable)));

        //アダプタを設定
        LessonTableAdapter ltadapter = new LessonTableAdapter(this.getContext(), table);
        gv.setAdapter(ltadapter);

        return v;
    }
}
