package view;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Tag;

/**
 * This class is the controller for editphoto.fxml 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class EditPhotoController implements Controller{
	/**
	 * the main controller
	 */
	MainController main_controller;
	
	/**
	 * the album button
	 */
	public @FXML Button album_button;
	/**
	 * the apply button
	 */
	public @FXML Button apply_button;
	/**
	 * the add tag button
	 */
	public @FXML Button add_tag_button;
	/**
	 * the copy photo button
	 */
	public @FXML Button copy_photo_button;
	/**
	 * the move photo button
	 */
	public @FXML Button move_photo_button;
	/**
	 * the quit button
	 */
	public @FXML Button quit_button;
	/**
	 * the album name Text
	 */
	public @FXML Text album_text;
	/**
	 * the caption text 
	 */
	public @FXML Text caption_text;
	/**
	 * the date time description Text
	 */
	public @FXML Text date_time_text;
	/**
	 * the tags list text
	 */
	public @FXML Text tags_text;
	/**
	 * the caption text field
	 */
	public @FXML TextField caption_textfield;
	/**
	 * the tag name ComboBox
	 */
	public @FXML ComboBox<String> tag_name_combobox;
	/**
	 * the tag value text field
	 */
	public @FXML TextField tag_value_textfield;
	/**
	 * the to album text field
	 */
	public @FXML TextField to_album_textfield;
	/**
	 * the tags TableView
	 */
	public @FXML TableView<Tag> tags_tableview;
	/**
	 * the tag type TableColumn
	 */
	public @FXML TableColumn<Tag,String> type_column;
	/**
	 * the tag value TableColumn
	 */
	public @FXML TableColumn<Tag,String> value_column;
	/**
	 * the delete button column
	 */
	public @FXML TableColumn<Tag,Tag> button_column;
	/**
	 * the photo ImageView
	 */
	public @FXML ImageView photo_imageview;
	/**
	 * the ObservableList for tags_tableview
	 */
	public @FXML ObservableList<Tag> obs;
	
	public void setup(MainController mc) {
		this.main_controller = mc;
		type_column.setCellValueFactory(new PropertyValueFactory<>("name"));
		value_column.setCellValueFactory(new PropertyValueFactory<>("value"));
		button_column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

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
