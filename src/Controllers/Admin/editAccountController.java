package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class editAccountController implements Initializable {
    @FXML
    TextField txtAccountID,txtUsername,txtPassword,txtType;
    @FXML
    Button btnSave,btnClose;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCBB();
    }

    @FXML
    ComboBox cbbTeacherCode;
    AccountDAO accountDAO = new AccountDAO();
    TeacherDAO teacherDAO = new TeacherDAO();
    AlertCustom alertCustom = new AlertCustom();

    private void loadCBB() {
        ArrayList<Teacher> teachers = teacherDAO.getTeacherList();
        for (Teacher teacher : teachers) {
            cbbTeacherCode.getItems().addAll(teacher.getTeacherCode());
        }
    }
    public void getID(Account account){
        txtAccountID.setText(String.valueOf(account.getAccountId()));
        txtUsername.setText(account.getUsername());
//        txtPassword.setText(account.getPassword());
        cbbTeacherCode.setPromptText(account.getTeacherCode());
        txtType.setText(String.valueOf(account.getType()));
    }
    public void Save() throws IOException {
        String ID =txtAccountID.getText();
        String userName = txtUsername.getText();
        String password = txtPassword.getText();
        String teacherCode = (String) cbbTeacherCode.getSelectionModel().getSelectedItem();
        if(teacherCode == null ){
            teacherCode = cbbTeacherCode.getPromptText();
        }
        String type = txtType.getText();
        Account account = new Account(Integer.valueOf(ID),userName,password,teacherCode,Integer.valueOf(type));
        if (userName.isEmpty()||teacherCode.isEmpty()||type.isEmpty()) {
            alertCustom.alertError("Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        if(accountDAO.update(account)){
            alertCustom.alertSuccess("Cập nhật tài khoản thành công!");
        }else {
            alertCustom.alertError("Đã xảy ra lỗi");
        }
        Close();
    }

    public void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }


}
