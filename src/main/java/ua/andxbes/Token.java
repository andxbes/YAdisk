package ua.andxbes;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ua.andxbes.util.QueryString;
import ua.andxbes.util.ShowPage;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    // "код" и "адрес приложения"  зарегестрированные на яндекс диске.
    public final static String SOFTWARE_ID = "45f708d998054dd29dfc73c7e33c664d",
	    CALLBACK_URL = "http://ya.ru/";

    public static final String urlForReceivingToken = "https://oauth.yandex.ru/authorize",
	    fileSave = "./save.token";

    private String access_token = null;
    private long endTimeToken = 0;

    private static Token token;

    public static Token instance() {
	if (token == null) {

	    try {
		Logger.getLogger(Token.class.getName()).log(Level.INFO, "LoadFile");
		loadFieldFromFile();

	    } catch (FileNotFoundException ex) {

		Logger.getLogger(Token.class.getName()).log(Level.INFO, "Dont load file. Query token from " + urlForReceivingToken, ex);
		token = new Token();
		token.queryToken();
	    }

	}

	return token;
    }

    private Token() {
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
	try {
	    QueryString queryString = new QueryString();
	    queryString.add("response_type", "token")
		    .add("client_id", SOFTWARE_ID)
		    .add("display", "popup")
		    .add("state", "((: give me token :))");
	    String url = Token.urlForReceivingToken + "?" + queryString.toString();

	    String endUrl = ShowPage.run(new ShowPage.ConrolShowPanel(url) {

		@Override
		public void variabelMethodForChangedPage(String curentUrl, Stage stage) {
		    if (CALLBACK_URL.equals(curentUrl.split("#")[0])) {
			stage.close();
		    }
		}
	    });

	    Map<String, String> result = queryString.parseURL(endUrl);

	    extractFieldfromMap(result);
	    saveFieldinFile();

	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    private void saveFieldinFile() {
	Gson gson = new Gson();
	String json = gson.toJson(this);

	try (FileWriter fw = new FileWriter(new File(fileSave));) {

	    fw.write( SecuritySettings.encrypt(json) );

	} catch (IOException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    private static void loadFieldFromFile() throws FileNotFoundException {
	StringBuilder json = new StringBuilder();
	FileReader fr = new FileReader(new File(fileSave));
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
	    token = gson.fromJson(SecuritySettings.decrypt(json.toString()), Token.class);
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

	Logger.getLogger(Token.class.getName()).log(Level.INFO, "endToken - {0}", new SimpleDateFormat("dd.MM.yyyy").format(new Date(endTimeToken)));
	return access_token;

    }

}
