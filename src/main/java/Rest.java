
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
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

    public void postRest(String url, QueryString query) throws IOException {
	//устанавливаем соединение
	URLConnection conn = new URL(url).openConnection();
	//мы будем писать POST данные в out stream
	conn.setDoOutput(true);

	log.info(" Тип соединения = " + conn.getContentType()
		+ " \n длинна тела запроса = " + conn.getContentLength());

	try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "ASCII")) {
	    out.write(query.toString());
	    out.write("\r\n");
	    out.flush();
	} catch (IOException e) {
	    Logger.getLogger(this.getClass().getName()).info("Ошибка записи в out " + e);
	}

	//читаем то, что отдал нам сервер
	String html = readStreamToString(conn.getInputStream(), "UTF-8");

	//выводим информацию в консоль
	log.log(Level.INFO, "query:{0}URL:{1}\n Html:\n{2}", new Object[]{url, html});
	
	
    }

    public void getRest(String url, QueryString query) throws IOException {
	//устанавливаем соединение
	URLConnection conn = new URL(url + "?" + query).openConnection();
	//заполним header request парамеры, можно и не заполнять
	//conn.setRequestProperty("Referer", "http://google.com/http.example.html");
	//conn.setRequestProperty("Cookie", "a=1");
		//можно установить и другие парамеры, такие как User-Agent

	//читаем то, что отдал нам сервер
	String html = readStreamToString(conn.getInputStream(), "UTF-8");

	//выводим информацию в консоль
	log.log(Level.INFO, "query : {0}\n URL:{1}\n Html:\n{2}", new Object[]{url + "?" + query , url , html});
	
	
	
    }
    
   

//	public static void main(String[] args) throws IOException {
//
//		QueryString q = new QueryString()
//			.add("login","admin")
//			.add("password", "pass");
//		
//		Rest e = new Rest();
//		e.getRest("http://juravskiy.ru/", q);
//		e.postRest("http://juravskiy.ru/", q);
//	}

    
}
