package mainWindow;

import java.util.List;

import communication.ChatTabController;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import mainWindow.MainFrameController;
import loginSystem.Browser;

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
	private MainFrameController mainFrameController = new MainFrameController();
	
	//private List<String> assistantSubjects = mainFrameController.getUserInfo(); 
	private List<String> assistantSubjects;
	
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
		/*if (assistantSubjects.isEmpty()) {
			assistantBtn.setDisable(true);
		} 
		else {
			assistantBtn.setDisable(false);
		}*/
		//if (!assistantSubjects.isEmpty()) {
			assistantBtn.setOnAction( (event) ->{modeSelection("StudentAssistant");});
		//}
		helperBtn.setOnAction( (event) ->{modeSelection("StudentHelper");});
		studentBtn.setOnAction( (event) ->{modeSelection("Student");});
		
		/*if (!assistantSubjects.contains("TDT4100")) {
			javaBtn.setDisable(true);
		}
		else {
			javaBtn.setDisable(false);
		}*/
		
		//if (assistantSubjects.contains("TDT4100")) {
			javaBtn.setOnAction((event)-> {
			subjectSelection("Java");
		});//}
		
		/*if (!assistantSubjects.contains("TDT4110")) {
			itgkPythonBtn.setDisable(true);
		}
		else {
			itgkPythonBtn.setDisable(false);
		}*/
		//if (assistantSubjects.contains("TDT4110")) {
			itgkPythonBtn.setOnAction((event) -> {
			subjectSelection("ITGK");
		});//}
		
//		itgkMatlabBtn.setOnAction((event)-> {
//			mainFrameController.chatTabController.setSubject("ITKG");
//			resetSubjectButtonColor();
//			itgkMatlabBtn.setStyle("-fx-background-color: " + selectedButtonColor);
//		});
		
		connectBtn.setOnAction((event)->{
			useConnectButton();
		});
	}
	
	
	/**
	 * @param mode
	 * Performs correct action based on String representation of mode passed to the function based what is set in initButtons
	 */
	public void modeSelection(String mode){
		mainFrameController.chatTabController.setStudentHelperMode();
		resetModeButtonColor();
		colorConnectButton();
		switch (mode) {
		case "StudentAssistant":
			/*if (!assistantSubjects.isEmpty()) {*/
				mainFrameController.chatTabController.setAssistantMode();
				assistantBtn.setStyle("-fx-background-color: " + selectedButtonColor);
				break;
			//}
		case "StudentHelper": 
			mainFrameController.chatTabController.setStudentHelperMode();
			helperBtn.setStyle("-fx-background-color: " + selectedButtonColor);
			break;
		case "Student":
			mainFrameController.chatTabController.setStudentMode();
			studentBtn.setStyle("-fx-background-color: " + selectedButtonColor);
			break;
		default:
			break;
		}
	}
	

	/**
	 * @param subject
	 * Performs correct action for subjectselection based on a String representation of the subject set in initButtons
	 */
	public void subjectSelection(String subject){
		mainFrameController.chatTabController.setSubject(subject);
		resetSubjectButtonColor();
		colorConnectButton();
		switch (subject) {
		case "ITGK":
			/*if (assistantSubjects.contains("TDT4110")) {*/
				itgkPythonBtn.setStyle("-fx-background-color: " + selectedButtonColor);
				codeLanguage = "python";
				break;
			/*}
			else {
				break;
			}*/
		case "Java":
			/*if (assistantSubjects.contains("TDT4100")) {*/
				javaBtn.setStyle("-fx-background-color: " + selectedButtonColor);
				codeLanguage = "java";
				break;
			/*}
			else {
				break;
			}*/
		default:
			break;
		}
	}
	
	public void useConnectButton(){
		if(mainFrameController.chatTabController.modeAndSubjectIsSet()){
			mainFrameController.chatTabController.combineTags();
			errorText.setVisible(false);
			mainFrameController.chatTabController.newChatTab(codeLanguage);
		}
		else{
			errorText.setVisible(true);
			System.out.println(assistantSubjects);
		}
	}
	
	public List<String> getAssistantSubjects() {
		return this.assistantSubjects;
	}
	
	public void setAssistantSubjects(List<String> assistantSubjects) {
		this.assistantSubjects = assistantSubjects;
		System.out.println("ASubj modeselector: "+this.assistantSubjects);
	}

}
