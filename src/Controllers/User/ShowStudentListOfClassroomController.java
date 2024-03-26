package Controllers.User;

import Controllers.Admin.editStudentController;
import Models.ScheduleDAO;
import Models.Student;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class ShowStudentListOfClassroomController {
    @FXML
    TableView<Student> tbvStudentList;
    @FXML
    TableColumn<Student,String> colStudentCode;

    @FXML
    TableColumn <Student,String> colFullName;

    @FXML
    TableColumn <Student,String> colInfo;

    @FXML
    Button btnCancel;

    Student student = new Student();
    ScheduleDAO scheduleDAO = new ScheduleDAO();
    ObservableList<Student> StudentList = FXCollections.observableArrayList();

    public void loadStudentList(String classroomId){
        tbvStudentList.getItems().clear();
        ArrayList<Student> students = scheduleDAO.getStudent(classroomId);
        for (Student student : students) {
            StudentList.add(student);
        }
        tbvStudentList.setItems(StudentList);
        tbvStudentList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY );
        colStudentCode.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFoctory = (TableColumn<Student, String> param) -> {
            final TableCell<Student, String> cell = new TableCell<Student, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView infoIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH_PLUS);
                        infoIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );
                        infoIcon.setOnMouseClicked(event -> {
                            student = tbvStudentList.getSelectionModel().getSelectedItem();
                            try {
//                                System.out.println(student.getStudentCode());
                                showStudentForm(student);
//                                editForm(student);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        HBox managebtn = new HBox(infoIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(infoIcon, new Insets(2, 2, 0, 3));

                        setGraphic(managebtn);
                        setText(null);

                    }
                }
            };
            return cell;
        };
        colInfo.setCellFactory(cellFoctory);
        tbvStudentList.setItems(StudentList);
    }

    @FXML
    public void CloseForm() throws IOException {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void showStudentForm(Student student) throws IOException {
        Stage successForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/User/StudentInfoForm.fxml"));
        Parent root = loader.load();
        StudentInfoController studentInfoController = loader.getController();
        studentInfoController.getStudent(student);
        successForm.initStyle(StageStyle.UNDECORATED);
        successForm.initModality(Modality.APPLICATION_MODAL);
        successForm.setScene(new Scene(root));
        successForm.showAndWait();
    }
}
