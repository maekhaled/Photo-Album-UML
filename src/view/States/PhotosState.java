package view.States;

import javafx.event.ActionEvent;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;
import view.MainController;

/**
 * The PhotoState abstract class subclasses all the other singleton states for this Photo82 application.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public abstract class PhotosState {
	/**
	 * the MainController instance that connects all the controllers and states
	 */
	protected MainController main_controller;
	/**
	 * an Admin instance 
	 */
	protected Admin admin;
	/**
	 * a User instance
	 */
	protected User user;
	/**
	 * an Album instance
	 */
	protected Album album;
	/**
	 * a Photo instance
	 */
	protected Photo photo;
	
	/**
	 * Sets up the State by connecting to the MainController
	 * 
	 * @param mc	the MainController instance
	 */
	public abstract void setup(MainController mc);
	/**
	 * Prepares and finishes all the required steps before doing anything in the current state.
	 * 
	 * @param admin	the Admin instance
	 * @param user	the User instance
	 * @param album	the Album instance
	 * @param photo	the Photo instance
	 */
	public abstract void enter(Admin admin, User user, Album album, Photo photo);
	/**
	 * Processes the event sent from the controllers.
	 * 
	 * @param e	the ActionEvent sent from the controllers
	 * @return	the state that the current state transitions to
	 */
	public abstract PhotosState processEvent(ActionEvent e);
}
