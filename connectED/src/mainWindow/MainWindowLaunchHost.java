package mainWindow;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class MainWindowLaunchHost extends Application {
	MainFrameController controller;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml")); //don't neccesarily need this. Can load directly instead of creating loader
			Parent root = loader.load(); // this loads the stuff into the root which is the root node displayed in the Stage
			controller = loader.getController();
			Scene window = new Scene(root,1000,700);
			primaryStage.setOnCloseRequest((event) ->{
				controller.onCloseRequest();
				System.exit(0);
			}); 
			primaryStage.setTitle("Main Window");
			primaryStage.setScene(window);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
