package communication;

import java.io.IOException;

import java.net.Socket;
import java.util.ArrayDeque;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import mainWindow.MainFrameController;

public class ChatTabController {
	@FXML private TabPane chatTab;
	@FXML private Button studentHelperBtn;
	@FXML private Button studentAssistantBtn;
	@FXML private Button studentBtn;
	
	
	private String tag;
	
	private Connector connector;
	static private int PotentialConnections = 0;
	private ArrayDeque<Thread> waitingThreads;
	ArrayDeque<ChatController> chatControllerQueue;
	private boolean isWaitingForConnection = false;
	private Stage stage;
	private MainFrameController mainFrameController = null;

	
	public ChatTabController(){
		connector = new Connector(this);
		waitingThreads = new ArrayDeque<Thread>();
		chatControllerQueue = new ArrayDeque<ChatController>();
	}
	
	public static int getPotentialConnections() {
		return PotentialConnections;
	}

	public static void decrementPotentialConnections() {
		ChatTabController.PotentialConnections--;
	}
	
	public void passMainFrameController(MainFrameController mainFrameController){ // new
		this.mainFrameController = mainFrameController;
	}
	
	public void setStudentHelperMode() throws Exception{
		if(this.connector.isHelperHost() == null){
			connector.setHelperHost();
			initializePopUpSubject();
		}
		else if (this.connector.isHelperHost() == false && this.connector.isAssistantHost() == false)
			System.out.println("Already set to be client");
		else
			System.out.println("Already helperHostMode");
	}
	
	public void setStudentMode() throws Exception{
		if(this.connector.isHelperHost() == null && this.connector.isAssistantHost() == null){
			connector.setClient();
			initializePopUpSubject();
		}
		else if(this.connector.isHelperHost() == true || this.connector.isAssistantHost() == true)
			System.out.println("Already set to be host");
		else
			System.out.println("Already clientMode");
	}
	
	public void setAssistantMode() throws Exception{
		if(this.connector.isAssistantHost() == null){
			connector.setAssistantHost();
			initializePopUpSubject();
		}
		else if (this.connector.isAssistantHost() == false && this.connector.isHelperHost() == false)
			System.out.println("Already set to be client");
		else
			System.out.println("Already assistantHostMode");
	}
	
	//initializes a new popup window with subjects
	public void initializePopUpSubject() throws Exception{               
        try {
        		FXMLLoader subjectLoader = new FXMLLoader(getClass().getResource("PopUpSubject.fxml"));
                Parent root = (Parent) subjectLoader.load();
                PopUpSubjectController contr = subjectLoader.getController();
                contr.passChatTabController(this);
                this.stage = new Stage();
                this.stage.setScene(new Scene(root));  
                this.stage.show();
                
        } catch(Exception e) {
           e.printStackTrace();
        }
      
	}	
	
	//closes popup subjects
	public void closePopUp(){
		this.stage.close();
	}
	

	
	//merging user string with subject string to make a tag in purpose of identifying itself to server
	//subject string has to begin with uppercase letter
	public void mergeTags(String subject){
		if (this.connector.isAssistantHost())
			tag = "StudentAssistant" + subject;
		else if (this.connector.isHelperHost())
			tag = "StudentHelper" + subject;
		else if (!this.connector.isHelperHost() && !this.connector.isAssistantHost())
			tag = "Student" + subject;
	}
	
	//method for returning tag in purpose of retreiving it in Connector - method run()
	public String getTag(){
		return tag;
	}

	
	@FXML
	public void newChatTab(){ // TODO should send message to server queuing its ip
		if(connector.isHelperHost() == null && connector.isAssistantHost() == null)
			System.out.println("Must choose user type before opening connection");
		// host can serve 3, client can only queue once
		else if(ChatTabController.getPotentialConnections() < 3 && (connector.isHelperHost() || connector.isAssistantHost()) || ChatTabController.getPotentialConnections() < 1 && (!connector.isHelperHost() && !connector.isAssistantHost())){
		try {
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
					chatController.initializeInteractionArea();
					connector.sendHelperRequest(tag);
				}
				else if(connector.isAssistantHost()){
					chatController.setAssistantHost(true);
					chatController.initializeInteractionArea();
					connector.sendHelperRequest(tag);
				}
				else {
					chatController.setHelperHost(false);
					chatController.setAssistantHost(false);
					chatController.initializeInteractionArea();
				}
				chatControllerQueue.addLast(chatController);
				
				newTab.setOnCloseRequest((event) -> { 	// on closeRequest, end connection that is tied to this chattab
					chatControllerQueue.remove(chatController);
					ChatTabController.decrementPotentialConnections();
					chatController.onClosed(tag); 
					if(chatTab.getTabs().size() == 1){
						mainFrameController.getInteractionTabManagerController().setDefaultURL();
						mainFrameController.loadNewInteractionArea(mainFrameController.getStartingInteractionTab());
					}
				});
				
				newTab.setOnSelectionChanged((event) -> {
					if(newTab.isSelected()){
						mainFrameController.loadNewInteractionArea(chatController.getInteractionArea());
					}
				});
				
				if(chatTab.getTabs().size() == 1){
					Event.fireEvent(newTab, new Event(Tab.SELECTION_CHANGED_EVENT));
				}
				
				chatTab.getSelectionModel().select(ChatTabController.getPotentialConnections()-1);
				
				if(this.waitingThreads.isEmpty() && !isWaitingForConnection){
					isWaitingForConnection = true;
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
	
	
	
	public void startChatSession(Socket socket){ //, ChatController chatController){
		isWaitingForConnection = false;
		ChatController chatController = chatControllerQueue.poll();
		RecieveAndSend connection = new RecieveAndSend(socket, chatController);	
		new Thread(connection).start();
		if(!this.waitingThreads.isEmpty()){
			this.waitingThreads.poll().start(); // removes and starts the next thread in queue to retrive socket
			isWaitingForConnection = true;
		}
	}
	
	public void onCloseRequest(){
		this.connector.closeWelcomeSocket();
		final EventType<Event> closeRequestEventType = Tab.TAB_CLOSE_REQUEST_EVENT;
		final Event closeRequestEvent = new Event(closeRequestEventType);
		for(Tab tab : chatTab.getTabs()){
			Event.fireEvent(tab, closeRequestEvent);
		}
	}

	

}
