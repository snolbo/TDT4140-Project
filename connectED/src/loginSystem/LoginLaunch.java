package loginSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import loginSystem.LoginWindowController;

public class LoginLaunch extends Application {
	LoginWindowController controller;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml")); //don't necessarily need this. Can load directly instead of creating loader
			Parent root = loader.load(); // this loads the stuff into the root which is the root node displayed in the Stage
			this.controller = loader.getController();
			Scene window = new Scene(root,450,250);
			primaryStage.setOnCloseRequest((event) ->{
				System.exit(0);
			}); 
			primaryStage.setTitle("Login Window");
			primaryStage.setScene(window);
			primaryStage.show();
//			
//			Screen screen = Screen.getPrimary(); 
//			Rectangle2D bounds = screen.getVisualBounds();
//			primaryStage.setX(bounds.getMinX());
//			primaryStage.setY(bounds.getMinY());
//			primaryStage.setWidth(bounds.getWidth()*0.5);
//			primaryStage.setHeight(bounds.getHeight()*0.5);
//			primaryStage.setMinWidth(600);
//			primaryStage.setMinHeight(400);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
