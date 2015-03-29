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
public class SystemFolders {
    private String applications,
	   downloads;

    

    @Override
    public String toString() {
	return "SystemFolders{" + "applications=" + getApplications() + ", downloads=" + getDownloads() + '}';
    }

    /**
     * @return the applications  <Путь к папке "Приложения".>,
     */
    public String getApplications() {
	return applications;
    }

    /**
     * @return the downloads <Путь к папке "Загрузки".>
     */
    public String getDownloads() {
	return downloads;
    }
   
    
}
