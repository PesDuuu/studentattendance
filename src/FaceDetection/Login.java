package FaceDetection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;

import java.io.File;

public class Login extends Application {

    public static void main(String[] args) {
//         load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("../Views/login.fxml"));
            File add = new File("resources/images/icon-app.jpg");
            Image addClass = new Image(add.toURI().toString());
            primaryStage.getIcons().add(addClass);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 520, 340);
            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
