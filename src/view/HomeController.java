package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * This class is the controller for home.fxml 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class HomeController implements Controller{
	/**
	 * the main controller
	 */
	MainController main_controller;
	
	/**
	 * the create album button
	 */
	public @FXML Button create_album_button;
	/**
	 * the search photos button
	 */
	public @FXML Button search_photos_button;
	/**
	 * the open album button
	 */
	public @FXML Button open_album_button;
	/**
	 * the delete album button
	 */
	public @FXML Button delete_album_button;
	/**
	 * the rename album button
	 */
	public @FXML Button rename_button;
	/**
	 * the log out button
	 */
	public @FXML Button log_out_button;
	/**
	 * the quit button
	 */
	public @FXML Button quit_button;
	/**
	 * The add tags button. Is connected to the button that says "Edit Tag Types"
	 */
	public @FXML Button add_tags_button;
	/**
	 * the create album text field
	 */
	public @FXML TextField create_album_textfield;
	/**
	 * the ListView of albums
	 */
	public @FXML ListView<String> albums_listview;
	/**
	 * the ObservableList for albums_listview
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
