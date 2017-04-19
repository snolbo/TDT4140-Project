package communication;

import java.io.IOException;

import org.w3c.dom.Document;


import T2.ServerRequest;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import mainWindow.InteractionTabManagerController;
import mainWindow.MainFrameController;
import voiceclient.ClientVoice;
import voiceserver.ServerVoice;



// Controls the chat of one connection
public class ChatController {

	@FXML private TextArea userText;
	@FXML private ListView<Label> chatWindow;
	@FXML private Button microphoneBtn;
	private ServerVoice sv;
	private ClientVoice cv;
	
	
	private ReceiveAndSend receiveAndSend = null;

	private boolean isAssistantHost= false;
	private boolean isHelperHost = false;
	private Tab chatTab;
	private Node InteractionArea;
	private InteractionTabManagerController interactionTabManagerController;
	
	@FXML
	public void initialize(){
		System.out.println("running initialize in chatController");
		microphoneBtn.getStyleClass().add("micButton");
	}
	
	
	public boolean isHelperHost() {
		return isHelperHost;
	}
	
	public boolean isAssistantHost() {
		return isAssistantHost;
	}

	public void setHelperHost(boolean isHelperHost) {
		this.isHelperHost = isHelperHost;
	}
	
	public void setAssistantHost(boolean isAssistantHost) {
		this.isAssistantHost = isAssistantHost;
	}
	
	public void requestVoiceCommunication(){
		if(!ChatTabController.isVoiceCommunicating){
			receiveAndSend.sendChatMessage("VOICE-request");
		}
		else
			viewMessage("Cannot initiate voice communication as you already have an active voiceConversation", true);
	}
	
	public void acceptVoiceCommunication(){
		receiveAndSend.sendChatMessage("VOICE-accept");
		setupVoiceCommunication();
	}
	
	public void cancelVoiceCommunication(){
		if(ChatTabController.isVoiceCommunicating){
			ChatTabController.isVoiceCommunicating = false;
			receiveAndSend.sendChatMessage("VOICE-cancel");
		}
	}
	
	public void setupVoiceCommunication(){
		ChatTabController.isVoiceCommunicating = true;
		sv = new ServerVoice();
		sv.initializeAudio();
		cv = new ClientVoice();
		cv.initializeAudio(receiveAndSend.getInetAddress());
	}
	
	@FXML // listens to keyevents in userText, if keyevent is enter, send CHAT protovol and the text
	public void handleChatEnterKey(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER ){
			String message = userText.getText();
			if(message.length() > 0){
				Platform.runLater(() -> {receiveAndSend.sendChatMessage("CHAT-" + message);});
				viewMessage(message, true);
			}
			event.consume();
		}
	}
	
	// used by tab creates in serverChatController on closeRequest of tab

	public void onClosed(String tag) {
		cancelVoiceCommunication();
		if(receiveAndSend != null){
			System.out.println("Closing chatController - sends END message and closes connection...");
			receiveAndSend.sendChatMessage("END-null");
			receiveAndSend.closeConnection();
		}

		if( (isHelperHost || isAssistantHost) && receiveAndSend == null){
			System.out.println("Closing chatController - sends " + tag + "Delete" + " to TCPserver since in queue...");
			ServerRequest request = new ServerRequest(tag + "Delete");
			request.removeAdressFromQueue();
		}
		else if(receiveAndSend == null){

			System.out.println("Closing chatController - sends " + tag + "Delete" + " to TCPserver since in queue...");
			ServerRequest request = new ServerRequest(tag + "Delete");
			request.removeAdressFromQueue();
		}

		interactionTabManagerController.deleteFirepad();
	}
	
	// shows message in chatWIndow
	public void viewMessage(String text, boolean madeByMe){  // TODO: want the label to be smaller, wrap text, and positioned at one side
		Label message = new Label(text);
		message.prefWidthProperty().bind(chatWindow.widthProperty().subtract(25));
		message.setWrapText(true);
		if(madeByMe) {
			message.setTextFill(Color.WHITE);
			message.setBackground(new Background(new BackgroundFill(Color.web("#4678FB"), new CornerRadii(5), null)));
		}
		else {
			message.setTextFill(Color.BLACK);
			message.setBackground(new Background(new BackgroundFill(Color.web("#EFEEEE"), new CornerRadii(5), null)));
		}	
		Platform.runLater(() -> {
			chatWindow.getItems().add(message);
			chatWindow.scrollTo(message);
			});
		this.userText.clear();
	}

	// sets a serverConnection to this chattab
	public void setRecieveAndSendConnection(ReceiveAndSend connection){
		this.receiveAndSend = connection;
	}
	
	public ReceiveAndSend getReceiveAndSendConnection(){
		return this.receiveAndSend;
	}

	public void ableToType(boolean tof) {
		this.userText.setEditable(tof);
	}

	
	
	public void setChatTab(Tab chatTab) {
		this.chatTab = chatTab;
	}
	
	
	public Tab getChatTab(){
		return this.chatTab;
	}

	public void initializeInteractionArea() {
		System.out.println("Initializing interactionArea assisiated with this chatController");
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("/mainWindow/InteractionTabManager.fxml"));
		try{
		this.InteractionArea  = loader.load();
		this.interactionTabManagerController = loader.getController();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		if(!isAssistantHost() && !isHelperHost()){
			interactionTabManagerController.setURL("http://connected-1e044.firebaseapp.com");
		}
		
		else{
			setDefaultURL();

		}
	}
	
	public void setDefaultURL(){
		interactionTabManagerController.setDefaultURL();
	}
	
	
	public Node getInteractionArea(){
		return this.InteractionArea;
	}
	
	public void setCodeUrl(String URL){
		this.interactionTabManagerController.setURL(URL);
	}
	
	public String getCodeURL(){
		return interactionTabManagerController.getURL();
	}
	
//	public void reloadCodeEditor(){
//		interactionTabManagerController.reloadCodeEditor();
//	}
	

	public boolean codeEditorFinishedLoading() {
		return interactionTabManagerController.isFinishedLoading();
	}

	public void sendCodeURLWhenLoaded() {
		System.out.println("Sending codeURL to helper when the page is finished loading...");
		interactionTabManagerController.sendPageURLWhenLoaded(this);

	}
	
	public void sendCodeURL(){
		System.out.println("Sending CodeURL to helper...");
		receiveAndSend.sendCodeUrl("CODEURL-"+ interactionTabManagerController.getURL());
	}
	
	
}
