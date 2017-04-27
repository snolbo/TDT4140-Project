package mainWindow;

import java.io.IOException;
import java.util.List;

import communication.ChatTabController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

/**
 * @author snorr
 * Controller for the root element of the application
 */
public class MainFrameController {
	@FXML private Pane interactionPane;
	@FXML private Pane chatPane;
	@FXML private WebView searchBrowser;
	
	private GridPane rootNode;
	
	public InteractionTabManagerController interactionTabManagerController;
	public ChatTabController chatTabController;

	Node startingInteractionTabPane;
	Node currentInteractionTabPane;
	public InteractionTabManagerController currentInteractionTabManagerController;
	public InteractionTabManagerController startingInteractionTabManagerController;
	
	public List<String> assistantSubjects;


	
	
	@FXML
	public void initialize(){
			this.rootNode = (GridPane) interactionPane.getParent();
			initializeChat();
			initializeInteractionTab();
			searchBrowser.getEngine().load("http://www.google.com");
			searchBrowser.setOnKeyPressed((event) ->{
				if(event.isControlDown()){
					double currentVal = searchBrowser.getZoom();
					if(event.getCode() == KeyCode.MINUS && currentVal > 0.5)
						searchBrowser.setZoom(currentVal - 0.1);
					else if(event.getCode() == KeyCode.PLUS && currentVal < 2)
						searchBrowser.setZoom(currentVal + 0.1);
				}
			});
	}
	
	
	public void handleControlDownInSearchBrowser(){
		
	}
	
	public WebView getSeachBrowser(){
		return searchBrowser;
	}
	
	/**
	 * @return
	 * Returns the interactionTabManagerController controlling the InteractionTab of the application that is currently shown
	 */
	public InteractionTabManagerController getInteractionTabManagerController(){
		return this.interactionTabManagerController;
	}
	
	
	/**
	 * Loads the ChatTab and saves a reference to its controller
	 */
	private void initializeChat(){
		System.out.println("Initializing chat-area...");
		FXMLLoader chatLoader =  new FXMLLoader(getClass().getResource("/communication/ChatManager.fxml"));
		try{
		Node newChatPane  = chatLoader.load();
		chatTabController = chatLoader.getController();
		chatTabController.passMainFrameController(this);
		rootNode.getChildren().remove(chatPane);
		rootNode.add(newChatPane, 1, 2);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 * Returns the startingInteractionTab
	 */
	public Node getStartingInteractionTab(){
		return this.startingInteractionTabPane;
	}

	
	/**
	 * @return
	 * Returns the the TabPane of the interactionArea currently shown by the application
	 */
	public Node getCurrentInteractionArea(){
		return currentInteractionTabPane;
	}

	
	/**
	 * Loasa the InteractionTab and saves this as the starting and current interactiontab
	 */
	private void initializeInteractionTab(){
		System.out.println("Initializing welcome interactiontab in interactionArea...");
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("InteractionTabManager.fxml"));
		try{
		Node newTabManager  = loader.load();
		interactionTabManagerController = loader.getController();
		interactionTabManagerController.getTabPane().getTabs().remove(0);
		System.out.println(this == null);
		interactionTabManagerController.initSelectionModeContent(this);
		rootNode.getChildren().remove(interactionPane);
		rootNode.add(newTabManager, 0, 0, 1, 3);
		currentInteractionTabPane = newTabManager;
		startingInteractionTabPane = newTabManager;
		startingInteractionTabManagerController = interactionTabManagerController;
		currentInteractionTabManagerController = interactionTabManagerController;
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	

		
	
	/**
	 * @param interactionArea
	 * Loads a new TabPane in the interactionArea shown by the application
	 */
	public void loadNewInteractionArea(Node interactionArea, InteractionTabManagerController interactionTabManagerController){
		System.out.println("Loading new interactionTab in interactionArea");
		rootNode.getChildren().remove(currentInteractionTabPane);
		rootNode.add(interactionArea, 0, 0, 1, 3);
		currentInteractionTabPane = interactionArea;
		currentInteractionTabManagerController = interactionTabManagerController;
	}
	
	/**
	 * Handles what to do on being asked to close
	 */
	public void onCloseRequest(){
		System.out.println("Closerequest in MainFrameController calling onCloseRequest in chatTabController...");
		chatTabController.onCloseRequest();
	}

	public InteractionTabManagerController getStartingInteractionTabManagerController() {
		return startingInteractionTabManagerController;
	}


	public void passUserInfo(List<String> assistantSubjects) {
		this.assistantSubjects = assistantSubjects;
		System.out.println("assistantSubjects: "+this.assistantSubjects);
		ModeSelectionController MSC = new ModeSelectionController();
		MSC.setAssistantSubjects(this.assistantSubjects);
		
		
	}
	
	public List<String> getUserInfo() {
		return this.assistantSubjects;
	}

}
