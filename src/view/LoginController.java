package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * This class is the controller for login.fxml 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class LoginController implements Controller{
	/**
	 * the main controller
	 */
	MainController main_controller;
	
	/**
	 * the log in button
	 */
	public @FXML Button login_button;
	/**
	 * the quit button
	 */
	public @FXML Button quit_button;
	/**
	 * the text field for username
	 */
	public @FXML TextField username_textfield;
	
	public void setup(MainController mc) {
		this.main_controller = mc;
	}
	/**
	 * The event handling method for all button clicks
	 * 
	 * @param e	ActionEvent from button clicks
	 */
	@FXML
	private void processEvent(ActionEvent e) {
		this.main_controller.processEvent(e);
	}
}
