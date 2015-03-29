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
public class Type extends Field{
    public static  final String FILE="file",
	    DIR="dir";
    private String type;

    public Type(String type) {
	this.type = type;
	super.nameField = "type";
    }
    
    
    @Override
    public String getField() {
          return type;	
    }
    
}
