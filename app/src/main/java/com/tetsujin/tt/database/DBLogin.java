package com.tetsujin.tt.database;

public class DBLogin
{
    int id;
    private String StudentID;
    private String MailAddress;
    
    public void setId(int id)
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
    
    public int getId()
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
