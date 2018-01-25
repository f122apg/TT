package com.tetsujin.tt.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeTable implements Parcelable
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
    
    //すべてのレコードを取得するクエリ
    final static String GET_RECORD_ALL = "SELECT * FROM " + TABLE_NAME + ";";
    
    //IDに合致するすべてのレコードを取得するクエリ
    final static String GET_RECORD_ID_QUERY = "SELECT * FROM " +
            TABLE_NAME + " WHERE " + COLUMN_TIMETABLEID + " = ?;";
    
    //特定の曜日に合致するすべてのレコードを取得するクエリ
    final static String GET_RECORD_AT_WEEKDAY_QUERY = "SELECT * FROM " +
            TABLE_NAME + " WHERE " + COLUMN_WEEKDAY + " = ?;";

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
    //前期 = 0 か後期 = 1 か
    private int Season;
    private String ClassRoomName;
    private int TeacherID;
    private String TeacherName;
    private String Description;
    
    /*
        コンストラクタ
     */

    private TimeTable(Builder builder)
    {
        this.TimeTableID = builder.TimeTableID;
        this.LessonCode = builder.LessonCode;
        this.LessonName = builder.LessonName;
        this.WeekDay = builder.WeekDay;
        this.StartTime = builder.StartTime;
        this.EndTime = builder.EndTime;
        this.Season = builder.Season;
        this.ClassRoomName = builder.ClassRoomName;
        this.TeacherID = builder.TeacherID;
        this.TeacherName = builder.TeacherName;
        this.Description = builder.Description;
    }

    //サーバサイドから受け取った情報を基に時間割データを生成する場合
//    public TimeTable(int timeTableID, String lessonCode, String lessonName,
//              int weekDay, String startTime, String endTime, int season,
//              String classRoomName, int teacherID, String teacherName, String description)
//    {
//        this.TimeTableID = timeTableID;
//
//        if(lessonCode.length() == 13)
//            this.LessonCode = lessonCode;
//        else
//            throw new IllegalArgumentException("LessonCodeは13文字でなくてはなりません。");
//
//        this.LessonName = lessonName;
//
//        if(weekDay > 6)
//            this.WeekDay = weekDay;
//        else
//            throw new IllegalArgumentException("WeekDayの値が不正です。");
//
//        //HH:mm形式の文字列が存在するかチェック(find())して、存在していたらgroup()で取得
//        Pattern p = Pattern.compile("[0-9]{2}:[0-9]{2}");
//        Matcher m = p.matcher(startTime);
//        if(m.find())
//            this.StartTime = m.group();
//        else
//            throw new IllegalArgumentException("StartTime内にHH:mmが存在しません。");
//
//        m = p.matcher(endTime);
//        if(m.find())
//            this.EndTime = m.group();
//        else
//            throw new IllegalArgumentException("EndTime内にHH:mmが存在しません。");
//
//        if(season == 0 || season == 1)
//            this.Season = season;
//        else
//            throw new IllegalArgumentException("Seasonの値が不正です。");
//
//        this.ClassRoomName = classRoomName;
//        this.TeacherID = teacherID;
//        this.TeacherName = teacherName;
//        this.Description = description;
//    }
//
//    //ArrayListからTimeTableを作成する
//    //ローカルのデータベースから時間割を生成する場合
//    public TimeTable(ArrayList<Object> list)
//    {
//        this.TimeTableID = (int)list.get(0);
//
//        String lessonCode = (String)list.get(1);
//        if(lessonCode.length() == 13)
//            this.LessonCode = lessonCode;
//        else
//            throw new IllegalArgumentException("LessonCodeは13文字でなくてはなりません。");
//
//        this.LessonName = (String)list.get(2);
//
//        this.WeekDay = (int)list.get(3);
//
//        //HH:mm形式の文字列が存在するかチェック(find())して、存在していたらgroup()で取得
//        String startTime = (String)list.get(4);
//        Pattern p = Pattern.compile("[0-9]{2}:[0-9]{2}");
//        Matcher m = p.matcher(startTime);
//        if(m.find())
//            this.StartTime = m.group();
//        else
//            throw new IllegalArgumentException("StartTime内にHH:mmが存在しません。");
//
//        String endTime = (String)list.get(5);
//        m = p.matcher(endTime);
//        if(m.find())
//            this.EndTime = m.group();
//        else
//            throw new IllegalArgumentException("EndTime内にHH:mmが存在しません。");
//
//        int season = (int)list.get(6);
//        if(season == 0 || season == 1)
//            this.Season = season;
//        else
//            throw new IllegalArgumentException("Seasonの値が不正です。");
//
//        this.ClassRoomName = (String)list.get(7);
//        this.TeacherID = (int)list.get(8);
//        this.TeacherName = (String)list.get(9);
//        this.Description = (String)list.get(10);
//    }
    
    //Parcelを復元する
    public TimeTable(Parcel in)
    {
        this.TimeTableID = in.readInt();
        this.LessonCode = in.readString();
        this.LessonName = in.readString();
        this.WeekDay = in.readInt();
        this.StartTime = in.readString();
        this.EndTime = in.readString();
        this.Season = in.readInt();
        this.ClassRoomName = in.readString();
        this.TeacherID = in.readInt();
        this.TeacherName = in.readString();
        this.Description = in.readString();
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
    
    public int getWeekDay()
    {
        return this.WeekDay;
    }
    
    public String getStartTime()
    {
        return this.StartTime;
    }
    
    public String getEndTime()
    {
        return this.EndTime;
    }
    
    public int getSeason()
    {
        return this.Season;
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
    
    /*
        show
     */
    public String showDescription()
    {
        if(!getDescription().equals("null"))
            return getDescription();
        else
            return "なし";
    }
    
    /*
        Parcel
     */
    @Override
    public int describeContents()
    {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.TimeTableID);
        dest.writeString(this.LessonCode);
        dest.writeString(this.LessonName);
        dest.writeInt(this.WeekDay);
        dest.writeString(this.StartTime);
        dest.writeString(this.EndTime);
        dest.writeInt(this.Season);
        dest.writeString(this.ClassRoomName);
        dest.writeInt(this.TeacherID);
        dest.writeString(this.TeacherName);
        dest.writeString(this.Description);
    }
    
    public static final Parcelable.Creator<TimeTable> CREATOR = new Parcelable.Creator<TimeTable>()
    {
        @Override
        public TimeTable createFromParcel(Parcel source)
        {
            return new TimeTable(source);
        }
        
        @Override
        public TimeTable[] newArray(int size)
        {
            return new TimeTable[size];
        }
    };

    /*
        ビルダー
     */
    public static class Builder
    {
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
        //前期 = 0 か後期 = 1 か
        private int Season;
        private String ClassRoomName;
        private int TeacherID;
        private String TeacherName;
        private String Description;

        public Builder(int timeTableID)
        {
            TimeTableID = timeTableID;
        }

        public Builder LessonCode(String lessonCode)
        {
            LessonCode = lessonCode;
            return this;
        }

        public Builder LessonName(String lessonName)
        {
            LessonName = lessonName;
            return this;
        }

        public Builder WeekDay(int weekDay)
        {
            WeekDay = weekDay;
            return this;
        }

        public Builder StartTime(String startTime)
        {
            StartTime = startTime;
            return this;
        }

        public Builder EndTime(String endTime)
        {
            EndTime = endTime;
            return this;
        }

        public Builder Season(int season)
        {
            Season = season;
            return this;
        }

        public Builder ClassRoomName(String classRoomName)
        {
            ClassRoomName = classRoomName;
            return this;
        }

        public Builder TeacherID(int teacherID)
        {
            TeacherID = teacherID;
            return this;
        }

        public Builder TeacherName(String teacherName)
        {
            TeacherName = teacherName;
            return this;
        }

        public Builder Description(String description)
        {
            Description = description;
            return this;
        }

        public TimeTable build() {
            //月～金までのどれかかチェックして、違うならばthrowする
            if(!(WeekDay >= 2 && WeekDay <= 6))
                throw new IllegalStateException("曜日の値が不正です。");

            //HH:mm形式の文字列が存在するかチェック(find())して、存在していたらgroup()で取得
            //存在しない場合throwする
            Pattern p = Pattern.compile("[0-9]{2}:[0-9]{2}");
            Matcher m = p.matcher(StartTime);
            if(m.find())
                StartTime = m.group();
            else
                throw new IllegalStateException("StartTime内にHH:mmが存在しません。");

            m = p.matcher(EndTime);
            if(m.find())
                EndTime = m.group();
            else
                throw new IllegalStateException("EndTime内にHH:mmが存在しません。");

            //Seasonが0または1じゃない場合は、throw
            if(!(Season == 0) && !(Season == 1))
                throw new IllegalStateException("Seasonの値が不正です。");

            //フィールドの値が正常ならばbuilderをコンストラクタに渡す
            return new TimeTable(this);
        }
    }
}
