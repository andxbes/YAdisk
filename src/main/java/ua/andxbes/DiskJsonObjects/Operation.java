/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.DiskJsonObjects;

/**
 *
 * @author andr
 * <b>Статус операции. Операции запускаются, когда вы копируете, перемещаете или
 * удаляете непустые папки. URL для запроса статуса возвращается в ответ на
 * такие запросы.</b>
 */
public class Operation {

    public static final String OK = "success";
    private String status;

    @Override
    public String toString() {
	return "Operation{" + "status=" + getStatus() + '}';
    }

    /**
     * @return the status
     */
    public String getStatus() {
	return status;
    }

}
