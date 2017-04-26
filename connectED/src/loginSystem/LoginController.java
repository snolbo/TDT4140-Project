package loginSystem;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import javafx.scene.control.PasswordField;

public class LoginController {
	@FXML
	private TextField usernameField;
	@FXML
	private Button signInBtn;
	@FXML
	private PasswordField passwordField;

	
	private boolean isEmailVerified = false;
	private List<String> assistantSubjects = new ArrayList<String>();
	
	
	@FXML
	void initialize(){
		
	}
	
	
	public void onSignInButtonPressed(){
		// retrieve info from firebase server
		
		// populate isEmailVerified
		
		if(isEmailVerified){
			
		}
		
		
	}
	
	
	
	
	
	
}
