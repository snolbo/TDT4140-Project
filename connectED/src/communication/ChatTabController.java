package communication;

import java.io.IOException;


import java.net.Socket;
import java.util.ArrayDeque;


import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import mainWindow.MainFrameController;

/**
 * @author snorr
 * Handles the logic related to organizing the different tabs and creating them
 */
public class ChatTabController {
	@FXML private TabPane chatTab;

	private String tag;
	private String subject;
	private String mode;
	private Connector connector;
	static private int PotentialConnections = 0;
	private ArrayDeque<Thread> waitingThreads;
	ArrayDeque<ChatController> chatControllerQueue;
	private boolean isWaitingForConnection = false;
	
	public static boolean isVoiceCommunicating = false;
	
	
	private Stage stage;
	private MainFrameController mainFrameController = null;
	
	private Tab extraConnectionTab;

	private String codeLanguage;
	
	public ChatTabController(){
		connector = new Connector(this);
		waitingThreads = new ArrayDeque<Thread>();
		chatControllerQueue = new ArrayDeque<ChatController>();
		extraConnectionTab = new Tab("+");
		extraConnectionTab.setOnSelectionChanged((event) ->{
			if(extraConnectionTab.isSelected())
				newChatTab(codeLanguage);
		});
		
	}
	
	/**
	 * @return
	 * Get potential connections that can be made in the current state
	 */
	public static int getPotentialConnections() {
		return PotentialConnections;
	}

	/**
	 * Decrements the variable indicating how many potential connections that can be made in the current state
	 */
	public static void decrementPotentialConnections() {
		ChatTabController.PotentialConnections--;
	}
	
	/**
	 * @param mainFrameController
	 * Pass the MainFrameController, being the root controller, to this controller
	 */
	public void passMainFrameController(MainFrameController mainFrameController){ // new
		this.mainFrameController = mainFrameController;
	}


	
	/**
	 * Tells the connector handling queuing that it is connection as a student offering help
	 */
	public void setStudentHelperMode(){
		resetQueueData();
		connector.setHelperHost();
		mode = "StudentHelper";
	}
	
	/**
	 * Tells the connector handling queuing that it is connection as a student, requesting help
	 */
	public void setStudentMode(){
		resetQueueData();
			connector.setClient();
			mode = "Student";
	}
	
	/**
	 * Tells the connector handling queuing that it is connection as a student-assistant offering help
	 */
	public void setAssistantMode(){
		resetQueueData();
		connector.setAssistantHost();
		mode = "StudentAssistant";
	}
	
	

	 /**
	 * Resets the selected mode (student/helper/assistant) and the subject.
	 */
	public void resetQueueData(){
		waitingThreads.clear();
		isWaitingForConnection = false;
	}
	
	
	
	/**
	 * @return
	 * Returns the current subject selected
	 */
	public String getSubject(){
		return subject;
	}
	
	/**
	 * @return
	 * Returns the current mode seleceted
	 */
	public String getMode(){
		return mode;
	}
	
	/**
	 * @return
	 * Returns a boolean variable indicating if both subject and mode is set
	 */
	public boolean modeAndSubjectIsSet(){
		return subject != null && mode != null;
	}
	
	
	/**
	 * @return
	 * Combine mode and subject to a tag, if both are set. Otherwise, returns
	 */
	public boolean combineTags(){
		if(!modeAndSubjectIsSet())
			return false;
		else{
			tag = mode + subject;
			return true;
		}
	}
	
	/**
	 * @param subject
	 * Sets the subject
	 */
	public void setSubject(String subject){
		this.subject = subject;
	}
	
	/**
	 * @param mode
	 * Sets the mode
	 */
	public void setMode(String mode){
		this.mode = mode;
	}
	
	/**
	 * @return
	 * Returns the current tag
	 */
	public String getTag(){
		return tag;
	}
	
	/**
	 * @param tag
	 * Sets the tag
	 */
	public void setTag(String tag){
		this.tag = tag;
	}
	
	/**
	 * Decides wheather or not the tab with a '+' sign is shown and active
	 */
	public void setExtraConnectionTab(){
		if(mode.equals("StudentAssistant") || mode.equals("StudentHelper")){
			removeExtraConnectionTab();
			if(ChatTabController.getPotentialConnections() < 3 && ChatTabController.getPotentialConnections() != 0)
				chatTab.getTabs().add(extraConnectionTab);
		}
	}
	
	/**
	 * Removes the tab for adding extra connections
	 */
	public void removeExtraConnectionTab(){
		if(chatTab.getTabs().contains(extraConnectionTab))
			chatTab.getTabs().remove(extraConnectionTab);
	}
	
	/**
	 * Creates a new Chat, queue the user depending on tag
	 */
	@FXML
	public void newChatTab(String codeLanguage){ 
		if(connector.isHelperHost() == null && connector.isAssistantHost() == null || tag == null)
			System.out.println("Must choose user type and subject before opening connection");
		// host can serve 3, client can only queue once
		else if(ChatTabController.getPotentialConnections() < 3 && (connector.isHelperHost() || connector.isAssistantHost()) || ChatTabController.getPotentialConnections() < 1 && (!connector.isHelperHost() && !connector.isAssistantHost())){
		try {
				this.codeLanguage = codeLanguage;
				System.out.println("Starting to create a new ChatTab...");
				ChatTabController.PotentialConnections++;
				// setting up the Chat GUI element
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatTab.fxml"));	// loads the content of the given FXML file		
				Node node = (Node) loader.load();				// sets the loaded FXML content in a Node
				Tab newTab = new Tab("Chat session", node);	// creates new tab with the content in the Node and adds the tab to the current tabs in GUI
				this.chatTab.getTabs().add(newTab);
				
				ChatController chatController = loader.getController();
				chatController.setChatTab(newTab);

				if(connector.isHelperHost()){
					chatController.setHelperHost(true);
					connector.sendHelperRequest(tag);
				}
				else if(connector.isAssistantHost()){
					chatController.setAssistantHost(true);
					connector.sendHelperRequest(tag);
				}
				else {
					chatController.setHelperHost(false);
					chatController.setAssistantHost(false);
				}
				chatController.initializeInteractionTab(codeLanguage);

				chatControllerQueue.addLast(chatController);
				setExtraConnectionTab();
	

				
				newTab.setOnCloseRequest((event) -> { 	// on closeRequest, end connection that is tied to this chattab
					System.out.println("Closing current tab -  removing assisiated controller form queue if exists...");
					chatControllerQueue.remove(chatController);
					ChatTabController.decrementPotentialConnections();
					System.out.println("Closing current tab -  calling onClosed on assisiated chatController...");
					chatController.onClosed(tag); 
					setExtraConnectionTab();
					
					if(ChatTabController.getPotentialConnections() == 0){
						mainFrameController.getInteractionTabManagerController().setDefaultURL();
						mainFrameController.loadNewInteractionArea(mainFrameController.getStartingInteractionTab(), mainFrameController.getStartingInteractionTabManagerController());
					}
					
				});
				

				newTab.setOnSelectionChanged((event) -> {
					if(newTab.isSelected()){
						mainFrameController.loadNewInteractionArea(chatController.getInteractionArea(), chatController.getInteractionTabManagerController());
						chatController.clearNotifiedMessage();
					}
				});
				
				
				Event.fireEvent(newTab, new Event(Tab.SELECTION_CHANGED_EVENT));
				
				chatTab.getSelectionModel().select(ChatTabController.getPotentialConnections()-1);
				

				
				if(this.waitingThreads.isEmpty() && !isWaitingForConnection){
					isWaitingForConnection = true;
					System.out.println("running connector");
					new Thread(connector).start();
				}
				else
					this.waitingThreads.addLast(new Thread(connector));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Cannot take any more connections, reached max limit");
		}
	}
	
	
	
	/**
	 * @param socket
	 * Creates a new chat session and assigns a chatController to the Chat-tab. Used by the connector when a match is made from the distributing server
	 */
	public void startChatSession(Socket socket){ //, ChatController chatController){
		System.out.println("Starting a chat session after received a person to connect to...");
		isWaitingForConnection = false;
		ChatController chatController = chatControllerQueue.poll();
		ReceiveAndSend connection = new ReceiveAndSend(socket, chatController);
		new Thread(connection).start();
		if(!this.waitingThreads.isEmpty()){
			this.waitingThreads.poll().start(); // removes and starts the next thread in queue to retrive socket
			isWaitingForConnection = true;
		}
	}
	
	
	/**
	 * Handles the event of being closed
	 */
	public void onCloseRequest(){
		System.out.println("Closing welcomesocket in Connector");
		this.connector.closeWelcomeSocket();
		final EventType<Event> closeRequestEventType = Tab.TAB_CLOSE_REQUEST_EVENT;
		final Event closeRequestEvent = new Event(closeRequestEventType);
		for(Tab tab : chatTab.getTabs()){
			System.out.println("Firing closerequest on remaining open tab");
			Event.fireEvent(tab, closeRequestEvent);
		}
	}
	
	/**
	 * @return
	 * Returns the connector
	 */
	public Connector getConnector(){
		return this.connector;
	}
	
	/**
	 * @return
	 * Returns this stage
	 */
	public Stage getStage(){
		return this.stage;
	}
	
	/**
	 * @return
	 * Returns the mainframeController, being the root controller of the application
	 */
	public MainFrameController getMainFrameController(){
		return this.mainFrameController;
	}


}
