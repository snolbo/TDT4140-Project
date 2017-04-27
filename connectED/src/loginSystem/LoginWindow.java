package loginSystem;

import java.net.URL;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
	
	
	public class LoginWindow extends Application {
	    private Scene scene;
	    @Override public void start(Stage stage) {
	        // create the scene
	        stage.setTitle("Login window");
	        scene = new Scene(new Browser(stage),880,280, Color.web("#666970"));
	        stage.setScene(scene);        
	        stage.show();
	    }
	 
	    public static void main(String[] args){
	        launch(args);
	    }
	}
