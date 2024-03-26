package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class teacherController implements Initializable {

    @FXML
    TableView <Teacher> TableVTeacher;

    @FXML
    TableColumn <Teacher,String> colTeacherCode;

    @FXML
    TableColumn <Teacher, String> colFullName;

    @FXML
    TableColumn <Teacher,String> colEmail;

    @FXML
    TableColumn <Teacher,String> colPhoneNumber;

    @FXML
    TableColumn<Teacher,String> colButton;
    ObservableList<Teacher> TeacherList = FXCollections.observableArrayList();

    @FXML
    Button btnAdd,btnReload,btnExcel;

    @FXML
    ImageView imgVAdd,imgVReload,imgExcel;
    @FXML
    TextField txtTeacherCode,txtFullName;

    ObservableList<Teacher> SuccessList= FXCollections.observableArrayList();
    ObservableList<teacherError> ErrorList = FXCollections.observableArrayList();
    ObservableList<Teacher> ExistList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImg();
        loadData();
        enterForTeacherCodeFilter();
        enterFullNameFilter();
    }
    Teacher teacher = new Teacher();
    TeacherDAO teacherDAO = new TeacherDAO();
    FileChooser fileChooser = new FileChooser();
    AlertCustom alertCustom = new AlertCustom();

    public void setImg(){
        File add = new File("resources/images/add-user.png");
        Image addclass = new Image(add.toURI().toString());
        imgVAdd.setImage(addclass);

        File reload = new File("resources/images/refresh.png");
        Image reloadclass = new Image(reload.toURI().toString());
        imgVReload.setImage(reloadclass);

        File excel = new File("resources/images/excel-add.png");
        Image Excel = new Image(excel.toURI().toString());
        imgExcel.setImage(Excel);
    }
    @FXML
    public void getAddView(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../../Views/Admin/addTeacherForm.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void reFresh(){
        TableVTeacher.getItems().clear();

        ArrayList<Teacher> teachers = teacherDAO.getTeacherList();
        for (Teacher teacher : teachers){
            TeacherList.add(teacher);
        }
        TableVTeacher.setItems(TeacherList);

    }

    private void loadData(){
        reFresh();
//        TableVTeacher.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colTeacherCode.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        Callback<TableColumn<Teacher, String>, TableCell<Teacher, String>> cellFoctory = (TableColumn<Teacher, String> param) -> {
            final TableCell<Teacher, String> cell = new TableCell<Teacher, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );

                        deleteIcon.setOnMouseClicked(event -> {
                            teacher = TableVTeacher.getSelectionModel().getSelectedItem();
                            if (teacherDAO.checkTeacherCodeExist(teacher.getTeacherCode())==true){
                                try {
                                    alertCustom.alertError("Bạn không thể xóa giảng viên này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                            if (new AccountDAO().checkTeacherCodeExist(teacher.getTeacherCode())==true){
                                try {
                                    alertCustom.alertError("Bạn không thể xóa giảng viên này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                           if (teacherDAO.delete(teacher)) {
                                try {
                                    alertCustom.alertSuccess("Xóa giảng viên thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                reFresh();
                            }
                        });
                        editIcon.setOnMouseClicked(event -> {
                            teacher = TableVTeacher.getSelectionModel().getSelectedItem();
                            try {
                                editForm(teacher);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            reFresh();
                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);
                        setText(null);

                    }
                }
            };
            return cell;
        };
        colButton.setCellFactory(cellFoctory);
        TableVTeacher.setItems(TeacherList);
    }

    public void editForm(Teacher teacher) throws IOException {
        Stage editForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/editTeacherForm.fxml"));
        Parent root = loader.load();
        editTeacherController editTeacherController = loader.getController();
        editTeacherController.getTeacher(teacher);
        editForm.setScene(new Scene(root));
        editForm.initStyle(StageStyle.UNDECORATED);
        editForm.initModality(Modality.APPLICATION_MODAL);
        editForm.showAndWait();
    }

    private void enterForTeacherCodeFilter(){
        txtTeacherCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String teacherCode = txtTeacherCode.getText();
                String fullName = txtFullName.getText();
                filter(teacherCode,fullName);
            }
        });
    }

    private void enterFullNameFilter(){
        txtFullName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String teacherCode = txtTeacherCode.getText();
                String fullName = txtFullName.getText();
                filter(teacherCode,fullName);
            }
        });
    }
    public void filter(String teacherCode,String fullName){
        TableVTeacher.getItems().clear();
        ArrayList<Teacher> teachers = teacherDAO.getTeacherListWithFilter(teacherCode,fullName);
        for (Teacher teacher : teachers){
            TeacherList.addAll(teacher);
        }
//        TableVTeacher.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colTeacherCode.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        Callback<TableColumn<Teacher, String>, TableCell<Teacher, String>> cellFoctory = (TableColumn<Teacher, String> param) -> {
            final TableCell<Teacher, String> cell = new TableCell<Teacher, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );

                        deleteIcon.setOnMouseClicked(event -> {
                            teacher = TableVTeacher.getSelectionModel().getSelectedItem();
                            if (teacherDAO.checkTeacherCodeExist(teacher.getTeacherCode())==true){
                                try {
                                    alertCustom.alertError("Bạn không thể xóa giảng viên này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else if (teacherDAO.delete(teacher)) {
                                try {
                                    alertCustom.alertSuccess("Xóa giảng viên thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                reFresh();
                            }
                        });

                        editIcon.setOnMouseClicked(event -> {
                            teacher = TableVTeacher.getSelectionModel().getSelectedItem();
                            try {
                                editForm(teacher);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            reFresh();
                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);
                        setText(null);

                    }
                }
            };
            return cell;
        };
        colButton.setCellFactory(cellFoctory);
        TableVTeacher.setItems(TeacherList);
    }

    public void addExcelFile(String url) {
        try {
            FileInputStream fileInputStream = new FileInputStream(url);
            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = wb.getSheetAt(0);
            int i = 0;
            for (Row row : sheet) {
                //Bỏ dòng đầu tiên
                if (i == 0) {
                    i++;
                    continue;
                }
                String ErrorInsert = "";
                String teacherCode = row.getCell(0)!=null?row.getCell(0).toString().toUpperCase():"";
                if(teacherCode==""){
                    ErrorInsert = "Mã giảng viên không được bỏ trống";
                }
                String fullName = row.getCell(1)!=null?row.getCell(1).toString():"";
                if(fullName == "" && ErrorInsert == ""){
                    ErrorInsert = "Tên không được bỏ trống";
                }

                String email = row.getCell(2)!=null?row.getCell(2).toString():"";
                if (email == "" && ErrorInsert == ""){
                        ErrorInsert = "email không được để trống";
                }
                String phoneNumber = row.getCell(3)!=null?row.getCell(3).toString():"";

                if (phoneNumber=="" && ErrorInsert==""){
                    ErrorInsert ="Số điện thoại không được bỏ trống";
                }
                phoneNumber = phoneNumber.split("\\.")[0];

                if(isNumeric(phoneNumber)==false && phoneNumber.length() !=10 && ErrorInsert == ""){
                    ErrorInsert = "Số điện thoại không hợp lệ";
                }

                if(ErrorInsert !=""){
                    teacherError tc = new teacherError(teacherCode,ErrorInsert);
                    ErrorList.add(tc);
                    continue;
                }

                TeacherDAO teacherDAO = new TeacherDAO();
                Teacher tc = new Teacher(teacherCode,fullName,email,phoneNumber);
                System.out.println("====================");
                if (teacherDAO.checkTeacherExit(tc.getTeacherCode()) == true) {
                    //add vào list tồn tại
                    ExistList.add(tc);
                    System.out.println("đã tồn tại");
                } else {
                    if (teacherDAO.insertTeacher(tc) == true) {
                        //vào list thành công
                        SuccessList.add(tc);
                        System.out.println("thành công");
                    }
                }
            }
            wb.close();
            fileInputStream.close();
            System.out.println("==========thông báo============");
            openManageForm(SuccessList,ExistList,ErrorList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9]+");
    }

    @FXML
    public void openFile(MouseEvent event) {
        Window stage = btnExcel.getScene().getWindow();
        fileChooser.setTitle("Tải lên Excel File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel file","*.xlsx","*.xlsm","*.xls","*.xltx"));
        try{
            File file = fileChooser.showOpenDialog(stage);
            System.out.println(file.toString());
            fileChooser.setInitialDirectory((file.getParentFile()));
            ExistList.clear();
            SuccessList.clear();
            ErrorList.clear();
            addExcelFile(file.toString());
//            addExcelFile("C:\\Users\\NTT\\Desktop\\studentlist.xlsx");
        }catch (Exception e){

        }
    }

    @FXML
    public void openManageForm(ObservableList sc,ObservableList exist,ObservableList error) throws IOException {
        Stage successForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/showAddTeacherExcelFileForm.fxml"));
        Parent root = loader.load();
        showAddTeacherExcelFileController manageController = loader.getController();
        manageController.getTeacherList(sc,exist,error);
        successForm.initStyle(StageStyle.UNDECORATED);
        successForm.initModality(Modality.APPLICATION_MODAL);
        successForm.setScene(new Scene(root));
        successForm.showAndWait();
        loadData();
    }

    public class teacherError {
        String teacherCode;
        String Error;

        public teacherError() {
        }

        public String getTeacherCode() {
            return teacherCode;
        }

        public void setTeacherCode(String teacherCode) {
            this.teacherCode = teacherCode;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }

        public teacherError(String teacherCode, String error) {
            this.teacherCode = teacherCode;
            Error = error;
        }
    }
}
