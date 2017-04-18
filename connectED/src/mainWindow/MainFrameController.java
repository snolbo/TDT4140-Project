package mainWindow;

import java.io.IOException;

import communication.ChatTabController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

public class MainFrameController {
	@FXML private Pane interactionPane;
	@FXML private Pane chatPane;
	@FXML private Pane infoPane;
	@FXML private WebView searchBrowser;
	
	private GridPane rootNode;
	
	private InteractionTabManagerController interactionTabManagerController;
	private ChatTabController chatTabController;

	Node startingInteractionTab;
	Node currentInteractionArea;
	private Node selectionModeContent;
	private Tab selectionModeTab;


	
	
	@FXML
	public void initialize(){
			this.rootNode = (GridPane) interactionPane.getParent();
			initializeChat();
			initializeInteractionTab();
			searchBrowser.getEngine().load("http://www.google.com");
			

	}
	
	public InteractionTabManagerController getInteractionTabManagerController(){
		return this.interactionTabManagerController;
	}
	
	
	
	private void initializeChat(){
		System.out.println("Initializing chat-area...");
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

	
	public Node getCurrentInteractionArea(){
		return this.currentInteractionArea;
	}

	
	private void initializeInteractionTab(){
		System.out.println("Initializing welcome interactiontab in interactionArea...");
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
		System.out.println("Loading new interactionTab in interactionArea");
		rootNode.getChildren().remove(currentInteractionArea);
		rootNode.add(interactionArea, 0, 0, 1, 3);
		currentInteractionArea = interactionArea;
	}
	
	public void onCloseRequest(){
		System.out.println("Closerequest in MainFrameController calling onCloseRequest in chatTabController...");
		chatTabController.onCloseRequest();
	}

}
