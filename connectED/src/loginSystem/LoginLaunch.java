package loginSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import loginSystem.LoginController;

public class LoginLaunch extends Application {
	LoginController controller;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
			Parent root = loader.load(); 
			this.controller = loader.getController();
			Scene window = new Scene(root,450,250);
			primaryStage.setOnCloseRequest((event) ->{
				System.exit(0);
			}); 
//			primaryStage.setTitle("Login Window");
			primaryStage.setScene(window);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//public static void main(String[] args) {
	//	launch(args);
	//}
}
