package Models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Classroom {
    int classroomId;
    String classroomName;
    Date createDate;
    String teacherCode;

    public Classroom() {
    }

    public Classroom(int classroomId, String classroomName, Date createDate, String teacherCode) {
        this.classroomId = classroomId;
        this.classroomName = classroomName;
        this.createDate = createDate;
        this.teacherCode = teacherCode;
    }
    public Classroom(String classroomName, Date createDate, String teacherCode) {
        this.classroomName = classroomName;
        this.createDate = createDate;
        this.teacherCode = teacherCode;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }
}