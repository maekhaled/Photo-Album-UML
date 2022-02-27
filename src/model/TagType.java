package model;

import java.io.Serializable;

public class TagType implements Serializable{

	/**
	 * the serial version UID for storing data
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * name of the tag type
	 */
	public String name;
	/**
	 * multiplicity of the tag type (either single or multiple)
	 */
	public String multiplicity;
	
	/**
	 * the constructor
	 * 
	 * @param name	the tag name
	 * @param multiplicity	the multiplicity
	 */
	private TagType(String name, String multiplicity) {
		this.name = name;
		this.multiplicity = multiplicity;
	}
	/**
	 * Creates new tag type with the specified name and multiplicity
	 * 
	 * @param name		tag name
	 * @param multiplicity	tag multiplicity
	 * @return	the new TagType instance
	 */
	public static TagType createTagType(String name, String multiplicity) {
		if(multiplicity == null) {
			return null;
		}
		if(multiplicity.compareTo("single") == 0 || multiplicity.compareTo("multiple") == 0) {
			return new TagType(name,multiplicity);
		}
		return null;
	}

}
