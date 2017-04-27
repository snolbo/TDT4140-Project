package loginSystem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import mainWindow.MainFrameController;
import mainWindow.MainWindowLaunchHost;

public class Browser extends Region {
	 
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    private Boolean loginStatus;
    private Boolean isEmailVerified;
    private String role;
    private Boolean enterStatus;
    private List<String> assistantSubjects = new ArrayList<String>();
    
    public Browser(Stage stage) {
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
        URL url = getClass().getResource("LoginClient.html");
        webEngine.load(url.toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener((observed, oldValue, newValue) -> {
			if(newValue.equals(Worker.State.SUCCEEDED)) { 
				webEngine.executeScript("loadPage();");
				browser.setOnMouseClicked((event)->{
					loginStatus = (Boolean) webEngine.executeScript("getLoginStatus();"); //Gets variables from javascript
					System.out.println("loginStatus: "+loginStatus);
					webEngine.executeScript("loadPage();");
					if (loginStatus) {
						role = (String) webEngine.executeScript("getRole()");
						if (!role.contains("null")) {
							role = role.substring(1); //Removes first ; to prevent index0 element of the list to be null 
							String[] list = role.split(";");
							for (int i = 0; i<list.length; i+=1) {
								assistantSubjects.add(list[i]);
							}
						}
						
						isEmailVerified = (Boolean) webEngine.executeScript("getEmailVerifiedStatus();");
						System.out.println("Role: "+role+"\nemailVerified: "+isEmailVerified);
						enterStatus = (Boolean) webEngine.executeScript("getEnterStatus();");
						System.out.println("enterStatus: "+enterStatus);
						if (enterStatus) {
							Stage appStage = new Stage();
							MainWindowLaunchHost appLauncher = new MainWindowLaunchHost();
							appLauncher.start(appStage);
//							appLauncher.getMainFrameController().passUserInfo(assistantSubjects);
							appLauncher.getMainFrameController().interactionTabManagerController.modeSelectionController.setAssistantSubjects(assistantSubjects);
							appLauncher.showStage();
							
							webEngine.executeScript("firebase.auth().signOut();");
							
							stage.close();

						}
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
        return 270;
    }
}

