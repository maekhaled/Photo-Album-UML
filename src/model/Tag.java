package model;

import java.io.Serializable;

/**
 * The Tag class contains information about a tag, including tag name and value.
 * 
 * @author Seok Yim, Mae Khaled
 *
 */
public class Tag implements Serializable{
	/**
	 * the serial version UID for storing data
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the name of the tag
	 */
	public String name;
	/**
	 * the value of the tag
	 */
	public String value;
	/**
	 * the photo that has this Tag instance
	 */
	public Photo photo;
	
	/**
	 * the constructor 
	 * 
	 * @param name	name of tag
	 * @param value	value of tag
	 * @param photo	photo that has this tag
	 */
	public Tag(String name, String value, Photo photo) {
		this.name = name;
		this.value = value;
		this.photo = photo;
	}
	
	/**
	 * Getter for name
	 * @return	tag name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Getter for value
	 * @return	tag value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * the toString method for Tag
	 * @return the String version for this tag
	 */
	public String toString() {
		return this.name + ": " + this.value;
	}
	/**
	 * the equals method for Tag
	 * @return true if equal, false otherwise
	 */
	public boolean equals(Object o) {
		if(o != null && o instanceof Tag) {
			Tag tag = (Tag)o;
			if(this.name == null && tag.name != null) {
				return false;
			}
			if(this.name != null && tag.name == null) {
				return false;
			}
			if(this.value == null && tag.value != null) {
				return false;
			}
			if(this.value != null && tag.value == null) {
				return false;
			}
			return this.name.compareTo(tag.name) == 0 && this.value.compareTo(tag.value) == 0;
		}
		return false;
	}
}
