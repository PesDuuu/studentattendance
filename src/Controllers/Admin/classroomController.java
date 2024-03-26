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

public class classroomController implements Initializable{
    @FXML
    Button btnAdd,btnReload,btnExcel;
    @FXML
    ImageView imgVAdd,imgVReload,imgExcel;
    @FXML
    TextField txtClassroomName,txtTeacherCode;
    @FXML
    TableView<Classroom> TableVClassroom;
    @FXML
    TableColumn<Classroom,Integer> colClassroomID;
    @FXML
    TableColumn<Classroom,String> colClassroomName;
    @FXML
    TableColumn<Classroom, Date> colCreateDate;
    @FXML
    TableColumn<Classroom,String> colTeacherCode;
    @FXML
    TableColumn<Classroom,String> colButton;
    ObservableList<Classroom> ClassroomList = FXCollections.observableArrayList();
    ObservableList<Classroom> SuccessList= FXCollections.observableArrayList();
    ObservableList<ClassroomError> ErrorList = FXCollections.observableArrayList();
    ObservableList<Classroom> ExistList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImg();
        loadData();
        enterClassroomFilter();
        enterTeacherCode();
    }
    ClassroomDAO classroomDAO = new ClassroomDAO();
    Classroom classroom = new Classroom();
    FileChooser fileChooser =  new FileChooser();
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

    @FXML
    private void reFresh(){
        TableVClassroom.getItems().clear();
        ArrayList<Classroom> classrooms = classroomDAO.ClassroomList();
        for (Classroom classroom : classrooms){
            ClassroomList.add(classroom);
        }
        TableVClassroom.setItems(ClassroomList);
    }
    private void  enterClassroomFilter(){
        txtClassroomName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String classroomName = txtClassroomName.getText();
                String teacherCode = txtTeacherCode.getText();
                filter(classroomName,teacherCode);
            }
        });
    }

    private void enterTeacherCode(){
        txtTeacherCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String classroom = txtClassroomName.getText();
                String teacherCode = txtTeacherCode.getText();
                filter(classroom,teacherCode);
            }
        });
    }
    private void filter(String classroomName, String teacherCode){
        TableVClassroom.getItems().clear();
        ArrayList<Classroom> classrooms = classroomDAO.getClassroomListFilter(classroomName,teacherCode);
        for (Classroom classroom : classrooms){
            ClassroomList.add(classroom);
        }
//        TableVClassroom.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colClassroomID.setCellValueFactory(new PropertyValueFactory<>("classroomId"));
        colClassroomName.setCellValueFactory(new PropertyValueFactory<>("classroomName"));
        colCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        colCreateDate.setCellFactory(column -> {
            TableCell<Classroom, Date> cell = new TableCell<Classroom,Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(java.sql.Date item, boolean empty) {
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
        colTeacherCode.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));

        Callback<TableColumn<Classroom, String>, TableCell<Classroom, String>> cellFoctory = (TableColumn<Classroom, String> param) -> {
            final TableCell<Classroom, String> cell = new TableCell<Classroom, String>() {
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
                            classroom  = TableVClassroom.getSelectionModel().getSelectedItem();
                            if (classroomDAO.checkClassroomIdExit(classroom.getClassroomId())==true || classroomDAO.checkClassroomExistAttendanceSession(classroom.getClassroomId())==true){
                                try {
                                    alertCustom.alertError("Bạn không thể xóa phòng học này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(classroomDAO.delete(classroom)) {
                                try {
                                    alertCustom.alertSuccess("Xóa phòng học thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            reFresh();
                        });

                        editIcon.setOnMouseClicked(event -> {
                            classroom = TableVClassroom.getSelectionModel().getSelectedItem();
                            try {
                                editForm(classroom);
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
        TableVClassroom.setItems(ClassroomList);
    }
    public void loadData(){
        reFresh();

//        TableVClassroom.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colClassroomID.setCellValueFactory(new PropertyValueFactory<>("classroomId"));
        colClassroomName.setCellValueFactory(new PropertyValueFactory<>("classroomName"));
        colCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        colCreateDate.setCellFactory(column -> {
            TableCell<Classroom, Date> cell = new TableCell<Classroom,Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(java.sql.Date item, boolean empty) {
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
        colTeacherCode.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));

        Callback<TableColumn<Classroom, String>, TableCell<Classroom, String>> cellFoctory = (TableColumn<Classroom, String> param) -> {
            final TableCell<Classroom, String> cell = new TableCell<Classroom, String>() {
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
                           classroom  = TableVClassroom.getSelectionModel().getSelectedItem();
                            if (classroomDAO.checkClassroomIdExit(classroom.getClassroomId())==true || classroomDAO.checkClassroomExistAttendanceSession(classroom.getClassroomId())==true){
                                try {
                                    alertCustom.alertError("Bạn không thể xóa phòng học này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(classroomDAO.delete(classroom)) {
                                try {
                                    alertCustom.alertSuccess("Xóa phòng học thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            reFresh();
                        });

                        editIcon.setOnMouseClicked(event -> {
                            classroom = TableVClassroom.getSelectionModel().getSelectedItem();
                            try {
                                editForm(classroom);
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
        TableVClassroom.setItems(ClassroomList);
    }

    public void getAddView(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../../Views/Admin/addClassroomForm.fxml"));
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

    public void editForm(Classroom classroom) throws IOException {
        Stage editForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/editClassroomForm.fxml"));
        Parent root = loader.load();
        editClassroomController editClassroomController = loader.getController();
        editClassroomController.getClassroomID(classroom);
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
                String classroomId = row.getCell(0)!=null?row.getCell(0).toString():"";

                System.out.println();
                System.out.println("classroomId: " + classroomId.split("\\.")[0]);
                classroomId = classroomId.split("\\.")[0];

                if (isNumeric(classroomId)==false){
                    ErrorInsert="Mã lớp không hợp lệ";
                }

                System.out.println(classroomId);
//
//                if(ErrorInsert==""){
//                    if (classroomDAO.checkClassroomExit(classroomId)){
//                        ErrorInsert ="Mã đã tồn tại";
//                    }
//                }

                String classroomName = row.getCell(1)!=null?row.getCell(1).toString():"";
                if(classroomName =="" && ErrorInsert==""){
                    ErrorInsert = "Tên không được bỏ trống";
                }

                java.sql.Date createDate = null;
                try{
                    createDate = new java.sql.Date(row.getCell(2).getDateCellValue().getTime());
                }catch (Exception e){
                    if(ErrorInsert != "")
                    {
                        ErrorInsert = "Ngày tạo không hợp lệ";
                    }
                }
                String teacherCode = row.getCell(3)!=null?row.getCell(3).toString().toUpperCase():"";

                if(teacherCode == "" && ErrorInsert == ""){
                    ErrorInsert = "Mã giảng viên không được bỏ trống";
                }

                if (new TeacherDAO().checkTeacherCodeExist(teacherCode)==false && ErrorInsert == ""){
                    ErrorInsert ="Mã giảng viên không tồn tại";
                }

                if(ErrorInsert !=""){
                    ClassroomError ce = new ClassroomError(classroomId,ErrorInsert);
                    ErrorList.add(ce);
                    continue;
                }

                ClassroomDAO classDAO = new ClassroomDAO();
                Classroom st = new Classroom(Integer.valueOf(classroomId),classroomName,createDate,teacherCode);

                if (classroomDAO.checkClassroomExit(String.valueOf(st.getClassroomId())) == true) {
                    //add vào list tồn tại
                    ExistList.add(st);
                    System.out.println("đã tồn tại");
                } else {
                    if (classroomDAO.insertClassroom(st) == true) {
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
//            System.out.println("==========thông báo============");
//            for (Student student : ExistList) {
//                System.out.println("Sinh viên đã tồn tại");
//                System.out.println(student.getStudentCode());
//            }
//            for (studentController.StudentError student : ErrorList) {
//                System.out.println("Đã xảy ra lỗi khi thêm");
//                System.out.println(student.getStudentCode());
//            }
//            for (Student student : SuccessList) {
//                System.out.println("Thêm thành công");
//                System.out.println(student.getStudentCode());
//            }
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
        loader.setLocation(getClass().getResource("/Views/Admin/showAddClassroomExcelFileForm.fxml"));
        Parent root = loader.load();
        showAddClassroomExcelFileController manageController = loader.getController();
        manageController.getClassroomList(sc,exist,error);
        successForm.initStyle(StageStyle.UNDECORATED);
        successForm.initModality(Modality.APPLICATION_MODAL);
        successForm.setScene(new Scene(root));
        successForm.showAndWait();
        loadData();
    }

    public class ClassroomError {
        String classroomCode;
        String Error;


        public ClassroomError(String classroomCode, String error) {
            this.classroomCode = classroomCode;
            Error = error;
        }

        public String getClassroomCode() {
            return classroomCode;
        }

        public void setClassroomCode(String classroomCode) {
            this.classroomCode = classroomCode;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
