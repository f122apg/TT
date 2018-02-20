package com.tetsujin.tt.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tetsujin.tt.ActivityMain;
import com.tetsujin.tt.R;
import com.tetsujin.tt.database.TimeTableHelper;
import com.tetsujin.tt.task.TaskGetTimeTable;
import com.tetsujin.tt.task.TaskLogin;

public class ActivityLogin extends AppCompatActivity implements Runnable
{
    private String loginId;
    private SQLiteDatabase timeTableDB;
    private TimeTableHelper timeTableHelper;
    public ActivityLogin activityLogin;
    Thread thread;
    ProgressDialog pdialog;
    ViewGroup.LayoutParams param;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activityLogin = this;
        timeTableHelper = TimeTableHelper.getInstance(activityLogin);
        timeTableDB = timeTableHelper.getWritableDatabase();
        
        final EditText email = (EditText) findViewById(R.id.AyLogin_email_edittext);
        final EditText password = (EditText) findViewById(R.id.AyLogin_password_edittext);
        
        //ログインボタンのクリックイベント
        findViewById(R.id.AyLogin_login_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView notice = (TextView) findViewById(R.id.AyLogin_notice_textview);

                //入力チェック チェックが済んだらログイン処理を実行
                if (email.getText().toString().isEmpty() &&
                        password.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_all);
                    return;
                } else if (email.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_email);
                    return;
                } else if (password.getText().toString().isEmpty())
                {
                    notice.setText(R.string.forget_input_password);
                    return;
                }

                pdialog = new ProgressDialog(activityLogin);
                pdialog.setMessage("ログイン中...");
                pdialog.show();
                
                TaskLogin taskLogin = new TaskLogin(email.getText().toString(), password.getText().toString())
                {
                    @Override
                    public void callBack(String result)
                    {
                        TaskGetTimeTable taskGetTimeTable = new TaskGetTimeTable()
                        {
                            @Override
                            public void callBack(String result)
                            {

                            }
                        };
                    }
                };
            }
        });
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(hasFocus)
        {
            //RootであるLinearLayoutの幅を取得して、それを元にEditTextの幅を設定する
            LinearLayout ll = (LinearLayout) findViewById(R.id.AyLogin_parent_linearlayout);
            EditText email = (EditText) findViewById(R.id.AyLogin_email_edittext);
            EditText password = (EditText) findViewById(R.id.AyLogin_password_edittext);
            
            //LinearLayoutを5で割った値をLinearLayoutのサイズで引いて設定する
            email.setWidth(ll.getWidth() - ll.getWidth() / 5);
            password.setWidth(ll.getWidth() - ll.getWidth() / 5);
        }
    }

    @Override
    public void run() {
        try
        {
            thread.sleep(1000);
        }
        catch (InterruptedException e) { }
    
        Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
        startActivity(intent);
        pdialog.dismiss();
    }
}

//    private void login()
//    {
//        EditText mailaddressEd = (EditText) findViewById(R.id.AyLogin_studentid_edittext);
//        EditText passwordEd = (EditText) findViewById(R.id.AyLogin_password_edittext);
//        String mailaddress = mailaddressEd.getText().toString();
//        String password = passwordEd.getText().toString();
//
//        String jsonDataDebugOnly = "{\"student\":{\"name\":\"情報太郎\",\"studentId\":\"K016C0000\",\"email\":\"taro@it-neec.jp\",\"departmentCode\":\"CD\",\"departmentName\":\"情報処理科\",\"grade\":2,\"classNum\":4},\"lessons\":[{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"firstDate\":\"2/7/2018\",\"totalCount\":12,\"details\":[{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"2/7/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"2/14/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"2/21/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"2/28/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"3/7/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"3/14/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"3/21/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"3/28/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"4/4/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"4/11/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"4/18/2018\"}]},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"firstDate\":\"5/17/2018\",\"totalCount\":12,\"details\":[{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"5/17/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"5/24/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"5/31/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"6/7/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"6/14/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"6/21/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"6/28/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"7/5/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"7/12/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"7/19/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"7/26/2018\"}]}],\"lessonCount\":2}";
//        perseJson(jsonDataDebugOnly);
//        try
//        {
//            Cursor r = ttDB.rawQuery("SELECT * FROM TimeTable", null);
//
//            int linecnt = 1;
//            if(r.getCount() != 0)
//            {
//                StringBuilder sb = new StringBuilder();
//                while (r.moveToNext())
//                {
//                    sb.append(linecnt++ + " = TTId:" + r.getInt(r.getColumnIndex("TimeTableID")) + ", LN:" + r.getString(r.getColumnIndex("LessonName")) + ", WD:" + r.getString(r.getColumnIndex("WeekDay")) + "\n");
//                }
//
//                Log.d("TTAPP", sb.toString());
//            }
//            else
//                Log.d("TTAPP", "null");
//        }
//        catch (Exception e)
//        {
//            Log.d("TTAPP", "exception");
//            Log.d("TTAPP", e.getMessage());
//        }
//
//        //Log.d("LOGIN_TT", getTimeTable(getToken("taro@it-neec.jp", "taro@it-neec.jp")));
//    }