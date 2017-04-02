package communication;

import java.io.IOException;

import T2.ServerRequest;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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


// Controls the chat of one connection
public class ChatController {

	@FXML private TextArea userText;
	@FXML private ListView<Label> chatWindow;
	
	private RecieveAndSend receiveAndSend = null;
	private boolean isAssistantHost= false;
	private boolean isHelperHost = false;
	private Tab chatTab;
	private Node InteractionArea;
	private InteractionTabManagerController interactionTabManagerController;

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
	
	@FXML // listens to keyevents in userText, if keyevent is enter, send CHAT protovol and the text
	public void handleChatEnterKey(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER ){
			String message = userText.getText();
			Platform.runLater(() -> {receiveAndSend.sendChatMessage("CHAT-" + message);});
			viewMessage(message, true);
			event.consume();
		}
		else if(event.getCode() == KeyCode.I){
			System.out.println(interactionTabManagerController.isFinishedLoading());
		}
	}
	
	// used by tab creates in serverChatController on closeRequest of tab
	public void onClosed(String tag) {
		if(receiveAndSend != null){
			receiveAndSend.sendChatMessage("END-null");
			receiveAndSend.closeConnection();
		}
		if(isHelperHost && receiveAndSend == null){
			ServerRequest request = new ServerRequest(tag + "Delete");
			request.removeAdressFromQueue();
		}
		else if(isAssistantHost && receiveAndSend == null){
			ServerRequest request = new ServerRequest(tag + "Delete");
			request.removeAdressFromQueue();
		}
		else if(receiveAndSend == null){
			ServerRequest request = new ServerRequest(tag + "Delete");
			request.removeAdressFromQueue();
		}
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
		Platform.runLater(() -> {chatWindow.getItems().add(message);});
		this.userText.clear();
	}

	// sets a serverConnection to this chattab
	public void setRecieveAndSendConnection(RecieveAndSend connection){
		this.receiveAndSend = connection;
	}
	
	public RecieveAndSend getReceiveAndSendConnection(){
		return this.receiveAndSend;
	}

	public void ableToType(boolean tof) {
		this.userText.setEditable(tof);
	}
	
	public TextArea getTextArea(){
		return this.userText;
	}
	
	public void setChatTab(Tab chatTab) {
		this.chatTab = chatTab;
	}
	
	
	
	public Tab getChatTab(){
		return this.chatTab;
	}

	public void initializeInteractionArea() {
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
		interactionTabManagerController.sendPageURLWhenLoaded(this);

	}
	
	public void sendCodeURL(){
		receiveAndSend.sendCodeUrl("CODEURL-"+ interactionTabManagerController.getURL());
	}
	
	
}
