package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.Account;
import Models.AccountDAO;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class accountController implements Initializable {
    @FXML
    Button btnAdd,btnReload;

    @FXML
    ImageView imgVAdd,imgVReload;

    @FXML
    TableView<Account> TableVAccount;
    @FXML
    TableColumn<Account,Integer> colID;
    @FXML
    TableColumn<Account,String > colName;
    @FXML
    TableColumn<Account,String > colPassword;
    @FXML
    TableColumn<Account,String> colTeacherCode;
    @FXML
    TableColumn <Account,Integer> colType;
    @FXML
    TableColumn<Account,String> colButton;
    ObservableList <Account> AccountList = FXCollections.observableArrayList();

    @FXML
    TextField txtCode,txtUsername;
    @FXML
    ComboBox cbbType;

    AlertCustom alertCustom = new AlertCustom();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImg();
        loadData();
        loadCBB();
        enterForUsernameFilter();
        enterForTeacherCodeFilter();
    }
    AccountDAO accountDAO = new AccountDAO();
    Account account = new Account();
    private void setImg(){
        File add = new File("resources/images/add-user.png");
        Image addclass = new Image(add.toURI().toString());
        imgVAdd.setImage(addclass);

        File reload = new File("resources/images/refresh.png");
        Image reloadclass = new Image(reload.toURI().toString());
        imgVReload.setImage(reloadclass);
    }

    private void loadCBB(){
        cbbType.getItems().addAll("0","1","Tất cả");
    }
    @FXML
    private void getAddView(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Views/Admin/addAccountForm.fxml"));
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
        TableVAccount.getItems().clear();

        ArrayList<Account> accountList = accountDAO.getAccountList();
        for(Account account : accountList){
            AccountList.add(account);
        }
        TableVAccount.setItems(AccountList);
    }

    private void enterForUsernameFilter(){
        txtUsername.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String cbbAccountType = (String)(cbbType.getSelectionModel().getSelectedItem());
                if(cbbAccountType == null || cbbAccountType == "Tất cả") {
                    cbbAccountType ="";
                }
                String username = txtUsername.getText();
                String teacherCode = txtCode.getText();
                filter(cbbAccountType,username,teacherCode);
            }
        });
    }

    private void enterForTeacherCodeFilter(){
        txtCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String cbbAccountType = (String)(cbbType.getSelectionModel().getSelectedItem());
                if(cbbAccountType == null || cbbAccountType == "Tất cả"){
                    cbbAccountType ="";
                }
                String username = txtUsername.getText();
                String teacherCode = txtCode.getText();
                filter(cbbAccountType,username,teacherCode);
            }
        });
    }

    private void filter(String cbbType,String userName ,String  Code){
        TableVAccount.getItems().clear();

        ArrayList<Account> accounts = accountDAO.getAccountListFilter(cbbType,userName,Code);
        for (Account account : accounts){
            AccountList.addAll(account);
        }
        TableVAccount.setItems(AccountList);
        TableVAccount.setColumnResizePolicy(TableVAccount.CONSTRAINED_RESIZE_POLICY);
        colID.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colTeacherCode.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        Callback<TableColumn<Account, String>, TableCell<Account, String>> cellFoctory = (TableColumn<Account, String> param) -> {
            final TableCell<Account, String> cell = new TableCell<Account, String>() {
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
                            account = TableVAccount.getSelectionModel().getSelectedItem();
                            if(accountDAO.delete(account)){
                                try {
                                    alertCustom.alertSuccess("Xóa tài khoản thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    alertCustom.alertError("Không thể xóa!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            reFresh();
                        });

                        editIcon.setOnMouseClicked(event -> {
                            account = TableVAccount.getSelectionModel().getSelectedItem();
                            try {
                                editForm(account);
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
        TableVAccount.setItems(AccountList);
    }

    @FXML
    public void getAccountType(){
        String cbbTypeAccount = (String)(cbbType.getSelectionModel().getSelectedItem());
        if(cbbTypeAccount == null || cbbTypeAccount == "Tất cả"){
            cbbTypeAccount ="";
        }
        String username = txtUsername.getText();
        String teacherCode = txtCode.getText();
        filter(cbbTypeAccount,username,teacherCode);
    }
    public void loadData(){
        reFresh();

        TableVAccount.setColumnResizePolicy(TableVAccount.CONSTRAINED_RESIZE_POLICY);
        colID.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colTeacherCode.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        Callback<TableColumn<Account, String>, TableCell<Account, String>> cellFoctory = (TableColumn<Account, String> param) -> {
            final TableCell<Account, String> cell = new TableCell<Account, String>() {
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
                            account = TableVAccount.getSelectionModel().getSelectedItem();
                            if(accountDAO.delete(account)){
                                try {
                                    alertCustom.alertSuccess("Xóa tài khoản thành công!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    alertCustom.alertError("Không thể xóa!");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            reFresh();
                        });

                        editIcon.setOnMouseClicked(event -> {
                            account = TableVAccount.getSelectionModel().getSelectedItem();
                            try {
                                editForm(account);
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
        TableVAccount.setItems(AccountList);
    }

    public void editForm(Account account) throws IOException {
        Stage editForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Admin/editAccountForm.fxml"));
        Parent root = loader.load();
        editAccountController editAccountController = loader.getController();
        editAccountController.getID(account);
        editForm.setScene(new Scene(root));
        editForm.initStyle(StageStyle.UNDECORATED);
        editForm.initModality(Modality.APPLICATION_MODAL);
        editForm.showAndWait();
    }
}
