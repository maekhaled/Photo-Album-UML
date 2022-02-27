package model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * The Photo class is the class for storing photo information including file path, caption, date-time,
 * and more.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class Photo implements Serializable{
	/**
	 * the serial version UID for storing data
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the filePath for this photo
	 */
	public String filePath;
	/**
	 * the caption for this photo
	 */
	public String caption;
	/**
	 * the date-time of when this photo was last modified in the local file system
	 */
	public LocalDateTime datetime;
	/**
	 * the ArrayList of Album that contains all the albums that this photo is in
	 */
	public ArrayList<Album> albums;
	/**
	 * the ArrayList of Tag that contains all the tags corresponding to thie photo
	 */
	public ArrayList<Tag> tags;
	
	/**
	 * The constructor for Photo. Cannot be called directly outside this class.
	 * 
	 * @param filePath	the file path for the photo to be added
	 * @param album	the Album instance to which this photo will belong
	 * @throws IOException
	 */
	private Photo(String filePath, Album album) throws IOException {
		this.filePath = filePath;
		FileTime filetime = Files.getLastModifiedTime(new File(filePath).toPath());
		this.datetime = LocalDateTime.ofInstant(filetime.toInstant(), ZoneId.of("America/New_York"));
		this.albums = new ArrayList<Album>();
		this.tags = new ArrayList<Tag>();
		this.caption = "";
		this.albums.add(album);
	}
	/**
	 * Creates a Photo instance. 
	 * 
	 * @param filePath	the file path for this photo
	 * @param album	the Album instance to which this photo will belong
	 * @return	the Photo instance that corresponds to the provided file path
	 * @throws IOException	if an input or output exception occurred
	 */
	public static Photo createPhoto(String filePath, Album album) throws IOException {
		File file = new File(filePath);
		if(file.exists()) {
			return new Photo(filePath,album);
		}
		return null;
	}
	/**
	 * Adds a tag to this photo. Assumes that the tag type/name given already exists.
	 * 
	 * @param name	tag name
	 * @param value	tag value
	 * @param user	the current user
	 * @return	0 if successful, 1 if failed due to duplicate combination issue, 2 if failed due to multiplicity issue
	 */
	public int addTag(String name, String value, User user) {
		for(int i = 0; i < this.tags.size(); i++) {
			if(this.tags.get(i).name.compareTo(name) == 0 
					&& this.tags.get(i).value.compareTo(value) == 0) {
				return 1;//tag name + value combination has to be unique for a photo
			}
		}
		
		String multiplicity = "";
		for(int i = 0; i < user.tagnames.size(); i++) {
			if(user.tagnames.get(i).name.compareTo(name) == 0) {
				if(user.tagnames.get(i).multiplicity.compareTo("single") == 0) {
					multiplicity = "single";
				}
				else {
					multiplicity = "multiple";
				}
			}
		}
		int count = 0;
		for(int i = 0 ; i < this.tags.size(); i++) {
			if(this.tags.get(i).name.compareTo(name) == 0) {
				count++;
			}
		}
		if(multiplicity.compareTo("single") == 0) {
			if(count != 0) {
				return 2;//invalid for tag types that allow only single value
			}
		}
		Tag tag = new Tag(name,value,this);
		this.tags.add(tag);
		return 0;
	}
	/**
	 * Deletes a tag from the ArrayList of tags.
	 * 
	 * @param name	tag name
	 * @param value	tag value
	 * @return	true if successful, false otherwise
	 */
	public boolean deleteTag(String name, String value) {
		for(int i = 0; i < this.tags.size(); i++) {
			if(this.tags.get(i).name.compareTo(name) == 0 
					&& this.tags.get(i).value.compareTo(value) == 0) {
				this.tags.remove(i);
				return true;
			}
		}
		return false;
	}
	/**
	 * Modifies the caption of this photo.
	 * 
	 * @param caption	the caption that this photo will start having
	 */
	public void modifyCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * Copies this photo to another album.
	 * 
	 * @param album_name	the name of the album which this photo will be copied to
	 * @param given_albums	the ArrayList of all the Albums for this user
	 * @return	0 if successful, 1 if the same photo already exists in the album specified, 2 if the album name given is not an actual album name
	 */
	public int copyToAlbum(String album_name, ArrayList<Album> given_albums) {
		for(int i = 0; i < given_albums.size(); i++) {
			if(given_albums.get(i).name.compareTo(album_name) == 0) {
				Album temp = given_albums.get(i);
				for(int j = 0; j < temp.photos.size(); j++) {
					if(temp.photos.get(j).equals(this)) {
						return 1;
					}
				}
				temp.addPhotoThroughLink(this);
				return 0;
			}
		}
		return 2;
	}
	/**
	 * Moves this photo to another album
	 * 
	 * @param album_name	the name of the album to which this photo will be moved 
	 * @param tbr_album		the current album that will no longer have this photo after moving it
	 * @param given_albums	the ArrayList of all the Albums for this user
	 * @return	0 if successful, 1 if the same photo already exists in the album specified, 2 if the album name given is not an actual album name
	 */
	public int moveToAlbum(String album_name, Album tbr_album, ArrayList<Album> given_albums) {
		for(int i = 0; i < given_albums.size(); i++) {
			if(given_albums.get(i).name.compareTo(album_name) == 0) {
				Album temp = given_albums.get(i);
				for(int j = 0; j < temp.photos.size(); j++) {
					if(temp.photos.get(j).equals(this)) {
						return 1;
					}
				}
				
				temp.addPhotoThroughLink(this);
				
				tbr_album.photos.remove(this);
				tbr_album.num_of_photos--;
				tbr_album.updateDateRange();
				this.albums.remove(tbr_album);
				return 0;
			}
		}
		return 2;
	}
}
