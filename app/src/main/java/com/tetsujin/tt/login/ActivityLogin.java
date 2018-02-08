package com.tetsujin.tt.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.tetsujin.tt.R;
import com.tetsujin.tt.database.Login;
import com.tetsujin.tt.database.TimeTable;
import com.tetsujin.tt.database.TimeTableHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ActivityLogin extends AppCompatActivity
{
    private MobileServiceClient mClient;
    private String loginId;
    private SQLiteDatabase ttDB;
    private TimeTableHelper ttDBHelper;
    public ActivityLogin activityLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activityLogin = this;
        ttDBHelper = new TimeTableHelper(activityLogin);
        ttDB = ttDBHelper.getWritableDatabase();

        try
        {
            mClient = new MobileServiceClient("https://tetsujin-tt.azurewebsites.net", this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        //ログインボタンのクリックイベント
        findViewById(R.id.AyLogin_login_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final EditText studentId = (EditText) findViewById(R.id.AyLogin_studentid_edittext);
                EditText password = (EditText) findViewById(R.id.AyLogin_password_edittext);
                final TextView notice = (TextView) findViewById(R.id.AyLogin_notice_textview);

                //TODO:ログイン処理が実装できているか確認するために、一時的に入力チェックをオフにする
                //入力チェック
//                if (studentId.getText().toString().isEmpty() && password.getText().toString().isEmpty())
//                {
//                    notice.setText(R.string.forget_input_all);
//                    return;
//                } else if (studentId.getText().toString().isEmpty())
//                {
//                    notice.setText(R.string.forget_input_number);
//                    return;
//                } else if (password.getText().toString().isEmpty())
//                {
//                    notice.setText(R.string.forget_input_password);
//                    return;
//                }

//                //入力欄が空ではないならログイン処理を実行
//                final ProgressDialog progressDialog = new ProgressDialog(activityLogin);
//                progressDialog.setCancelable(false);
//                progressDialog.setMessage(getResources().getString(R.string.logging_in));
//                progressDialog.show();

                AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
                {
                    @Override
                    protected String doInBackground(Void... voids)
                    {
                        login();
                        return null;
                    }
                };

                task.execute();
            }
        });
    }

    private void login()
    {
        EditText mailaddressEd = (EditText) findViewById(R.id.AyLogin_studentid_edittext);
        EditText passwordEd = (EditText) findViewById(R.id.AyLogin_password_edittext);
        String mailaddress = mailaddressEd.getText().toString();
        String password = passwordEd.getText().toString();

        String jsonDataDebugOnly = "{\"student\":{\"name\":\"情報太郎\",\"studentId\":\"K016C0000\",\"email\":\"taro@it-neec.jp\",\"departmentCode\":\"CD\",\"departmentName\":\"情報処理科\",\"grade\":2,\"classNum\":4},\"lessons\":[{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"firstDate\":\"2/7/2018\",\"totalCount\":12,\"details\":[{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"2/7/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"2/14/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"2/21/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"2/28/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"3/7/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"3/14/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"3/21/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"3/28/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"4/4/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"4/11/2018\"},{\"lessonCode\":\"KGCD016CD1044\",\"lessonName\":\"キャリアデザイン４\",\"teacherName\":\"教師次郎\",\"classroomName\":\"30711\",\"weekDayNumber\":2,\"weekDay\":\"Monday\",\"startPeriod\":1,\"endPeriod\":2,\"startTime\":\"09:00:00\",\"endTime\":\"09:45:00\",\"date\":\"4/18/2018\"}]},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"firstDate\":\"5/17/2018\",\"totalCount\":12,\"details\":[{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"5/17/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"5/24/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"5/31/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"6/7/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"6/14/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"6/21/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"6/28/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"7/5/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"7/12/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"7/19/2018\"},{\"lessonCode\":\"KGCD016CD1045\",\"lessonName\":\"卒業制作\",\"teacherName\":\"教師一郎\",\"classroomName\":\"30712\",\"weekDayNumber\":6,\"weekDay\":\"Friday\",\"startPeriod\":3,\"endPeriod\":4,\"startTime\":\"10:40:00\",\"endTime\":\"11:25:00\",\"date\":\"7/26/2018\"}]}],\"lessonCount\":2}";
        perseJson(jsonDataDebugOnly);
        try
        {
            Cursor r = ttDB.rawQuery("SELECT * FROM TimeTable", null);

            int linecnt = 1;
            if(r.getCount() != 0)
            {
                StringBuilder sb = new StringBuilder();
                while (r.moveToNext())
                {
                    sb.append(linecnt++ + " = TTId:" + r.getInt(r.getColumnIndex("TimeTableID")) + ", LN:" + r.getString(r.getColumnIndex("LessonName")) + ", WD:" + r.getString(r.getColumnIndex("WeekDay")) + "\n");
                }

                Log.d("TTAPP", sb.toString());
            }
            else
                Log.d("TTAPP", "null");
        }
        catch (Exception e)
        {
            Log.d("TTAPP", "exception");
            Log.d("TTAPP", e.getMessage());
        }

        //Log.d("LOGIN_TT", getTimeTable(getToken("taro@it-neec.jp", "taro@it-neec.jp")));
    }

    private String getToken(String username, String password)
    {
        HttpURLConnection con = null;
        String urlStr = "https:/tetsujintimes.azurewebsites.net/token";
        StringBuilder postDataBuilder = new StringBuilder();

        //POSTするデータを作成
        postDataBuilder.append("grant_type=password&");
        postDataBuilder.append("UserName=" + username + "&");
        postDataBuilder.append("Password=" + password);

        try
        {
            URL url = new URL(urlStr);

            //トークンを取得するために、メールアドレスとパスワードを投げる
            con = (HttpURLConnection) url.openConnection();
            //POST形式でリクエストを投げる
            con.setRequestMethod("POST");
            //POSTで投げるときは必ずtrueにする
            con.setDoOutput(true);
            //フォームエンコード形式に設定
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //POSTするデータの長さを設定
            con.setRequestProperty("Content-Length", String.valueOf(postDataBuilder.length()));
            //リクエストボディにPOSTするデータを書き込む
            OutputStreamWriter requestBody = new OutputStreamWriter(con.getOutputStream());
            requestBody.write(postDataBuilder.toString());
            requestBody.flush();
            //URL先に接続
            con.connect();

            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK)
            {
                //InputStreamを取得
                InputStream streamInput = con.getInputStream();
                //InputStreamReader、StringBuilderを用いて、InputStreamをStringに変換
                InputStreamReader streamReader = new InputStreamReader(streamInput);
                StringBuilder strBuilder = new StringBuilder();

                //InputStreamReaderを読み込んで、StringBuilderに追記する
                char[] buf = new char[1024];
                int read;
                while (0 <= (read = streamReader.read(buf)))
                {
                    strBuilder.append(buf, 0, read);
                }

                String jsonStr = strBuilder.toString();
                streamReader.close();
                streamInput.close();

                //jsonの読み込み
                JSONObject json = new JSONObject(jsonStr);
                //トークンを取得する
                return json.getString("access_token");
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }

    private String getTimeTable(String token)
    {
        HttpURLConnection con = null;
        String urlStr = "https:/tetsujintimes.azurewebsites.net/api/schedules";

        //リクエストするデータを準備する
        String requestHeaderName = "Authorization";
        String requestHeaderValue = "Bearer " + token;

        try
        {
            URL url = new URL(urlStr);

            //トークンを用いて認証を始める
            con = (HttpURLConnection) url.openConnection();
            //GET形式でリクエストを投げる
            con.setRequestMethod("GET");
            //リクエストヘッダにAuthorization：トークンを追加
            con.setRequestProperty(requestHeaderName, requestHeaderValue);
            //URL先に接続
            con.connect();

            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK)
            {
                //InputStreamを取得
                InputStream streamInput = con.getInputStream();
                //InputStreamReader、StringBuilderを用いて、InputStreamをStringに変換
                InputStreamReader streamReader = new InputStreamReader(streamInput);
                StringBuilder strBuilder = new StringBuilder();

                //InputStreamReaderを読み込んで、StringBuilderに追記する
                char[] buf = new char[1024];
                int read;
                while (0 <= (read = streamReader.read(buf)))
                {
                    strBuilder.append(buf, 0, read);
                }

                String jsonStr = strBuilder.toString();
                streamReader.close();
                streamInput.close();

                //jsonの読み込み
                //JSONObject json = new JSONObject(jsonStr);
                //トークンを取得する
                return jsonStr;
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }

    private void perseJson(String jsonStr)
    {
        try
        {
            Log.d("TTAPP", "JSON_OK");
            JSONObject jsonRoot = new JSONObject(jsonStr);
            JSONArray jsonLesson = jsonRoot.getJSONArray("lessons");
            TimeTable[] retValues = new TimeTable[jsonLesson.length()];

            Log.d("TTAPP", String.valueOf(jsonLesson.length()));

            for(int i = 0; i < jsonLesson.length(); i ++)
            {
                JSONObject jsonChild = jsonLesson.getJSONObject(i);

                //jsonの要素名から値を取得し、変数に一時保存させる
                int timeTableId = i;
                String lessonCode = jsonChild.getString("lessonCode");
                String lessonName = jsonChild.getString("lessonName");
                int weekDay = jsonChild.getInt("weekDayNumber");
                String startTime = jsonChild.getString("startTime");
                String endTime = jsonChild.getString("endTime");
                String description = "id:" + String.valueOf(i);
                String classRoomName = jsonChild.getString("classroomName");
                int teacherId = 0;
                String teacherName = jsonChild.getString("teacherName");

                //一時保存された変数からTimeTableのインスタンスを生成
                retValues[i] = new TimeTable.Builder(timeTableId)
                        .LessonCode(lessonCode)
                        .LessonName(lessonName)
                        .WeekDay(weekDay)
                        .StartTime(startTime)
                        .EndTime(endTime)
                        .ClassRoomName(classRoomName)
                        .TeacherID(teacherId)
                        .TeacherName(teacherName)
                        .Description(description)
                        .build();
            }

            Map<Integer, Object> data = new TreeMap<>();
            //インサートする前にDBのレコードを全削除する
            ttDBHelper.Clear(ttDB);

            //取得されたデータをDBへインサートする
            for (TimeTable v:retValues)
            {
                data.put(0, v.getTimeTableID());
                data.put(1, v.getLessonCode());
                data.put(2, v.getLessonName());
                data.put(3, v.getWeekDay());
                data.put(4, v.getStartTime());
                data.put(5, v.getEndTime());
                data.put(6, v.getClassRoomName());
                data.put(7, v.getTeacherID());
                data.put(8, v.getTeacherName());
                data.put(9, v.getDescription());

                ttDBHelper.Insert(ttDB, data);
            }
        }
        catch (Exception e)
        {
            Log.e("TTAPP", e.getMessage());
        }
    }
}