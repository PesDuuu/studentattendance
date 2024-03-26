package Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Teacher {
    String teacherCode;
    String fullName;
    String email;
    String phoneNumber;

    public Teacher() {
    }

    public Teacher(String teacherCode, String fullName, String email, String phoneNumber) {
        this.teacherCode = teacherCode;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}


