package mainWindow;

import java.io.IOException;

import org.w3c.dom.Document;


import communication.ChatController;
import communication.ChatTabController;
import javafx.concurrent.Worker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;

/**
 * @author snorr
 *
 */
public class InteractionTabManagerController {
	@FXML private WebView sharedCodeBrowser;
	@FXML private TabPane tabContainer;
	@FXML private Button assistantBtn;
	@FXML private Button helperBtn;
	@FXML private Button studentBtn;
	@FXML private Button javaBtn;
	@FXML private Button itgkPythonBtn;
	@FXML private Button itgkMatlabBtn;


	
	
	
	
	private Node selectionModeContent;
	private Tab selectionModeTab;
	private ChatTabController chatTabController;
	private MainFrameController mainFrameController;
	
		

	
	@FXML
	void initialize(){
		this.sharedCodeBrowser.getEngine().setUserStyleSheetLocation("data:,body { font: 15px Helvetica; font-weight: bold; }");
		sharedCodeBrowser.getEngine().load("defaultHTML.html");
		sharedCodeBrowser.setOnKeyPressed((event) ->{
			handleKeyEvent(event);
		});
		
		tabContainer.getSelectionModel().select(0);
		
	
		
		
	}

	
	
	/**
	 * @param mainFrameController
	 * Used by MainFrameController to pass its reference on order to get access to its children and its methods
	 */
	public void initSelectionModeContent(MainFrameController mainFrameController){
		mainFrameController = mainFrameController;
		initializeSelectionModeContent();
	}
	
	
	
	/**
	 * Loads the FXML content to be shown for selection of purpose and subject, and loads the content into a tab
	 * called selectionModeTab, that will hold this content. Then the tab is added to the TabPane of the InteractionTab
	 */
	private void initializeSelectionModeContent(){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ModeSelection.fxml"));
		try {
			selectionModeContent = loader.load();
			ModeSelectionController modeSelectionController = loader.getController();
			modeSelectionController.initButtons(mainFrameController);
			selectionModeTab = new Tab("Queue Selection", selectionModeContent);
			tabContainer.getTabs().add(0,selectionModeTab);
			tabContainer.getSelectionModel().select(tabContainer.getTabs().indexOf(selectionModeTab));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @param URL
	 * Sets url in the sharedCodeBrowser, which main function is to view the shared code editor
	 */
	public void setURL(String URL){
		System.out.println("Loading the given URL: " + URL);
		sharedCodeBrowser.getEngine().load(URL);
	}
	
	
	/**
	 * @return
	 * Returns a String representation of the current URL held by the browset which main purpose is show the shared code editing window
	 */
	public String getURL(){
		Document doc = sharedCodeBrowser.getEngine().getDocument();
		if(doc != null)
			return doc.getBaseURI();
		else return null;
	}
	
	/**
	 * Reloads the content of the browser, eqvivalent to reloading the content of a webpage
	 */
	public void reloadCodeEditor(){
		sharedCodeBrowser.getEngine().reload();
	}
	

	
	/**
	 * Sets the url of the browser to the default value and loads the content. Default value is hardcoded
	 */
	public void setDefaultURL(){
		System.out.println("Setting default URL");
		sharedCodeBrowser.getEngine().load("defaultHTML.html");
	}
	
	/**
	 * @return
	 * Returns a boolean value that indicates wheather the browser is finished loading the content it is currently loading
	 */
	public boolean isFinishedLoading(){
		if(sharedCodeBrowser.getEngine().getLoadWorker().getState().equals(Worker.State.SUCCEEDED))
			return true;
		else
			return false;
	}
	
	
	/**
	 * @param event
	 * Listens to keyevents typed in the browser. Shortcuts combinations is to be handled here
	 */
	public void handleKeyEvent(KeyEvent event){
		if(event.isControlDown()){
			double currentVal = sharedCodeBrowser.getFontScale();
			if(event.getCode() == KeyCode.MINUS && currentVal > 0.5)
				sharedCodeBrowser.setFontScale(currentVal - 0.1);
			else if(event.getCode() == KeyCode.PLUS && currentVal < 2)
				sharedCodeBrowser.setFontScale(currentVal + 0.1);
		}
	}
	
	
	
	/**
	 * @param chatController
	 * The chatController taken as arguments sends the URL of this InteractionTab's browser when it is finished loading its currently loading content
	 */
	public void sendPageURLWhenLoaded(ChatController chatController){
		sharedCodeBrowser.getEngine().getLoadWorker().stateProperty().addListener((observed, oldValue, newValue) -> {
			if(newValue.equals(Worker.State.SUCCEEDED)){
				System.out.println("URL finished loading, sending to helper...");
				chatController.sendCodeURL();
			}
		});
	}
	
	/**
	 * Deletes the firepad created in the database to this user. It is called when a connection between two user is closed, as this firepad is no longer needed
	 */
	public void deleteFirepad() {
		if(getURL() != null && getURL().startsWith("https://connected-1e044.firebaseapp.com"))
			sharedCodeBrowser.getEngine().executeScript("deleteFirepadReference();");	
		sharedCodeBrowser.getEngine().load(null);
	}
	
	
	/**
	 * @param codeLanguage
	 * Changes the color syntax of the text in the shared code editing browser to the language in input if it is supported
	 */
	public void changeCodeLanguage(String codeLanguage){
		if(getURL().startsWith("https://connected-1e044.firebaseapp.com"))
			sharedCodeBrowser.getEngine().executeScript("changeCodeLanguage(" + "'" + codeLanguage + "'" + ");");
	}
	
	
	
}


