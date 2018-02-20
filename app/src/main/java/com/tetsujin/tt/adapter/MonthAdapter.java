package com.tetsujin.tt.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tetsujin.tt.FragmentMain;
import com.tetsujin.tt.R;

import java.util.Calendar;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static com.tetsujin.tt.ActivityMain.activityMain;
import static java.util.Calendar.DAY_OF_MONTH;

public class MonthAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[][] items;
    private String[] memoDate;
    private int year;
    private int month;

    public MonthAdapter(Context context, String[][] objects, int yyyy, int mm, String[] memoDate)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = objects;
        this.year = yyyy;
        this.month = mm;
        this.memoDate = memoDate;
    }

    public int getCount()
    {
        //カレンダーを構成する多次元配列が7*7なのでアイテム数が49になる
        return 49;
    }

    public Object getItem(int position)
    {
        return items[position / 7][position % 7];
    }

    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final TextView tv;
        //カレンダーにおける日
        String dayStr = (String) getItem(position);
    
        //Viewが再利用できるならばそれを利用する
        if (convertView == null)
            tv = new TextView(context);
        else
            tv = (TextView)convertView;
        
        tv.setText(dayStr);
        tv.setTextSize(COMPLEX_UNIT_DIP, 15);
        //TextViewのサイズをwrap_contentに設定
        tv.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //TextViewの位置を調整
        tv.setWidth(context.getResources().getDimensionPixelSize(R.dimen.size_50dp));
        tv.setHeight(context.getResources().getDimensionPixelSize(R.dimen.size_50dp));
        tv.setGravity(Gravity.CENTER);
    
        //日付がMemoDBに存在していたらTextViewに印をつける
        if(dayCheck(dayStr))
            tv.setBackgroundResource(R.drawable.calendar_day_checkmark);
    
        //click処理及び、今日の日付だけを赤い文字にする処理
        if(((String) getItem(position)).matches("[0-9]+"))
        {
            //現在の日付を取得し、その日付が今現在処理しているitemと同一だったら文字を赤くする
            Calendar cal = Calendar.getInstance();
            //get(MONTH)で得られる値は0～11なので1足す
            if(month == (cal.get(Calendar.MONTH) + 1) &&
                    //日の比較
                    dayStr.equals(String.valueOf(cal.get(DAY_OF_MONTH))))
                tv.setTextColor(Color.RED);
            
            tv.setClickable(true);
            tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //クリックアニメーションとしてTextViewの背景色を
                    //白からライトグレーに変えていくアニメーションを設定
                    int colorFrom = Color.WHITE;
                    int colorTo = Color.LTGRAY;
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(50);
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            tv.setBackgroundColor((int) animator.getAnimatedValue());
                        }
        
                    });
                    colorAnimation.start();
                    
                    String dayStr = (String) getItem(position);
                    String monthStr = String.valueOf(month);
                    
                    //一桁台の日付だったら先頭に0をつける
                    if(dayStr.length() == 1)
                        dayStr = "0" + dayStr;
                    //三桁台だったら本来の日付が二桁台なので、0を消去する
                    else if(dayStr.length() == 3)
                        dayStr = dayStr.substring(1, 3);
    
                    //月に対しても一桁台だったら先頭に0をつける
                    if(monthStr.length() == 1)
                        monthStr = "0" + monthStr;
                    
                    //メイン画面にクリックされた年月日をフォーマットして送信
                    Bundle bundle = new Bundle();
                    bundle.putString("date", String.valueOf(year) + "-" + monthStr + "-" + dayStr);
                    
                    FragmentMain frgmain = new FragmentMain();
                    frgmain.setArguments(bundle);
                    activityMain.showFragment(frgmain);
                }
            });
        }
        
        return tv;
    }
    
    //日付がMemoDBに存在するかチェックして、Booleanを返す
    private Boolean dayCheck(String dayStr)
    {
        //曜日ではないことをチェック
        if(dayStr.matches("[0-9]+"))
        {
            String monthStr = String.valueOf(month);
            
            //一桁台の日付だったら先頭に0をつける
            if(dayStr.length() == 1)
                dayStr = "0" + dayStr;
            //三桁台だったら本来の日付が二桁台なので、0を消去する
            else if(dayStr.length() == 3)
                dayStr = dayStr.substring(1, 3);
            
            //月に対しても一桁台だったら先頭に0をつける
            if(monthStr.length() == 1)
                monthStr = "0" + monthStr;
            
            for(String date:memoDate)
            {
                if(date.equals(String.valueOf(year) + "-" + monthStr + "-" + dayStr))
                    return true;
            }
        }
        
        return false;
    }
}