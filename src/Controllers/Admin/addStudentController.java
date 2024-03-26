package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Controllers.Alert.alertErrorController;
import Models.Class;
import Models.ClassDAO;
import Models.Student;
import Models.StudentDAO;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;


public class addStudentController implements Initializable {
    @FXML
    Button btnClose,btnSave, btnLoadImage;
    @FXML
    TextField txtStudentCode,txtFullName,txtAddress;
    @FXML
    DatePicker dpDateOfBirth;
    @FXML
    ComboBox <String> cbbClassCode;
    @FXML
    ImageView imgAvatar;

    StudentDAO studentDAO = new StudentDAO();
    FileChooser fileChooser = new FileChooser();
    AlertCustom alertCustom = new AlertCustom();

    public void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }
    private void loadCBB() {
        ClassDAO classDAO = new ClassDAO();
        ArrayList<Models.Class> classes = classDAO.getClassList();
        for( Class iclass : classes){
            cbbClassCode.getItems().addAll(iclass.getClassCode());
        }
    }

    @FXML
    private void loadImage(){
        Window stage = btnLoadImage.getScene().getWindow();
        fileChooser.setTitle("Thêm hình ảnh");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image","*.jpg","*.png"));
        try{
            File file = fileChooser.showOpenDialog(stage);
//            System.out.println(file.toString());
//            String link = file.toString();
//            linkImage.setText(link);
            fileChooser.setInitialDirectory((file.getParentFile()));
            Image anhdaidien = new Image(file.toURI().toString());
            imgAvatar.setImage(anhdaidien);
        }catch (Exception e){

        }
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9]+");
    }

    public void Save() throws IOException {
        String studentCode = txtStudentCode.getText();
        String fullName = txtFullName.getText();
        String address = txtAddress.getText();
        String classCode = cbbClassCode.getSelectionModel().getSelectedItem();

        BufferedImage bImage = SwingFXUtils.fromFXImage(imgAvatar.getImage(), null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", s);
        byte[] res  = s.toByteArray();
        s.close();
        String image_base64 = Base64.getEncoder().encodeToString(res);
//        System.out.println(image_base64);

        if(dpDateOfBirth.getValue()==null||studentCode.isEmpty()||fullName.isEmpty()||address.isEmpty()||classCode.isEmpty()){
            alertCustom.alertError("Vui lòng điển đầy đủ thông tin!");
            return;
        }
        if (studentCode.length() != 10){
            alertCustom.alertError("Mã sinh viên 10 chữ số!");
            return;
        }
        if(studentDAO.checkStudentIDExit(studentCode)==true){
            alertCustom.alertError("Mã sinh viên đã tồn tại!");
            return;
        }
        Student student = new Student(studentCode,fullName,Date.valueOf(dpDateOfBirth.getValue()),address,classCode,image_base64);
        if (studentDAO.insertStudent(student)) {
            alertCustom.alertSuccess("Thêm sinh viên thành công!");
            Close();
        } else {
            alertCustom.alertError("Đã xảy ra lỗi!");
            return;
        }
    }
    public void alertSuccess() throws IOException {
        Stage successForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Alert/alertSuccessfulForm.fxml"));
        Parent root = loader.load();
        successForm.setScene(new Scene(root));
        successForm.initStyle(StageStyle.UNDECORATED);
        successForm.initModality(Modality.APPLICATION_MODAL);
        successForm.showAndWait();
    }
    public void alertError(String errorName) throws IOException{
        Stage errorForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Alert/alertErrorForm.fxml"));
        Parent root = loader.load();
        errorForm.setScene(new Scene(root));
        alertErrorController alertErrorController = loader.getController();
        alertErrorController.getNameError(errorName);
        errorForm.initStyle(StageStyle.UNDECORATED);
        errorForm.initModality(Modality.APPLICATION_MODAL);
        errorForm.showAndWait();
    }

    public void setImage() {
        File logo = new File("resources/images/User.png");
        Image Logo = new Image(logo.toURI().toString());
        imgAvatar.setImage(Logo);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCBB();
        setImage();
    }
}
