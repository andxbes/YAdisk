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
public class Disk {

  private   int trash_size,
	        total_space,
	        used_space;

   private  SystemFolders system_folders;

    /**
     * @return the trash_size
     */
    public int getTrash_size() {
	return trash_size;
    }

    /**
     * @param trash_size the trash_size to set
     */
    public void setTrash_size(int trash_size) {
	this.trash_size = trash_size;
    }

    /**
     * @return the total_space
     */
    public int getTotal_space() {
	return total_space;
    }

    /**
     * @param total_space the total_space to set
     */
    public void setTotal_space(int total_space) {
	this.total_space = total_space;
    }

    /**
     * @return the used_space
     */
    public int getUsed_space() {
	return used_space;
    }

    /**
     * @param used_space the used_space to set
     */
    public void setUsed_space(int used_space) {
	this.used_space = used_space;
    }

    /**
     * @return the system_folders
     */
    public SystemFolders getSystem_folders() {
	return system_folders;
    }

    /**
     * @param system_folders the system_folders to set
     */
    public void setSystem_folders(SystemFolders system_folders) {
	this.system_folders = system_folders;
    }

}
