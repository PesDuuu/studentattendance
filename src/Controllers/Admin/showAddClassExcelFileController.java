package Controllers.Admin;

import Models.Student;
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
import java.sql.Date;
import java.util.ResourceBundle;

public class showAddClassExcelFileController implements Initializable {
    @FXML
    ImageView imgBack;

    @FXML
    Button btnBack;

    @FXML
    TableView<Class> tbvClassSuccess;

    @FXML
    TableColumn<Class,String> colSuccessID;

    @FXML
    TableColumn <Class,String> colSuccessName;

    @FXML
    TableColumn <Class,String> colSuccessFacultyCode;

    @FXML
    TableView<Class> tbvClassExist;

    @FXML
    TableColumn<Class,String> colExistID;

    @FXML
    TableColumn <Class,String> colExistName;

    @FXML
    TableColumn <Class,String> colExistFacultyCode;

    @FXML
    TableView<classError> tbvClassError;
    @FXML
    TableColumn <classError,String> colErrorID;
    @FXML
    TableColumn <classError,String> colErrorName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImg();
    }

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

    public class classError {
        String classCode;
        String Error;

        public classError(String classCode, String error) {
            this.classCode = classCode;
            Error = error;
        }

        public String getClassCode() {
            return classCode;
        }

        public void setClassCode(String classCode) {
            this.classCode = classCode;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }
    }

    public void getClassList(ObservableList<Class> ob, ObservableList<Class> exist, ObservableList<classError> error){
        System.out.println("Danh sách success"+ob);
        tbvClassSuccess.setItems(ob);
//        tbvClassSuccess.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colSuccessID.setCellValueFactory(new PropertyValueFactory<>("classCode"));
        colSuccessName.setCellValueFactory(new PropertyValueFactory<>("className"));
        colSuccessFacultyCode.setCellValueFactory(new PropertyValueFactory<>("facultyCode"));

        System.out.println("Danh sách exits"+exist);
        tbvClassExist.setItems(exist);
//        tbvClassExist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colExistID.setCellValueFactory(new PropertyValueFactory<>("classCode"));
        colExistName.setCellValueFactory(new PropertyValueFactory<>("className"));
        colExistFacultyCode.setCellValueFactory(new PropertyValueFactory<>("facultyCode"));

        System.out.println("Danh sách error"+error);
        System.out.println(error);
        tbvClassError.setItems(error);
//        tbvClassError.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colErrorID.setCellValueFactory(new PropertyValueFactory<>("classCode"));
        colErrorName.setCellValueFactory(new PropertyValueFactory<>("Error"));
    }
}
