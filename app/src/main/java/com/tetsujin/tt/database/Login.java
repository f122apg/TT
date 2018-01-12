package com.tetsujin.tt.database;

public class Login
{
    String id;
    String StudentID;
    String MailAddress;
    public final static String COLUMN_STUDENTID = "StudentID";

    Login(String sid, String ma)
    {
        setStudentID(sid);
        setMailAddress(ma);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setStudentID(String studentID)
    {
        this.StudentID = studentID;
    }
    
    public void setMailAddress(String mailAddress)
    {
        this.MailAddress = mailAddress;
    }

    public String getId()
    {
        return id;
    }

    public String getStudentID()
    {
        return StudentID;
    }
    
    public String getMailAddress()
    {
        return MailAddress;
    }
}
