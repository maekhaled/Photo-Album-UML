package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * This class is the controller for slideshow.fxml 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class SlideshowController implements Controller{
	/**
	 * the main controller
	 */
	MainController main_controller;
	
	/**
	 * the left button
	 */
	public @FXML Button left_button;
	/**
	 * the right button
	 */
	public @FXML Button right_button;
	/**
	 * the album button
	 */
	public @FXML Button album_button;
	/**
	 * the quit button
	 */
	public @FXML Button quit_button;
	/**
	 * the caption Text
	 */
	public @FXML Text caption_text;
	/**
	 * the tags Text
	 */
	public @FXML Text tags_text;
	/**
	 * the date Text
	 */
	public @FXML Text date_text;
	/**
	 * the photo ImageView
	 */
	public @FXML ImageView photo_imageview;

	
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
