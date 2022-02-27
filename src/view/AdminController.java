package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * This class is the controller for admin.fxml 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class AdminController implements Controller{
	/**
	 * the main controller
	 */
	MainController main_controller;
	
	/**
	 * the delete user button
	 */
	public @FXML Button delete_button;
	/**
	 * the add user button
	 */
	public @FXML Button add_button;
	/**
	 * the log out button
	 */
	public @FXML Button log_out_button;
	/**
	 * the quit button
	 */
	public @FXML Button quit_button;
	/**
	 * the ListView of users
	 */
	public @FXML ListView<String> user_listview;
	/**
	 * the text field for creating new users
	 */
	public @FXML TextField add_textfield;
	/**
	 * the ObservableList for user_listview
	 */
	public ObservableList<String> obs;

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
