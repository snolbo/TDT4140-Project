package mainWindow;

import com.sun.glass.ui.EventLoop.State;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;

public class InteractionTabManagerController {
	@FXML
	private WebView sharedCodeBrowser;
	
	@FXML
	void initialize(){
		this.sharedCodeBrowser.getEngine().setUserStyleSheetLocation("data:,body { font: 15px Helvetica; font-weight: bold; }");
		this.sharedCodeBrowser.getEngine().load("https://connected-1e044.firebaseapp.com/#-KexK4r_hmnBYMtjSObv");
		sharedCodeBrowser.setOnKeyPressed((event) ->{
			handleKeyEvent(event);
		});
		


	}
	
	public void handleKeyEvent(KeyEvent event){
		if(event.isControlDown()){
			double currentVal = sharedCodeBrowser.getFontScale();
			if(event.getCode() == KeyCode.MINUS && currentVal > 0.5)
				sharedCodeBrowser.setFontScale(currentVal - 0.1);
			else if(event.getCode() == KeyCode.PLUS && currentVal < 2)
				sharedCodeBrowser.setFontScale(currentVal + 0.1);
		}
	}
	
}
