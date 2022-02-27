package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Photo;

/**
 * This class is the controller for search.fxml 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class SearchController implements Controller{
	/**
	 * the main controller
	 */
	MainController main_controller;
	
	/**
	 * the quit button
	 */
	public @FXML Button quit_button;
	/**
	 * the go back home button
	 */
	public @FXML Button go_back_home_button;
	/**
	 * the create album button
	 */
	public @FXML Button create_album_button;
	/**
	 * the search by date button
	 */
	public @FXML Button search_by_date_button;
	/**
	 * the search by tags button
	 */
	public @FXML Button search_by_tags_button;
	/**
	 * the clear button
	 */
	public @FXML Button clear_button;
	/**
	 * the ListView for Photos
	 */
	public @FXML ListView<Photo> photos_listview;
	/**
	 * the ObservableList for photos_listvew
	 */
	public @FXML ObservableList<Photo> obs;
	/**
	 * the text field for from date
	 */
	public @FXML TextField from_textfield;
	/**
	 * the text field for to date
	 */
	public @FXML TextField to_textfield;
	/**
	 * the text field for tag value 1
	 */
	public @FXML TextField tag_value1_textfield;
	/**
	 * the text field for tag value 2
	 */
	public @FXML TextField tag_value2_textfield;
	/**
	 * the text field for album name
	 */
	public @FXML TextField album_name_textfield;
	/**
	 * the ComboBox for tag name 1
	 */
	public @FXML ComboBox<String> tag_name1_combobox;
	/**
	 * the ComboBox for tag name 2
	 */
	public @FXML ComboBox<String> tag_name2_combobox;
	/**
	 * the ComboBox for and/or
	 */
	public @FXML ComboBox<String> and_or_combobox;
	
	public void setup(MainController mc) {
		this.main_controller = mc;
		and_or_combobox.setItems(FXCollections.observableArrayList("and","or"));
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
