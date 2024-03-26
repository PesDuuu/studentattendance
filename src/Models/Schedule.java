package Models;

import java.util.Date;

public class Schedule {
    private int classroomId;
    private String classroomName;
    private Date createDate;
    private String teacherCode;
    private String studentCode;

    public Schedule() {
    }

    public Schedule(int classroomId, String classroomName, Date createDate, String teacherCode, String studentCode) {
        this.classroomId = classroomId;
        this.classroomName = classroomName;
        this.createDate = createDate;
        this.teacherCode = teacherCode;
        this.studentCode = studentCode;
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

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
}

