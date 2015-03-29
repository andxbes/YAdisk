/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.fieldsForQuery;

/**
 *
 * @author Andr
 * Фильтр по типам ресурсов
 */
public class MediaType extends Field {

     //TODO НЕ ВСЕ ТИПЫ 
    public static final String COMPRESSED = "compressed",
	    DOCUMENT = "document",
	    UNKNOWN="unknown",
	    DEVELOPMENT = "development",
	    TEXT="text";
    
   
    private String mediaType;

    public MediaType(String mediaType) {
	this.mediaType = mediaType;
	super.nameField = "media_type";
    }

    @Override
    public String getField() {
	return mediaType;
    }

}
