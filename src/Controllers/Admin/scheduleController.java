package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.Classroom;
import Models.Schedule;
import Models.ScheduleDAO;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class scheduleController implements Initializable {
    @FXML
    Button btnAdd,btnReload;
    @FXML
    ImageView imgVAdd,imgVReload;
    @FXML
    TableView<Schedule> TableVShedule;
    @FXML
    TableColumn<Schedule,Integer> colClassroomID;
    @FXML
    TableColumn<Schedule,String> colStudentCode;
    @FXML
    TableColumn<Schedule,String> colButton;
    @FXML
    TableColumn<Schedule,String> colName;
    @FXML
    TableColumn<Schedule, Date> colCreateDate;
    @FXML
    TableColumn<Schedule,String> colTeacherCode;
    ObservableList<Schedule> sheduleList = FXCollections.observableArrayList();
    @FXML
    TextField txtCode,txtClassroom,txtTeacher;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       setImg();
       loadData();
       enterStudentIDFilter();
       enterClassNameFilter();
       enterTeacherCodeFilter();
    }
    ScheduleDAO sheduleDAO = new ScheduleDAO();
    Schedule shedule = new Schedule();
    AlertCustom alertCustom = new AlertCustom();

    public void setImg(){
        File add = new File("resources/images/add-user.png");
        Image addclass = new Image(add.toURI().toString());
        imgVAdd.setImage(addclass);

        File reload = new File("resources/images/refresh.png");
        Image reloadclass = new Image(reload.toURI().toString());
        imgVReload.setImage(reloadclass);
    }
    public void getAddView(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../../Views/Admin/addScheduleForm.fxml"));
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
        TableVShedule.getItems().clear();

        ArrayList<Schedule> shedules = sheduleDAO.getClassroom_StudentList();
        for (Schedule shedule : shedules) {
            sheduleList.add(shedule); }
        TableVShedule.setItems(sheduleList);
    }
    private void enterStudentIDFilter(){
        txtCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String ID = txtCode.getText();
                String Name = txtClassroom.getText();
                String teacherID = txtTeacher.getText();
                filter(ID,Name,teacherID);
            }
        });
    }

    private void enterClassNameFilter(){
        txtClassroom.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String ID = txtCode.getText();
                String Name = txtClassroom.getText();
                String teacherID = txtTeacher.getText();
                filter(ID,Name,teacherID);
            }
        });
    }
    private void enterTeacherCodeFilter(){
        txtTeacher.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String ID = txtCode.getText();
                String Name = txtClassroom.getText();
                String teacherID = txtTeacher.getText();
                filter(ID,Name,teacherID);
            }
        });
    }
    private void filter(String studentID,String name,String teacherID){
        TableVShedule.getItems().clear();
        ArrayList<Schedule> sheduleArrayList = sheduleDAO.getSheduleWithFilter(studentID,name,teacherID);
        for (Schedule shedule : sheduleArrayList){
            sheduleList.add(shedule);
        }
//        TableVShedule.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colClassroomID.setCellValueFactory(new PropertyValueFactory<>("classroomId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("classroomName"));
        colCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        colCreateDate.setCellFactory(column -> {
            TableCell<Schedule, Date> cell = new TableCell<Schedule,Date>() {
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
        colStudentCode.setCellValueFactory(new PropertyValueFactory<>("studentCode"));

        Callback<TableColumn<Schedule, String>, TableCell<Schedule, String>> cellFoctory = (TableColumn<Schedule, String> param) -> {
            final TableCell<Schedule, String> cell = new TableCell<Schedule, String>() {
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
                            shedule = TableVShedule.getSelectionModel().getSelectedItem();
                            if(sheduleDAO.delete(shedule.getClassroomId())){
                                try {
                                    alertCustom.alertSuccess("Xóa lịch học thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    alertCustom.alertError("Bạn không thể xóa lịch học này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            reFresh();
                        });

                        editIcon.setOnMouseClicked(event -> {
                            shedule = TableVShedule.getSelectionModel().getSelectedItem();
                            editForm(shedule);
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
        TableVShedule.setItems(sheduleList);
    }
    private void loadData(){
        reFresh();
//        TableVShedule.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colClassroomID.setCellValueFactory(new PropertyValueFactory<>("classroomId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("classroomName"));
        colCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        colCreateDate.setCellFactory(column -> {
            TableCell<Schedule, Date> cell = new TableCell<Schedule,Date>() {
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
        colStudentCode.setCellValueFactory(new PropertyValueFactory<>("studentCode"));

        Callback<TableColumn<Schedule, String>, TableCell<Schedule, String>> cellFoctory = (TableColumn<Schedule, String> param) -> {
            final TableCell<Schedule, String> cell = new TableCell<Schedule, String>() {
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
                            shedule = TableVShedule.getSelectionModel().getSelectedItem();
                            if(sheduleDAO.delete(shedule.getClassroomId())){
                                try {
                                    alertCustom.alertSuccess("Xóa lịch học thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    alertCustom.alertError("Bạn không thể xóa lịch học này!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            reFresh();
                        });

                        editIcon.setOnMouseClicked(event -> {
                            shedule = TableVShedule.getSelectionModel().getSelectedItem();
                            editForm(shedule);
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
        TableVShedule.setItems(sheduleList);
    }

    public void editForm(Schedule shedule){
        Stage editForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/editScheduleForm.fxml"));
        try {
            Parent parent = loader.load();
            editScheduleController edit = loader.getController();
            edit.getSchedule(shedule);
            editForm.setScene(new Scene(parent));
            editForm.initStyle(StageStyle.UNDECORATED);
            editForm.initModality(Modality.APPLICATION_MODAL);
            editForm.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

