package communication;

import java.io.IOException;


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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import mainWindow.InteractionTabManagerController;
import voiceclient.ClientVoice;
import voiceserver.ServerVoice;



/**
 * @author snorr
 * Controls the chatWindow in a connection, and all the logic related to and coming from this element
 */
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
	private Node InteractionTab;
	private InteractionTabManagerController interactionTabManagerController;
	
	@FXML
	public void initialize(){
		System.out.println("running initialize in chatController");
		microphoneBtn.getStyleClass().add("micButton");
		chatWindow.setMouseTransparent(true);
		chatWindow.setFocusTraversable(false);
		microphoneBtn.setDisable(true);
	}
	
	
	/**
	 * @param value
	 * Disables and enables the microphone button
	 */
	public void disableMicButton(boolean value){
		microphoneBtn.setDisable(value);
	}

	/**
	 * @return
	 * Returns boolean value to indicate if this person is in helpful student mode
	 */
	public boolean isHelperHost() {
		return isHelperHost;
	}
	
	/**
	 * @return
	 * Returns boolean value telling if this person is in student assistant mode
	 */
	public boolean isAssistantHost() {
		return isAssistantHost;
	}

	/**
	 * @param isHelperHost
	 * Sets helper mode to this boolean value
	 */
	public void setHelperHost(boolean isHelperHost) {
		this.isHelperHost = isHelperHost;
	}
	
	/**
	 * @param isAssistantHost
	 * Sets studetnassistant to this boolean value
	 */
	public void setAssistantHost(boolean isAssistantHost) {
		this.isAssistantHost = isAssistantHost;
	}
	
	/**
	 * Checks if this person is eligable for voicecommunication, then sends a request to the other person to request voicecomm
	 */
	public void requestVoiceCommunication(){
		if(!ChatTabController.isVoiceCommunicating){
			sv = new ServerVoice();
			cv = new ClientVoice();
			if(sv.playingIsSupported() && cv.recordingIsSupported()){
				receiveAndSend.sendChatMessage("VOICE-request");
			}
			else
				viewMessage("-----Voice communication is not supported on your device-----", false);

		}
		else
			viewMessage("-----Cannot initiate voice communication as you already have an active voiceConversation-----", false);
	}
	
	/**
	 * Tests if this person is eligable for voicecomm, and sends an accept message and sets up voicecom
	 */
	public void acceptVoiceCommunication(){
		if(!ChatTabController.isVoiceCommunicating){
			sv = new ServerVoice();
			cv = new ClientVoice();
			if(sv.playingIsSupported() && cv.recordingIsSupported()){
				receiveAndSend.sendChatMessage("VOICE-accept");
				setupVoiceCommunication();
			}
			else{
				System.out.println("Voice communication is not supported on this device");
				viewMessage("-----The other person request voice communication, but it is not supported on your device-----", false);
				receiveAndSend.sendChatMessage("VOICE-deny");
			}
		}
		else
			viewMessage("-----The other person request voice communication, but you are already in an active conversation-----", false);
	}
	
	/**
	 * Handles denied request for voicecom
	 */
	public void handleDeniedVoiceRequest(){
		viewMessage("-----The other person's device does not support voice communication-----", false);
		ChatTabController.isVoiceCommunicating = false;
	}
	
	/**
	 * Handles accepted request for voicecom
	 */
	public void handleAcceptedVoiceRequest(){
		setupVoiceCommunication();
	}
	
	/**
	 * Cancels voicecomm
	 */
	public void cancelVoiceCommunication(){
		if(ChatTabController.isVoiceCommunicating){
			receiveAndSend.sendChatMessage("VOICE-cancel");
			ChatTabController.isVoiceCommunicating = false;
		}
	}
	
	/**
	 * Sets up and initializes voicecomm
	 */
	public void setupVoiceCommunication(){
		if(sv.playingIsSupported() && cv.recordingIsSupported()){
			ChatTabController.isVoiceCommunicating = true;
			sv.initializeAudio();
			cv.initializeAudio(receiveAndSend.getInetAddress());
		}
	}
	
	/**
	 * @param event
	 * Listens for keyevents in the write area of the chat and sends a messsage if the key typed is enter
	 */
	@FXML 
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
	

	/**
	 * @param tag
	 * Handles closerequest
	 */
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
	

	/**
	 * @param text
	 * @param madeByMe
	 * Shows a message in chat, color depending on wheather the message was created by this person or by the other
	 */
	public void viewMessage(String text, boolean madeByMe){  
		Label message = new Label(text);
		message.setStyle("-fx-padding: 10px;");
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
		if(!madeByMe)
			notifyIncomingMessage();
	}
	
	/**
	 * Changed background color on a tab if there is an incoming message and the tab is currently not selected
	 */
	private void notifyIncomingMessage(){
		System.out.println("Running notifyIncomingMessage in chatController");
		if(!chatTab.isSelected()){
			chatTab.setStyle("-fx-background-color: #ff5400");
		}
	}
	
	/**
	 * Sets back the default value of the tab backgroundcolor
	 */
	public void clearNotifiedMessage(){
		chatTab.setStyle("");
	}
	
	/**
	 * @param connection
	 * Method to pass the object handling the sending and receiving of messages
	 */
	public void setRecieveAndSendConnection(ReceiveAndSend connection){
		this.receiveAndSend = connection;
	}
	
	/**
	 * @return
	 * return object handeling sending and receiving
	 */
	public ReceiveAndSend getReceiveAndSendConnection(){
		return this.receiveAndSend;
	}

	/**
	 * @param tof
	 * Sets wheather is is possible or not to type in text to be sent i nchat
	 */
	public void ableToType(boolean tof) {
		this.userText.setEditable(tof);
	}


	/**
	 * @param chatTab
	 * Sets a chatTab object to be assiciated with this chatController
	 */
	public void setChatTab(Tab chatTab) {
		this.chatTab = chatTab;
	}
	
	
	/**
	 * @return
	 * Returns the chattab object associated with this controller
	 */
	public Tab getChatTab(){
		return this.chatTab;
	}

	/**
	 * @param codeLanguage
	 * Initializes the interactionTab to be assiciated with this chatcontroller object
	 */
	public void initializeInteractionTab(String codeLanguage) {
		System.out.println("Initializing interactionArea assisiated with this chatController");
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("/mainWindow/InteractionTabManager.fxml"));
		try{
		InteractionTab  = loader.load();
		interactionTabManagerController = loader.getController();
		interactionTabManagerController.setSharedCodeLanguage(codeLanguage);
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
	
	/**
	 * Sets the url to the webview of the shared code editing window to the default chosen value
	 */
	public void setDefaultURL(){
		interactionTabManagerController.setDefaultURL();
	}

	
	public Node getInteractionArea(){
		return this.InteractionTab;
	}
	
	public InteractionTabManagerController getInteractionTabManagerController(){
		return interactionTabManagerController;
	}
	
	/**
	 * @param URL
	 * sets and loads given url in the webengine of the shared code editor
	 */
	public void setCodeUrl(String URL){
		this.interactionTabManagerController.setURL(URL);
	}
	
	/**
	 * @return
	 * Returns the current url loaded by the engine of the shared code editing browser
	 */
	public String getCodeURL(){
		return interactionTabManagerController.getURL();
	}

	/**
	 * @return
	 * Returns boolean value indicating if the shared code browser is finished loading the url it is currently loading
	 */
	public boolean codeEditorFinishedLoading() {
		return interactionTabManagerController.isFinishedLoading();
	}

	/**
	 * Sends the URL of the code editor when it is finished loading
	 */
	public void sendCodeURLWhenLoaded() {
		System.out.println("Sending codeURL to helper when the page is finished loading...");
		interactionTabManagerController.sendPageURLWhenLoaded(this);

	}
	
	/**
	 * sends the code url of the shared code browser
	 */
	public void sendCodeURL(){
		System.out.println("Sending CodeURL to helper...");
		receiveAndSend.sendChatMessage("CODEURL-"+ interactionTabManagerController.getURL());
	}
	
	
}
