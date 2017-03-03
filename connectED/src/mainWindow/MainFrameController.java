package mainWindow;

import java.io.IOException;
import java.util.List;

import communication.ChatTabController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainFrameController {
	@FXML private Pane interactionPane;
	@FXML private Pane chatPane;
	@FXML private Pane infoPane;
	@FXML private Pane stuffPane;
	
	private GridPane rootNode;
	
	private InteractionTabManagerController interactionTabManagerController;
	private ChatTabController chatTabController;
//	private InfoController;
//	private StuffController;
	private boolean hostMode;
	
	
	
//	public void setHostMode(){
//		this.hostMode = true;
//	}
//	
//	public void setClientMode(){
//		this.hostMode = false;
//	}
//	
	
	@FXML
	public void initialize(){
		// load FXML for components into the different panes
			this.rootNode = (GridPane) interactionPane.getParent();
//			this.hostMode = true;
			initializeChat();
			InitializeInteractionTab();

	}
	
	private void initializeChat(){
		FXMLLoader chatLoader =  new FXMLLoader(getClass().getResource("/communication/ChatManager.fxml"));
		try{
		Node newChatPane  = chatLoader.load();
		this.chatTabController = chatLoader.getController();
//		if(hostMode)
//			chatTabController.setHostMode();
//		else
//			chatTabController.setClientMode();
		rootNode.getChildren().remove(chatPane);
		rootNode.add(newChatPane, 1, 2);
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
	
	
	private void InitializeInteractionTab(){
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("InteractionTabManager.fxml"));
		try{
		Node newTabManager  = loader.load();
		this.interactionTabManagerController = loader.getController();
		rootNode.getChildren().remove(interactionPane);
		rootNode.add(newTabManager, 0, 0, 1, 3);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void onCloseRequest(){
		chatTabController.onCloseRequest();
	}
	

}
