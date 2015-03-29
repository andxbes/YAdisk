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

  private   long trash_size,
	        total_space,
	        used_space;

   private  SystemFolders system_folders;

   

    @Override
    public String toString() {
	return "Disk{" + "trash_size=" + getTrash_size() + ", total_space=" + getTotal_space() + ", used_space=" + getUsed_space() + ", system_folders=" + getSystem_folders() + '}';
    }

    /**
     * @return the trash_size <Общий размер файлов в Корзине (байт). Входит в used_space.>
     */
    public long getTrash_size() {
	return trash_size;
    }

    /**
     * @return the total_space <Общий объем диска (байт)>
     */
    public long getTotal_space() {
	return total_space;
    }

    /**
     * @return the used_space  <Используемый объем диска (байт)>
     */
    public long getUsed_space() {
	return used_space;
    }

    /**
     * @return the system_folders <Адреса системных папок в Диске пользователя.>
     */
    public SystemFolders getSystem_folders() {
	return system_folders;
    }


    
    

}
