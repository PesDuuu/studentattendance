package Models;

//tên class này tương ứng với table của database
public class Class {
    private String classCode;
    private String className;
    private String facultyCode;

    public Class() {
    }

    public Class(String classCode, String className, String facultyCode) {
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
