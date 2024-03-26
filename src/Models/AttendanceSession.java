package Models;

import java.util.Date;

public class AttendanceSession {
    int attendanceSessionId;
    int numberSession;
    int classroomId;
    Date attendanceDate;

    public int getAttendanceSessionId() {
        return attendanceSessionId;
    }

    public void setAttendanceSessionId(int attendanceSessionId) {
        this.attendanceSessionId = attendanceSessionId;
    }

    public int getNumberSession() {
        return numberSession;
    }

    public void setNumberSession(int numberSession) {
        this.numberSession = numberSession;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public AttendanceSession(int attendanceSessionId, int numberSession, int classroomId, Date attendanceDate) {
        this.attendanceSessionId = attendanceSessionId;
        this.numberSession = numberSession;
        this.classroomId = classroomId;
        this.attendanceDate = attendanceDate;
    }

    public AttendanceSession() {
    }
}
