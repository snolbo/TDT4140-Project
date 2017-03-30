package mainWindow;

import java.io.IOException;

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
	
	Node startingInteractionTab;
	Node currentInteractionArea;
	
	
	@FXML
	public void initialize(){
			this.rootNode = (GridPane) interactionPane.getParent();
			initializeChat();
			initializeInteractionTab();

	}
	
	public InteractionTabManagerController getInteractionTabManagerController(){
		return this.interactionTabManagerController;
	}
	
	private void initializeChat(){
		FXMLLoader chatLoader =  new FXMLLoader(getClass().getResource("/communication/ChatManager.fxml"));
		try{
		Node newChatPane  = chatLoader.load();
		this.chatTabController = chatLoader.getController();
		chatTabController.passMainFrameController(this);
		rootNode.getChildren().remove(chatPane);
		rootNode.add(newChatPane, 1, 2);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Node getStartingInteractionTab(){
		return this.startingInteractionTab;
	}
	
	private void initializeInteractionTab(){
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("InteractionTabManager.fxml"));
		try{
		Node newTabManager  = loader.load();
		this.interactionTabManagerController = loader.getController();
		rootNode.getChildren().remove(interactionPane);
		rootNode.add(newTabManager, 0, 0, 1, 3);
		currentInteractionArea = newTabManager;
		startingInteractionTab = newTabManager;
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void loadNewInteractionArea(Node interactionArea){
		rootNode.getChildren().remove(currentInteractionArea);
		System.out.println("removed interationsArea");
		System.out.println("adding: " + interactionArea);
		rootNode.add(interactionArea, 0, 0, 1, 3);
		System.out.println("added: " + interactionArea);
		currentInteractionArea = interactionArea;
	}
	
	public void onCloseRequest(){
		chatTabController.onCloseRequest();
	}
	

}
