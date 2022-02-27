package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Admin class contains the list of normal users as well as special user stock. 
 * This class can manipulate user creation,deletion, and also is used to store the data
 * for the Photos82 application.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class Admin implements Serializable{
	/**
	 * the serial version UID for storing data
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the name of the directory that contains the file that stores the Admin object data
	 */
	public static final String storeDir = "src/model/dat";
	/**
	 * the name of the file that stores the Admin object data
	 */
	public static final String storeFile = "admin.dat";
	
	/**
	 * the ArrayList of User
	 */
	public ArrayList<User> users;
	/**
	 * the constructor
	 */
	public Admin() {
		this.users = new ArrayList<User>();
	}
	
	/**
	 * Writes the data of a given Admin object to a file.
	 * 
	 * @param admin	the Admin instance that will be stored
	 * @throws IOException	if an input or output exception occurred
	 */
	public static void writeApp(Admin admin) throws IOException {//add to UML??
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir 
				+ File.separator + storeFile));
		oos.writeObject(admin);
		oos.close();//necessary??
	}
	/**
	 * Reads the data stored for the Admin object.
	 * 
	 * @return	the retrieved Admin instance
	 * @throws IOException	if an input or output exception occurred
	 * @throws ClassNotFoundException	if class not found for the serialized Admin object
	 */
	public static Admin readApp() throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir
				+ File.separator + storeFile));
		Admin admin = (Admin)ois.readObject();
		ois.close();
		return admin;
	}
	
	/**
	 * Adds a User instance to the ArrayList users
	 * 
	 * @param name	the username to be added
	 * @return	true if successful, false otherwise
	 */
	public boolean addUser(String name) {
		//check duplicate user names
		for(int i = 0; i < this.users.size(); i++) {
			if(this.users.get(i).username.compareTo(name) == 0) {
				return false;
			}
		}
		User user = new User(name);
		this.users.add(user);
		return true;
	}
	/**
	 * Deletes a user instance from the ArrayList users
	 * 
	 * @param name	the username to be deleted
	 * @return	true if successful, false otherwise
	 */
	public boolean deleteuser(String name) {
		for(int i = 0; i < this.users.size(); i++) {
			if(this.users.get(i).username == name) {
				this.users.remove(i);
				return true;
			}
		}
		return false;
	}
}
