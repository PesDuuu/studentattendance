package Models;

import com.mysql.cj.xdevapi.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Faculty {
    private String facultyCode;
    private String facultyName;

    public Faculty() {
    }

    public Faculty(String facultyCode, String facultyName  ) {
        this.facultyCode = facultyCode;
        this.facultyName = facultyName;
    }

    public String getFacultyCode() { return facultyCode; }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

}