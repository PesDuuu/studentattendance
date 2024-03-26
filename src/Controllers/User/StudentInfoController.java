package Controllers.User;

import Models.Student;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class StudentInfoController {

    @FXML
    Button btnClose;
    @FXML
    TextField txtStudentID, txtStudentName, txtAddress,txtDateOfBirth, txtClassCode;
    @FXML
    ImageView imageAvatar;

    public void getStudent(Student student) {
        txtStudentID.setText(student.getStudentCode());
        txtStudentName.setText(student.getFullName());
        txtDateOfBirth.setText(dateFormat(student.getDateOfBirth()));
        txtAddress.setText(student.getAddress());
        txtClassCode.setText(student.getClassCode());
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

    private static String dateFormat(Date date) {
        String dateStr = new SimpleDateFormat("dd/MM/yyyy").format(date);
        return dateStr;
    }

    @FXML
    public void CloseForm() throws IOException {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
