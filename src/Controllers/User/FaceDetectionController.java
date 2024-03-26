package Controllers.User;

import Controllers.Alert.AlertCustom;
import javafx.application.Platform;
import DBConnection.DBConnection;
import FaceDetection.Utils;
import Models.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import RequestServer.FaceRecognition;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;

public class FaceDetectionController implements Initializable {
	// FXML buttons
	@FXML
	private Button cameraButton,btnLogout,btnStudentList;
	// the FXML area for showing the current frame
	@FXML
	private ImageView originalFrame,imgStart,imgStudentList, imgBack, imgStudent, imgStudentAtt;
	@FXML
	VBox vBox;
	@FXML
	TextField txtFind;
	@FXML
	Label lblID,lblFullName,lblDateOfBirth,lblClassName,lblFacultyName,lblClassCode,lblName;

	@FXML
	ComboBox<String> cbbNumberOfSessions;

	AttendanceSessionDAO attendanceSessionDAO = new AttendanceSessionDAO();
	AttendanceSessionDetailsDAO attendanceSessionDetailsDAO = new AttendanceSessionDetailsDAO();
	ScheduleDAO scheduleDAO = new ScheduleDAO();
	AlertCustom alertCustom = new AlertCustom();

	final ObservableList<Student> data = FXCollections.observableArrayList();
	public static final int FONT_HERSHEY_PLAIN = 0;
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	private VideoCapture capture;
	// a flag to change the button behavior
	private boolean cameraActive;

	// face cascade classifier
	private CascadeClassifier faceCascade;
	private int absoluteFaceSize;

	//Flag for face recognition
	private int flagForSendToServer = 0;

	private boolean flag = false;

	private String result = "";

	Image image_Att_default = null;

	int attendanceSessionId;

	String base64ImageSendToServer ;

	public void init() {
		this.capture = new VideoCapture();
		this.faceCascade = new CascadeClassifier();
		this.absoluteFaceSize = 0;
		this.faceCascade.load("resources/haarcascades/haarcascade_frontalface_alt.xml");
		// set a fixed width for the frame
		originalFrame.setFitWidth(600);
		// preserve image ratio
		originalFrame.setPreserveRatio(true);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		File image_link = new File("resources/images/StudentAtt.png");
		image_Att_default = new Image(image_link.toURI().toString());
		setImage();
		enterForStudentCodeFilter();
	}

	private void setImage(){
		File start = new File("resources/images/camera.png");
		Image Start = new Image(start.toURI().toString());
		imgStart.setImage(Start);

		File image_StudentList = new File("resources/images/list-icon.png");
		Image ImageStudentList = new Image(image_StudentList.toURI().toString());
		imgStudentList.setImage(ImageStudentList);

		File image_Back = new File("resources/images/Logout.png");
		Image ImageBack = new Image(image_Back.toURI().toString());
		imgBack.setImage(ImageBack);

		File image_start = new File("resources/images/camera-viewfinder-screen-photo.jpg");
		Image image_Start = new Image(image_start.toURI().toString());
		originalFrame.setImage(image_Start);
		originalFrame.setFitWidth(600);

		imgStudentAtt.setImage(image_Att_default);
		imgStudent.setImage(image_Att_default);
	}

	@FXML
	protected void startCamera() throws  IOException {

		if(cbbNumberOfSessions.getSelectionModel().getSelectedItem() == null){
			alertCustom.alertError("Vui lòng chọn buổi điểm danh!");
			return;
		}

		if (!this.cameraActive) {

			// start the video capture
			this.capture.open(1);

			// is the video stream available?
			if (this.capture.isOpened()) {
				this.cameraActive = true;

				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {

					@Override
					public void run() {
						// effectively grab and process a single frame
						Mat frame = grabFrame();
						// convert and show the frame
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(originalFrame, imageToShow);
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

				// update the button content
				this.cameraButton.setText("Kết Thúc");
			} else {
				// log the error
				System.err.println("Failed to open the camera connection...");
			}
		} else {
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.cameraButton.setText("Bắt đầu");
			// stop the timer
			this.stopAcquisition();
		}
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 *
	 * @return the {@link Image} to show
	 */
	private Mat grabFrame() {
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (!frame.empty()) {
					// face detection
					this.detectAndDisplay(frame);
				}

			} catch (Exception e) {
				// log the (full) error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		return frame;
	}

	public Mat cutImage(Mat src, Rect rect) {
		Mat src_roi = new Mat(src, rect);
		Mat cutImage = new Mat();
		src_roi.copyTo(cutImage);
		return cutImage;
	}

	private void detectAndDisplay(Mat frame) throws Exception {
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();
		Mat image = frame.clone();

		//Raw line for detect face
		RawLine(frame);

		// convert frame to gray
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);

		// height of face size > 50% height of frame image
		if (this.absoluteFaceSize == 0) {
			int height = grayFrame.rows();
			if (Math.round(height * 0.5f) > 0) {
				this.absoluteFaceSize = Math.round(height * 0.5f);
			}
		}
		// detect faces
		this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

		// each rectangle in faces is a face: draw them!
		Rect[] facesArray = faces.toArray();

		//only detect for 1 face, if face >  1 then notify and reset  flagForSendToServer
		if (facesArray.length > 1) {
			String text = "Only Face !";
			Point org = new Point(640 / 4, (480 / 6) - 5);
			Scalar color = new Scalar(0, 0, 255);
			int fontType = FONT_HERSHEY_PLAIN;
			int fontSize = 1;
			int thickness = 3;
			Imgproc.putText(frame, text, org, fontType, fontSize, color, thickness);
			flagForSendToServer = 0;
		}

		// flagForSendToServer + 1 when detected face
		if (facesArray.length == 1 && flag == false) {
			flagForSendToServer++;
			for (int i = 0; i < facesArray.length; i++) {
//				System.out.println("tl: " + facesArray[i].tl() + "| br: " + facesArray[i].br());
				Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
				Rect rect = new Rect(facesArray[i].tl(), facesArray[i].br());
				image = cutImage(image, rect);
			}
		}

		//each 20 frames then send to server for recognition
		if (flagForSendToServer > 20 && flag == false) {
			flagForSendToServer = 0;
			System.out.println("send");
			flag = true;
			Mat finalImage = image;
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						byte[] return_buff = new byte[(int) (finalImage.total() * finalImage.channels())];
						System.out.println(finalImage.size());
						finalImage.get(0, 0, return_buff);
						MatOfByte matOfByte = new MatOfByte();
						Imgcodecs.imencode(".jpg", finalImage, matOfByte);
						byte[] byteArray = matOfByte.toArray();
						base64ImageSendToServer = Base64.getEncoder().encodeToString(byteArray);
						result = RecognitionStudent(base64ImageSendToServer);
						Thread.sleep(2000);

						System.out.println(result);
					} catch (Exception e) {
						flag = false;
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}



		if(result != ""){
			if(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceSessionId, result)){
				Student studentAtt = new StudentDAO().getStudentByID(result);
				String base64ImageOfStudentAtt = attendanceSessionDetailsDAO.getBase64ImageFromAtt(attendanceSessionId, result);
				Platform.runLater(() ->{
					Image image_Att = null;
					if(base64ImageOfStudentAtt == null){
						image_Att = image_Att_default ;
					}else {
						image_Att = convertBase64ImageToImage(base64ImageOfStudentAtt);
					}
					imgStudentAtt.setImage(image_Att);

					if(studentAtt.getBase64Image()!=null){
						imgStudent.setImage(convertBase64ImageToImage((studentAtt.getBase64Image())));
					}
					lblID.setText(studentAtt.getStudentCode());
					lblFullName.setText(studentAtt.getFullName());
					lblDateOfBirth.setText(String.valueOf(studentAtt.getDateOfBirth()));
					lblClassName.setText(studentAtt.getClassCode());
					lblFacultyName.setText(new FacultyDAO().getFacultyNameByClassCode(studentAtt.getClassCode()));
				});
			}else {
				System.out.println("=========: " + result);
				if(scheduleDAO.checkClassroom_StudentExist(lblClassCode.getText().split(" ")[2], result)){
					if(attendanceSessionDetailsDAO.insertStudentAtt(attendanceSessionId,result, base64ImageSendToServer)){
						Student studentAtt = new StudentDAO().getStudentByID(result);
						String base64ImageOfStudentAtt = attendanceSessionDetailsDAO.getBase64ImageFromAtt(attendanceSessionId, result);
						Platform.runLater(() ->{
							Image image_Att = null;
							if(base64ImageOfStudentAtt == null){
								image_Att = image_Att_default ;
							}else {
								image_Att = convertBase64ImageToImage(base64ImageOfStudentAtt);
							}
							imgStudentAtt.setImage(image_Att);

							if(studentAtt.getBase64Image()!=null){
								imgStudent.setImage(convertBase64ImageToImage((studentAtt.getBase64Image())));
							}
							lblID.setText(studentAtt.getStudentCode());
							lblFullName.setText(studentAtt.getFullName());
							lblDateOfBirth.setText(String.valueOf(studentAtt.getDateOfBirth()));
							lblClassName.setText(studentAtt.getClassCode());
							lblFacultyName.setText(new FacultyDAO().getFacultyNameByClassCode(studentAtt.getClassCode()));
							refreshStudentListInASD();
						});
					}else {
						Platform.runLater(() ->{
							refreshStudentInfo();
						});
					}
				}
			}
		}

		if (!result.isEmpty()) {
			System.out.println("Kết quả: " + result);
			flag = false;
			result = "";
		}

		if (flag == true) {
			String text = "true";
			Point org = new Point(640 / 4, (480 / 6) - 5);
			Scalar color = new Scalar(0, 0, 255);
			int fontType = FONT_HERSHEY_PLAIN;
			int fontSize = 1;
			int thickness = 3;
			Imgproc.putText(frame, text, org, fontType, fontSize, color, thickness);
		} else {
			String text = "false";
			Point org = new Point(640 / 4, (480 / 6) - 5);
			Scalar color = new Scalar(0, 0, 255);
			int fontType = FONT_HERSHEY_PLAIN;
			int fontSize = 1;
			int thickness = 3;
			Imgproc.putText(frame, text, org, fontType, fontSize, color, thickness);
		}
	}

	private void insertBase64Image(String base64Image){
		DBConnection DBConnection = new DBConnection();
		Connection connection = DBConnection.getConnection();
		String sql = "update attendancesessiondetails set base64Image = ? where studentCode = '1811063212' and attendanceSessionId = 1";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,base64Image);
			preparedStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String RecognitionStudent(String image_base64) throws Exception {
		FaceRecognition http = new FaceRecognition();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		System.out.println("Send image:" + image_base64);
//		http.sendGet();
		return http.sendPost(image_base64);
	}

	protected void RawLine(Mat frame) {

		Scalar color = new Scalar(75, 230, 109);

		// line vertical (1)
		Imgproc.line(frame, new Point(640 / 4, 480 / 6), new Point((640 / 4), (480 / 6) + 30), color, 5);
		// lane horizontal (1)
		Imgproc.line(frame, new Point(640 / 4, 480 / 6), new Point((640 / 4) + 30, (480 / 6)), color, 5);

		// line vertical (2)
		Imgproc.line(frame, new Point((640 / 4), 480 - (480 / 6)), new Point((640 / 4), 480 - (480 / 6) - 30), color, 5);
		// lane horizontal (2)
		Imgproc.line(frame, new Point((640 / 4), 480 - (480 / 6)), new Point(((640 / 4)) + 30, 480 - (480 / 6)), color, 5);

		// line vertical (3)
		Imgproc.line(frame, new Point(640 - (640 / 4), (480 / 6)), new Point(640 - (640 / 4) - 30, (480 / 6)), color, 5);
		// lane horizontal (3)
		Imgproc.line(frame, new Point(640 - (640 / 4), (480 / 6)), new Point(640 - (640 / 4), (480 / 6) + 30), color, 5);

		// line vertical (4)
		Imgproc.line(frame, new Point(640 - (640 / 4), 480 - (480 / 6)), new Point((640 - (640 / 4)) - 30, 480 - (480 / 6)), color, 5);
		// lane horizontal (4)
		Imgproc.line(frame, new Point(640 - (640 / 4), 480 - (480 / 6)), new Point((640 - (640 / 4)), 480 - (480 / 6) - 30), color, 5);


	}

	private void stopAcquisition() {
		if (this.timer != null && !this.timer.isShutdown()) {
			try {
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}

		if (this.capture.isOpened()) {
			// release the camera
			this.capture.release();
		}
	}

	private void updateImageView(ImageView view, Image image) {
		Utils.onFXThread(view.imageProperty(), image);
	}

	@FXML
	public void changeAttendanceSession(){
		String classroomId = lblClassCode.getText().split(" ")[2];

		if(cbbNumberOfSessions.getSelectionModel().getSelectedItem() == "Thêm.."){
			cbbNumberOfSessions.getItems().clear();
			attendanceSessionDAO.insertNumberSessionForClass(classroomId);
			loadNumberSessionsToCombobox(Integer.valueOf(classroomId));
			cbbNumberOfSessions.setValue(String.valueOf(attendanceSessionDAO.countNumberOfSession(Integer.valueOf(classroomId))));
		}
		imgStudent.setImage(image_Att_default);
		imgStudentAtt.setImage(image_Att_default);
		refreshStudentInfo();
		String idNumber = cbbNumberOfSessions.getSelectionModel().getSelectedItem();
		attendanceSessionId = new AttendanceSessionDAO().getIdByNumberSessionAndClassroomId(idNumber,classroomId );
		ArrayList<StudentAttendance> studentAttendances = new AttendanceSessionDetailsDAO().getStudentAttendanceList(attendanceSessionId);
		showListStudent(studentAttendances);
	}

	private String dateTimeFormat(Timestamp date){
		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
		String strDate = dateFormat.format(date);
		return strDate;
	}

	private String dateFormat(Date date){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = dateFormat.format(date);
		return strDate;
	}

	private void loadNumberSessionsToCombobox(int classroomId){
		for(int i = 1; i <= attendanceSessionDAO.countNumberOfSession(classroomId); i++){
			cbbNumberOfSessions.getItems().addAll(String.valueOf(i));
		}
		cbbNumberOfSessions.getItems().addAll("Thêm..");
	}

	public void setAttendanceOfClassroom(Classroom classroom){
		lblClassCode.setText("Mã lớp: "+ String.valueOf(classroom.getClassroomId()));
		loadNumberSessionsToCombobox(classroom.getClassroomId());
		lblName.setText("Tên môn học: "+ classroom.getClassroomName());
	}

	private void enterForStudentCodeFilter(){
		txtFind.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(cbbNumberOfSessions.getSelectionModel().getSelectedItem() == null){
					try {
						alertCustom.alertError("Vui lòng chọn buổi điểm danh!");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				String studentCode = txtFind.getText();
//				int attendanceSessionId = attendanceSessionDAO.getIdByNumberSessionAndClassroomId(cbbNumberOfSessions.getSelectionModel().getSelectedItem(),lblClassCode.getText().split(" ")[2]);
				ArrayList<StudentAttendance> getStudentAttendanceListByStudentCode = attendanceSessionDetailsDAO.getStudentAttendanceListByStudentCode(attendanceSessionId, studentCode);
				showListStudent(getStudentAttendanceListByStudentCode);
			}
		});
	}

	public void Logout() throws IOException {
		Stage stage = (Stage) btnLogout.getScene().getWindow();
		stage.close();
	}

	public void deleteStudentInASD() throws IOException {
		if(cbbNumberOfSessions.getSelectionModel().getSelectedItem() == null){
			alertCustom.alertError("Vui lòng chọn buổi điểm danh!");
			return;
		}

		if(lblID.getText() == ""){
			alertCustom.alertError("Vui lòng chọn chọn sinh viên cần hủy!");
			return;
		}

//		int attendanceSessionId = attendanceSessionDAO.getIdByNumberSessionAndClassroomId(cbbNumberOfSessions.getSelectionModel().getSelectedItem(),lblClassCode.getText().split(" ")[2]);

		Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION);
		alertDelete.setTitle("Hủy điểm danh sinh viên");
		alertDelete.setHeaderText(null);
		alertDelete.setContentText("Bạn có chắc chắn muốn xóa sinh viên: " + lblFullName.getText());
		Optional<ButtonType> result = alertDelete.showAndWait();
		if(result.get() == ButtonType.OK){
			if(attendanceSessionDetailsDAO.deleteStudent(attendanceSessionId, lblID.getText())){
				alertCustom.alertSuccess("Hủy điểm danh thành công!");
				refreshStudentListInASD();
				refreshStudentInfo();
				return;
			}
		}
	}

	@FXML
	private void OpenAddStudentToAttendanceForm() throws IOException {
		if(cbbNumberOfSessions.getSelectionModel().getSelectedItem() == null){
			alertCustom.alertError("Vui lòng chọn buổi điểm danh!");
			return;
		}

		Stage addStudentToAttendance = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/Views/User/addStudentToAttendance.fxml"));
		Parent root = loader.load();
		addStudentToAttendanceController aSTATController = loader.getController();
		aSTATController.setLbl(lblClassCode.getText().split(" ")[2], cbbNumberOfSessions.getSelectionModel().getSelectedItem());
		addStudentToAttendance.initStyle(StageStyle.UNDECORATED);
		addStudentToAttendance.initModality(Modality.APPLICATION_MODAL);
		addStudentToAttendance.setScene(new Scene(root));
		addStudentToAttendance.getScene().getRoot().setEffect(new DropShadow());
		addStudentToAttendance.showAndWait();
		refreshStudentListInASD();
	}

	@FXML
	public void refreshStudentListInASD(){
//		String classroomId = lblClassCode.getText().split(" ")[2];
//		String idNumber = cbbNumberOfSessions.getSelectionModel().getSelectedItem();
//		int id = new AttendanceSessionDAO().getIdByNumberSessionAndClassroomId(idNumber,classroomId );
		ArrayList<StudentAttendance> studentAttendances = new AttendanceSessionDetailsDAO().getStudentAttendanceList(attendanceSessionId);
		showListStudent(studentAttendances);
	}

	private void showListStudent(ArrayList<StudentAttendance> studentAttendances){
		vBox.getChildren().clear();
		int heightOfVBox = 0;
		if(studentAttendances.size() > 0)
		{
			for (StudentAttendance student : studentAttendances) {
				Button btn = new Button();
				Image image_Att = null;
				if(student.getBase64Image() == null){
					image_Att = image_Att_default ;
				}else {
					image_Att = convertBase64ImageToImage((student.getBase64Image()));
				}
				ImageView view = new ImageView(image_Att);
				view.setFitHeight(50);
				view.setPreserveRatio(true);

				VBox vBoxLabel = new VBox();
				HBox hBoxBtn = new HBox();
				hBoxBtn.setSpacing(10);
				Label labelStudentCode= new Label(student.getStudentCode());
				Label labelFullName= new Label(student.getFullName());
				Label labelTimeCheckIn= new Label(dateTimeFormat(student.getTimeCheckIn()));
//				dateTimeFormat(student.getTimeCheckIn());
				vBoxLabel.getChildren().addAll(labelStudentCode,labelFullName,labelTimeCheckIn);
				hBoxBtn.getChildren().addAll(view,vBoxLabel);

				btn.setGraphic(hBoxBtn);

//				btn.setLineSpacing(10);
				btn.setPrefWidth(270);
				btn.setPadding(new Insets(10, 10, 10, 10));
//				btn.setText("Mã SV: " + student.getStudentCode() + "\n" +"Họ và Tên: "+ student.getFullName() +"\nThời gian: " + student.getTimeCheckIn());
				heightOfVBox += 80;
				vBox.getChildren().add(btn);
				btn.setOnMousePressed(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event)  {
						Object node = event.getSource();
						Button b = (Button)node;
						String btnStudentID = labelStudentCode.getText();
						imgStudentAtt.setImage(view.getImage());
						try {
							Student student = new StudentDAO().getStudentByID(btnStudentID);
//							System.out.println(student.getBase64Image());
							if(student.getBase64Image()!=null){
								imgStudent.setImage(convertBase64ImageToImage((student.getBase64Image())));
							}
							lblID.setText(student.getStudentCode());
							lblFullName.setText(student.getFullName());
							lblDateOfBirth.setText(dateFormat(student.getDateOfBirth()));
							lblClassName.setText(student.getClassCode());
							lblFacultyName.setText(new FacultyDAO().getFacultyNameByClassCode(student.getClassCode()));
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				});
			}
		}
		if(heightOfVBox > 480){
			vBox.setPrefHeight(heightOfVBox);
		}
		else {
			vBox.setPrefHeight(480);
		}
	}



	private Image convertBase64ImageToImage(String base64Image){
		BufferedImage image = null;
		byte[] imageByte;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			imageByte = decoder.decodeBuffer(base64Image);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Image avt_image = SwingFXUtils.toFXImage(image, null);
		return avt_image;
	}

	private  void refreshStudentInfo(){
		imgStudentAtt.setImage(image_Att_default);
		imgStudent.setImage(image_Att_default);
		lblID.setText("");
		lblFullName.setText("");
		lblDateOfBirth.setText("");
		lblClassName.setText("");
		lblFacultyName.setText("");
	}

	public void openStudentList() throws IOException {
		Stage addStudentToAttendance = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/Views/User/showStudentListOfClassroomForm.fxml"));
		Parent root = loader.load();
		ShowStudentListOfClassroomController showStudentListOfClassroomController = loader.getController();
		showStudentListOfClassroomController.loadStudentList(lblClassCode.getText().split(" ")[2]);
		addStudentToAttendance.initStyle(StageStyle.UNDECORATED);
		addStudentToAttendance.initModality(Modality.APPLICATION_MODAL);
		addStudentToAttendance.setScene(new Scene(root));
		addStudentToAttendance.getScene().getRoot().setEffect(new DropShadow());
		addStudentToAttendance.showAndWait();
	}
}