/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.DiskJsonObjects;

/**
 *
 * @author Andr
 */
public class Error {
    private String message
	    ,description
	    ,error;

    

    @Override
    public String toString() {
	return "Error{" + "message=" + getMessage() + ", description=" + getDescription() + ", error=" + getError() + '}';
    }

    /**
     * @return the massage
     */
    public String getMessage() {
	return message;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return the error
     */
    public String getError() {
	return error;
    }

   
    
    
    
    
}
