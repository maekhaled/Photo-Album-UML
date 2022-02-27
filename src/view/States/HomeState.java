package view.States;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;
import view.MainController;

/**
 * This class takes care of events that happen in the scene that corresponds to the HomeController
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class HomeState extends PhotosState{
	/**
	 * the singleton HomeState
	 */
	private static HomeState currentState;
	/**
	 * the constructor
	 */
	private HomeState() {
		
	}
	
	@Override
	public void setup(MainController mc) {
		this.main_controller = mc;
	}
	@Override
	public void enter(Admin admin, User user, Album album, Photo photo) {
		this.main_controller.primaryStage.setTitle("Home");
		this.admin = admin;
		this.user = user;
		this.album = album;
		this.photo = photo;
		
		updateAlbumList();
	}

	@Override
	public PhotosState processEvent(ActionEvent e) {
		Button button = (Button)e.getSource();
		if(button == this.main_controller.home_controller.quit_button) {
			this.main_controller.primaryStage.close();
			return null;
		}
		if(button == this.main_controller.home_controller.log_out_button) {
			this.main_controller.primaryStage.setScene(this.main_controller.login_scene);
			LoginState tempState = this.main_controller.login_state;
			tempState.enter(null,null,null,null);
			return tempState;
		}
		if(button == this.main_controller.home_controller.add_tags_button) {
			this.main_controller.primaryStage.setScene(this.main_controller.edittag_scene);
			EditTagState tempState = this.main_controller.edittag_state;
			tempState.enter(this.admin,this.user,this.album,this.photo);
			return tempState;
		}
		if(button == this.main_controller.home_controller.create_album_button) {
			String tbc_album = this.main_controller.home_controller.create_album_textfield.getText();
			if(tbc_album.trim().length() == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: empty/invalid albumname");
				alert.setContentText("Error: Empty/invalid albumname. Please type in "
						+ "an album name that is not empty and does not only contains spaces.");
				alert.showAndWait();
				return this;
			}
			if(!this.user.createAlbum(tbc_album.trim())) {//cannot create album
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: duplicate albumname");
				alert.setContentText("Error: Duplicate album name exists. Please type in "
						+ "an album name that does not exist yet.");
				alert.showAndWait();
				return this;
			}
			else {//created album
				Album temp = this.user.albums.get(this.user.albums.size()-1);
				ObservableList<String> obs = this.main_controller.home_controller.obs;
				//taking care of case where album has no photos and thus no date range 
				String fromDate, toDate;
				if(temp.date_range[0] == null) {
					fromDate = "none";
				}
				else {
					fromDate = temp.date_range[0].toString();
				}
				if(temp.date_range[1] == null) {
					toDate = "none";
				}
				else {
					toDate = temp.date_range[1].toString();
				}
				
				obs.add("Album Name: " + temp.name + "\nNumber of Photos: " + temp.num_of_photos
						+ "\nDate Range: " + fromDate + " to " + toDate);
				try {
					Admin.writeApp(this.admin);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					this.main_controller.primaryStage.close();
				}
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Success: created album");
				alert.setContentText("Success: created album.");
				alert.showAndWait();
				return this;
			}
		}
		if(button == this.main_controller.home_controller.rename_button) {
			if(this.main_controller.home_controller.albums_listview.getSelectionModel().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: no album selected");
				alert.setContentText("Error: No album is selected. Please select "
						+ "an album from the list that you want to rename.");
				alert.showAndWait();
				return this;
			}
			
			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.initOwner(this.main_controller.primaryStage);
			confirmation.setResizable(false);
			confirmation.setHeaderText("Are you sure you want to rename this album?");
			Optional<ButtonType> result = confirmation.showAndWait();
			if(result.isPresent() && result.get() == ButtonType.CANCEL) {
				return this;
			}
			this.album = this.user.albums.get(this.main_controller.home_controller.albums_listview.getSelectionModel().getSelectedIndex());
			String name = this.main_controller.home_controller.create_album_textfield.getText();
			if(name.trim().length()==0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: invalid album name");
				alert.setContentText("Error: Invalid album name. Please provide "
						+ "a valid album name for renaming (cannot be empty or spaces).");
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
				updateAlbumList();
				return this;
			}
		}
		if(button == this.main_controller.home_controller.search_photos_button) {
			SearchState tempState = this.main_controller.search_state;
			tempState.enter(this.admin, this.user, this.album, this.photo);
			this.main_controller.primaryStage.setScene(this.main_controller.search_scene);
			return tempState;
		}
		if(button == this.main_controller.home_controller.open_album_button) {
			if(this.main_controller.home_controller.albums_listview.getSelectionModel().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: no album selected");
				alert.setContentText("Error: No album is selected. Please select "
						+ "an album from the list that you want to open.");
				alert.showAndWait();
				return this;
			}
			
			AlbumState tempState = this.main_controller.album_state;
			int index = this.main_controller.home_controller.albums_listview.getSelectionModel().getSelectedIndex();
			this.album = this.user.albums.get(index);
			tempState.enter(this.admin, this.user, this.album, this.photo);
			this.main_controller.primaryStage.setScene(this.main_controller.album_scene);
			return tempState;
		}
		//delete album button clicked
		if(this.main_controller.home_controller.albums_listview.getSelectionModel().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setResizable(false);
			alert.setHeaderText("Error: no album selected");
			alert.setContentText("Error: No album is selected. Please select "
					+ "an album from the list that you want to delete.");
			alert.showAndWait();
			return this;
		}
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.initOwner(this.main_controller.primaryStage);
		confirmation.setResizable(false);
		confirmation.setHeaderText("Deleting Album");
		confirmation.setContentText("Are you sure you want to delete this album?");
		Optional<ButtonType> result = confirmation.showAndWait();
		if(result.isPresent() && result.get() == ButtonType.CANCEL) {
			return this;
		}
		
		ObservableList<String> obs = this.main_controller.home_controller.obs;
		int index = this.main_controller.home_controller.albums_listview.getSelectionModel().getSelectedIndex();
		this.user.albums.remove(index);
		obs.remove(index);
		try {
			Admin.writeApp(this.admin);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.main_controller.primaryStage.close();
		}
		return this;
	}
	/**
	 * Returns the singleton HomeState
	 * @return	the singleton HomeState
	 */
	public static HomeState getInstance() {
		if(HomeState.currentState == null) {
			HomeState.currentState = new HomeState();
		}
		return HomeState.currentState;
	}
	/**
	 * Updates the ListView of Albums
	 */
	private void updateAlbumList() {
		this.main_controller.home_controller.obs = FXCollections.observableArrayList();
		ObservableList<String> obs = this.main_controller.home_controller.obs;
		for(int i = 0; i < this.user.albums.size(); i++) {
			Album temp = this.user.albums.get(i);
			String fromDate, toDate;
			if(temp.date_range[0] == null) {
				fromDate = "none";
			}
			else {
				fromDate = temp.date_range[0].toLocalDate().toString();
			}
			if(temp.date_range[1] == null) {
				toDate = "none";
			}
			else {
				toDate = temp.date_range[1].toLocalDate().toString();
			}
			obs.add("Album Name: " + temp.name + "\nNumber of Photos: " + temp.num_of_photos
					+ "\nDate Range: " + fromDate + " to " + toDate);
		}
		this.main_controller.home_controller.albums_listview.setItems(obs);
	}

}
