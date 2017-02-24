package server;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class IMServerLauncher extends Application{	
	private ServerController controller;
	// GUI is separated from the Server and ClientController. FXMLLoader loads the GUI and it's controller which is made and connected with Scenebuilder
	public IMServerLauncher(){
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlResources/ServerChatConnectionGUI.fxml")); //don't neccesarily need this. Can load directly instead of creating loader
			Parent root = loader.load();
			ServerController controller = loader.getController();
			Scene serverConnectionWindow = new Scene(root,400,475);
			primaryStage.setScene(serverConnectionWindow);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
				@Override
				public void handle(WindowEvent event) {
					if(event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST){
						controller.windowClosed();
						System.exit(0);
					}
				}	
			});
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public ServerController getController(){
		return this.controller;
	}
	
	public static void main(String[] args) {
		launch(args);		
	}
}