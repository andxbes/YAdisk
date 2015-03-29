/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.fieldsForQuery;

/**
 *
 * @author Andr
 */
public class Fields extends Field{
     private String fields;
   
    public Fields(String fields) {
	this.fields = fields;
        super.nameField = "fields";
    }

    @Override
    public String getField() {
	return fields;
    
    }
}
