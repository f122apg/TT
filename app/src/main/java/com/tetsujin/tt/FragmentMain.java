package com.tetsujin.tt;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteStatement;
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
import com.tetsujin.tt.database.DBInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.tetsujin.tt.ActivityMain.db;

public class FragmentMain extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_main, container, false);

        //日付取得と表示
        TextView date_tv = (TextView)v.findViewById(R.id.AyMain_date_textview);
        //現在の日付を取得
        Date nowdate = new Date();
        //Calendarに現在の日付を設定
        final Calendar cal = Calendar.getInstance();
        //現在の曜日のみを取得
        CharSequence week = DateFormat.format("E", nowdate);
        //現在の曜日が「土」か「日」だったら次週の月曜日にする
        if(week.equals(getResources().getString(R.string.week_saturday))
                || week.equals(getResources().getString(R.string.week_sunday)))
        {
            //現在の日付を２日足し、今週の月曜日にする
            cal.add(Calendar.DAY_OF_MONTH, 2);
            cal.set(Calendar.DAY_OF_WEEK, 2);
        }
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

        CustomListViewAdapter ca = new CustomListViewAdapter(getContext(), oneday);
        timetable_lv.setAdapter(ca);

        final EditText memo_ed = (EditText)v.findViewById(R.id.FrgMain_memo_edittext);
        memo_ed.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                //フォーカスが外れた時、メモ欄が空白ではなかったら保存をかける
                if(!hasFocus && !memo_ed.getText().toString().isEmpty())
                {
                    Toast.makeText(v.getContext(), "フォーカスが外れ、かつ空白ではない", Toast.LENGTH_SHORT);
                    String datenow = (String)DateFormat.format("yyyy-MM-dd", cal.getTime());

                    if(datenow.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}"))
                    {
                        //今日の日付がもうDBに追加されているかどうかチェックする
                        String query = "SELECT COUNT(*) FROM " + DBInfo.DBMEMO_TABLENAME + " WHERE " +
                                "MemoAt = " + DatabaseUtils.sqlEscapeString(datenow) + ";";

                        String[] result = new String[1];
                        db.rawQuery(query, result);
                        Toast.makeText(v.getContext(), result[0], Toast.LENGTH_SHORT);

                        //追加されていなかったらそのままインサート処理をする
                        if (result[0].equals("0"))
                        {
                            query = "INSERT INTO ? VALUES(?, ?);";
                            SQLiteStatement sqst = db.compileStatement(query);
                            sqst.bindString(1, DBInfo.DBMEMO_TABLENAME);
                            sqst.bindString(2, DatabaseUtils.sqlEscapeString(datenow));
                            sqst.bindString(3, DatabaseUtils.sqlEscapeString(memo_ed.getText().toString()));
                            sqst.executeInsert();
                            Toast.makeText(v.getContext(), "インサート", Toast.LENGTH_SHORT);
                        }
                        //追加されていたらアップデートする
                        else
                        {
                            query = "UPDATE ? SET ? = ? WHERE ? = ?;";
                            SQLiteStatement sqst = db.compileStatement(query);
                            sqst.bindString(1, DBInfo.DBMEMO_TABLENAME);
                            sqst.bindString(2, DBInfo.DBMEMO_COLUMN[1]);
                            sqst.bindString(3, DatabaseUtils.sqlEscapeString(memo_ed.getText().toString()));
                            sqst.bindString(4, DBInfo.DBMEMO_COLUMN[0]);
                            sqst.bindString(5, DatabaseUtils.sqlEscapeString(datenow));
                            sqst.executeUpdateDelete();
                            Toast.makeText(v.getContext(), "アップデート", Toast.LENGTH_SHORT);
                        }
                    }
                    else
                    {
                        Toast.makeText(v.getContext(), getResources().getString(R.string.memo_cannot_save), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }
}
