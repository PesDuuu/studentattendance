package Controllers;

import Controllers.Admin.adminController;
import Controllers.User.mainFormController;
import DBConnection.DBConnection;
import Models.Account;
import Models.AccountDAO;
import Models.Teacher;
import Models.TeacherDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    @FXML
    private Button btnCancel;

    @FXML
    private Label lblLoginMessage;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private ImageView iconUser;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ImageView iconPass;

    @FXML
    public void onEnter(ActionEvent ae) throws Exception {
        loginButtonOnAction();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
//        File icon = new File("resources/images/logo.jpg");
//        Image Icon = new Image(icon.toURI().toString());
//        Stage stage = new Stage();
//        stage.getIcons().add(Icon);

        File brandingFile = new File("resources/images/background-ui.jpg");
        Image branding = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(branding);

        File user = new File("resources/images/lock.png");
        Image userImage = new Image(user.toURI().toString());
        iconPass.setImage(userImage);

        File pass = new File("resources/images/user.png");
        Image passImage = new Image(pass.toURI().toString());
        iconUser.setImage(passImage);
    }

    public void loginButtonOnAction() throws Exception {
        if(!txtUsername.getText().isEmpty() && !txtPassword.getText().isEmpty() ){
            int validateLogin = new AccountDAO().validateLogin(txtUsername.getText(),txtPassword.getText());
            if(validateLogin == 1){
                Stage stage = (Stage)btnCancel.getScene().getWindow();
                stage.close();
                AccountDAO accountDAO = new AccountDAO();
                adminForm(accountDAO.getUserName(txtUsername.getText()));
//                System.out.println("Đang đăng nhập bằng tài khoản admin");
            }else {
                if(validateLogin == 0){
                    Stage stage = (Stage)btnCancel.getScene().getWindow();
                    stage.close();
                    TeacherDAO teacher = new TeacherDAO();
                    mainForm(teacher.getTeacherInfo(txtUsername.getText()));
                    lblLoginMessage.setText("Login Successfully");
                }else {
                    lblLoginMessage.setText("Tài khoản hoặc mật khẩu sai!");
                }
            }
        }else {
            lblLoginMessage.setText("Vui lòng điền mật khẩu!");
        }

    }
    public void cancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.close();
    }

    public void mainForm(Teacher teacher) throws IOException {
        Stage mainForm = new Stage();
        File add = new File("resources/images/icon-app.jpg");
        Image addClass = new Image(add.toURI().toString());
        mainForm.getIcons().add(addClass);
        FXMLLoader loader = new FXMLLoader();
        mainForm.initStyle(StageStyle.UNDECORATED);
        loader.setLocation(getClass().getResource("/Views/User/mainForm.fxml"));
        Parent root = loader.load();
        mainFormController mainFormController = loader.getController();
        mainFormController.setTeacher(teacher);
        mainForm.setScene(new Scene(root));
        mainForm.show();
    }
    public void adminForm(Account account) throws IOException {
        Stage adminForm = new Stage();
        File add = new File("resources/images/icon-app.jpg");
        Image addClass = new Image(add.toURI().toString());
        adminForm.getIcons().add(addClass);
        adminForm.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../Views/Admin/adminForm.fxml"));
        Parent r = loader.load();
        adminController adminController = loader.getController();
        adminController.setAdmin(account);
        adminForm.setScene(new Scene(r));
        adminForm.show();
    }
}
