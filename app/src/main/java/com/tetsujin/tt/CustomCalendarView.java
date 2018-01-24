package com.tetsujin.tt;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class CustomCalendarView extends LinearLayout
{
    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
        
        initView(context);
    }
    
    private void initView(Context context)
    {
        //現在の日付を取得
        Calendar cal = Calendar.getInstance();
        
        //年月を設定する
        TextView yyyymm = new TextView(context);
        yyyymm.setText(DateFormat.format("yyyy年M月", cal));
        yyyymm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        LayoutParams lpyyyymm = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lpyyyymm.gravity = Gravity.CENTER;
        lpyyyymm.topMargin = 50;
        yyyymm.setLayoutParams(lpyyyymm);
        
        //曜日表示
        TextView weekday = new TextView(context);
        weekday.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        weekday.setText("日  月  火  水  木  金  土");
    
        LayoutParams lpweekday = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lpweekday.gravity = Gravity.CENTER;
        weekday.setLayoutParams(lpweekday);
        
        
        
        
        addViews(yyyymm, weekday);
    }
    
    private void addViews(View ... view)
    {
        for(View v:view)
        {
            addView(v);
        }
    }
}
