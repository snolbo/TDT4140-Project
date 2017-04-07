package communication;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PopUpSubjectController {
	
	@FXML private Button javaBtn;
	@FXML private Button itgkBtn;
	
	private ChatTabController chatTabController;
	
	public void passChatTabController(ChatTabController chatTabController) {
		this.chatTabController = chatTabController;
	}
	
	public void initializeJavaButton(){
		chatTabController.mergeTags("Java");
		chatTabController.closePopUp();
	}
	
	public void initializeITGKButton(){
		chatTabController.mergeTags("ITGK");
		chatTabController.closePopUp();
	}
	
	public ChatTabController getChatTabController(){
		return chatTabController;
	}
	
}
