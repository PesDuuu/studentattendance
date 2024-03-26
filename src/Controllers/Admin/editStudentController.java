package Controllers.Admin;

import Controllers.Alert.AlertCustom;
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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.*;
import javafx.scene.image.Image;
import javafx.util.StringConverter;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;

public class editStudentController implements Initializable {
    @FXML
    Button btnSave, btnClose, btnLoadImage;
    @FXML
    TextField txtStudentID, txtStudentName, txtAddress;
    @FXML
    DatePicker txtDateOfBirth;
    @FXML
    ComboBox <String> cbbClassCode;
    @FXML
    ImageView imageAvatar;

    StudentDAO studentDAO = new StudentDAO();
    ClassDAO classDAO = new ClassDAO();
    FileChooser fileChooser = new FileChooser();
    AlertCustom alertCustom = new AlertCustom();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCBB();
    }

    public void loadCBB(){
        ArrayList<Class> classes= classDAO.getClassList();
        for (Class iclass : classes){
            cbbClassCode.getItems().addAll(iclass.getClassCode());
        }
    }
    public String convert(Date date){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
        String Date = DATE_FORMAT.format(date);
        return Date;
    }
    public void getStudent(Student student) {
        txtStudentID.setText(student.getStudentCode());
        txtStudentName.setText(student.getFullName());
        txtDateOfBirth.valueProperty().setValue(LocalDate.parse(String.valueOf(student.getDateOfBirth())));
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        txtDateOfBirth.setConverter(converter);
        txtDateOfBirth.setPromptText("dd/MM/yyyy");
        txtAddress.setText(student.getAddress());
        cbbClassCode.setPromptText(student.getClassCode());
//        System.out.println(student.getBase64Image());
        if(student.getBase64Image()!=null){
            imageAvatar.setImage(convertBase64ImageToImage((student.getBase64Image())));
        }
    }

    private Image convertBase64ImageToImage(String base64Image){
        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image avt_image = SwingFXUtils.toFXImage(image, null);
        return avt_image;
    }

    public void Close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void Save() throws IOException {
        String studentCode = txtStudentID.getText();
        String fullName = txtStudentName.getText();
        Date dateOfBirth = Date.valueOf(txtDateOfBirth.valueProperty().getValue());
        String address = txtAddress.getText();
        String classCode = cbbClassCode.getSelectionModel().getSelectedItem();

        String  image_base64 = null;
        if(imageAvatar.getImage() != null){
            BufferedImage bImage = SwingFXUtils.fromFXImage(imageAvatar.getImage(), null);
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", s);
            byte[] res  = s.toByteArray();
            s.close();
            image_base64 = Base64.getEncoder().encodeToString(res);
        }

        System.out.println(image_base64);

        if (classCode==null){
            classCode= cbbClassCode.getPromptText();
        }
        Student student = new Student(studentCode,fullName,dateOfBirth,address,classCode,image_base64);

        if (fullName.isEmpty() || dateOfBirth == null || address.isEmpty()) {
            alertCustom.alertError("Vui lòng điền đầy đủ thôn tin!");
            return;
        }
        if (studentDAO.update(student)) {
            alertCustom.alertSuccess("Cập nhật thông tin thành công!");
            Close();
        } else {
            alertCustom.alertError("Đã xảy ra lỗi!");
            return;
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
            imageAvatar.setImage(anhdaidien);
        }catch (Exception e){

        }
    }
}
