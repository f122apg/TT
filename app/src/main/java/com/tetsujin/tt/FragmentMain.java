package com.tetsujin.tt;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tetsujin.tt.adapter.LessonListAdapter;
import com.tetsujin.tt.database.TimeTable;

import java.util.Map;
import java.util.TreeMap;

import static com.tetsujin.tt.ActivityMain.activityMain;
import static com.tetsujin.tt.ActivityMain.getParsedDate;
import static com.tetsujin.tt.ActivityMain.getToDay;
import static com.tetsujin.tt.ActivityMain.getWeekDay;
import static com.tetsujin.tt.ActivityMain.memoHelper;
import static com.tetsujin.tt.ActivityMain.timeTable;
import static com.tetsujin.tt.ActivityMain.timeTableDB;
import static com.tetsujin.tt.ActivityMain.timeTableHelper;
import static com.tetsujin.tt.ActivityMain.state;

public class FragmentMain extends Fragment {

    public static String todaydate;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_main, container, false);
        state.setState(ManagementFragmentState.stateList.MAIN);

        Bundle bundle = getArguments();
        try
        {
            //渡された値を元に日付を設定
            todaydate = bundle.getString("date");
        }
        catch (NullPointerException e)
        {
            //今日の日付を取得
            todaydate = getToDay();
        }
        
        //日付取得と表示
        TextView date_tv = (TextView)v.findViewById(R.id.FrgMain_date_textview);
        //日付を表示
        date_tv.setText(DateFormat.format(getResources().getString(R.string.format_MM_dd_E), getParsedDate(todaydate, getResources().getString(R.string.format_yyyy_MM_dd))));

        /*************************************/
        /*****   ListView(時間割表示用)    *****/
        /*************************************/
        //指定した曜日の時間割データをTimeTableDBから取得し、static変数に入れる
        //TODO:timeTableDBをちゃんと定義すること
        timeTable = timeTableHelper.GetRecordAtWeekDay(timeTableDB, getWeekDay(todaydate));
         ListView timetable_lv = (ListView)v.findViewById(R.id.FrgMain_timetable_listview);
        //時間割データをアダプタに渡し、表示を行う
        LessonListAdapter ca = new LessonListAdapter(getContext(), timeTable, getWeekDay(todaydate), true, false);
        timetable_lv.setAdapter(ca);

        //ListViewのクリックイベント
        timetable_lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
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
                new AlertDialog.Builder(activityMain)
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

        /*************************************/
        /*****     EditText(メモ欄用)     *****/
        /*************************************/
        final EditText memo_ed = (EditText)v.findViewById(R.id.FrgMain_memo_edittext);
        //今日のメモがすでに存在している場合、メモ内容を取得する
        if (memoHelper.HasDate(todaydate))
            memo_ed.setText(memoHelper.GetRecord(todaydate));

        //メモ欄のフォーカスを感知
        memo_ed.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                //フォーカスが外れた時、EditTextが空欄だった場合、データベース内の列が存在していたら削除する
                if(!hasFocus && memo_ed.getText().toString().isEmpty())
                {
                    //今日の日付が存在していたら削除する
                    if (memoHelper.HasDate(todaydate))
                    {
                        memoHelper.Delete(todaydate);
                        Toast.makeText(v.getContext(), "メモを削除しました。", Toast.LENGTH_SHORT).show();
                    }
                }
                //フォーカスが外れた時、メモ欄の保存を行う
                else if(!hasFocus)
                {
                    //追加されていたらアップデート処理をする
                    if (memoHelper.HasDate(todaydate))
                    {
                        Map<Integer, Object> data = new TreeMap<>();
                        data.put(1, memo_ed.getText().toString());
                        data.put(2, todaydate);

                        if(memoHelper.InsertorUpdate(data, true))
                            Toast.makeText(v.getContext(), "メモを更新しました。", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(v.getContext(), "メモを更新することができませんでした。", Toast.LENGTH_SHORT).show();
                    }
                    //追加されていなかったらインサート処理をする
                    else
                    {
                        Map<Integer, Object> data = new TreeMap<>();
                        data.put(1, todaydate);
                        data.put(2, memo_ed.getText().toString());

                        if(memoHelper.InsertorUpdate(data, false))
                            Toast.makeText(v.getContext(), "メモを保存しました。", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(v.getContext(), "メモを保存することができませんでした。", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }
}
