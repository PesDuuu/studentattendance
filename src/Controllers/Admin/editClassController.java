package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.Class;
import Models.ClassDAO;
import Models.Faculty;
import Models.FacultyDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class editClassController implements Initializable {
    @FXML
    Button btnClose,btnSave;

    @FXML
    TextField txtClassID,txtClassName,txtOldClass;
    @FXML
    ComboBox <String> cbbFaculty;
    ClassDAO classDAO = new ClassDAO();
    FacultyDAO facultyDAO = new FacultyDAO();
    AlertCustom alertCustom = new AlertCustom();
    private void loadCBB() {
        ArrayList<Faculty> faculties = facultyDAO.getFacultyList();
        for(Faculty faculty : faculties){
            cbbFaculty.getItems().addAll(faculty.getFacultyName());
        }
    }
    public void setClass(Class iclass){
        txtClassID.setText(iclass.getClassCode());
        txtClassName.setText(iclass.getClassName());
        cbbFaculty.setPromptText(iclass.getFacultyCode());
        txtOldClass.setText(iclass.getClassCode());
    }

    private boolean countLowerCase(String s) {
        System.out.println(s.codePoints().filter(c-> c>='A' && c<='Z').count());
        return s.codePoints().filter(c-> c>='a' && c<='z').count() > 0;
    }

    public void enterForClassName(){
        txtClassID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int len = txtClassID.getText().length();
                if(countLowerCase(txtClassID.getText())){
                    txtClassID.setText(txtClassID.getText().toUpperCase());
                    return;
                }
                switch (len){
                    case 0:
                        txtClassName.setText("");
                        txtClassName.clear();
                        break;
                    case 1:
                        if(isNumeric(txtClassID.getText(0,1))){
                            txtClassName.setText("Lớp "+txtClassID.getText()+" Khóa "+txtClassID.getText(0,1));
                        }else {
                            try {
                                alertCustom.alertError("Vui lòng nhập sô!");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            txtClassID.setText("");
                            txtClassName.setText("");
                        }
                        break;
                    case 2:
                        if(isNumeric(txtClassID.getText(0,2))){
                            txtClassName.setText("Lớp "+txtClassID.getText()+" Khóa "+txtClassID.getText(0,2));
                        }else {
                            try {
                                alertCustom.alertError("Vui lòng nhập sô!");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            txtClassID.setText(txtClassID.getText(0,len -1));
                            txtClassName.setText("");
                        }
                        break;
                    case 3:
                        if(isNumeric(txtClassID.getText(len -1,len)) == false){
                            if(checkEducationProgram(txtClassID.getText(2,3)).isEmpty()){
                                try {
                                    alertCustom.alertError("Chương trình đào tạo không hợp lệ");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                txtClassID.setText(txtClassID.getText(0,len -1));
                                txtClassName.setText("");
                            }else {
                                txtClassName.setText("Lớp "+txtClassID.getText()+" Khóa "+txtClassID.getText(0,2) +" " + checkEducationProgram(txtClassID.getText(2,3)));
                            }
                        }else {
                            try {
                                alertCustom.alertError("Vui lòng nhập kí tự");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            txtClassID.setText(txtClassID.getText(0,len -1));
                            txtClassName.setText("");
                        }
                        break;
                    case 4:
                        if(isNumeric(txtClassID.getText(len -1, len)) == false){
                            txtClassName.setText("Lớp "+txtClassID.getText()+" Khóa "+txtClassID.getText(0,2) +" Khoa " + checkEducationProgram(txtClassID.getText(2,3)));
                        }else {
                            try {
                                alertCustom.alertError("Vui lòng nhập kí tự");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            txtClassID.setText(txtClassID.getText(0,len - 1));
                            txtClassName.setText("");
                        }
                        break;
                    case 5:
                        if(isNumeric(txtClassID.getText(4,5)) == false){
                            String facultyName = facultyDAO.getFacultyNameByFacultyCode(txtClassID.getText(3,5));
                            if(facultyName.isEmpty()){
                                txtClassID.setText(txtClassID.getText(0,len -1));
                                try {
                                    alertCustom.alertError("Mã khoa không tồn tại. Vui lòng nhập lại!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                cbbFaculty.setValue(facultyName);
                                txtClassName.setText("Lớp "+txtClassID.getText()+" Khóa "+txtClassID.getText(0,2) +" Khoa " +facultyName +" " + checkEducationProgram(txtClassID.getText(2,3)));
                            }
                        }else {
                            try {
                                alertCustom.alertError("Vui lòng nhập kí tự");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            txtClassID.setText(txtClassID.getText(0,len -1));
                            txtClassName.setText("");
                        }
                        break;
                    case 6:
                    case 7:
                    case 8:
                        txtClassName.setText("Lớp "+txtClassID.getText()+" Khóa "+txtClassID.getText(0,2) +" Khoa " +facultyDAO.getFacultyNameByFacultyCode(txtClassID.getText(3,5)) +" " + checkEducationProgram(txtClassID.getText(2,3)));
                        txtClassID.setText(txtClassID.getText());
                        break;
                    case 9:
                        try {
                            alertCustom.alertError("Mã lớp học quá dài!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        txtClassID.setText(txtClassID.getText(0,8));
                        break;
                }
            }
        });
    }

    @FXML
    public void changeCBBFaculty(){
        String cbbFacultyName =  cbbFaculty.getSelectionModel().getSelectedItem();
        if(cbbFacultyName == null ){
            cbbFacultyName ="";
        }
        int len = txtClassID.getText().length();
        System.out.println(cbbFacultyName);
        if(len > 1){
            txtClassName.setText("Lớp "+txtClassID.getText()+" Khóa "+txtClassID.getText(0,2) +" Khoa " +cbbFacultyName);
        }
        if(len > 2){
            txtClassName.setText("Lớp "+txtClassID.getText()+" Khóa "+txtClassID.getText(0,2) +" Khoa " +cbbFacultyName +" " + checkEducationProgram(txtClassID.getText(2,3)));
        }
    }

    private String checkEducationProgram(String educationProgram){
        if(educationProgram.equals("d") || educationProgram.equals("D")){
            return "Hệ Đại học";
        }
        else{
            if(educationProgram.equals("c") || educationProgram.equals("C")){
                return "Hệ Cao đẳng";
            }else {
                return "";
            }
        }
    }


    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }
    @FXML
    public void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }

    public void Save() throws IOException {
        String ID = txtClassID.getText();
        String clasName = txtClassName.getText();
        String faucltyName = cbbFaculty.getSelectionModel().getSelectedItem();
        if (faucltyName==null){
            faucltyName=cbbFaculty.getPromptText();
        }
        String facultyCode = new FacultyDAO().getFacultyCodeByFacultyName(faucltyName);
        Class iclass = new Class(ID,clasName,facultyCode);
        if (ID.isEmpty() || clasName.isEmpty() || facultyCode == "") {
            alertCustom.alertError("Vui lòng nhập đầy đủ các thông tin!");
            return;
        }
        if (ID.length() < 7 || ID.length() > 10) {
            alertCustom.alertError("Độ dài mã lớp không hợp lệ!");
            return;
        } else {
            if (classDAO.update(iclass)) {
                alertCustom.alertSuccess("Cập nhật lớp thành công!");
            } else {
                alertCustom.alertError("Đã xảy ra lỗi!");
            }
            Close();
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCBB();
        enterForClassName();
    }
}
