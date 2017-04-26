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
	        scene = new Scene(new Browser(),880,260, Color.web("#666970"));
	        stage.setScene(scene);        
	        stage.show();
	    }
	 
	    public static void main(String[] args){
	        launch(args);
	    }
	}
	class Browser extends Region {
	 
	    final WebView browser = new WebView();
	    final WebEngine webEngine = browser.getEngine();
	    public Boolean loginStatus;
	    public Boolean emailVerified;
	    public String role;
	     
	    public Browser() {
	        //apply the styles
	        getStyleClass().add("browser");
	        // load the web page
	        URL url = getClass().getResource("LoginClient.html");
	        webEngine.load(url.toExternalForm());
	        webEngine.getLoadWorker().stateProperty().addListener((observed, oldValue, newValue) -> {
				if(newValue.equals(Worker.State.SUCCEEDED)) { 
					webEngine.executeScript("loadPage();");
					browser.setOnMouseClicked((event)->{
//						try {
//							Thread.sleep(3000); //Delay to give browser time to sign in before getting updated loginstatus
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						loginStatus = (Boolean) webEngine.executeScript("getLoginStatus();"); //Gets variables from javascript
						System.out.println(loginStatus);
						if (loginStatus) {
							if (!role.contains("null")) {
								role = (String) webEngine.executeScript("getRole()");
							} 
							else {
								role = null;
							}
							emailVerified = (Boolean) webEngine.executeScript("getEmailVerifiedStatus();");
							System.out.println(role+" "+emailVerified);
						}
					});
				}});

	        
	        //add the web view to the scene
	        getChildren().add(browser);
	       
	 
	    }
	    
	    private Node createSpacer() {
	        Region spacer = new Region();
	        HBox.setHgrow(spacer, Priority.ALWAYS);
	        return spacer;
	    }
	 
	    @Override protected void layoutChildren() {
	        double w = getWidth();
	        double h = getHeight();
	        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
	    }
	 
	    @Override protected double computePrefWidth(double height) {
	        return 880;
	    }
	 
	    @Override protected double computePrefHeight(double width) {
	        return 260;
	    }
	}
