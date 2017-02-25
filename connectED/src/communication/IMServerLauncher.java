package communication;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class IMServerLauncher extends Application{	
	private ChatTabController controller;
	// GUI is separated from the Server and ClientController. FXMLLoader loads the GUI and it's controller which is made and connected with Scenebuilder
	public IMServerLauncher(){
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatManager.fxml"));
			Parent root = loader.load();
			ChatTabController controller = loader.getController();
			controller.setHostMode();	// Tells the Connector class that is should host connections
			Scene serverConnectionWindow = new Scene(root,400,475);
			primaryStage.setTitle("Host IM Messenger");
			primaryStage.setScene(serverConnectionWindow);
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
	
	public ChatTabController getController(){
		return this.controller;
	}
	
	public static void main(String[] args) {
		launch(args);		
	}
}