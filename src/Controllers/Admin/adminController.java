package Controllers.Admin;


import Models.Account;
import Models.AccountDAO;
import Models.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class adminController implements Initializable {
    @FXML
    ImageView btnClose,imgLogout,imgVClass,imgVStudent,imgSlogo;

    @FXML
    ImageView imgVTeacher,imgVClassroom,imgVFaculty,imgVAccount,imgVShedule,imgLogo;

    @FXML
    Button btnOpenClassForm,btnOpenStudentForm,btnOpenTeacherForm,btnLogout;

    @FXML
    BorderPane bdP,bdpMain;

    @FXML
    Text lblTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImage();
    }

     public void loadclassForm(String ui) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../../Views/Admin/"+ui+"Form.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bdpMain.setCenter(root);
    }

    public void setAdmin(Account account) throws  IOException{
        lblTitle.setText("Xin chào "+account.getUsername()+"!");
    }

    @FXML
    private void Close(MouseEvent event){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openStudentForm(){
        loadclassForm("student");
        lblTitle.setText("Danh Sách Sinh Viên");
    }
    @FXML
    private void openClassForm(){
        loadclassForm("class");
        lblTitle.setText("Danh Sách Lớp Học");
    }
    @FXML
    private void openTeacherForm(){
        loadclassForm("teacher");
        lblTitle.setText("Danh Sách Giảng Viên");
    }

    @FXML
    private void openAccountForm()
    {
        loadclassForm("account");
        lblTitle.setText("Danh Sách Tài Khoản");
    }

    @FXML
    private void openFacultyForm(){
        loadclassForm("faculty");
        lblTitle.setText("Danh Sách Khoa");
    }

    @FXML
    private void openClassroomForm()   {
        loadclassForm("classroom");
        lblTitle.setText("Danh Sách Phòng Học");
    }

    @FXML
    private void openScheduleForm(){
        loadclassForm("schedule");
        lblTitle.setText("Danh Sách Lịch Học");
    }

    public void Back(MouseEvent event) throws IOException {
        Stage stage = (Stage)btnLogout.getScene().getWindow();
        stage.close();
        Stage Login = new Stage();
        File add = new File("resources/images/icon-app.jpg");
        Image addClass = new Image(add.toURI().toString());
        Login.getIcons().add(addClass);
        FXMLLoader loader = new FXMLLoader();
        Login.initStyle(StageStyle.UNDECORATED);
        loader.setLocation(getClass().getResource("/Views/login.fxml"));
        Parent root1 = loader.load();
        Login.setScene(new Scene(root1));
        Login.show();
    }
    public void setImage(){
        File logo = new File("resources/images/LOGO.png");
        Image Logo = new Image(logo.toURI().toString());
        imgLogo.setImage(Logo);

        File slogo = new File("resources/images/LOGO.png");
        Image sLogo = new Image(slogo.toURI().toString());
        imgSlogo.setImage(sLogo);

        File C = new File("resources/images/1class.png");
        Image c = new Image(C.toURI().toString());
        imgVClass.setImage(c);

        File logout = new File("resources/images/Logout.png");
        Image Logout = new Image(logout.toURI().toString());
        imgLogout.setImage(Logout);

        File fstudent = new File("resources/images/Student.png");
        Image fStudent = new Image(fstudent.toURI().toString());
        imgVStudent.setImage(fStudent);

        File teacher = new File("resources/images/teacher.png");
        Image Teacher = new Image(teacher.toURI().toString());
        imgVTeacher.setImage(Teacher);

        File user = new File("resources/images/account.png");
        Image User = new Image(user.toURI().toString());
        imgVAccount.setImage(User);

        File shedule = new File("resources/images/shedule.png");
        Image Shedule = new Image(shedule.toURI().toString());
        imgVShedule.setImage(Shedule);

        File faculty = new File("resources/images/faculty.png");
        Image Faculty = new Image(faculty.toURI().toString());
        imgVFaculty.setImage(Faculty);

        File classroom = new File("resources/images/classroom.png");
        Image Classroom = new Image(classroom.toURI().toString());
        imgVClassroom.setImage(Classroom);
    }
}
