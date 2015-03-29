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
public abstract class Field {

    protected String nameField;

    /**
     *
     * @return name field 
     */
    public String getNameField() {
	return nameField;
    }
     /**
     * @return the field
     */
    public abstract String getField();
    
    @Override
    public String toString() {
	return nameField + "=" + getField();
    }
    
}
