package ua.andxbes;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ua.andxbes.util.QueryString;
import ua.andxbes.UI.ShowPage;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import ua.andxbes.util.SecuritySettings;

/*

 */
/**
 *
 * @author Andr
 */
public final class Token {
   

    private final  Logger log = Logger.getLogger(this.getClass().getName());

    // "код" и "адрес приложения"  зарегестрированные на яндекс диске.
    public final static String SOFTWARE_ID = "45f708d998054dd29dfc73c7e33c664d",
	    CALLBACK_URL = "http://ya.ru/";

    public final static  String urlForReceivingToken = "https://oauth.yandex.ru/authorize",
	    NAME_FILE = "./save.token";

    private volatile static  String access_token = null;
    private long endTimeToken = 0;

    

    public static Token getinstance() {
	return ConToken.TOKEN;
    }

   
    private static class ConToken{
        public static final Token TOKEN = new Token();
    }

    public Token() {
	    try {
		Logger.getLogger(Token.class.getName()).log(Level.INFO, "LoadFile");
		loadFieldFromFile();

	    } catch (FileNotFoundException ex) {
		Logger.getLogger(Token.class.getName()).log(Level.INFO, "Dont load file. Query token from " + urlForReceivingToken, ex);
		queryToken();
	    }

	
    }
    

    /* 
     Запрос кода подтверждения

     https://oauth.yandex.ru/authorize?
     response_type=token
     & client_id=<идентификатор приложения>
     [& display=popup]
     [& state=<произвольная строка>]
     */
    /*
     Ответ OAuth-сервера
     myapp://token#
     access_token=<новый OAuth-токен>
     & expires_in=<время жизни токена в секундах>
     [& state=<значение параметра state, переданное в запросе>]
    
     В случае ошибки 
     myapp://token#
     state=<значение параметра state в запросе>
     & error=<код ошибки>

     Возможные коды ошибок:

     access_denied — пользователь отказал приложению в доступе.
     unauthorized_client — приложение было отклонено при модерации или только ожидает ее. Также возвращается, если приложение заблокировано.

    
     */
    private void queryToken() {

	QueryString queryString = new QueryString();
	queryString.add("response_type", "token")
		.add("client_id", SOFTWARE_ID)
		.add("display", "popup")
		.add("state", "((: give me token :))");
	String url = Token.urlForReceivingToken + "?" + queryString.toString();

	 ShowPage.run(new ShowPage.ConrolShowPanel(url) {

	    @Override
	    public void variabelMethodForChangedPage(String curentUrl, Stage stage) {
		System.out.println("in variable methods " + curentUrl);
		if (CALLBACK_URL.equals(curentUrl.split("#")[0])) {
		    
		    System.out.println("парсим " + curentUrl);
		    Map<String, String> result = queryString.parseURL(curentUrl);
		    System.out.println("отпарсили");
		    extractFieldfromMap(result);
		    System.out.println("сохраняем ");
		    saveFieldinFile();
		    stage.close();
		}
	    }
	});

    }

    private void saveFieldinFile() {
	Gson gson = new Gson();
	String json = gson.toJson(access_token);
	System.out.println("save file" + NAME_FILE);
	try (FileWriter fw = new FileWriter(new File(NAME_FILE));) {
	    fw.write(SecuritySettings.encrypt(json));

	} catch (IOException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    private static void loadFieldFromFile() throws FileNotFoundException {
	StringBuilder json = new StringBuilder();
	FileReader fr = new FileReader(new File(NAME_FILE));
	BufferedReader br = new BufferedReader(fr);

	try {
	    while (br.ready()) {
		json.append(br.readLine());
	    }
	} catch (IOException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	} finally {

	    try {
		br.close();
	    } catch (IOException ex) {
		Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	Gson gson = new Gson();
	try {
	    access_token = gson.fromJson(SecuritySettings.decrypt(json.toString()), String.class);
	} catch (IOException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    private void extractFieldfromMap(Map<String, String> result) throws NumberFormatException {
	String error = result.get("error");
	access_token = result.get("access_token");
	endTimeToken = System.currentTimeMillis() + (Long.parseLong(result.get("expires_in")) * 1000);

	if (error != null || access_token == null && endTimeToken == 0) {
	    showDialog(error);
	}
    }

    private void showDialog(String error) {
	JOptionPane.showMessageDialog(null, error);
    }

    @Override
    public String toString() {

	if (access_token == null) {
	    throw new RuntimeException("Token is null");
	}
	//Logger.getLogger(Token.class.getName()).log(Level.INFO, "endToken - {0}", new SimpleDateFormat("dd.MM.yyyy").format(new Date(endTimeToken)));
	return access_token;

    }

}
