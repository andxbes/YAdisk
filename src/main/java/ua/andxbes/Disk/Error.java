/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.Disk;

/**
 *
 * @author Andr
 */
public class Error {
    private String massage
	    ,description
	    ,error;

    /**
     * @return the massage
     */
    public String getMassage() {
	return massage;
    }

    /**
     * @param massage the massage to set
     */
    public void setMassage(String massage) {
	this.massage = massage;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @return the error
     */
    public String getError() {
	return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
	this.error = error;
    }

    @Override
    public String toString() {
	return "\nmassage = ".concat(massage).concat("\ndescription = ")
		.concat(description).concat("\nerror = ").concat(error);
    }
    
    
    
    
}
