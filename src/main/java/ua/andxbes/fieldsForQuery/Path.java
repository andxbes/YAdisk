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
public class Path extends Field {
    private final String path;
   
    public Path(String path) {
	this.path = path;
        super.nameField = "path";
    }

    @Override
    public String getField() {
	return path.replace("\\", "/");
    }
 
}
