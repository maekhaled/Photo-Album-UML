package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The User class contains information about a user, including their username, their albums, their 
 * tag types, and more.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class User implements Serializable{
	/**
	 * the serial version UID for storing data
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the username of this User
	 */
	public String username;
	/**
	 * the Arraylist of Tag that contains all the tag types for this user
	 */
	public ArrayList<TagType> tagnames;
	/**
	 * the ArrayList of Album that contains all the albums for this user
	 */
	public ArrayList<Album> albums;

	/**
	 * the constructor
	 * 
	 * @param username	the username for this user
	 */
	public User(String username) {
		this.username = username;
		this.tagnames = new ArrayList<TagType>();
		this.albums = new ArrayList<Album>();
		
		this.addTagType("location", "single");
		this.addTagType("person", "multiple");
	}
	/**
	 * Creates an album using the given album name.
	 * 
	 * @param name	album name
	 * @return	true if successful, false otherwise
	 */
	public boolean createAlbum(String name) {//add to UML?
		for(int i = 0; i < this.albums.size(); i++) {
			if(this.albums.get(i).name.compareTo(name) == 0) {
				return false;//album names unique for each user
			}
		}
		Album album = new Album(name,this);
		this.albums.add(album);
		return true;
		
	}
	/**
	 * Deletes the album specified.
	 * 
	 * @param name	album name
	 * @return	true if successful, false otherwise
	 */
	public boolean deleteAlbum(String name) {
		for(int i = 0; i < this.albums.size(); i++) {
			if(this.albums.get(i).name.compareTo(name) == 0) {
				this.albums.remove(i);
				return true;
			}
		}
		return false;
	}
	/**
	 * Deletes a tag type from this user's ArrayList of Tags
	 * 
	 * @param name	tag name
	 * @return	true if successful, false otherwise
	 */
	public boolean deleteTag(String name) {
		for(int i = 0; i < this.tagnames.size(); i++) {
			if(this.tagnames.get(i).name.compareTo(name) == 0) {
				this.tagnames.remove(i);
				return true;
			}
		}
		return false;
	}
	/**
	 * Adds a tag type to this user's ArrayList of Tags
	 * 
	 * @param name	tag name
	 * @param multiplicity	the tag multiplicity
	 * @return	true if successful, false otherwise
	 */
	public boolean addTagType(String name, String multiplicity) {
		for(int i = 0; i < this.tagnames.size(); i++) {
			if(name.compareTo(this.tagnames.get(i).name) == 0) {//duplicate tag type found
				return false;
			}
		}
		this.tagnames.add(TagType.createTagType(name, multiplicity));
		return true;
	}
	/**
	 * Returns an ArrayList of Photos that were last modified between the fromDate and the toDate (inclusive).
	 * The Photos are from all albums, not just one.
	 * 
	 * @param fromDate	the from date
	 * @param toDate	the to date
	 * @return	the resulting ArrayList
	 */
	public ArrayList<Photo> searchByDate(String fromDate, String toDate){//assumes dates are parsable
		ArrayList<Photo> tbr = new ArrayList<Photo>();
		for(int i = 0; i < this.albums.size(); i++) {
			Album album = this.albums.get(i);
			for(int j = 0; j < album.photos.size(); j++) {
				Photo photo = album.photos.get(j);
				if(!tbr.contains(photo)) {
					LocalDate from_date = LocalDate.parse(fromDate);
					LocalDate to_date = LocalDate.parse(toDate);
					if((photo.datetime.toLocalDate().compareTo(from_date) == 0 || photo.datetime.toLocalDate().isAfter(from_date))
							&& (photo.datetime.toLocalDate().compareTo(to_date) == 0 || photo.datetime.toLocalDate().isBefore(to_date))) {
						tbr.add(photo);
					}
				}
			}
		}
		return tbr;
	}
	/**
	 * Returns an ArrayList of Photos that contain the specified tags.
	 * The Photos are from all albums, not just one.
	 * 
	 * @param tag1_name	tag 1 name
	 * @param tag1_val	tag 1 value
	 * @param tag2_name	tag 2 name
	 * @param tag2_val	tag 2 value
	 * @param isAnd		boolean variable that tells whether the search uses a conjunction or a disjunction
	 * @return			the resulting ArrayList
	 */
	public ArrayList<Photo> searchByTags(String tag1_name, String tag1_val, String tag2_name, String tag2_val, boolean isAnd){
		Tag t1 = null, t2 = null;
		if(tag1_name.compareTo("") != 0 && tag1_val.compareTo("") != 0) {
			t1 = new Tag(tag1_name,tag1_val,null);
		}
		if(tag2_name.compareTo("") != 0 && tag2_val.compareTo("") != 0) {
			t2 = new Tag(tag2_name,tag2_val,null);
		}
		ArrayList<Tag> tag_arr = new ArrayList<>();
		if(t1 != null) {
			tag_arr.add(t1);
		}
		if(t2 != null) {
			tag_arr.add(t2);
		}
			
		ArrayList<Photo> tbr = new ArrayList<Photo>();
		for(int i = 0; i < this.albums.size(); i++) {
			Album album = this.albums.get(i);
			for(int j = 0; j < album.photos.size(); j++) {
				Photo photo = album.photos.get(j);
				if(!tbr.contains(photo)) {
					if(tag_arr.isEmpty()) {
						tbr.add(photo);
						continue;
					}
					if(isAnd) {
						boolean satisfies = true;
						for(int k = 0; k < tag_arr.size(); k++) {
							if(!photo.tags.contains(tag_arr.get(k))) {
								satisfies = false;
								break;
							}
						}
						if(satisfies) {
							tbr.add(photo);
						}
					}
					else {//isOr
						for(int k = 0; k < tag_arr.size(); k++) {
							if(photo.tags.contains(tag_arr.get(k))) {
								tbr.add(photo);
								break;
							}
						}
					}
				}
			}
		}
		return tbr;
	}
}
