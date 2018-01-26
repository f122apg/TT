package com.tetsujin.tt;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomCalendarView extends LinearLayout {

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
        test(this, context);
    }

    private void test(LinearLayout parent, Context context) {
        //Calendarのインスタンスを生成
        //現在の日付を取得している
        Calendar cal = Calendar.getInstance();

        cal.clear();
        //インスタンスに以下の日付を設定している
        //つまりcal = 2017月12月1日が設定される
        cal.set(2017, 11, 1);

        int maxDate = cal.getActualMaximum(Calendar.DATE);

        String[][] testTable =
                {
                        {"日", "月", "火", "水", "木", "金", "土"},

                        {"　", "　", "　", "　", "　", "　", "　"},
                        {"　", "　", "　", "　", "　", "　", "　"},
                        {"　", "　", "　", "　", "　", "　", "　"},
                        {"　", "　", "　", "　", "　", "　", "　"},
                        {"　", "　", "　", "　", "　", "　", "　"},
                        {"　", "　", "　", "　", "　", "　", "　"}
                };

        testTable[1][(cal.get(Calendar.DAY_OF_WEEK) - 1)] = "1";

        int count = 2;
        boolean breakflg = false;

        for(int row = 1; row < 7; row ++)
        {
            for(int column = 0; column < 7; column ++)
            {
                if(row == 1){
                    column = (cal.get(Calendar.DAY_OF_WEEK));
                }
                testTable[row][column] = String.valueOf(count);
                count ++;

                if(count > maxDate){
                    breakflg = true;
                    break;
                }
            }

            if(breakflg)
                break;
        }

        for(int row = 0; row < 7; row ++)
        {
            LinearLayout child = new LinearLayout(context);
            child.setOrientation(HORIZONTAL);

            for(int column = 0; column < 7; column ++)
            {
                TextView tv = new TextView(context);
                tv.setText(testTable[row][column]);

                child.addView(tv);
            }

            parent.addView(child);
        }
    }
}