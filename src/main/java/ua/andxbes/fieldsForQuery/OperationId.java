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
public class OperationId extends Field{

    private String field;
    public OperationId(String value) {
        field = value; 
	this.nameField = "operation_id";
	
    }
    

    @Override
    public String getField() {
         return field;	
    }
    
}
