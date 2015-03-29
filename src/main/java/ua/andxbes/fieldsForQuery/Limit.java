/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.fieldsForQuery;

/**
 *
 * @author Andr
 * Количество выводимых вложенных ресурсов.
 */
public class Limit extends Field{

    private long limit;

    public Limit(long limit) {
	this.limit = limit;
        super.nameField="limit";
    }
    
    
    
    @Override
    public String getField() {
	return String.valueOf(limit);
    }
    
}
