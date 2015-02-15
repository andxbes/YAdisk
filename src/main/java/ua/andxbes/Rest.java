package ua.andxbes;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Andr
 */
public class Rest {
     private  final Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * Метод читает из потока данные и преобразует в строку
     *
     * @param in - входной поток
     * @param encoding - кодировка данных
     * @return - данные в виде строки
     */
    private String readStreamToString(InputStream in, String encoding) throws IOException {
	StringBuffer b = new StringBuffer();
	InputStreamReader r = new InputStreamReader(in, encoding);
	int c;
	while ((c = r.read()) != -1) {
	    b.append((char) c);
	}
	return b.toString();
    }

    public void postRest(String url) {
	
	 
        
	
	
    }

    
    
   



    
}
