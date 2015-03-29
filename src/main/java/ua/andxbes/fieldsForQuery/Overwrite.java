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
public class Overwrite extends Field{
    private boolean overwrite;

  public Overwrite(boolean overwrite) {
	this.overwrite = overwrite;
        super.nameField = "overwrite";
    }
    

 

    @Override
    public String getField() {
	return String.valueOf(overwrite);
    }
    
}
