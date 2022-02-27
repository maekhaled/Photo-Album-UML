package view.States;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;
import view.MainController;

/**
 * This class takes care of events that happen in the scene that corresponds to the SearchController.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class SearchState extends PhotosState{
	/**
	 * the singleton SearchState
	 */
	private static SearchState currentState;
	/**
	 * the ArrayList of Photos that contains the resulting photos after search
	 */
	private ArrayList<Photo> searched_photos;
	/**
	 * the constructor
	 */
	private SearchState() {
		
	}

	@Override
	public void setup(MainController mc) {
		this.main_controller = mc;
	}
	@Override
	public void enter(Admin admin, User user, Album album, Photo photo) {
		this.main_controller.primaryStage.setTitle("Search Photos");
		this.admin = admin;
		this.user = user;
		this.album = album;
		this.photo = photo;
		clearItems();
	}

	@Override
	public PhotosState processEvent(ActionEvent e) {
		Button button = (Button)e.getSource();
		if(button == this.main_controller.search_controller.quit_button) {
			this.main_controller.primaryStage.close();
			return null;
		}
		if(button == this.main_controller.search_controller.go_back_home_button) {
			this.main_controller.primaryStage.setScene(this.main_controller.home_scene);
			HomeState tempState = this.main_controller.home_state;
			tempState.enter(this.admin, this.user, this.album, this.photo);
			return tempState;
		}
		if(button == this.main_controller.search_controller.search_by_date_button) {
			String fromDate = this.main_controller.search_controller.from_textfield.getText().trim();
			String toDate = this.main_controller.search_controller.to_textfield.getText().trim();
			if(!areValidDates(fromDate, toDate)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setTitle("Error: invalid date input ");
				alert.setResizable(false);
				alert.setHeaderText("Error: Invalid date input. Please provide " +
				"a pair of dates that are in the form of YYYY-MM-DD, with valid year, month and date.");
				alert.showAndWait();
				return this;
			}
			//are valid dates
			LocalDate fd = LocalDate.parse(fromDate);
			LocalDate td = LocalDate.parse(toDate);
			if(fd.isAfter(td)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setTitle("Error: invalid date input ");
				alert.setResizable(false);
				alert.setHeaderText("Error: Invalid date input. Please provide " +
				"a pair of dates that are in order (From-Date has to be before To-Date).");
				alert.showAndWait();
				return this;
			}
			this.searched_photos = this.user.searchByDate(fromDate, toDate);
			showPhotos(this.searched_photos);
			return this;
		}
		if(button == this.main_controller.search_controller.search_by_tags_button) {//not working properly!!
			String tag1_name = "";
			if(!this.main_controller.search_controller.tag_name1_combobox.getSelectionModel().isSelected(0)) {
				tag1_name = this.main_controller.search_controller.tag_name1_combobox.getValue();
			}
			String tag1_val = this.main_controller.search_controller.tag_value1_textfield.getText();
			if(this.main_controller.search_controller.tag_name1_combobox.getSelectionModel().isSelected(0) 
					&& tag1_val.trim().length() != 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setTitle("Error: unselected tag type ");
				alert.setResizable(false);
				alert.setHeaderText("Error: Unselected tag type. Please select " +
				"a tag name for the provided tag value");
				alert.showAndWait();
				return this;
			}
			if(!this.main_controller.search_controller.tag_name1_combobox.getSelectionModel().isSelected(0) 
					&& tag1_val.trim().length() == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setTitle("Error: empty tag value ");
				alert.setResizable(false);
				alert.setHeaderText("Error: Empty tag value. Please type in " +
				"a tag value for the selected tag name (pure sequence of spaces not allowed)");
				alert.showAndWait();
				return this;
			}
			
			String tag2_name = "";
			if(!this.main_controller.search_controller.tag_name2_combobox.getSelectionModel().isSelected(0)) {
				tag2_name = this.main_controller.search_controller.tag_name2_combobox.getValue();
			}
			String tag2_val = this.main_controller.search_controller.tag_value2_textfield.getText();
			if(this.main_controller.search_controller.tag_name2_combobox.getSelectionModel().isSelected(0) 
					&& tag2_val.trim().length() != 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setTitle("Error: unselected tag type ");
				alert.setResizable(false);
				alert.setHeaderText("Error: Unselected tag type. Please select " +
				"a tag name for the provided tag value");
				alert.showAndWait();
				return this;
			}
			if(!this.main_controller.search_controller.tag_name2_combobox.getSelectionModel().isSelected(0) 
					&& tag2_val.trim().length() == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setTitle("Error: empty tag value ");
				alert.setResizable(false);
				alert.setHeaderText("Error: Empty tag value. Please type in " +
				"a tag value for the selected tag name (pure sequence of spaces not allowed)");
				alert.showAndWait();
				return this;
			}
			
			boolean isAnd = true;
			if(this.main_controller.search_controller.and_or_combobox.getValue().compareTo("or")==0) {
				isAnd = false;
			}
			this.searched_photos = this.user.searchByTags(tag1_name, tag1_val, tag2_name, tag2_val, isAnd);
			showPhotos(this.searched_photos);			
			return this;
		}
		if(button == this.main_controller.search_controller.clear_button) {
			clearItems();
			return this;
		}
		//clicked create album based on search button
		String tbc_album = this.main_controller.search_controller.album_name_textfield.getText();
		if(tbc_album.trim().length() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setTitle("Error: empty/invalid album name ");
			alert.setResizable(false);
			alert.setHeaderText("Error: Empty/invalid album name. Please type in " +
			"an album name for the new album. The name cannot be empty or only contain spaces.");
			alert.showAndWait();
			return this;
		}
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.initOwner(this.main_controller.primaryStage);
		confirmation.setResizable(false);
		confirmation.setHeaderText("Are you sure you want to create a new album with these photos?");
		Optional<ButtonType> result = confirmation.showAndWait();
		if(result.isPresent() && result.get() == ButtonType.CANCEL) {
			return this;
		}
		if(!this.user.createAlbum(tbc_album.trim())) {//duplicate album name exists
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setTitle("Error: duplicate album name ");
			alert.setResizable(false);
			alert.setHeaderText("Error: Duplicate album name. Please type in " +
			"an album name that does not already exist.");
			alert.showAndWait();
			return this;
		}
		//valid album name
		Album temp_album = this.user.albums.get(this.user.albums.size()-1);
		for(int i = 0; i < this.searched_photos.size(); i++) {
			temp_album.addPhotoThroughLink(this.searched_photos.get(i));
		}
		try {
			Admin.writeApp(this.admin);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.main_controller.primaryStage.close();
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(this.main_controller.primaryStage);
		alert.setTitle("Success: creating new album based on search result");
		alert.setResizable(false);
		alert.setHeaderText("Success: creating new album based on search result.");
		alert.showAndWait();
		return this;
	}
	/**
	 * Returns the singleton SearchState
	 * @return	the singleton SearchState
	 */
	public static SearchState getInstance() {
		if(SearchState.currentState == null) {
			SearchState.currentState = new SearchState();
		}
		return SearchState.currentState;
	}
	/**
	 * Checks the validity of the provided strings in terms of them being a valid LocalDate instance
	 * 
	 * @param fromDate	the from date String
	 * @param toDate	the to date	String
	 * @return	true if they are valid String that can be converted to LocalDate instances, false otherwise
	 */
	private boolean areValidDates(String fromDate, String toDate) {
		try {
			LocalDate.parse(fromDate);
			LocalDate.parse(toDate);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * Shows/Displays all the Photos that result after a search
	 * 
	 * @param photo_arr	the ArrayList of Photos that is a search result
	 */
	private void showPhotos(ArrayList<Photo> photo_arr) {
		if(this.main_controller.search_controller.obs == null) {
			this.main_controller.search_controller.obs = FXCollections.observableArrayList();
		}
		else {
			this.main_controller.search_controller.obs.clear();
		}
		ObservableList<Photo> obs = this.main_controller.search_controller.obs;
		for(int i = 0; i < photo_arr.size(); i++) {
			obs.add(photo_arr.get(i));
		}
		
		ListView<Photo> listview = this.main_controller.search_controller.photos_listview;
		listview.setCellFactory(p -> new ListCell<Photo>() {
			private ImageView imageview = new ImageView();
			
			public void updateItem(Photo photo, boolean empty) {
				super.updateItem(photo, empty);
				if(empty || photo == null) {
					setText(null);
					setGraphic(null);
				}
				else {
					String filePath = photo.filePath;
					try {
						imageview.setImage(new Image(new FileInputStream(filePath)));
					} catch (FileNotFoundException e) {
						setText("this image's location has been modified");
						return;
					}
					String tags_list_string = "";
					for(int i = 0; i < photo.tags.size(); i++) {
						if(i == photo.tags.size()-1) {
							tags_list_string += photo.tags.get(i);
						}
						else {
							tags_list_string += photo.tags.get(i)+ ", ";
						}
					}
					String which_albums = "";
					for(int i = 0; i < photo.albums.size(); i++) {
						if(i == photo.albums.size()-1) {
							which_albums += photo.albums.get(i).name;
						}
						else {
							which_albums += photo.albums.get(i).name + ", ";
						}
					}
					String refined_datetime = refineLocalDateTime(photo.datetime);
					setText("In Albums: " + which_albums + "\nCaption: " + photo.caption + "\nDate-time: " 
					+ refined_datetime + "\nTags: " + tags_list_string);
					imageview.setPreserveRatio(true);
					imageview.setFitHeight(140);
					imageview.setFitWidth(190);
					setGraphic(imageview);
				}
			}
		});
		listview.setItems(obs);
	}
	/**
	 * Turns the given LocalDateTime instance into a proper String version
	 * 
	 * @param datetime	the given LocalDateTime instance
	 * @return	A String version of datetime 
	 */
	private String refineLocalDateTime(LocalDateTime datetime) {//try testing in the morning (or change local time)
		String[] split_arr1 = datetime.toString().split("T");
		String[] split_arr2 = split_arr1[1].split(":");
		int hour = Integer.parseInt(split_arr2[0]);
		String minute = split_arr2[1];
		String am_pm = "am";
		
		if(hour > 12) {
			am_pm = "pm";
			hour -= 12;
		}
		return split_arr1[0] + " " + hour + ":" + minute + am_pm;
	}
	/**
	 * Clears the changes made to the original setup for the current scene
	 */
	private void clearItems() {
		this.main_controller.search_controller.from_textfield.setText("");
		this.main_controller.search_controller.to_textfield.setText("");
		this.main_controller.search_controller.tag_value1_textfield.setText("");
		this.main_controller.search_controller.tag_value2_textfield.setText("");
		if(this.main_controller.search_controller.obs != null) {
			this.main_controller.search_controller.obs.clear();
		}
		this.searched_photos = new ArrayList<Photo>();
		populateComboBoxes();
	}
	/**
	 * Populates the ComboBoxes for the current scene
	 */
	private void populateComboBoxes() {
		ComboBox<String> cb1 = this.main_controller.search_controller.tag_name1_combobox;
		ComboBox<String> cb2 = this.main_controller.search_controller.tag_name2_combobox;
		ComboBox<String> cb3 = this.main_controller.search_controller.and_or_combobox;
		cb1.getItems().clear();
		cb2.getItems().clear();
		cb3.getItems().clear();
		
		cb1.getItems().add("Select Tag");
		for(int i = 0; i < this.user.tagnames.size(); i++) {
			cb1.getItems().add(this.user.tagnames.get(i).name);
		}
		cb2.getItems().add("Select Tag");
		for(int i = 0; i < this.user.tagnames.size(); i++) {
			cb2.getItems().add(this.user.tagnames.get(i).name);
		}
		cb3.getItems().add("and");
		cb3.getItems().add("or");
		cb1.getSelectionModel().clearAndSelect(0);
		cb2.getSelectionModel().clearAndSelect(0);
		cb3.getSelectionModel().clearAndSelect(0);
	}
}
