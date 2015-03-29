/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.fieldsForQuery;

/**
 *
 * @author Andr
 * 
 */
public class URL extends Field{
    String url;

    public URL(String url ) {
        this.url=url;
	super.nameField = "url";
    }
    
    @Override
    public String getField() {
	return url;
    }
    
}
