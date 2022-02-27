package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Photo;

/**
 * This class is the controller for album.fxml 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class AlbumController implements Controller{
	/**
	 * the main controller
	 */
	MainController main_controller;
	
	/**
	 * the rename album button
	 */
	public @FXML Button rename_album_button;
	/**
	 * the home button
	 */
	public @FXML Button home_button;
	/**
	 * the slide show button
	 */
	public @FXML Button slideshow_button;
	/**
	 * the add photo button
	 */
	public @FXML Button add_photo_button;
	/**
	 * the remove photo button
	 */
	public @FXML Button remove_photo_button;
	/**
	 * the edit photo button
	 */
	public @FXML Button edit_photo_button;
	/**
	 * the log out button
	 */
	public @FXML Button log_out_button;
	/**
	 * the quit button
	 */
	public @FXML Button quit_button;
	/**
	 * the text field for renaming album
	 */
	public @FXML TextField rename_album_textfield;
	/**
	 * the text that contains the album information
	 */
	public @FXML Text album_info_text;
	/**
	 * the ListView of Photos in the album
	 */
	public @FXML ListView<Photo> photos_list;
	/**
	 * the ObservableList for photos_list
	 */
	public ObservableList<Photo> obs;
	
	
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
