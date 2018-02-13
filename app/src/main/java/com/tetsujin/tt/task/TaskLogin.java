package com.tetsujin.tt.task;

import android.os.AsyncTask;
import android.util.Log;

import com.tetsujin.tt.database.TimeTable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

public class TaskLogin extends AsyncTask<Void, Void, Void>
{
    @Override
    protected Void doInBackground(Void... voids)
    {
        return null;
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

    private String getJsonTimeTable(String token)
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
            //Jsonを読み込む
            JSONObject jsonRoot = new JSONObject(jsonStr);
            //Json内にある要素配列 lessons を取得
            JSONArray jsonLesson = jsonRoot.getJSONArray("lessons");
            //返り値を準備
            TimeTable[] retValues = new TimeTable[jsonLesson.length()];

            Log.d("TTAPP", String.valueOf(jsonLesson.length()));

            for(int i = 0; i < jsonLesson.length(); i ++)
            {
                JSONObject jsonChild = jsonLesson.getJSONObject(i);

                //jsonの要素名から値を取得し、変数に一時保存させる
                //TODO:元データにtimetableidとteacheridがない問題を解決する
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
