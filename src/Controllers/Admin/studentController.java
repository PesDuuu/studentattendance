package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.Class;
import Models.ClassDAO;
import Models.Student;
import Models.StudentDAO;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class studentController implements Initializable {

    @FXML
    HBox hBox;
    @FXML
    Button btnADD,btnReload,btnExcel;

    @FXML
    ImageView imgVAdd,imgVReload,imgExcel;

    @FXML
    TableView<Student> TableVStudent;

    @FXML
    TableColumn<Student,String> colStudentCode;

    @FXML
    TableColumn <Student,String> colFullName;

    @FXML
    TableColumn <Student, Date> colDateOfBirth;

    @FXML
    TableColumn <Student,String> colAddress;

    @FXML
    TableColumn <Student,String> colClassCode;

    @FXML
    TableColumn<Student,String> colButton;
    ObservableList<Student> StudentList = FXCollections.observableArrayList();
    ObservableList<Student> SuccessList= FXCollections.observableArrayList();
    ObservableList<StudentError> ErrorList = FXCollections.observableArrayList();
    ObservableList<Student> ExistList = FXCollections.observableArrayList();
    @FXML
    ComboBox cbbClass;
    @FXML
    TextField txtCode, txtFullName;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImg();
        loadData();
        loadCBB();
        enterForStudentCodeFilter();
        enterForFullNameFilter();
    }

    StudentDAO studentDAO = new StudentDAO();
    Student student = new Student();
    FileChooser fileChooser = new FileChooser();
    AlertCustom alertCustom = new AlertCustom();

    private void setImg(){
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

    private void enterForFullNameFilter(){
        txtFullName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String cbbClassCode = (String) cbbClass.getSelectionModel().getSelectedItem();
                if(cbbClassCode == null || cbbClassCode == "Tất cả") {
                    cbbClassCode = "";
                }
                String fullName = txtFullName.getText();
                String studentCode = txtCode.getText();
                filter(cbbClassCode,fullName,studentCode);
            }
        });
    }

    private void enterForStudentCodeFilter(){
        txtCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String cbbClassCode = (String) cbbClass.getSelectionModel().getSelectedItem();
                if(cbbClassCode == null || cbbClassCode == "Tất cả"){
                    cbbClassCode = "";
                }
                String fullName = txtFullName.getText();
                String studentCode = txtCode.getText();
                filter(cbbClassCode,fullName,studentCode);
            }
        });
    }

    @FXML
    public void getClassName(){
        String cbbClassCode = (String) cbbClass.getSelectionModel().getSelectedItem();
        if(cbbClassCode == null || cbbClassCode == "Tất cả"){
            cbbClassCode ="";
        }
        String fullName = txtFullName.getText();
        String studentCode = txtCode.getText();
        filter(cbbClassCode,fullName,studentCode);
    }

    private void filter(String cbbClassCode, String fullName, String studentCode){
        TableVStudent.getItems().clear();
        ArrayList<Student> students = studentDAO.getStudentListWithFilter(cbbClassCode,fullName,studentCode);
        for (Student student : students) {
            StudentList.add(student);
        }
        TableVStudent.setItems(StudentList);
        TableVStudent.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY );
        colStudentCode.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colDateOfBirth.setCellFactory(column -> {
            TableCell<Student, Date> cell = new TableCell<Student, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(item));
                    }
                }
            };

            return cell;
        });
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colClassCode.setCellValueFactory(new PropertyValueFactory<>("classCode"));

        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFoctory = (TableColumn<Student, String> param) -> {
            final TableCell<Student, String> cell = new TableCell<Student, String>() {
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
                            student = TableVStudent.getSelectionModel().getSelectedItem();
                            if (studentDAO.checkStudentCodeExit(student.getStudentCode())==true){
                                try {
                                    alertCustom.alertError("Bạn không thể xóa sinh viên này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else if (studentDAO.delete(student)) {
                                try {
                                    alertCustom.alertSuccess("Xóa sinh viên thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                reFresh();
                            }
                        });
                        editIcon.setOnMouseClicked(event -> {
                            student = TableVStudent.getSelectionModel().getSelectedItem();
                            try {
                                editForm(student);
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
        TableVStudent.setItems(StudentList);
    }

    private void loadCBB() {
        ClassDAO classDAO = new ClassDAO();
        ArrayList<Models.Class> classes = classDAO.getClassList();
        for( Class iclass : classes){
            cbbClass.getItems().addAll(iclass.getClassCode());
        }
        cbbClass.getItems().addAll("Tất cả");
    }

    @FXML
    private void getAddView(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Views/Admin/addStudentForm.fxml"));
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
    private void reFresh() {
        TableVStudent.getItems().clear();
        ArrayList<Student> students = studentDAO.getStudentList();
        for (Student student : students) {

            StudentList.add(student);
        }
        TableVStudent.setItems(StudentList);
    }

    private void loadData(){
        reFresh();
        TableVStudent.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY );
        colStudentCode.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colDateOfBirth.setCellFactory(column -> {
            TableCell<Student, Date> cell = new TableCell<Student, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(item));
                    }
                }
            };

            return cell;
        });
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colClassCode.setCellValueFactory(new PropertyValueFactory<>("classCode"));

        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFoctory = (TableColumn<Student, String> param) -> {
        final TableCell<Student, String> cell = new TableCell<Student, String>() {
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
                            student = TableVStudent.getSelectionModel().getSelectedItem();
                            if (studentDAO.checkStudentCodeExit(student.getStudentCode())==true){
                                try {
                                    alertCustom.alertError("Bạn không thể xóa sinh viên này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else if (studentDAO.delete(student)) {
                                try {
                                    alertCustom.alertSuccess("Xóa sinh viên thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            reFresh();
                            }
                        });

                        editIcon.setOnMouseClicked(event -> {
                            student = TableVStudent.getSelectionModel().getSelectedItem();
                            try {
                                editForm(student);
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
        TableVStudent.setItems(StudentList);
    }

    public void editForm(Student student) throws IOException {
        Stage successForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/editStudentForm.fxml"));
        Parent root = loader.load();
        editStudentController editStudentController = loader.getController();
        editStudentController.getStudent(student);
        successForm.initStyle(StageStyle.UNDECORATED);
        successForm.initModality(Modality.APPLICATION_MODAL);
        successForm.setScene(new Scene(root));
        successForm.showAndWait();
    }

    public void addExcelFile(String url) {
        try {
            FileInputStream fileInputStream = new FileInputStream(url);
//            FileInputStream fileInputStream = new FileInputStream(new File(url));
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
                String studentCode = row.getCell(0)!=null?row.getCell(0).toString():"";
                if (studentCode ==""){
                    ErrorInsert = "MSSV không được bỏ trống";
                }
                if(studentCode.length() != 10 || isNumeric(studentCode)==false && ErrorInsert==""){
                    ErrorInsert = "MSSV không hợp lệ";
                }

                String fullName = row.getCell(1)!=null?row.getCell(1).toString():"";
                if(fullName == "" && ErrorInsert == ""){
                    ErrorInsert = "Tên không được bỏ trống";
                }



                java.sql.Date dateOfBirth = null;
                try{
                    dateOfBirth= new java.sql.Date(row.getCell(2).getDateCellValue().getTime());
                    System.out.println(dateOfBirth);
                    System.out.println("adadsdasd");
//                    String date = dateTimeFormat(row.getCell(2).getDateCellValue());
//                    dateOfBirth = Date.valueOf(date);
//                    System.out.println("Ngày sinh: "+date);
                }catch (Exception e){
                    if(ErrorInsert != "")
                    {
                        ErrorInsert = "Ngày sinh không hợp lệ";
                    }
                }
                String address = row.getCell(3)!=null?row.getCell(3).toString():"";

                if(address == "" && ErrorInsert == ""){
                    ErrorInsert = "Địa chỉ không được bỏ trống";
                }

                String classCode = row.getCell(4)!=null?row.getCell(4).toString().toUpperCase():"";
                if(new ClassDAO().checkExistClass(classCode) == false && ErrorInsert == ""){
                    ErrorInsert = "Lớp học không hợp lệ";
                }

                if(ErrorInsert !=""){
                    StudentError se = new StudentError(studentCode,ErrorInsert);
                    ErrorList.add(se);
                    continue;
                }

                StudentDAO studentDAO = new StudentDAO();
                Student st = new Student(studentCode, fullName, dateOfBirth, address, classCode, null);
                System.out.println("====================");
                System.out.println("Code:" + st.getStudentCode());
                System.out.println("Name:" + st.getFullName());
                System.out.println("Date:" + st.getDateOfBirth());
                System.out.println("Address:" + st.getAddress());
                System.out.println("Class:" + st.getClassCode());

                if (studentDAO.checkStudentIDExit(st.getStudentCode()) == true) {
                    //add vào list tồn tại
                    ExistList.add(st);
                    System.out.println("đã tồn tại");
                } else {
                    if (studentDAO.insertStudent(st) == true) {
                        //vào list thành công
                        SuccessList.add(st);
                        System.out.println("thành công");
                    }
//                    else {
//                        //add vào list có lỗi
//                        ErrorList.add(st);
//                        System.out.println("đã xảy ra lỗi");
//                    }
                }
            }
            wb.close();
            fileInputStream.close();
            System.out.println("==========thông báo============");
            for (Student student : ExistList) {
                System.out.println("Sinh viên đã tồn tại");
                System.out.println(student.getStudentCode());
            }
            for (StudentError student : ErrorList) {
                System.out.println("Đã xảy ra lỗi khi thêm");
                System.out.println(student.getStudentCode());
            }
            for (Student student : SuccessList) {
                System.out.println("Thêm thành công");
                System.out.println(student.getStudentCode());
            }
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
        }catch (Exception e){

        }
    }

    @FXML
    public void openManageForm(ObservableList sc,ObservableList exist,ObservableList error) throws IOException {
        Stage successForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/showAddStudentExcelFileForm.fxml"));
        Parent root = loader.load();
        showAddStudentExcelFileController manageController = loader.getController();
        manageController.getStudentList(sc,exist,error);
        successForm.initStyle(StageStyle.UNDECORATED);
        successForm.initModality(Modality.APPLICATION_MODAL);
        successForm.setScene(new Scene(root));
        successForm.showAndWait();
        loadData();
    }

    public class StudentError{
        String studentCode;
        String Error;

        public StudentError(String studentCode, String error) {
            this.studentCode = studentCode;
            Error = error;
        }

        public String getStudentCode() {
            return studentCode;
        }

        public void setStudentCode(String studentCode) {
            this.studentCode = studentCode;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }
    }
}


