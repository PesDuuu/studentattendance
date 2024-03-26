package Controllers.User;

import Models.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.ResourceBundle;

public class mainFormController implements Initializable {
    @FXML
    private Label lblHello;
    @FXML
    private FlowPane flpMainForm;
    @FXML
    Button btnLogout,btnChangePassword;
    @FXML
    ImageView imgLogout, imgChangePassword;

    Image Image_Classroom;
    Image Image_Info;
    Image Image_Statistics;

    Account account = new Account();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImage();
        File classroom = new File("resources/images/classroom.png");
        Image_Classroom = new Image(classroom.toURI().toString());

        File info = new File("resources/images/list-icon.png");
        Image_Info = new Image(info.toURI().toString());

        File statistic = new File("resources/images/statistics-icon.png");
        Image_Statistics = new Image(statistic.toURI().toString());

        btnChangePassword.setStyle("-fx-border-width: 0;-fx-background-color: transparent");
    }
    private void setImage(){
        File logout = new File("resources/images/Logout.png");
        Image Logout = new Image(logout.toURI().toString());
        imgLogout.setImage(Logout);

        File ImgLinkChangePassword = new File("resources/images/change-password.png");
        Image ImgChangePassword = new Image(ImgLinkChangePassword.toURI().toString());
        imgChangePassword.setImage(ImgChangePassword);
    }


    public void setTeacher(Teacher teacher) throws IOException {

        lblHello.setText("Xin chào: " + teacher.getFullName());
        ArrayList<Classroom> listClassroom = new ClassroomDAO().listClass(teacher.getTeacherCode());
        account = new AccountDAO().getUserName(teacher.getTeacherCode().toLowerCase());
        if (listClassroom.size() > 0) {
            for (Classroom classroom : listClassroom) {

                Button btn = new Button();
                //image for classroom
                ImageView view = new ImageView(Image_Classroom);
                view.setFitHeight(50);
                HBox hBoxBtn = new HBox();
                hBoxBtn.setSpacing(10);
                view.setPreserveRatio(true);
                //add label to vbox
                VBox vBoxLabel = new VBox();
                Label labelClassroomId= new Label("Mã Lớp: " + classroom.getClassroomId());
                Label labelClassroomName= new Label(classroom.getClassroomName());
                Label labelCreateDte= new Label("Ngày tạo: " + dateFormat(classroom.getCreateDate()));
                vBoxLabel.getChildren().addAll(labelClassroomId,labelClassroomName,labelCreateDte);
                //add info student of class to vbox
                VBox vBoxBtn = new VBox();

                ImageView img_stat = new ImageView(Image_Statistics);
                img_stat.setFitWidth(30);
                img_stat.setFitHeight(30);
                img_stat.setStyle("-fx-cursor: hand;");
                img_stat.setOnMouseClicked(event -> {
                    try {
                        openStatisticOfClassroom(classroom.getClassroomId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


                ImageView img_info = new ImageView(Image_Info);
                img_info.setFitWidth(30);
                img_info.setFitHeight(35);
                img_info.setStyle("-fx-cursor: hand;");
                img_info.setOnMouseClicked(event -> {
                    try {
                        openStudentList(classroom.getClassroomId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                vBoxBtn.setSpacing(5);
                vBoxBtn.getChildren().addAll(img_stat,img_info);

                //add image & vbox to hbox
                hBoxBtn.getChildren().addAll(view,vBoxLabel);
                hBoxBtn.setSpacing(10);
                //add hbox to btn
                btn.setGraphic(hBoxBtn);
                btn.setPrefWidth(250);
                btn.setPadding(new Insets(10, 10, 10, 10));
                btn.setStyle("-fx-background-color: transparent;"+
                        "-fx-cursor: hand ;"+
                        "-fx-border-color: transparent, black;" +
                        "-fx-border-width: 1, 1;" +
                        "-fx-border-style: solid;" +
                        "-fx-border-radius: 0, 0;" +
                        "-fx-border-insets: 1 1 1 1, 0;" +
                        "-fx-border-radius: 10");

                //add btn, icon for hboxfl
                HBox hBoxFlp = new HBox();
                hBoxFlp.setSpacing(15);
                hBoxFlp.getChildren().addAll(btn,vBoxBtn);
                hBoxFlp.setStyle("-fx-padding: 10; -fx-background-color: cornsilk; -fx-border-color: #212121; -fx-border-radius: 5");


                //flow pane of main form
                flpMainForm.getChildren().addAll(hBoxFlp);
                flpMainForm.setHgap(30);
                flpMainForm.setPadding(new Insets(20, 20, 20, 40));
                flpMainForm.setPrefWrapLength(10);
                flpMainForm.setVgap(30);
                btn.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Object node = event.getSource();
                        Button b = (Button) node;
                        int btnClassroomId = Integer.valueOf(labelClassroomId.getText().split(" ")[2]);
                        try {
                            Classroom classroom = new ClassroomDAO().getClassroomById(btnClassroomId);
                            faceDetection(classroom);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    }

    private static String dateFormat(Date date) {
        String dateStr = new SimpleDateFormat("dd/MM/yyyy").format(date);
        return dateStr;
    }

    public void faceDetection(Classroom classroom) throws IOException {
        Stage pry = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/User/faceDetectionForm.fxml"));
        Parent root = loader.load();
        FaceDetectionController FDTController = loader.getController();
        FDTController.init();
        FDTController.setAttendanceOfClassroom(classroom);
        pry.initStyle(StageStyle.UNDECORATED);
        pry.initModality(Modality.APPLICATION_MODAL);
        pry.setScene(new Scene(root));
        pry.showAndWait();
    }

    public void Logout() throws IOException {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
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

    public void openStudentList(int classroomId) throws IOException {
        Stage addStudentToAttendance = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/User/showStudentListOfClassroomForm.fxml"));
        Parent root = loader.load();
        ShowStudentListOfClassroomController showStudentListOfClassroomController = loader.getController();
        showStudentListOfClassroomController.loadStudentList(String.valueOf(classroomId));
        addStudentToAttendance.initStyle(StageStyle.UNDECORATED);
        addStudentToAttendance.initModality(Modality.APPLICATION_MODAL);
        addStudentToAttendance.setScene(new Scene(root));
        addStudentToAttendance.getScene().getRoot().setEffect(new DropShadow());
        addStudentToAttendance.showAndWait();
    }
    private TableView table = new TableView();

    private void openStatisticOfClassroom(int idClassroom) throws Exception{
        Stage addStudentToAttendance = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/User/AttendanceListForm.fxml"));
        Parent root = loader.load();
        AttendanceListController attendanceListController = loader.getController();
        attendanceListController.loadStudentList(String.valueOf(idClassroom));
        addStudentToAttendance.initStyle(StageStyle.UNDECORATED);
        addStudentToAttendance.initModality(Modality.APPLICATION_MODAL);
        addStudentToAttendance.setScene(new Scene(root));
        addStudentToAttendance.getScene().getRoot().setEffect(new DropShadow());
        addStudentToAttendance.showAndWait();
    }

    @FXML
    private void openChangePasswordForm() throws IOException {
        Stage addStudentToAttendance = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/User/changePasswordForm.fxml"));
        Parent root = loader.load();
        changePasswordController changePasswordController = loader.getController();
        changePasswordController.init(account);
        addStudentToAttendance.initStyle(StageStyle.UNDECORATED);
        addStudentToAttendance.initModality(Modality.APPLICATION_MODAL);
        addStudentToAttendance.setScene(new Scene(root));
        addStudentToAttendance.getScene().getRoot().setEffect(new DropShadow());
        addStudentToAttendance.showAndWait();
    }

}
