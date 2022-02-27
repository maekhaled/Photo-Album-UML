package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * This class is the controller for addphoto.fxml 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class AddPhotoController implements Controller{
	/**
	 * the main controller 
	 */
	MainController main_controller;
	
	/**
	 * the add photo button
	 */
	public @FXML Button add_button;
	/**
	 * the cancel button
	 */
	public @FXML Button cancel_button;
	/**
	 * the quit button
	 */
	public @FXML Button quit_button;
	/**
	 * the caption text field
	 */
	public @FXML TextField caption_textfield;
	/**
	 * the file path text field
	 */
	public @FXML TextField filepath_textfield;
	
	
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
