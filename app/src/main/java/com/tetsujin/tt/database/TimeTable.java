package com.tetsujin.tt.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.tetsujin.tt.ActivityMain.getToDayWeekDay;

public class TimeTable implements Serializable
{
    //データベース名
    final static String DB_NAME = "TimeTableDB";
    //テーブル名
    final static String TABLE_NAME = "TimeTable";
    //テーブルの列名
    final static String COLUMN_TIMETABLEID = "TimeTableID";
    final static String COLUMN_LESSONCODE = "LessonCode";
    final static String COLUMN_LESSONNAME = "LessonName";
    final static String COLUMN_WEEKDAY = "WeekDay";
    final static String COLUMN_STARTTIME = "StartTime";
    final static String COLUMN_ENDTIME = "EndTime";
    final static String COLUMN_SEASON = "Season";
    final static String COLUMN_CLASSROOMNAME = "ClassRoomName";
    final static String COLUMN_TEACHERID = "TeacherID";
    final static String COLUMN_TEACHERNAME = "TeacherName";
    final static String COLUMN_DESCRIPTION = "Description";

    //テーブルを作成するクエリ
    final static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_TIMETABLEID   + " INTEGER PRIMARY KEY," +
            COLUMN_LESSONCODE    + " TEXT NOT NULL," +
            COLUMN_LESSONNAME    + " TEXT NOT NULL," +
            COLUMN_WEEKDAY       + " INTEGER NOT NULL," +
            COLUMN_STARTTIME     + " TEXT NOT NULL," +
            COLUMN_ENDTIME       + " TEXT NOT NULL," +
            COLUMN_SEASON        + " INTEGER NOT NULL," +
            COLUMN_CLASSROOMNAME + " TEXT NOT NULL," +
            COLUMN_TEACHERID     + " INTEGER NOT NULL," +
            COLUMN_TEACHERNAME   + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION   + " TEXT" +
            ");";

    //特定条件に合致するすべての列を取得するクエリ
    final static String GET_RECORD_QUERY = "SELECT * FROM " +
            TABLE_NAME + " WHERE ? = ?;";

    //時間割のID
    private int TimeTableID;
    //授業コードは13文字
    private String LessonCode;
    private String LessonName;
    //java.util.Calendarの形式に沿う
    //-1でエラーとする
    private int WeekDay;
    private String StartTime;
    private String EndTime;
    //前期か後期か
    private int Season;
    private String ClassRoomName;
    private int TeacherID;
    private String TeacherName;
    private String Description;
    
    //コンストラクタ
    public TimeTable(ArrayList<Object> list)
    {
        this.TimeTableID = (int)list.get(0);

        String lessonCode = (String)list.get(1);
        if(lessonCode.length() == 13)
            this.LessonCode = lessonCode;
        else
            throw new IllegalArgumentException("LessonCodeは13文字でなくてはなりません。");

        this.LessonName = (String)list.get(2);

        this.WeekDay = Integer.parseInt(getToDayWeekDay(true, (String)list.get(3)));

        //HH:mm形式の文字列が存在するかチェック(find())して、存在していたらgroup()で取得
        String startTime = (String)list.get(4);
        Pattern p = Pattern.compile("[0-9]{2}:[0-9]{2}");
        Matcher m = p.matcher(startTime);
        if(m.find())
            this.StartTime = m.group();
        else
            throw new IllegalArgumentException("StartTime内にHH:mmが存在しません。");

        String endTime = (String)list.get(5);
        m = p.matcher(endTime);
        if(m.find())
            this.EndTime = m.group();
        else
            throw new IllegalArgumentException("EndTime内にHH:mmが存在しません。");

        String season = (String)list.get(6);
        if(season.equals("前期"))
            this.Season = 0;
        else if(season.equals("後期"))
            this.Season = 1;
        else
            throw new IllegalArgumentException("Seasonの値が不正です。");

        this.ClassRoomName = (String)list.get(7);
        this.TeacherID = (int)list.get(8);
        this.TeacherName = (String)list.get(9);
        this.Description = (String)list.get(10);
    }

    public TimeTable(int timeTableID, String lessonCode, String lessonName,
              int weekDay, String startTime, String endTime, int season,
              String classRoomName, int teacherID, String teacherName, String description)
    {
        this.TimeTableID = timeTableID;
        
        if(lessonCode.length() == 13)
            this.LessonCode = lessonCode;
        else
            throw new IllegalArgumentException("LessonCodeは13文字でなくてはなりません。");
        
        this.LessonName = lessonName;
        this.WeekDay = weekDay;
    
        //HH:mm形式の文字列が存在するかチェック(find())して、存在していたらgroup()で取得
        Pattern p = Pattern.compile("[0-9]{2}:[0-9]{2}");
        Matcher m = p.matcher(startTime);
        if(m.find())
            this.StartTime = m.group();
        else
            throw new IllegalArgumentException("StartTime内にHH:mmが存在しません。");
        
        m = p.matcher(endTime);
        if(m.find())
            this.EndTime = m.group();
        else
            throw new IllegalArgumentException("EndTime内にHH:mmが存在しません。");
    
        if(season == 0 || season == 1)
            this.Season = season;
        else
            throw new IllegalArgumentException("Seasonの値が不正です。");
            
        this.ClassRoomName = classRoomName;
        this.TeacherID = teacherID;
        this.TeacherName = teacherName;
        this.Description = description;
    }
    
    /*
        ゲッター
     */
    public int getTimeTableID()
    {
        return this.TimeTableID;
    }
    
    public String getLessonCode()
    {
        return this.LessonCode;
    }
    
    public String getLessonName()
    {
        return this.LessonName;
    }
    
    public String getWeekDay()
    {
        switch (this.WeekDay)
        {
            case Calendar.MONDAY:
                return "月";
            case Calendar.TUESDAY:
                return "火";
            case Calendar.WEDNESDAY:
                return "水";
            case Calendar.THURSDAY:
                return "木";
            case Calendar.FRIDAY:
                return "金";
            default:
                return null;
        }
    }
    
    public String getStartTime()
    {
        return this.StartTime;
    }
    
    public String getEndTime()
    {
        return this.EndTime;
    }
    
    public String getSeason()
    {
        switch (this.Season)
        {
            case 0:
                return "前期";
            case 1:
                return "後期";
            default:
                return null;
        }
    }
    
    public String getClassRoomName()
    {
        return this.ClassRoomName;
    }
    
    public int getTeacherID()
    {
        return this.TeacherID;
    }
    
    public String getTeacherName()
    {
        return this.TeacherName;
    }
    
    public String getDescription()
    {
        return this.Description;
    }
}
