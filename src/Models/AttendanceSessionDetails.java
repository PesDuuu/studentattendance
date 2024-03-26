package Models;

import java.time.format.DateTimeFormatter;

public class AttendanceSessionDetails {
    int attendanceSessionId;
    String studentCode;
    DateTimeFormatter timeCheckIn;
    String base64Image;

    public AttendanceSessionDetails() {
    }

    public AttendanceSessionDetails(int attendanceSessionId, String studentCode, DateTimeFormatter timeCheckIn, String base64Image) {
        this.attendanceSessionId = attendanceSessionId;
        this.studentCode = studentCode;
        this.timeCheckIn = timeCheckIn;
        this.base64Image = base64Image;
    }

    public int getAttendanceSessionId() {
        return attendanceSessionId;
    }

    public void setAttendanceSessionId(int attendanceSessionId) {
        this.attendanceSessionId = attendanceSessionId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public DateTimeFormatter getTimeCheckIn() {
        return timeCheckIn;
    }

    public void setTimeCheckIn(DateTimeFormatter timeCheckIn) {
        this.timeCheckIn = timeCheckIn;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
