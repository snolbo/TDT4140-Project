package mainWindow;

import communication.ChatTabController;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * @author snorr
 * Controller for the FXMLelement where mode and subject is selected
 */
public class ModeSelectionController {
	@FXML
	private Button assistantBtn;
	@FXML
	private Button helperBtn;
	@FXML
	private Button studentBtn;
	@FXML
	private Button javaBtn;
	@FXML
	private Button itgkPythonBtn;
//	@FXML
//	private Button itgkMatlabBtn;
	@FXML
	private Button connectBtn;
	
	@FXML Text errorText;

	private String codeLanguage;
	
	
	private ChatTabController chatTabController;
	private MainFrameController mainFrameController;
	
	private String selectedButtonColor = "linear-gradient(#ff5400, #be1d00)";
	
	
	@FXML
	void initialize(){
		errorText.setVisible(false);
		connectBtn.setOpacity(0.5);
		

	}
	
	/**
	 * @param chatTabController
	 * Takes the ChatTabController object that is associated with this ModeSelectionContent
	 */
	public void passChatTabController(ChatTabController chatTabController){
		this.chatTabController = chatTabController;
	}

	/**
	 * Resets the color of the subjext-buttons to its default color
	 */
	public void resetSubjectButtonColor(){
		itgkPythonBtn.setStyle("");
		javaBtn.setStyle("");
//		itgkMatlabBtn.setStyle("");
	}
	
	/**
	 * Resets the color of the mode buttons to its default color
	 */
	private void resetModeButtonColor(){
		assistantBtn.setStyle("");
		helperBtn.setStyle("");
		studentBtn.setStyle("");
	}
	
	/**
	 * Color the connect-button depending on a subject and mode is selected
	 */
	public void colorConnectButton(){
		if(mainFrameController.chatTabController.modeAndSubjectIsSet()){
			connectBtn.setOpacity(1);
			errorText.setVisible(false);

		}
		else
			connectBtn.setOpacity(0.5);
			
	}
	
	
	/**
	 * @param mainFrameController
	 * Takes the MainFrameController being the root controller of the application, and sets the action taken by the buttons on the content
	 */
	public void initButtons(MainFrameController mainFrameController) {
		this.mainFrameController = mainFrameController;
		System.out.println("mainframecontroller: " + mainFrameController);
		System.out.println("chatTabController: " + chatTabController);
		assistantBtn.setOnAction( (event) ->{
			try {
				mainFrameController.chatTabController.setAssistantMode();
				resetModeButtonColor();
				assistantBtn.setStyle("-fx-background-color: " + selectedButtonColor);
				colorConnectButton();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		helperBtn.setOnAction( (event) ->{
			try {
				mainFrameController.chatTabController.setStudentHelperMode();
				resetModeButtonColor();
				helperBtn.setStyle("-fx-background-color: " + selectedButtonColor);
				colorConnectButton();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		studentBtn.setOnAction( (event) ->{
			try {
				mainFrameController.chatTabController.setStudentMode();
				resetModeButtonColor();
				studentBtn.setStyle("-fx-background-color: " + selectedButtonColor);
				colorConnectButton();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		javaBtn.setOnAction((event)-> {
			mainFrameController.chatTabController.setSubject("Java");
			resetSubjectButtonColor();
			javaBtn.setStyle("-fx-background-color: " + selectedButtonColor);
			colorConnectButton();
			codeLanguage = "java";

		});
		
		itgkPythonBtn.setOnAction((event)-> {
			mainFrameController.chatTabController.setSubject("ITGK");
			resetSubjectButtonColor();
			itgkPythonBtn.setStyle("-fx-background-color: " + selectedButtonColor);
			colorConnectButton();
			codeLanguage = "python";
		});
		
//		itgkMatlabBtn.setOnAction((event)-> {
//			mainFrameController.chatTabController.setSubject("ITKG");
//			resetSubjectButtonColor();
//			itgkMatlabBtn.setStyle("-fx-background-color: " + selectedButtonColor);
//		});
		
		connectBtn.setOnAction((event)->{
			if(mainFrameController.chatTabController.combineTags()){
				errorText.setVisible(false);
				mainFrameController.chatTabController.newChatTab(codeLanguage);
			}
			else{
				errorText.setVisible(true);
			}
		});

	}
	

}
