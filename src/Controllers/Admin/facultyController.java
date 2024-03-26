package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.Faculty;
import Models.FacultyDAO;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class facultyController implements Initializable {
    @FXML
    Button btnAdd,btnReload,btnExcel;
    @FXML
    ImageView imgVAdd,imgVReload,imgExcel;
    @FXML
    TableView<Faculty> TableVFaculty;
    @FXML
    TableColumn<Faculty,String> colFacultyCode;
    @FXML
    TableColumn<Faculty,String> colFacultyName;
    @FXML
    TableColumn<Faculty,String> colButton;
    @FXML
    TextField txtCode,txtName;

    ObservableList <Faculty> FacultyList = FXCollections.observableArrayList();
    ObservableList<Faculty> SuccessList= FXCollections.observableArrayList();
    ObservableList<facultyError> ErrorList = FXCollections.observableArrayList();
    ObservableList<Faculty> ExistList = FXCollections.observableArrayList();

    Faculty faculty = null;
    FacultyDAO facultyDAO = new FacultyDAO();
    FileChooser fileChooser = new FileChooser();
    AlertCustom alertCustom = new AlertCustom();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImage();
        loadData();
        enterForFacultyCode();
        enterForFacultyNameFilter();
    }

    private void setImage(){
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
            Parent parent = FXMLLoader.load(getClass().getResource("../../Views/Admin/addFacultyForm.fxml"));
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
    public void reFresh(){
        FacultyList.clear();
        ArrayList<Faculty> faculties = facultyDAO.getFacultyList();
        for (Faculty faculty : faculties){
            FacultyList.add(faculty);
        }
        TableVFaculty.setItems(FacultyList);
    }

    private void enterForFacultyNameFilter(){
        txtName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String facultyName = txtName.getText();
                String facultyCode = txtCode.getText();
                filter(facultyCode,facultyName);
            }
        });
    }

    private void enterForFacultyCode(){
        txtCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String facultyName = txtName.getText();
                String facultyCode = txtCode.getText();
                filter(facultyCode,facultyName);
            }
        });
    }

    private void filter(String facultyCode,String facultyName){
        TableVFaculty.getItems().clear();
        ArrayList<Faculty> faculties = facultyDAO.facultyArrayListWithFilter(facultyCode,facultyName);
        for (Faculty faculty : faculties){
            FacultyList.add(faculty);
        }
        TableVFaculty.setItems(FacultyList);
        TableVFaculty.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colFacultyCode.setCellValueFactory(new PropertyValueFactory<>("facultyCode"));
        colFacultyName.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        Callback<TableColumn<Faculty, String>, TableCell<Faculty, String>> cellFoctory = (TableColumn<Faculty, String> param) -> {
            final TableCell<Faculty, String> cell = new TableCell<Faculty, String>() {
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
                            faculty = TableVFaculty.getSelectionModel().getSelectedItem();
                            if (facultyDAO.checkFacultyCodeExit(faculty.getFacultyCode())==true){
                                try {
                                    alertCustom.alertError("Bạn Không thể xóa khoa này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else if (facultyDAO.delete(faculty)){
                                try {
                                    alertCustom.alertSuccess("Xóa khoa thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            reFresh();
                            }
                        });

                        editIcon.setOnMouseClicked(event -> {
                            faculty = TableVFaculty.getSelectionModel().getSelectedItem();
                            try {
                                editForm(faculty);
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
        TableVFaculty.setItems(FacultyList);
    }
    private void loadData(){
        reFresh();

        TableVFaculty.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colFacultyCode.setCellValueFactory(new PropertyValueFactory<>("facultyCode"));
        colFacultyName.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        Callback<TableColumn<Faculty, String>, TableCell<Faculty, String>> cellFoctory = (TableColumn<Faculty, String> param) -> {
            final TableCell<Faculty, String> cell = new TableCell<Faculty, String>() {
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
                            faculty = TableVFaculty.getSelectionModel().getSelectedItem();
                            if (facultyDAO.checkFacultyCodeExit(faculty.getFacultyCode())==true){
                                try {
                                    alertCustom.alertError("Bạn Không thể xóa khoa này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else if (facultyDAO.delete(faculty)){
                                try {
                                    alertCustom.alertSuccess("Xóa khoa thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            reFresh();
                            }
                        });

                        editIcon.setOnMouseClicked(event -> {
                            faculty = TableVFaculty.getSelectionModel().getSelectedItem();
                            try {
                                editForm(faculty);
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
        TableVFaculty.setItems(FacultyList);
    }
    public void editForm(Faculty faculty) throws IOException {
        Stage editForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/editFacultyForm.fxml"));
        Parent root = loader.load();
        editFacultyController editFacultyController = loader.getController();
        editFacultyController.getFaculty(faculty);
        editForm.setScene(new Scene(root));
        editForm.initStyle(StageStyle.UNDECORATED);
        editForm.initModality(Modality.APPLICATION_MODAL);
        editForm.showAndWait();
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
                String facultyCode = row.getCell(0)!=null?row.getCell(0).toString().toUpperCase():"";
                if(facultyCode.length() !=2){
                    ErrorInsert = "Mã khoa không hợp lệ";
                }

                String facultyName = row.getCell(1)!=null?row.getCell(1).toString():"";
                if(facultyName == "" && ErrorInsert == ""){
                    ErrorInsert = "Tên khoa không được bỏ trống";
                }


                if(ErrorInsert !=""){
                    facultyError tc = new facultyError(facultyCode,ErrorInsert);
                    ErrorList.add(tc);
                    continue;
                }

                FacultyDAO facultyDAO = new FacultyDAO();
                Faculty f = new Faculty(facultyCode,facultyName);
                System.out.println("====================");
                if (facultyDAO.checkFacultyIDExit(f.getFacultyCode()) == true) {
                    //add vào list tồn tại
                    ExistList.add(f);
                    System.out.println("đã tồn tại");
                } else {
                    if (facultyDAO.insert(f) == true) {
                        //vào list thành công
                        SuccessList.add(f);
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
        loader.setLocation(getClass().getResource("/Views/Admin/showAddFacultyExcelFileForm.fxml"));
        Parent root = loader.load();
        showAddFacultyExcelFileController manageController = loader.getController();
        manageController.getFacultyList(sc,exist,error);
        successForm.initStyle(StageStyle.UNDECORATED);
        successForm.initModality(Modality.APPLICATION_MODAL);
        successForm.setScene(new Scene(root));
        successForm.showAndWait();
        loadData();
    }


    public class facultyError {
        String FacultyCode;
        String Error;

        public facultyError(String facultyCode, String error) {
            FacultyCode = facultyCode;
            Error = error;
        }

        public String getFacultyCode() {
            return FacultyCode;
        }

        public void setFacultyCode(String facultyCode) {
            FacultyCode = facultyCode;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }
    }
}

