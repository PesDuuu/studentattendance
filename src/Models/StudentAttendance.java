package Models;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class StudentAttendance {
    String studentCode;
    String fullName;
    Timestamp timeCheckIn;
    String base64Image;

    public StudentAttendance() {
    }

    public StudentAttendance(String studentCode, String fullName, Timestamp timeCheckIn, String base64Image) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.timeCheckIn = timeCheckIn;
        this.base64Image = base64Image;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Timestamp getTimeCheckIn() {
        return timeCheckIn;
    }

    public void setTimeCheckIn(Timestamp timeCheckIn) {
        this.timeCheckIn = timeCheckIn;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
