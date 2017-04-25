package loginSystem;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class LoginWindowController {
	@FXML private WebView browser;
	
	@FXML
	public void initialize() {
		browser.getEngine().load("LoginClient.html");
		
		//Event
	}
	
	private String displayName = (String) browser.getEngine().executeScript("firebase.auth().currentUser.displayName;"); //gets roles
	private boolean isValidLogin() {
		String authStatus = (String) browser.getEngine().executeScript("firebase.auth().currentUser;");	//returns true if login is valid
		if (authStatus.equals("true")) {
			System.out.println("authStatus = true");
			return true;
		}
		else if (authStatus.equals("false")) {
			System.out.println("authStatus = false");
			return false;
		}
		else {
			System.out.println("authStatus is neither true nor false, fix code");
			return false;
		}
	}
	private boolean isEmailVerified() {		//returns true if email is verified
		String emailVerified = (String) browser.getEngine().executeScript("firebase.auth().currentUser.emailVerified;");
		if (emailVerified.equals("true")) {
			System.out.println("emailVerified = true");
			return true;
		}
		else if (emailVerified.equals("false")) {
			System.out.println("emailVerified = false");
			return false;
		}
		else {
			System.out.println("emailVerified is neither true nor false, fix code");
			return false;
		}	
	}

}
