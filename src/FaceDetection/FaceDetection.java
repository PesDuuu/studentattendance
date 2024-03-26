package FaceDetection;

import Controllers.User.FaceDetectionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class FaceDetection extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/User/faceDetectionForm.fxml"));
			BorderPane root = (BorderPane) loader.load();
			// set a whitesmoke background
			root.setStyle("-fx-background-color: whitesmoke;");
			// create and style a scene
			Scene scene = new Scene(root, 1226, 609);
			// create the stage with the given title and the previously created
			// scene
			primaryStage.setTitle("Face Detection");
			primaryStage.setScene(scene);
			// show the GUI
			primaryStage.show();

			// init the controller
			FaceDetectionController controller = loader.getController();
			controller.init();
//
//			// set the proper behavior on closing the application
//			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
//				public void handle(WindowEvent we)
//				{
//					controller.setClosed();
//				}
//			}));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		launch(args);
	}
}
