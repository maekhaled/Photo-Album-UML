package view.States;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;
import view.MainController;

/**
 * This class takes care of events that happen in the scene that corresponds to the AdminController
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class AdminState extends PhotosState{
	/**
	 * the singleton AdminState
	 */
	private static AdminState currentState;
	/**
	 * the constructor
	 */
	private AdminState() {
		
	}

	@Override
	public void setup(MainController mc) {
		this.main_controller = mc;
	}
	@Override
	public void enter(Admin admin, User user, Album album, Photo photo) {
		this.main_controller.primaryStage.setTitle("Admin Subsystem");
		this.admin = admin;
		this.user = user;
		this.album = album;
		this.photo = photo;
		
		this.main_controller.admin_controller.obs = FXCollections.observableArrayList();
		ObservableList<String> obs = this.main_controller.admin_controller.obs;
		for(int i = 0; i < this.admin.users.size(); i++) {
			obs.add(this.admin.users.get(i).username);
		}
		this.main_controller.admin_controller.user_listview.setItems(obs);
	}

	@Override
	public PhotosState processEvent(ActionEvent e) {
		Button button = (Button)e.getSource();
		ObservableList<String> obs = this.main_controller.admin_controller.obs;
		if(button == this.main_controller.admin_controller.quit_button) {
			this.main_controller.primaryStage.close();
			return null;
		}
		if(button == this.main_controller.admin_controller.log_out_button) {
			this.main_controller.primaryStage.setScene(this.main_controller.login_scene);
			LoginState tempState = this.main_controller.login_state;
			tempState.enter(null,null,null,null);
			return tempState;
		}
		if(button == this.main_controller.admin_controller.add_button) {
			String typed_username = this.main_controller.admin_controller.add_textfield.getText();
			if(typed_username.trim().length() == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: empty/invalid username");
				alert.setContentText("Error: Empty/invalid username. Please type in "
						+ "a username that is not empty or does not contain only spaces.");
				alert.showAndWait();
				return this;
			}
			if(typed_username.trim().compareTo("admin") == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: invalid username");
				alert.setContentText("Error: Invalid username. Please type in "
						+ "a username that is not \"admin\".");
				alert.showAndWait();
				return this;
			}
			if(this.admin.addUser(typed_username.trim())) {
				obs.add(typed_username.trim());
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
				alert.setHeaderText("Success: username addition");
				alert.setContentText("Success: username addition");
				alert.showAndWait();
				
				return this;
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.main_controller.primaryStage);
				alert.setResizable(false);
				alert.setHeaderText("Error: duplicate username");
				alert.setContentText("Error: Duplicate username exists. Please type in "
						+ "a username that does not exist yet.");
				alert.showAndWait();
				return this;
			}
		}
		//button was delete user
		if(this.main_controller.admin_controller.user_listview.getSelectionModel().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.main_controller.primaryStage);
			alert.setResizable(false);
			alert.setHeaderText("Error: no selection");
			alert.setContentText("Error: No username is selected for deletion. Please select "
					+ "a username in the list.");
			alert.showAndWait();
			return this;
		}
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.initOwner(this.main_controller.primaryStage);
		confirmation.setResizable(false);
		confirmation.setHeaderText("Deleting user");
		confirmation.setContentText("Are you sure you want to delete this user?");
		Optional<ButtonType> result = confirmation.showAndWait();
		if(result.isPresent() && result.get() == ButtonType.CANCEL) {
			return this;
		}
		
		String tbd_username = this.main_controller.admin_controller.user_listview.getSelectionModel().getSelectedItem();
		this.admin.deleteuser(tbd_username);
		try {
			Admin.writeApp(this.admin);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.main_controller.primaryStage.close();
		}
		//removing username from the observable list
		for(int i = 0; i < obs.size(); i++) {
			if(obs.get(i).compareTo(tbd_username) == 0) {
				obs.remove(i);
			}
		}
		return this;		
		//DO NOT alLOW space as username?? or are there other restrictions??
	}
	
	/**
	 * Returns the singleton AdminState
	 * @return	the singleton AdminState
	 */
	public static AdminState getInstance() {
		if(AdminState.currentState == null) {
			AdminState.currentState = new AdminState();
		}
		return AdminState.currentState;
	}
}
