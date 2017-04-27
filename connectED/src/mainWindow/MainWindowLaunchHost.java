package mainWindow;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class MainWindowLaunchHost extends Application {
	
	MainFrameController controller;
	Stage stage;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml")); //don't neccesarily need this. Can load directly instead of creating loader
			Parent root = loader.load(); // this loads the stuff into the root which is the root node displayed in the Stage
			this.controller = loader.getController();
			Scene window = new Scene(root,700,400);
			stage.setOnCloseRequest((event) ->{
				controller.onCloseRequest();
				System.exit(0);
			}); 
			stage.setTitle("Main Window");
			stage.setScene(window);
			stage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showStage(){
		Screen screen = Screen.getPrimary(); 
		Rectangle2D bounds = screen.getVisualBounds();
		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth()*0.8);
		stage.setHeight(bounds.getHeight()*0.8);
		stage.setMinWidth(900);
		stage.setMinHeight(600);
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public MainFrameController getMainFrameController(){
		return this.controller;
	}
}
