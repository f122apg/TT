package com.tetsujin.tt;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.tetsujin.tt.adapter.CustomHorizontalListViewAdapter;
import com.tetsujin.tt.adapter.CustomListViewLessonListAdapter;
import com.tetsujin.tt.adapter.LessonListAdapter;
import com.tetsujin.tt.database.TimeTable;

import java.util.ArrayList;

public class FragmentWeek extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.fragment_week, container, false);
        ListView lv = (ListView)v.findViewById(R.id.FrgWeek_listview);

        //CustomFragmentPagerAdapterから送られた値を受け取る
        Bundle args = getArguments();
        ArrayList<Parcelable> timeTable = args.getParcelableArrayList("timetable");

        //ListViewに現在のデータを適用
        CustomListViewLessonListAdapter ca = new CustomListViewLessonListAdapter(v.getContext(), timeTable.toArray(new TimeTable[timeTable.size()]), 0, false, true);
        lv.setAdapter(ca);
        //ListViewのクリックイベント
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            //ListView内にあるアイテムをタップするとAlertDiaglogを用いて、
            //授業の詳細を表示するようにしている
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id)
            {
                ListView lv = (ListView)adapterView;
                //ListView内に存在するアイテムを取得
                TimeTable data = (TimeTable) lv.getItemAtPosition(pos);
            
                //詳細画面を表示
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.lesson_detail)
                        .setMessage(getResources().getString(R.string.lesson_name) + data.getLessonName() + "\n" +
                                getResources().getString(R.string.classroom) + data.getClassRoomName() + "\n" +
                                getResources().getString(R.string.starttime) + data.getStartTime() + "\n" +
                                getResources().getString(R.string.endtime) + data.getEndTime() + "\n" +
                                getResources().getString(R.string.teacher) + data.getTeacherName() + "\n" +
                                getResources().getString(R.string.description) + data.showDescription())
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
        });

        return v;
    }
}
