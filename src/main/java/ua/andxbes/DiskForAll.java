/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.io.FileNotFoundException;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.Map;
import ua.andxbes.DiskJsonObjects.Resource;

/**
 *
 * @author Andr
 */
public interface DiskForAll {
    
    public Map<String, List<Resource>> getResource();
    public ReadableByteChannel read(String path)throws FileNotFoundException;
    public void write(String path, ReadableByteChannel i);
    public void deleteFolderOrFile(String path);
}
