package view.States;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Admin;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import view.MainController;

/**
 * This class takes care of events that happen in the scene that corresponds to the EditPhotoController
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class EditPhotoState extends PhotosState{
	/**
	 * the singleton EditPhotoState
	 */
	private static EditPhotoState currentState;
	/**
	 * the constructor
	 */
	private EditPhotoState() {
		
	}
	
	@Override
	public void setup(MainController mc) {
		this.main_controller = mc;
	}
	@Override
	public void enter(Admin admin, User user, Album album, Photo photo) {
		this.main_controller.primaryStage.setTitle("Edit Photo");
		this.admin = admin;
		this.user = user;
		this.album = album;
		this.photo = photo;
		
		updatePhoto();
		setupTable();
		setupComboBox();
	}

	@Override
	public PhotosState processEvent(ActionEvent e) {
		Button button = (Button)e.getSource();
		if(button == this.main_controller.editphoto_controller.quit_button) {
			this.main_controller.primaryStage.close();
			return null;
		}
		if(button == this.main_controller.editphoto_controller.album_button) {
			this.main_controller.primaryStage.setScene(this.main_controller.album_scene);
			AlbumState tempState = this.main_controller.album_state;
			tempState.enter(this.admin, this.user, this.album, this.photo);
			return tempState;
		}
		if(button == this.main_controller.editphoto_controller.apply_button) {
			String caption = this.main_controller.editphoto_controller.caption_textfield.getText();
			this.photo.modifyCaption(caption);
			try {
				Admin.writeApp(this.admin);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				this.main_controller.primaryStage.close();
			}
			updatePhotoInfo();
			return this;
		}
		if(button == this.main_controller.editphoto_controller.add_tag_button) {
			String name = this.main_controller.editphoto_controller.tag_name_combobox.getValue();
			String value = this.main_controller.editphoto_controller.tag_value_textfield.getText();
			if(this.main_controller.editphoto_controller.tag_name_combobox.getSelectionModel().isSelected(0)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: empty selection");
				alert.setContentText("Error: Invalid selection. Please select "
						+ "a tag name from the list of tags provided.");
				alert.showAndWait();
				return this;
			}
			if(value.trim().compareTo("") == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: invalid tag value input");
				alert.setContentText("Error: Invalid tag value input. Please provide "
						+ "a tag value that is not empty or is not only spaces.");
				alert.showAndWait();
				return this;
			}
			int addtag_result = this.photo.addTag(name.trim(), value.trim(),this.user);
			if(addtag_result == 1) {//failed to add tag
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: duplicate tag exists");
				alert.setContentText("Error: Duplicate tag exists. Please provide "
						+ "a tag name and value combination that is unique.");
				alert.showAndWait();
				return this;
			}
			if(addtag_result == 2) {//failed to add tag
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: tag multiplicity inconsistency");
				alert.setContentText("Error: The selected tag type only supports a single value. Please "
						+ "select a different tag type, or simply delete the previous tag value associated with this tag type.");
				alert.showAndWait();
				return this;
			}
			//successfully added tag
			try {
				Admin.writeApp(this.admin);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				this.main_controller.primaryStage.close();
			}
			this.main_controller.editphoto_controller.obs.add(this.photo.tags.get(this.photo.tags.size()-1));
			updatePhotoInfo();
			return this;
		}
		String album_name = this.main_controller.editphoto_controller.to_album_textfield.getText();
		if(button == this.main_controller.editphoto_controller.copy_photo_button) {
			//confirmation alert
			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.initOwner(this.main_controller.primaryStage);
			confirmation.setResizable(false);
			confirmation.setHeaderText("Are you sure you want to copy this photo?");
			Optional<ButtonType> result = confirmation.showAndWait();
			if(result.isPresent() && result.get() == ButtonType.CANCEL) {
				return this;
			}
			
			if(album_name.compareTo(this.album.name) == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: invalid album name input");
				alert.setContentText("Error: Invalid album name input. Please provide "
						+ "an album name that is not the current album name");
				alert.showAndWait();
				return this;
			}
			int copy_result = this.photo.copyToAlbum(album_name,this.user.albums);
			if(copy_result == 2) {//failed to copy to a different album: album did not exist
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: invalid album name input");
				alert.setContentText("Error: Invalid album name input. Please provide "
						+ "an album name of an album that exists.");
				alert.showAndWait();
				return this;
			}
			else if(copy_result == 1) {//the specified album already has the photo
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: invalid album name input");
				alert.setContentText("Error: Invalid album name input. Please provide "
						+ "an album name of an album that doesn't have this photo.");
				alert.showAndWait();
				return this;
			}
			//successfully copied to the specified album
			try {
				Admin.writeApp(this.admin);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				this.main_controller.primaryStage.close();
			}
			return this;
		}
		//move photo button was clicked
		//confirmation alert
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.initOwner(this.main_controller.primaryStage);
		confirmation.setResizable(false);
		confirmation.setHeaderText("Are you sure you want to move this photo?");
		Optional<ButtonType> result = confirmation.showAndWait();
		if(result.isPresent() && result.get() == ButtonType.CANCEL) {
			return this;
		}
		
		if(album_name.compareTo(this.album.name) == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setResizable(false);
			alert.setHeaderText("Error: invalid album name input");
			alert.setContentText("Error: Invalid album name input. Please provide "
					+ "an album name that is not the current album name");
			alert.showAndWait();
			return this;
		}
		int move_result = this.photo.moveToAlbum(album_name,this.album,this.user.albums);
		if(move_result == 2) {//failed to copy to a different album: album did not exist
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setResizable(false);
			alert.setHeaderText("Error: invalid album name input");
			alert.setContentText("Error: Invalid album name input. Please provide "
					+ "an album name of an album that exists.");
			alert.showAndWait();
			return this;
		}
		else if(move_result == 1) {//specified album already has this photo
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setResizable(false);
			alert.setHeaderText("Error: invalid album name input");
			alert.setContentText("Error: Invalid album name input. Please provide "
					+ "an album name of an album that doesn't have this photo.");
			alert.showAndWait();
			return this;
		}
		//successfully moved to the specified album
		try {
			Admin.writeApp(this.admin);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.main_controller.primaryStage.close();
		}
		this.main_controller.primaryStage.setScene(this.main_controller.album_scene);
		AlbumState tempState = this.main_controller.album_state;
		tempState.enter(this.admin, this.user, this.album, this.photo);
		return tempState;
	}
	/**
	 * Returns the singleton EditPhotoState
	 * @return	the singleton EditPhotoState
	 */
	public static EditPhotoState getInstance() {
		if(EditPhotoState.currentState == null) {
			EditPhotoState.currentState = new EditPhotoState();
		}
		return EditPhotoState.currentState;
	}
	/**
	 * Updates/sets up the photo in the current scene
	 */
	private void updatePhoto() {
		ImageView temp_imageview = this.main_controller.editphoto_controller.photo_imageview;
		try {
			temp_imageview.setImage(new Image(new FileInputStream(this.photo.filePath)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			updatePhotoInfo();
			return;
		}
		temp_imageview.setFitHeight(190);
		temp_imageview.setFitWidth(288);
		temp_imageview.setPreserveRatio(true);
		updatePhotoInfo();
	}
	/**
	 * Updates the photo information for the current scene
	 */
	private void updatePhotoInfo() {
		String caption = this.photo.caption;
		String tags = "";
		String date = refineLocalDateTime(this.photo.datetime);
		for(int i = 0; i < this.photo.tags.size(); i++) {
			if(i == this.photo.tags.size()-1) {
				tags += this.photo.tags.get(i);
			}
			else {
				tags += this.photo.tags.get(i) + ", ";
			}
		}
		this.main_controller.editphoto_controller.caption_text.setText("Caption: " + caption);
		this.main_controller.editphoto_controller.date_time_text.setText("Date: " + date);
		this.main_controller.editphoto_controller.tags_text.setText("Tags: " + tags);
		this.main_controller.editphoto_controller.album_text.setText(this.album.name);
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
		if(hour == 0) {
			hour = 12;
		}
		return split_arr1[0] + " " + hour + ":" + minute + am_pm;
	}
	/**
	 * Sets up the TableView for the current scene
	 */
	private void setupTable() {
		TableColumn<Tag,Tag> col = this.main_controller.editphoto_controller.button_column;
		TableView<Tag> tv = this.main_controller.editphoto_controller.tags_tableview;
		this.main_controller.editphoto_controller.obs = FXCollections.observableArrayList();
		ObservableList<Tag> obs = this.main_controller.editphoto_controller.obs;
		for(int i = 0; i < this.photo.tags.size(); i++) {
			obs.add(this.photo.tags.get(i));
		}
		
		col.setCellFactory(p-> new TableCell<Tag,Tag>() {
			private Button delete_button = new Button("delete");
			
			public void updateItem(Tag item, boolean empty) {
				super.updateItem(item,empty);
				if(empty) {
					setGraphic(null);
					setText(null);
				}
				else {
					delete_button.setOnAction(e-> {
						obs.remove(item);
						photo.tags.remove(item);
						try {
							Admin.writeApp(admin);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							main_controller.primaryStage.close();
						}
						updatePhotoInfo();
					});
					setGraphic(delete_button);
					setText(null);
				}
			}
		});
		tv.setItems(obs);
	}
	/**
	 * Sets up the ComboBox for the current scene;
	 */
	private void setupComboBox() {
		ComboBox<String> cb1 = this.main_controller.editphoto_controller.tag_name_combobox;
		cb1.getItems().clear();
		cb1.getItems().add("Select");
		for(int i = 0; i < this.user.tagnames.size(); i++) {
			cb1.getItems().add(this.user.tagnames.get(i).name);
		}
		cb1.getSelectionModel().clearAndSelect(0);
	}
}
