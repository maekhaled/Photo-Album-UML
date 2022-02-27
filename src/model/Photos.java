package model;


import javafx.application.Application;
import javafx.stage.Stage;
import view.MainController;
/**
 * This is the class that contains the main method for starting up the application.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class Photos extends Application{
	/**
	 * Start the application.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		MainController mc = new MainController();
		mc.start(primaryStage);
	}
	/**
	 * The main method.
	 * 
	 * @param args	the arguments given to the main method
	 */
	public static void main(String[] args) {
		//when wanting to start with new data, uncomment the following codeblock and then run the program
		//afterwards, quit the program and comment out the codeblock and run the program
		
//		Admin admin = new Admin();
//		admin.addUser("stock");
//		User user = admin.users.get(0);
//		user.createAlbum("stock");
//		Album album = user.albums.get(0);
//		try {
//			album.addPhoto("data/iphone11.jpg");
//			album.addPhoto("data/oh_no.jpg");
//			album.addPhoto("data/piano.jpg");
//			album.addPhoto("data/round_glasses.jpg");
//			album.addPhoto("data/smiling_fox.jpg");
//			album.addPhoto("data/sunset.jpg");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		try {
//			Admin.writeApp(admin);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		
		launch(args);
	}
}
