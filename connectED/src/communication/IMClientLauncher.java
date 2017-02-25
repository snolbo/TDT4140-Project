package communication;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class IMClientLauncher extends Application {	
	// GUI is separated from the Server and ServerController. FXMLLoader loads the GUI and it's controller which is made and connected with Scenebuilder
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatManager.fxml")); //don't neccesarily need this. Can load directly instead of creating loader
			Parent root = loader.load(); // this loads the stuff into the root which is the root node displayed in the Stage
			ChatTabController controller = loader.getController();
			controller.setClientMode();
			Scene clientWindow = new Scene(root,400,475);
			primaryStage.setTitle("Client IM Messenger");
			primaryStage.setScene(clientWindow);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
				@Override
				public void handle(WindowEvent event) {
					if(event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST){
						System.exit(0);
						}
				}
			});
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
		
	public static void main(String[] args) {
		launch(args);		
	}
}