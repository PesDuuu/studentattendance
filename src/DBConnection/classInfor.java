package DBConnection;

public class classInfor {
    String classCode;
    String className;
    String facultyCode;

    public classInfor() {
    }

    public classInfor(String classCode, String className, String facultyCode) {
        this.classCode = classCode;
        this.className = className;
        this.facultyCode = facultyCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }
}
