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
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml")); //don't neccesarily need this. Can load directly instead of creating loader
			Parent root = loader.load(); // this loads the stuff into the root which is the root node displayed in the Stage
			this.controller = loader.getController();
			Scene window = new Scene(root,700,400);
			primaryStage.setOnCloseRequest((event) ->{
				controller.onCloseRequest();
				System.exit(0);
			}); 
			primaryStage.setTitle("Main Window");
			primaryStage.setScene(window);
			primaryStage.show();
			
			Screen screen = Screen.getPrimary(); 
			Rectangle2D bounds = screen.getVisualBounds();
			primaryStage.setX(bounds.getMinX());
			primaryStage.setY(bounds.getMinY());
			primaryStage.setWidth(bounds.getWidth()*0.4);
			primaryStage.setHeight(bounds.getHeight()*0.8);
			primaryStage.setMinWidth(900);
			primaryStage.setMinHeight(600);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public MainFrameController getMainFrameController(){
		return this.controller;
	}
}
