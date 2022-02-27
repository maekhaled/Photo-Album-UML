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
 * This class takes care of events that happen in the scene that corresponds to the AlbumController
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class AlbumState extends PhotosState{
	/**
	 * the singleton AlbumState
	 */
	private static AlbumState currentState;
	/**
	 * the constructor
	 */
	private AlbumState() {
		
	}
	
	@Override
	public void setup(MainController mc) {
		this.main_controller = mc;
	}
	@Override
	public void enter(Admin admin, User user, Album album, Photo photo) {
		this.main_controller.primaryStage.setTitle("Album");
		this.admin = admin;
		this.user = user;
		this.album = album;
		this.photo = photo;
		
		if(this.main_controller.album_controller.obs == null) {
			this.main_controller.album_controller.obs = FXCollections.observableArrayList();
		}
		else {
			this.main_controller.album_controller.obs.clear();
		}
		ObservableList<Photo> obs = this.main_controller.album_controller.obs;
		for(int i = 0; i < this.album.photos.size(); i++) {
			obs.add(this.album.photos.get(i));
		}
		ListView<Photo> listview = this.main_controller.album_controller.photos_list;
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
						setText("image location has been modified");
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
					String refined_datetime = refineLocalDateTime(photo.datetime);
					setText("Caption: " + photo.caption + "\nDate-time: " + 
							refined_datetime + "\nTags: " + tags_list_string);
					imageview.setPreserveRatio(true);
					imageview.setFitHeight(150);
					imageview.setFitWidth(190);
					setGraphic(imageview);
				}
			}
		});
		this.main_controller.album_controller.photos_list.setItems(obs);
		updateAlbumInfo();
	}

	@Override
	public PhotosState processEvent(ActionEvent e) {
		Button button = (Button)e.getSource();
		if(button == this.main_controller.album_controller.quit_button) {
			this.main_controller.primaryStage.close();
			return null;
		}
		if(button == this.main_controller.album_controller.log_out_button) {
			this.main_controller.primaryStage.setScene(this.main_controller.login_scene);
			LoginState tempState = this.main_controller.login_state;
			tempState.enter(null,null,null,null);
			return tempState;
		}
		if(button == this.main_controller.album_controller.home_button) {
			HomeState tempState = this.main_controller.home_state;
			tempState.enter(this.admin, this.user, this.album, this.photo);
			this.main_controller.primaryStage.setScene(this.main_controller.home_scene);
			return tempState;
		}
		if(button == this.main_controller.album_controller.slideshow_button) {
			this.main_controller.primaryStage.setScene(this.main_controller.slideshow_scene);
			SlideshowState tempState = this.main_controller.slideshow_state;
			tempState.enter(this.admin, this.user, this.album, this.photo);
			return tempState;
		}
		if(button == this.main_controller.album_controller.add_photo_button) {
			this.main_controller.primaryStage.setScene(this.main_controller.addphoto_scene);
			AddPhotoState tempState = this.main_controller.addphoto_state;
			tempState.enter(this.admin, this.user, this.album, this.photo);
			return tempState;
		}
		if(button == this.main_controller.album_controller.edit_photo_button) {
			if(this.main_controller.album_controller.photos_list.getSelectionModel().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: no photo selectded");
				alert.setContentText("Error: No photo selected. Please select "
						+ "a photo in the list that you want to edit.");
				alert.showAndWait();
				return this;
			}
			int index = this.main_controller.album_controller.photos_list.getSelectionModel().getSelectedIndex();
			this.photo = this.album.photos.get(index);
			this.main_controller.primaryStage.setScene(this.main_controller.editphoto_scene);
			EditPhotoState tempState = this.main_controller.editphoto_state;
			tempState.enter(this.admin, this.user, this.album, this.photo);
			return tempState;
		}
		if(button == this.main_controller.album_controller.rename_album_button) {
			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.initOwner(this.main_controller.primaryStage);
			confirmation.setResizable(false);
			confirmation.setHeaderText("Are you sure you want to rename this album?");
			Optional<ButtonType> result = confirmation.showAndWait();
			if(result.isPresent() && result.get() == ButtonType.CANCEL) {
				return this;
			}
			
			String name = this.main_controller.album_controller.rename_album_textfield.getText();
			if(name.trim().length()==0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: invalid album name");
				alert.setContentText("Error: Invalid album name. Please provide "
						+ "a valid album name for renaming.");
				alert.showAndWait();
				return this;
			}
			if(!this.album.rename(name)) {//renaming failed
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: duplicate album name found");
				alert.setContentText("Error: Duplicate album name found. Please provide "
						+ "an album name that does not already exist in the album list.");
				alert.showAndWait();
				return this;
			}
			else {//renamed album
				try { 
					Admin.writeApp(this.admin);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					this.main_controller.primaryStage.close();
				}
				updateAlbumInfo();
				return this;
			}
		}
		//remove photo button clicked --> need to come back to this one to test!!
		if(this.main_controller.album_controller.photos_list.getSelectionModel().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setResizable(false);
			alert.setHeaderText("Error: no photo selected");
			alert.setContentText("Error: No photo is selected. Please select "
					+ "a photo you wish to delete from the list of photos.");
			alert.showAndWait();
			return this;
		}
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.initOwner(this.main_controller.primaryStage);
		confirmation.setResizable(false);
		confirmation.setHeaderText("Are you sure you want to delete this photo?");
		Optional<ButtonType> result = confirmation.showAndWait();
		if(result.isPresent() && result.get() == ButtonType.CANCEL) {
			return this;
		}
		
		int index = this.main_controller.album_controller.photos_list.getSelectionModel().getSelectedIndex();
		this.main_controller.album_controller.obs.remove(index);
		this.album.deletePhoto(index);
		try {
			Admin.writeApp(this.admin);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		updateAlbumInfo();
		return this;
	}
	/**
	 * Returns the singleton AlbumState
	 * @return	the singleton AlbumState
	 */
	public static AlbumState getInstance() {
		if(AlbumState.currentState == null) {
			AlbumState.currentState = new AlbumState();
		}
		return AlbumState.currentState;
	}
	/**
	 * Updates the album info in the album scene
	 */
	private void updateAlbumInfo() {
		String fromDate, toDate;
		if(this.album.date_range[0] == null) {
			fromDate = "none";
		}
		else {
			fromDate = this.album.date_range[0].toLocalDate().toString();
		}
		if(this.album.date_range[1] == null) {
			toDate= "none";
		}
		else {
			toDate = this.album.date_range[1].toLocalDate().toString();
		}
		String album_info = "Name: " + this.album.name + "\nNumber of Photos: " +
		this.album.num_of_photos + "\nRange of Dates: " + fromDate + " to " + toDate;
		this.main_controller.album_controller.album_info_text.setText(album_info);
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

}
