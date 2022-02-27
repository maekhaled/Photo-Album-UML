package model;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Pattern;
/**
 * The Album class contains a list of photos on which various actions can be made.
 * An Album corresponds to exactly one user, and can contains many photos. 
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class Album implements Serializable{
	/**
	 * the serial version UID for storing data
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the name of the album
	 */
	public String name;
	/**
	 * the number of photos within the album
	 */
	public int num_of_photos;
	/**
	 * the date range of the photos within the album
	 */
	public LocalDateTime[] date_range;
	/**
	 * the user that owns this album instance
	 */
	public User user;
	/**
	 * the ArrayList of Photo which contains all the photos for this album
	 */
	public ArrayList<Photo> photos;
	
	/**
	 * the constructor
	 * @param name	the album name
	 * @param user 	the username
	 */
	public Album(String name, User user) {
		this.name = name;
		this.user = user;
		this.photos = new ArrayList<Photo>();
		this.num_of_photos = 0;
		this.date_range = new LocalDateTime[2];
	}
	
	/**
	 * Adds photo to the Album instance.
	 * 
	 * @param filePath	file path for the to-be-added photo
	 * @return	0 if successful, 1 if filePath is invalid, 2 if photo already exists, 3 if file path does not specify an image file
	 * @throws IOException if an input or output exception occurred
	 */
	public int addPhoto(String filePath) throws IOException {
		if(!isImageExtension(filePath)) {
			return 3;
		}
		for(int i = 0; i < this.photos.size(); i++) {
			if(this.photos.get(i).filePath.compareTo(filePath)== 0) {
				return 2;
			}
		}
		Photo photo = Photo.createPhoto(filePath, this);
		if(photo == null) {
			return 1;
		}
		
		//check whether there is the same photo in another album. If so, use addPhotoThroughLink
		for(int i = 0; i < this.user.albums.size(); i++) {
			Album temp = this.user.albums.get(i);
			if(temp.equals(this)) {
				continue;
			}
			for(int j = 0; j < temp.photos.size(); j++) {
				if(temp.photos.get(j).filePath.compareTo(filePath) == 0) {
					this.addPhotoThroughLink(temp.photos.get(j));
					return 0;
				}
			}
		}
		
		this.photos.add(photo);
		this.num_of_photos++;
		
		if(this.date_range[0] == null && this.date_range[1] == null) {
			this.date_range[0] = photo.datetime;
			this.date_range[1] = photo.datetime;
		}
		else {
			if(this.date_range[0].isAfter(photo.datetime)) {
				this.date_range[0] = photo.datetime;
			}
			else if(this.date_range[1].isBefore(photo.datetime)) {
				this.date_range[1] = photo.datetime;
			}
		}
		
		return 0;
	}
	/**
	 * Links an already existing Photo instance with this album.
	 * 
	 * @param given the Photo instance to be linked with this album
	 */
	public void addPhotoThroughLink(Photo given) {
		this.photos.add(given);
		this.num_of_photos++;
		if(this.date_range[0] == null && this.date_range[1] == null) {
			this.date_range[0] = given.datetime;
			this.date_range[1] = given.datetime;
		}
		else {
			if(this.date_range[0].isAfter(given.datetime)) {
				this.date_range[0] = given.datetime;
			}
			else if(this.date_range[1].isBefore(given.datetime)) {
				this.date_range[1] = given.datetime;
			}
		}
		given.albums.add(this);
	}
	/**
	 * Deletes the Photo that exists at the specified index within the ArrayList photos.
	 * 
	 * @param index	the index at which the to-be-deleted Photo instance exists
	 * @return	true if successful, false otherwise
	 */
	public boolean deletePhoto(int index) {
		if(index < 0 || index >= this.photos.size()) {
			return false;
		}
		this.photos.get(index).albums.remove(this);
		this.photos.remove(index);
		this.num_of_photos--;
		//apply change to date_range --> should Album just maintain a sorted list of photos?
		updateDateRange();
		return true;
	}
	/**
	 * Renames this album.
	 * 
	 * @param name	the new name for the album
	 * @return	true if successful, false otherwise
	 */
	public boolean rename(String name) {
		for(int i = 0; i < this.user.albums.size(); i++) {
			if(this.user.albums.get(i).name.compareTo(name) == 0) {
				return false;
			}
		}
		this.name = name;
		return true;
	}
	/**
	 * Checks whether the given filePath ends with an extension that corresponds to an image file
	 * 
	 * @param filePath	the filePath of the image file 
	 * @return	true if the file is an image file, false otherwise
	 */
	private boolean isImageExtension(String filePath) {
		String[] arr = filePath.split(Pattern.quote("."));
		String extension = arr[arr.length-1];
		extension.toLowerCase();
		
		String[] image_extensions = {"bmp","gif","jpg","jpeg","png"};
		for(int i = 0; i < image_extensions.length; i++) {
			if(extension.compareTo(image_extensions[i]) == 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Updates the date range for this album
	 * 
	 */
	public void updateDateRange() {
		if(this.photos.size() == 0) {
			this.date_range = new LocalDateTime[2];
		}
		else {
			LocalDateTime max = this.photos.get(0).datetime;
			LocalDateTime min = this.photos.get(0).datetime;
			for(int j = 1; j < this.photos.size(); j++) {
				LocalDateTime temp = this.photos.get(j).datetime;
				if(max.isBefore(temp)) {
					max = temp;
				}
				if(min.isAfter(temp)) {
					min = temp;
				}
			}
			this.date_range[0] = min;
			this.date_range[1] = max;
		}
	}
}
