package Controllers.User;

import Models.Account;
import Models.AccountDAO;
import Models.Faculty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Controllers.Alert.AlertCustom;
import java.io.IOException;

public class changePasswordController {
    @FXML
    Button btnClose, btnSave;

    @FXML
    TextField txtOldPassword, txtNewPassword,txtNewPasswordConfirm;
    AlertCustom alertCustom = new AlertCustom();

    Account account = new Account();
    AccountDAO accountDAO = new AccountDAO();
    public void init(Account acc){
        account = acc;
    }

    public void Save() throws IOException {
        String oldPassword = txtOldPassword.getText();
        String newPassword = txtNewPassword.getText();
        String newPasswordConfirm = txtNewPasswordConfirm.getText();
        if(oldPassword.isEmpty() || newPassword.isEmpty() || newPasswordConfirm.isEmpty()){
            alertCustom.alertError("Không được để trống!");
            return;
        }

        if(!newPassword.equals(newPasswordConfirm)){
            alertCustom.alertError("Mật khẩu mới không khớp!");
            return;
        }


        if(accountDAO.validateLogin(account.getUsername(),oldPassword) == -1){
            alertCustom.alertError("Mật khẩu cũ không đúng!");
            return;
        }

        if (accountDAO.changePassword(account.getAccountId(),newPassword)) {
            alertCustom.alertSuccess("Đổi mật khẩu thành công!");
            Close();
        } else {
            alertCustom.alertError("Đã xảy ra lỗi khi đổi mật khẩu!");
            return;
        }
    }

    public void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }
}
