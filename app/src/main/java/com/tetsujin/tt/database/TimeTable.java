package com.tetsujin.tt.database;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeTable
{
    private int TimeTableID;
    //授業コードは13文字
    private String LessonCode;
    private String LessonName;
    //java.util.Calendarの形式に沿う
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
    TimeTable(int timeTableID, String lessonCode, String lessonName,
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
