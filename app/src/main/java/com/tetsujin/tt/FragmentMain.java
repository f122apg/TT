package com.tetsujin.tt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tetsujin.tt.adapter.CustomListViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static com.tetsujin.tt.ActivityMain.MemoHelper;

public class FragmentMain extends Fragment {

    public static Calendar cal;
    public static String todaydate;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_main, container, false);

        //日付取得と表示
        TextView date_tv = (TextView)v.findViewById(R.id.AyMain_date_textview);
        //現在の日付を取得
        Date nowdate = new Date();
        //Calendarに現在の日付を設定
        cal = Calendar.getInstance();
        //現在の曜日のみを取得
        CharSequence week = DateFormat.format("E", nowdate);
        //現在の曜日が「土」か「日」だったら次週の月曜日にする
        if(week.equals(getResources().getString(R.string.week_saturday))
                || week.equals(getResources().getString(R.string.week_sunday)))
        {
            //現在の日付を２日足し、次週の月曜日にする
            cal.add(Calendar.DAY_OF_MONTH, 2);
            cal.set(Calendar.DAY_OF_WEEK, 2);
        }
        todaydate = (String)DateFormat.format("yyyy-MM-dd", cal.getTime());
        
        //日付を表示
        date_tv.setText(DateFormat.format("MM/dd(E)", cal.getTime()));

        ListView timetable_lv = (ListView)v.findViewById(R.id.AyMain_timetable_listview);

        String[][] testdata = new String[][]
                {
                        {"1", "卒業制作", "09:00", "10:30", "月"},
                        {"2", "クラウドコンピューティング", "10:40", "12:10", "月"},
                        {"3", "データベース応用", "09:00", "12:10", "火"},
                        {"4", "Linux実習", "13:00", "16:10", "火"},
                        {"5", "キャリアデザイン3", "09:00", "14:30", "水"},
                        {"6", "オブジェクト指向プログラミング実習1 S1", "09:00", "12:10", "木"},
                        {"7", "ソフトウェアデザイン S1", "13:00", "14:30", "木"},
                        {"8", "合同資格対策講座", "09:00", "14:30", "金"},
                };

        //今日の授業をリストアップする
        ArrayList<String[]> onedaylist = new ArrayList<>();
        for(int i = 0; i < testdata.length; i ++)
        {
            if(testdata[i][4].equals(week))
                onedaylist.add(testdata[i]);
        }

        //リストを配列にする
        String[][] oneday = new String[onedaylist.size()][4];
        onedaylist.toArray(oneday);

        //１日の時間割を表示
//        CustomListViewAdapter ca = new CustomListViewAdapter(getContext(), oneday);
//        timetable_lv.setAdapter(ca);

        final EditText memo_ed = (EditText)v.findViewById(R.id.FrgMain_memo_edittext);
        
        //今日のメモがすでに存在している場合、メモ内容を取得する
        if(MemoHelper.HasDate(todaydate))
        {
            System.out.println("メモが存在しています。");
            memo_ed.setText(MemoHelper.GetRecord(todaydate));
        }
        
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
                    if (MemoHelper.HasDate(todaydate))
                    {
                        MemoHelper.Delete(todaydate);
                        Toast.makeText(v.getContext(), "メモを削除しました。", Toast.LENGTH_SHORT).show();
                    }
                }
                //フォーカスが外れた時、メモ欄の保存を行う
                else if(!hasFocus)
                {
                    //追加されていたらアップデート処理をする
                    if (MemoHelper.HasDate(todaydate))
                    {
                        Map<Integer, Object> data = new TreeMap<>();
                        data.put(1, memo_ed.getText().toString());
                        data.put(2, todaydate);

                        if(MemoHelper.InsertorUpdate(data, true))
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

                        if(MemoHelper.InsertorUpdate(data, false))
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
