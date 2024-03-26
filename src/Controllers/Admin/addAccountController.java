package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Controllers.Alert.alertErrorController;
import Models.Account;
import Models.AccountDAO;
import Models.TeacherDAO;
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
import java.util.ResourceBundle;

public class addAccountController implements Initializable {

    @FXML
    Button btnClose,btnSave;

    @FXML
    TextField txtAccountID,txtUsername,txtPassword,txtTeacherCode;
    @FXML
    ComboBox cbbType;
    AccountDAO accountDAO = new AccountDAO();
    AlertCustom alertCustom = new AlertCustom();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCBB();
        autoUpperCase();
    }

    private void loadCBB(){
        cbbType.getItems().addAll("0","1");
    }

    private void autoUpperCase(){
        txtTeacherCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtTeacherCode.setText(txtTeacherCode.getText().toUpperCase());
            }
        });
    }
    @FXML
    private void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }

    public void Save() throws IOException {
        String userName = txtUsername.getText();
        String password = txtPassword.getText();
        String teacherCode = txtTeacherCode.getText();
        String type = (String)cbbType.getSelectionModel().getSelectedItem();

        if (userName.isEmpty()||password.isEmpty()||teacherCode.isEmpty()||type.isEmpty()) {
            alertCustom.alertError("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if(!(new TeacherDAO().checkTeacherCodeExist(teacherCode))){
            alertCustom.alertError("Mã giảng viên không tồn tại!");
            return;
        }

        Account account = new Account(userName,password,teacherCode,Integer.valueOf(type));
        if(accountDAO.insertAccount(account)){
            alertCustom.alertSuccess("Thêm tài khoản thành công!");
            Close();
        }
    }
}

