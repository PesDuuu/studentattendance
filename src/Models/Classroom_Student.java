package Models;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

public class Classroom_Student {
    private Integer classroomId;
    private String studentCode;

    public Classroom_Student() {
    }

    public Classroom_Student(Integer classroomId, String studentCode) {
        this.classroomId = classroomId;
        this.studentCode = studentCode;
    }

    public Integer getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Integer classroomId) {
        this.classroomId = classroomId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
}
