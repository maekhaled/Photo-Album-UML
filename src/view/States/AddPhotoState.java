package view.States;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;
import view.MainController;

/**
 * This class takes care of events that happen in the scene that corresponds to the AddPhotoController
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class AddPhotoState extends PhotosState{
	/**
	 * the singleton state for AddPhotostate
	 */
	private static AddPhotoState currentState;
	/**
	 * the constructor
	 */
	private AddPhotoState() {
		
	}
	
	@Override
	public void setup(MainController mc) {
		this.main_controller = mc;
	}
	@Override
	public void enter(Admin admin, User user, Album album, Photo photo) {
		this.main_controller.primaryStage.setTitle("Add Photo");
		this.admin = admin;
		this.user = user;
		this.album = album;
		this.photo = photo;
	}

	@Override
	public PhotosState processEvent(ActionEvent e) {
		Button button = (Button)e.getSource();
		if(button == this.main_controller.addphoto_controller.quit_button) {
			this.main_controller.primaryStage.close();
			return null;
		}
		if(button == this.main_controller.addphoto_controller.cancel_button) {
			AlbumState tempState = this.main_controller.album_state;
			tempState.enter(this.admin, this.user, this.album, this.photo);
			this.main_controller.primaryStage.setScene(this.main_controller.album_scene);
			return tempState;
		}
		//add button clicked
		String filePath = this.main_controller.addphoto_controller.filepath_textfield.getText();
		String caption = this.main_controller.addphoto_controller.caption_textfield.getText();
		int result = 0;
		try {
			result = this.album.addPhoto(filePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.main_controller.primaryStage.close();
		}
		if(result == 1) {//invalid file path
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setResizable(false);
			alert.setHeaderText("Error: invalid file path");
			alert.setContentText("Error: Invalid file path. Please provide "
					+ "a valid file path for a photo.");
			alert.showAndWait();
			return this;
		}
		else if(result == 2) {//photo already exists inside the album
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setResizable(false);
			alert.setHeaderText("Error: duplicate photo");
			alert.setContentText("Error: A photo with the same file path found in the album. Please provide "
					+ "a file path for a photo that existing in the current album.");
			alert.showAndWait();
			return this;
		}
		else if(result == 3) {//file is not a photo
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setResizable(false);
			alert.setHeaderText("Error: invalid file type");
			alert.setContentText("Error: Invalid file type. Please provide "
					+ "a valid file path for a photo file. Allowed types are bmp, gif, jpeg, and png.");
			alert.showAndWait();
			return this;
		}
		//successful adding of photo
		Photo temp_photo = this.album.photos.get(this.album.photos.size()-1);
		temp_photo.modifyCaption(caption);
		try {
			Admin.writeApp(this.admin);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.main_controller.primaryStage.close();
		}
		AlbumState tempState = this.main_controller.album_state;
		tempState.enter(this.admin, this.user, this.album, this.photo);
		this.main_controller.primaryStage.setScene(this.main_controller.album_scene);
		return tempState;
	}
	/**
	 * Get the singleton state for AddPhotoState
	 * 
	 * @return the singleton AddPhotoState
	 */
	public static AddPhotoState getInstance() {
		if(AddPhotoState.currentState == null) {
			AddPhotoState.currentState = new AddPhotoState();
		}
		return AddPhotoState.currentState;
	}
}
