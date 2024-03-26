package Controllers.Admin;
import Controllers.Alert.AlertCustom;
import Models.*;

import Models.Class;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

public class classController implements Initializable {
    @FXML
    Button btnAdd,btnReload,btnExcel;

    @FXML
    ImageView imgVAdd,imgVReload,imgExcel;

    @FXML
    private TableView<Class> TableVClass;

    @FXML
    private TableColumn<Class,String> colCode;

    @FXML
    private TableColumn<Class,String> colName;

    @FXML
    private TableColumn<Class,String> colFacultyName;

    @FXML
    private TableColumn<Class,String> colButton;

    @FXML
    ComboBox cbbFaculty;
    @FXML
    TextField txtClassCode;
    FacultyDAO facultyDAO = new FacultyDAO();
    FileChooser fileChooser = new FileChooser();
    ObservableList<Class> ClassList = FXCollections.observableArrayList();

    ObservableList<Class> SuccessList= FXCollections.observableArrayList();
    ObservableList<classError> ErrorList = FXCollections.observableArrayList();
    ObservableList<Class> ExistList = FXCollections.observableArrayList();

    ClassDAO classDAO = new ClassDAO();
    AlertCustom alertCustom = new AlertCustom();
    Class aClass = null;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        setImage();
        loadCBB();
        enterForClassFilter();
    }

    public void setImage(){
        File add = new File("resources/images/add-user.png");
        Image addClass = new Image(add.toURI().toString());
        imgVAdd.setImage(addClass);


        File reload = new File("resources/images/refresh.png");
        Image reLoadClass = new Image(reload.toURI().toString());
        imgVReload.setImage(reLoadClass);

        File excel = new File("resources/images/excel-add.png");
        Image Excel = new Image(excel.toURI().toString());
        imgExcel.setImage(Excel);

    }
    private void loadCBB(){
        ArrayList<Faculty> faculties= facultyDAO.getFacultyList();
        for (Faculty faculty : faculties){
            cbbFaculty.getItems().addAll(faculty.getFacultyName());
        }
        cbbFaculty.getItems().addAll("Tất cả");
    }

    private void enterForClassFilter(){
        txtClassCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String cbbFacultyName = (String) cbbFaculty.getSelectionModel().getSelectedItem();
                if(cbbFacultyName == null || cbbFacultyName == "Tất cả") {
                    cbbFacultyName = "";
                }
                String classCode = txtClassCode.getText();
                filter(classCode,cbbFacultyName);
            }
        });
    }

    @FXML
    public void getFaculty(){
        String cbbFacultyName = (String) cbbFaculty.getSelectionModel().getSelectedItem();
        if(cbbFacultyName == null || cbbFacultyName == "Tất cả"){
            cbbFacultyName ="";
        }
        String classCode = txtClassCode.getText();
        filter(classCode,cbbFacultyName);

    }

    private void filter( String classCode,String cbbFaculty){
        TableVClass.getItems().clear();
        ArrayList<Class> classes = classDAO.getClassListWithClassFilter(classCode,cbbFaculty);
        for (Class iclass : classes) {
            ClassList.add(iclass);
        }
//        TableVClass.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colCode.setCellValueFactory(new PropertyValueFactory<>("classCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("className"));
        colFacultyName.setCellValueFactory(new PropertyValueFactory<>("facultyCode"));

        Callback<TableColumn<Class, String>, TableCell<Class, String>> cellFoctory = (TableColumn<Class, String> param) -> {
            final TableCell<Class, String> cell = new TableCell<Class, String>() {
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
                            aClass = TableVClass.getSelectionModel().getSelectedItem();
                            if (classDAO.checkExistClassCode(aClass.getClassCode())==true){
                                try {
                                    alertCustom.alertError("Bạn không thể xóa lớp học này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }else if (classDAO.delete(aClass)) {
                                try {
                                    alertCustom.alertSuccess("Xóa lớp học thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            reFresh();
                        });

                        editIcon.setOnMouseClicked(event -> {
                            aClass  = TableVClass.getSelectionModel().getSelectedItem();
                            try {
                                editForm(aClass);
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
        TableVClass.setItems(ClassList);
    }
    public void loadData(){
        reFresh();
//        TableVClass.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colCode.setCellValueFactory(new PropertyValueFactory<>("classCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("className"));
        colFacultyName.setCellValueFactory(new PropertyValueFactory<>("facultyCode"));

        Callback<TableColumn<Class, String>, TableCell<Class, String>> cellFoctory = (TableColumn<Class, String> param) -> {
            final TableCell<Class, String> cell = new TableCell<Class, String>() {
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
                            aClass = TableVClass.getSelectionModel().getSelectedItem();
                            if (classDAO.checkExistClassCode(aClass.getClassCode())==true){
                                try {
                                    alertCustom.alertError("Bạn không thể xóa lớp học này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else if(classDAO.delete(aClass)) {
                                try {
                                    alertCustom.alertSuccess("Xóa lớp học thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            reFresh();
                        });

                        editIcon.setOnMouseClicked(event -> {
                            aClass  = TableVClass.getSelectionModel().getSelectedItem();
                            try {
                                editForm(aClass);
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
        TableVClass.setItems(ClassList);
    }

    @FXML
    public void reFresh() {
        TableVClass.getItems().clear();
        ArrayList<Class> classes = classDAO.getClassList();
        for (Class cclass : classes){
            ClassList.add(cclass);
        }
        TableVClass.setItems(ClassList);
    }

    @FXML
    private void getAddView(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Views/Admin/addClassForm.fxml"));
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

    public void editForm(Class aClass) throws IOException {
        Stage editForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/editClassForm.fxml"));
        Parent root = loader.load();
        editClassController editClassController = loader.getController();
        editClassController.setClass(aClass);
        editForm.setScene(new Scene(root));
        editForm.initStyle(StageStyle.UNDECORATED);
        editForm.initModality(Modality.APPLICATION_MODAL);
        editForm.showAndWait();
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
                String classCode = row.getCell(0)!=null?row.getCell(0).toString().toUpperCase():"";
                System.out.println(classCode);
                if(classCode == ""){
                    ErrorInsert = "Mã lớp không được bỏ trống";
                }

                if ( ErrorInsert == "" && classCode.length() < 7){
                    ErrorInsert = "Mã lớp không hợp lệ";
                }

                String className = row.getCell(1)!=null?row.getCell(1).toString():"";
                if(className == "" && ErrorInsert == ""){
                    ErrorInsert = "Tên lớp không được bỏ trống";
                }

                String facultyCode = row.getCell(2)!=null?row.getCell(2).toString().toUpperCase():"";
                if (facultyCode.length() !=2 && ErrorInsert == ""){
                    ErrorInsert = "Mã khoa không được để trống";
                }

                if (new FacultyDAO().checkFacultyIDExit(facultyCode)==false && ErrorInsert ==""){
                    ErrorInsert = "Mã khoa không tồn tại";
                }

                if(ErrorInsert !=""){
                    classError tc = new classError(classCode,ErrorInsert);
                    ErrorList.add(tc);
                    continue;
                }

                ClassDAO classDAO = new ClassDAO();
                Class cl = new Class(classCode,className,facultyCode);
                System.out.println("====================");
                if (classDAO.checkExistClass(cl.getClassCode()) == true) {
                    //add vào list tồn tại
                    ExistList.add(cl);
                    System.out.println("đã tồn tại");
                } else {
                    if (classDAO.insertClass(cl) == true) {
                        //vào list thành công
                        SuccessList.add(cl);
                        System.out.println("thành công");
                    }
                }
            }
            wb.close();
            fileInputStream.close();
            openManageForm(SuccessList,ExistList,ErrorList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openManageForm(ObservableList sc,ObservableList exist,ObservableList error) throws IOException {
        Stage successForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/showAddClassExcelFileForm.fxml"));
        Parent root = loader.load();
        showAddClassExcelFileController manageController = loader.getController();
        manageController.getClassList(sc,exist,error);
        successForm.initStyle(StageStyle.UNDECORATED);
        successForm.initModality(Modality.APPLICATION_MODAL);
        successForm.setScene(new Scene(root));
        successForm.showAndWait();
        loadData();
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
}
