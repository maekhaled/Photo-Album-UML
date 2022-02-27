package view.States;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;
import view.MainController;

/**
 * This class takes care of events that happen in the scene that corresponds to the SlideshowController.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class SlideshowState extends PhotosState{
	/**
	 * the singleton SlideshowState
	 */
	private static SlideshowState currentState;
	/**
	 * the index for the current photo shown in the slide show
	 */
	private int photo_index;
	/**
	 * the constructor
	 */
	private SlideshowState() {
		
	}

	@Override
	public void setup(MainController mc) {
		this.main_controller = mc;
	}
	@Override
	public void enter(Admin admin, User user, Album album, Photo photo) {
		this.main_controller.primaryStage.setTitle("Slideshow");
		this.admin = admin;
		this.user = user;
		this.album = album;
		this.photo = photo;
		this.photo_index = 0;
		
		if(this.album.num_of_photos != 0) {
			updatePhoto();
		}
		else {//no photos stored
			resetPhotoInfo();
		}
	}

	@Override
	public PhotosState processEvent(ActionEvent e) {
		Button button = (Button)e.getSource();
		if(button == this.main_controller.slideshow_controller.quit_button) {
			this.main_controller.primaryStage.close();
			return null;
		}
		if(button == this.main_controller.slideshow_controller.album_button) {
			AlbumState tempState = this.main_controller.album_state;
			tempState.enter(this.admin, this.user, this.album, this.photo);
			this.main_controller.primaryStage.setScene(this.main_controller.album_scene);
			return tempState;
		}
		if(button == this.main_controller.slideshow_controller.left_button) {
			this.photo_index -= 1;
			if(this.photo_index < 0) {
				this.photo_index = this.album.num_of_photos - 1;
			}
			updatePhoto();
			return this;
		}
		//right button clicked
		this.photo_index = (this.photo_index + 1) % this.album.num_of_photos;
		updatePhoto();
		return this;
	}

	/**
	 * Returns the singleton SlideshowState
	 * @return	the singleton SlideshowState
	 */
	public static SlideshowState getInstance() {
		if(SlideshowState.currentState == null) {
			SlideshowState.currentState = new SlideshowState();
		}
		return SlideshowState.currentState;
	}
	/**
	 * Updates the photo information for the current photo in the slideshow
	 */
	private void updatePhotoInfo() {
		String caption = this.album.photos.get(photo_index).caption;
		String tags = "";
		String date = refineLocalDateTime(this.album.photos.get(photo_index).datetime);
		for(int i = 0; i < this.album.photos.get(photo_index).tags.size(); i++) {
			if(i == this.album.photos.get(photo_index).tags.size()-1) {
				tags += this.album.photos.get(photo_index).tags.get(i);
			}
			else {
				tags += this.album.photos.get(photo_index).tags.get(i) + ", ";
			}
		}
		this.main_controller.slideshow_controller.caption_text.setText(caption);
		this.main_controller.slideshow_controller.date_text.setText(date);
		this.main_controller.slideshow_controller.tags_text.setText(tags);
	}
	/**
	 * Resets the Photo information for the current scene
	 */
	private void resetPhotoInfo() {
		this.main_controller.slideshow_controller.caption_text.setText("");
		this.main_controller.slideshow_controller.date_text.setText("");
		this.main_controller.slideshow_controller.tags_text.setText("");
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
	 * Updates the currently showed photo in the slide show
	 */
	private void updatePhoto() {
		ImageView temp_imageview = this.main_controller.slideshow_controller.photo_imageview;
		try {
			temp_imageview.setImage(new Image(new FileInputStream(this.album.photos.get(this.photo_index).filePath)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			updatePhotoInfo();
			return;
		}
		temp_imageview.setFitHeight(249);
		temp_imageview.setFitWidth(350);
		temp_imageview.setPreserveRatio(true);
		updatePhotoInfo();
	}
}
