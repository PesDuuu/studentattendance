package Controllers.Admin;

import Models.Faculty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class showAddFacultyExcelFileController implements Initializable {

    @FXML
    ImageView imgBack;

    @FXML
    Button btnBack;

    @FXML
    TableView <Faculty> tbvFacultySuccess;
    @FXML
    TableColumn <Faculty, String> colSuccessFacultyCode;
    @FXML
    TableColumn <Faculty, String> colSuccessFacultyName;

    @FXML
    TableView <Faculty> tbvFacultyExist;
    @FXML
    TableColumn <Faculty, String> colExistFacultyCode;
    @FXML
    TableColumn <Faculty, String> colExistFacultyName;

    @FXML
    TableView <facultyError> tbvFacultyError;
    @FXML
    TableColumn <facultyError, String> colErrorID;
    @FXML
    TableColumn <facultyError, String> colErrorName;

    @FXML
    private void Back(){
        Stage stage =(Stage)btnBack.getScene().getWindow();
        stage.close();
    }

    public void setImg(){
        File reload = new File("resources/images/back-adminForm.png");
        Image reLoad= new Image(reload.toURI().toString());
        imgBack.setImage(reLoad);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImg();
    }

    public class facultyError{
        String facultyCode;
        String Error;

        public facultyError(String facultyCode, String error) {
            this.facultyCode = facultyCode;
            Error = error;
        }

        public String getFacultyCode() {
            return facultyCode;
        }

        public void setFacultyCode(String facultyCode) {
            this.facultyCode = facultyCode;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }
    }

    public void getFacultyList(ObservableList<Faculty> ob, ObservableList<Faculty> exist, ObservableList<facultyError> error){
        System.out.println("Danh sách success"+ob);
        tbvFacultySuccess.setItems(ob);
        tbvFacultySuccess.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colSuccessFacultyCode.setCellValueFactory(new PropertyValueFactory<>("facultyCode"));
        colSuccessFacultyName.setCellValueFactory(new PropertyValueFactory<>("facultyName"));


        System.out.println("Danh sách exits"+exist);
        tbvFacultyExist.setItems(exist);
        tbvFacultyExist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colExistFacultyCode.setCellValueFactory(new PropertyValueFactory<>("facultyCode"));
        colExistFacultyName.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        System.out.println("Danh sách error"+error);
        System.out.println(error);
        tbvFacultyError.setItems(error);
        tbvFacultyError.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colErrorID.setCellValueFactory(new PropertyValueFactory<>("facultyCode"));
        colErrorName.setCellValueFactory(new PropertyValueFactory<>("Error"));
    }

}
