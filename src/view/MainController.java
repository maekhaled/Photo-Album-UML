package view;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.States.*;

/**
 * This class is the main controller that connects all controllers and states for Photos82.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class MainController {
	/**
	 * the primaryStage
	 */
	public Stage primaryStage;
	
	//controllers

	/**
	 * the LoginController
	 */
	public LoginController login_controller;
	/**
	 * the scene for LoginController
	 */
	public Scene login_scene;
	/**
	 * the AdminController
	 */
	public AdminController admin_controller;
	/**
	 * the scene for AdminController
	 */
	public Scene admin_scene;
	/**
	 * the HomeController
	 */
	public HomeController home_controller;
	/**
	 * the scene for HomeController
	 */
	public Scene home_scene;
	/**
	 * the SearchController
	 */
	public SearchController search_controller;
	/**
	 * the scene for SearchController
	 */
	public Scene search_scene;
	/**
	 * the AlbumController
	 */
	public AlbumController album_controller;
	/**
	 * the scene for AlbumController
	 */
	public Scene album_scene;
	/**
	 * the SlideshowController
	 */
	public SlideshowController slideshow_controller;
	/**
	 * the scene for SlideshowController
	 */
	public Scene slideshow_scene;
	/**
	 * the AddPhotoController
	 */
	public AddPhotoController addphoto_controller;
	/**
	 * the scene for AddPhotoController
	 */
	public Scene addphoto_scene;
	/**
	 * the EditPhotoController
	 */
	public EditPhotoController editphoto_controller;
	/**
	 * the scene for EditPhotoController
	 */
	public Scene editphoto_scene;
	/**
	 * the EditTagController
	 */
	public EditTagController edittag_controller;
	/**
	 * the scene for EditTagController
	 */
	public Scene edittag_scene;
	
	//states
	/**
	 * the current state for the main controller
	 */
	public PhotosState current_state;
	/**
	 * the LoginState
	 */
	public LoginState login_state;
	/**
	 * the AdminState
	 */
	public AdminState admin_state;
	/**
	 * the HomeState
	 */
	public HomeState home_state;
	/**
	 * the SearchState
	 */
	public SearchState search_state;
	/**
	 * the AlbumState
	 */
	public AlbumState album_state;
	/**
	 * the SlideshowState
	 */
	public SlideshowState slideshow_state;
	/**
	 * the AddPhotoState
	 */
	public AddPhotoState addphoto_state;
	/**
	 * the EditPhotoState
	 */
	public EditPhotoState editphoto_state;
	/**
	 * the EditTagState
	 */
	public EditTagState edittag_state;
	
	/**
	 * Starts up the Photos82 GUI application.
	 * 
	 * @param primaryStage	the primaryStage for javafx
	 * @throws IOException	if an input or output exception occurred
	 */
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setOnCloseRequest(e->Platform.exit());
		this.setup();
		this.current_state.enter(null,null,null,null);
		primaryStage.setScene(this.login_scene);
		primaryStage.setTitle("Log in");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * Takes an ActionEvent from the controllers and passes it onto its current state.
	 * 
	 * @param e	ActionEvent from controllers
	 */
	public void processEvent(ActionEvent e) {
		this.current_state = this.current_state.processEvent(e);
	}
	
	/**
	 * Sets up all the controllers and the states needed for the javafx application.
	 * 
	 * @throws IOException
	 */
	private void setup() throws IOException {
		//setting up controllers
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		login_scene = scene;
		login_controller  = loader.getController();
		
		loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
		root = loader.load();
		scene = new Scene(root);
		admin_scene = scene;
		admin_controller  = loader.getController();

		
		loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
		root = loader.load();
		scene = new Scene(root);
		home_scene = scene;
		home_controller  = loader.getController();

		
		loader = new FXMLLoader(getClass().getResource("/view/search.fxml"));
		root = loader.load();
		scene = new Scene(root);
		search_scene = scene;
		search_controller  = loader.getController();

		
		loader = new FXMLLoader(getClass().getResource("/view/album.fxml"));
		root = loader.load();
		scene = new Scene(root);
		album_scene = scene;
		album_controller  = loader.getController();

		
		loader = new FXMLLoader(getClass().getResource("/view/slideshow.fxml"));
		root = loader.load();
		scene = new Scene(root);
		slideshow_scene = scene;
		slideshow_controller  = loader.getController();

		
		loader = new FXMLLoader(getClass().getResource("/view/addphoto.fxml"));
		root = loader.load();
		scene = new Scene(root);
		addphoto_scene = scene;
		addphoto_controller  = loader.getController();

		
		loader = new FXMLLoader(getClass().getResource("/view/editphoto.fxml"));
		root = loader.load();
		scene = new Scene(root);
		editphoto_scene = scene;
		editphoto_controller  = loader.getController();
		
		loader = new FXMLLoader(getClass().getResource("/view/edittag.fxml"));
		root = loader.load();
		scene = new Scene(root);
		edittag_scene = scene;
		edittag_controller  = loader.getController();

		login_controller.setup(this);
		admin_controller.setup(this);
		home_controller.setup(this);
		search_controller.setup(this);
		album_controller.setup(this);
		slideshow_controller.setup(this);
		addphoto_controller.setup(this);
		editphoto_controller.setup(this);
		edittag_controller.setup(this);
		
		//setting up states
		this.login_state = LoginState.getInstance();this.login_state.setup(this);
		this.admin_state = AdminState.getInstance();this.admin_state.setup(this);
		this.home_state = HomeState.getInstance();this.home_state.setup(this);
		this.search_state = SearchState.getInstance();this.search_state.setup(this);
		this.album_state = AlbumState.getInstance();this.album_state.setup(this);
		this.slideshow_state = SlideshowState.getInstance();this.slideshow_state.setup(this);
		this.addphoto_state = AddPhotoState.getInstance();this.addphoto_state.setup(this);
		this.editphoto_state = EditPhotoState.getInstance();this.editphoto_state.setup(this);
		this.edittag_state = EditTagState.getInstance();this.edittag_state.setup(this);
		this.current_state = this.login_state;
	}
}
