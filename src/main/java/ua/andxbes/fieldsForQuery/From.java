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
public class From extends Field {

    private String field;

    public From(String value) {
	nameField = "from";
	field = value;
    }

    @Override
    public String getField() {
	return field;
    }

}
