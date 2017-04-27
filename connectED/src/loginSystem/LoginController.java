package loginSystem;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mainWindow.MainWindowLaunchHost;
import javafx.scene.control.PasswordField;

public class LoginController {
	@FXML
	private TextField usernameField;
	@FXML
	private Button signInBtn;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Text errorText;

	
	private boolean isEmailVerified = false;
	private List<String> assistantSubjects = new ArrayList<String>();
	
	
	@FXML
	void initialize(){
		errorText.setVisible(false);
		signInBtn.setOnAction((event)->{
			onSignInButtonPressed();
		});
	}
	
	
	
	/**
	 * This method is set to be called whenever signInBtn is pressed, it is set in @FXML void initialize()
	 */
	public void onSignInButtonPressed(){
		// 1. retrieve info of user from firebase with password
		
		// 2. update emailIsVerified and set info to assistantSubjects
		
		
		// check if email is verified
		if(isEmailVerified){
			Stage appStage = new Stage();
			MainWindowLaunchHost appLaunch = new MainWindowLaunchHost();
			appLaunch.getMainFrameController().passUserInfo(assistantSubjects);
			appLaunch.start(appStage);
		}
		else{
			errorText.setVisible(true);
		}
		
		
	}
	
	
	
	
	
	
}
