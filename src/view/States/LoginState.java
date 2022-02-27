package view.States;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;
import view.MainController;

/**
 * This class takes care of events that happen in the scene that corresponds to the LoginController.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class LoginState extends PhotosState{
	/**
	 * the singleton LoginState
	 */
	private static LoginState currentState;
	/**
	 * the constructor
	 */
	private LoginState() {
		
	}
	
	@Override
	public void setup(MainController mc) {
		this.main_controller = mc;
	}
	@Override
	public void enter(Admin admin, User user, Album album, Photo photo) {
		this.main_controller.primaryStage.setTitle("Log In");
		try {
			this.admin = Admin.readApp();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.main_controller.primaryStage.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.main_controller.primaryStage.close();
		}	
	}

	@Override
	public PhotosState processEvent(ActionEvent e) {
		Button button = (Button)e.getSource();
		if(button == this.main_controller.login_controller.quit_button) {
			this.main_controller.primaryStage.close();
			return null;
		}
		//login button pressed
		String typed_username = this.main_controller.login_controller.username_textfield.getText();
		if(typed_username.compareTo("admin") == 0) {//administrator case
			AdminState temp = this.main_controller.admin_state;
			temp.enter(this.admin, this.user, this.album, this.photo);
			this.main_controller.primaryStage.setScene(this.main_controller.admin_scene);
			return temp;
		}
		for(int i = 0; i < this.admin.users.size(); i++) {//normal user case
			if(this.admin.users.get(i).username.compareTo(typed_username) == 0) {
				this.user = this.admin.users.get(i);
				HomeState temp = this.main_controller.home_state;
				temp.enter(this.admin,this.user,this.album,this.photo);
				this.main_controller.primaryStage.setScene(this.main_controller.home_scene);
				return temp;
			}
		}
		
		//username not found
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(this.main_controller.primaryStage);
		alert.setTitle("Error: username not found");
		alert.setResizable(false);
		alert.setHeaderText("Error: Username could not be found. Please type in a valid username.");
		alert.showAndWait();
		
		return this;
	}
	/**
	 * Returns the singleton LoginState
	 * @return	the singleton LoginState
	 */
	public static LoginState getInstance() {
		if(LoginState.currentState == null) {
			LoginState.currentState = new LoginState();
		}
		return LoginState.currentState;
	}

}
