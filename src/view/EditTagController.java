package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * This class is the controller for edittage.fxml 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class EditTagController {
	/**
	 * the main controller
	 */
	MainController main_controller;
	
	/** 
	 * the add button
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
	 * the delete button
	 */
	public @FXML Button delete_button;
	/**
	 * the tag ComboBox
	 */
	public @FXML ComboBox<String> tag_combobox;
	/**
	 * the tag name text field
	 */
	public @FXML TextField name_textfield;
	
	/**
	 * the multiplicity ComboBox
	 */
	public @FXML ComboBox<String> multiplicity_combobox;
	
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
