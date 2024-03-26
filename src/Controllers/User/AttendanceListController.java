package Controllers.User;

import Controllers.Alert.AlertCustom;
import DBConnection.DBConnection;
import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AttendanceListController implements Initializable {
    @FXML
    TableView<AttendanceList> tbvAttendanceList;

    @FXML
    TableColumn<AttendanceList,String> colStudentCode,colFirstName,colLastName,col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14,col15,col16,col17,col18,col19,col20;

    @FXML
    Label lblClassroomId, lblClassroomName, lblCreateDate, lblTeacherName, lblTitle;

    @FXML
    Button btnCancel, btnExportExcel, btnExportPDF;

    @FXML
    ImageView imgExportPDF, imgExportExcel, imgBack;

    @FXML
    AnchorPane acpScene;


    ScheduleDAO scheduleDAO = new ScheduleDAO();
    ObservableList<AttendanceList> StudentList = FXCollections.observableArrayList();
    DBConnection dbConnection = new DBConnection();
    Connection connection = dbConnection.getConnection();
    AttendanceSessionDetailsDAO attendanceSessionDetailsDAO = new AttendanceSessionDetailsDAO();
    ClassroomDAO classroomDAO = new ClassroomDAO();
    AlertCustom alertCustom = new AlertCustom();
    String _classroomId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImage();
    }

    private void setImage(){
        File img_pdf_link = new File("resources/images/export-pdf.png");
        Image Image_PDF = new Image(img_pdf_link.toURI().toString());
        imgExportPDF.setImage(Image_PDF);

        File img_excel_link = new File("resources/images/export-excel.png");
        Image Image_Excel = new Image(img_excel_link.toURI().toString());
        imgExportExcel.setImage(Image_Excel);

        File img_back_link = new File("resources/images/back-adminForm.png");
        Image Image_Back = new Image(img_back_link.toURI().toString());
        imgBack.setImage(Image_Back);
    }

    public void loadStudentList(String classroomId){
        Classroom classroom = classroomDAO.getClassroomById(Integer.valueOf(classroomId));
        _classroomId = classroomId;
        lblClassroomId.setText(classroomId);
        lblClassroomName.setText(classroom.getClassroomName());
        lblCreateDate.setText(dateFormat(classroom.getCreateDate()));
        lblTeacherName.setText(getTeacherNameByTeacherCode(classroom.getTeacherCode()));
        tbvAttendanceList.getItems().clear();
        ArrayList<AttendanceList> students = getAttendanceList(classroomId);
        for (AttendanceList student : students) {
            StudentList.add(student);
        }
        tbvAttendanceList.setItems(StudentList);
//        tbvAttendanceList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY );
        colStudentCode.setCellValueFactory(new PropertyValueFactory<>("colStudentCode"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("colFirstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("colLastName"));
        col1.setCellValueFactory(new PropertyValueFactory<>("col1"));
        col2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        col3.setCellValueFactory(new PropertyValueFactory<>("col3"));
        col4.setCellValueFactory(new PropertyValueFactory<>("col4"));
        col5.setCellValueFactory(new PropertyValueFactory<>("col5"));
        col6.setCellValueFactory(new PropertyValueFactory<>("col6"));
        col7.setCellValueFactory(new PropertyValueFactory<>("col7"));
        col8.setCellValueFactory(new PropertyValueFactory<>("col8"));
        col9.setCellValueFactory(new PropertyValueFactory<>("col9"));
        col10.setCellValueFactory(new PropertyValueFactory<>("col10"));
        col11.setCellValueFactory(new PropertyValueFactory<>("col11"));
        col12.setCellValueFactory(new PropertyValueFactory<>("col12"));
        col13.setCellValueFactory(new PropertyValueFactory<>("col13"));
        col14.setCellValueFactory(new PropertyValueFactory<>("col14"));
        col15.setCellValueFactory(new PropertyValueFactory<>("col15"));
        col16.setCellValueFactory(new PropertyValueFactory<>("col16"));
        col17.setCellValueFactory(new PropertyValueFactory<>("col17"));
        col18.setCellValueFactory(new PropertyValueFactory<>("col18"));
        col19.setCellValueFactory(new PropertyValueFactory<>("col19"));
        col20.setCellValueFactory(new PropertyValueFactory<>("col20"));
        tbvAttendanceList.getSortOrder().add(colLastName);
        tbvAttendanceList.sort();

        tbvAttendanceList.setItems(StudentList);
    }

    @FXML
    public void CloseForm() throws IOException {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public ArrayList<AttendanceList> getAttendanceList(String classroomId){
        ArrayList<AttendanceList> studentArrayList = new ArrayList<AttendanceList>();
        String sql = "select st.studentCode 'studentCode',st.fullName 'fullName' from classroom_student c, student st where st.studentCode = c.studentCode and c.classroomId ="  + classroomId;
        ArrayList<Integer> attendanceLists = getAttendanceSessions(classroomId);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String studentCode = resultSet.getString("studentCode");
                String fullName =  resultSet.getString("fullName");
                String firstName = getFirstName(fullName);
                String lastName = getLastName(fullName);
                String result[] = sessions(attendanceLists, studentCode);
                AttendanceList attendanceList = new AttendanceList(studentCode, firstName, lastName,result[0],result[1],result[2],result[3],result[4],result[5],result[6],result[7],result[8],result[9],result[10],result[11],result[12],result[13],result[14],result[15],result[16],result[17],result[18],result[19]);
                studentArrayList.add(attendanceList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return studentArrayList;
    }

    public String[] sessions(ArrayList<Integer> attendanceLists, String studentCode){
        String col1="",col2="",col3="",col4="",col5="",col6="",col7="",col8="",col9="",col10="",col11="",col12="",col13="",col14="",col15="",col16="",col17="",col18="",col19="",col20="";
        col1 = attendanceLists.size() >= 1?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(0),studentCode)?"":"x"):"";
        col2 = attendanceLists.size() >= 2?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(1),studentCode)?"":"x"):"";
        col3 = attendanceLists.size() >= 3?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(2),studentCode)?"":"x"):"";
        col4 = attendanceLists.size() >= 4?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(3),studentCode)?"":"x"):"";
        col5 = attendanceLists.size() >= 5?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(4),studentCode)?"":"x"):"";
        col6 = attendanceLists.size() >= 6?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(5),studentCode)?"":"x"):"";
        col7 = attendanceLists.size() >= 7?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(6),studentCode)?"":"x"):"";
        col8 = attendanceLists.size() >= 8?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(7),studentCode)?"":"x"):"";
        col9 = attendanceLists.size() >= 9?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(8),studentCode)?"":"x"):"";
        col10 = attendanceLists.size() >= 10?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(9),studentCode)?"":"x"):"";
        col11 = attendanceLists.size() >= 11?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(10),studentCode)?"":"x"):"";
        col12 = attendanceLists.size() >= 12?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(11),studentCode)?"":"x"):"";
        col13 = attendanceLists.size() >= 13?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(12),studentCode)?"":"x"):"";
        col14 = attendanceLists.size() >= 14?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(13),studentCode)?"":"x"):"";
        col15 = attendanceLists.size() >= 15?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(14),studentCode)?"":"x"):"";
        col16 = attendanceLists.size() >= 16?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(15),studentCode)?"":"x"):"";
        col17 = attendanceLists.size() >= 17?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(16),studentCode)?"":"x"):"";
        col18 = attendanceLists.size() >= 18?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(17),studentCode)?"":"x"):"";
        col19 = attendanceLists.size() >= 19?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(18),studentCode)?"":"x"):"";
        col20 = attendanceLists.size() >= 20?(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceLists.get(19),studentCode)?"":"x"):"";
        return new String[]{col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14,col15,col16,col17,col18,col19,col20};
    }

    private static String dateFormat(Date date) {
        String dateStr = new SimpleDateFormat("dd/MM/yyyy").format(date);
        return dateStr;
    }

    public ArrayList<Integer> getAttendanceSessions(String classroomId){
        ArrayList<Integer> attendanceLists = new ArrayList<Integer>();
        String sql = "select attendanceSessionId from attendancesession where classroomId ="  + classroomId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int sessionId = resultSet.getInt("attendanceSessionId");
                attendanceLists.add(sessionId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return attendanceLists;
    }

    public String getTeacherNameByTeacherCode(String teacherCode){
        ArrayList<Integer> attendanceLists = new ArrayList<Integer>();
        String sql = "select fullName from teacher where teacherCode ='"  + teacherCode + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                return resultSet.getString("fullName");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    private String getLastName(String fullName){
        String len[] = fullName.split(" ");
        return len[len.length-1];
    }

    private String getFirstName(String fullName){
        String len[] = fullName.split(" ");
        String lastName = "";
        for(int i = 0; i< len.length - 1;i++){
            lastName += len[i] + " ";
        }

        return lastName;
    }

    public void saveToExcel() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("sample");

        Row row = spreadsheet.createRow(0);



        for (int j = 0; j < tbvAttendanceList.getColumns().size(); j++) {
            row.createCell(j).setCellValue(tbvAttendanceList.getColumns().get(j).getText());
        }

        for (int i = 0; i < tbvAttendanceList.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < tbvAttendanceList.getColumns().size(); j++) {
                if(tbvAttendanceList.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(tbvAttendanceList.getColumns().get(j).getCellData(i).toString());
                }
                else {
                    row.createCell(j).setCellValue("");
                }
            }
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls"),
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx")
        );
        File saveFile = fileChooser.showSaveDialog(tbvAttendanceList.getScene().getWindow());
        System.out.println(saveFile);
        if(saveFile ==  null){
            return;
        }
        FileOutputStream fileOut = new FileOutputStream(saveFile);
        workbook.write(fileOut);
        fileOut.close();
        alertCustom.alertSuccess("Xuất file excel thành công!");

    }

    public void saveToPDF() throws IOException {
        Printer pdfPrinter = null;
        Iterator<Printer> iter = Printer.getAllPrinters().iterator();
        while (iter.hasNext()) {
            Printer printer = iter.next();
            if (printer.getName().endsWith("PDF")) {
                pdfPrinter = printer;
            }
        }
        PageLayout layout = pdfPrinter.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

        setSceneForPrintPDF();

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            job.getJobSettings().setPageLayout(layout);
            job.showPrintDialog(btnExportPDF.getScene().getWindow());
            job.printPage(btnExportPDF.getScene().getRoot());
            job.endJob();
            CloseForm();
            loadStudentList(_classroomId);
            alertCustom.alertSuccess("Xuất file PDF thành công!");
        }
    }

    private void setSceneForPrintPDF(){
        lblTitle.setLayoutX(150);
        tbvAttendanceList.setPrefWidth(565);
        tbvAttendanceList.setPrefHeight(675);
        colStudentCode.setPrefWidth(75);
        tbvAttendanceList.getColumns().get(1).setPrefWidth(95);
        tbvAttendanceList.getColumns().get(2).setPrefWidth(45);
//        tbvAttendanceList.getColumns().get(3).setPrefWidth(18);
        for(int i = 3; i< 12;i++){
            tbvAttendanceList.getColumns().get(i).setPrefWidth(16);
        }
        for(int i = 12; i< 22;i++){
            tbvAttendanceList.getColumns().get(i).setPrefWidth(18);
        }
        acpScene.setStyle("-fx-border-width: 0");
        acpScene.getScene().getWindow().setWidth(590);
        acpScene.getScene().getWindow().setHeight(850);
    }
}
