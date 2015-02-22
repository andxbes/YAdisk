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
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
	    fileSave = "./save.txt";

    private String access_token = null;
    private long endTimeToken = 0;

    private static Token token;

    public static Token instance() {
	if (token == null) {

	    try {
		System.out.println("Грузим файл ");
		loadFieldFromFile();
		
	    } catch (FileNotFoundException ex) {
		System.out.println("Не загрузили файл");
		Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
		token = new Token();
		token.queryToken();
	    }

	}

	return token;
    }

    private  Token() {}

    /* 
     Запрос кода подтверждения

     https://oauth.yandex.ru/authorize?
     response_type=token
     & client_id=<идентификатор приложения>
     [& display=popup]
     [& state=<произвольная строка>]
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
    private void saveFieldinFile() {
	Gson gson = new Gson();
	String json = gson.toJson(this);

	System.out.println("json =" + json);

	try (FileWriter fw = new FileWriter(new File(fileSave));) {

	    fw.write(json);

	} catch (IOException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    private static void loadFieldFromFile() throws FileNotFoundException {
	StringBuilder json = new StringBuilder();
	FileReader fr = new FileReader(new File(fileSave));
	BufferedReader br = new BufferedReader(fr);

	System.out.println(fr.getEncoding());

	try {
	    while (br.ready()) {
		json.append(br.readLine());
	    }
	} catch (IOException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, "Ошибка чтения " + ex);
	} finally {

	    try {
		br.close();
	    } catch (IOException ex) {
		Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, "Ошибка закрытия потока " + ex);
	    }

	}

	Gson gson = new Gson();

	token = gson.fromJson(json.toString(), Token.class);

    }

    private void extractFieldfromMap(Map<String, String> result) throws NumberFormatException {
	String error = result.get("error");
	access_token = result.get("access_token");
	endTimeToken = (Long.parseLong(result.get("expires_in")) * 60) + Calendar.getInstance().getTimeInMillis();

	if (error != null || access_token == null && endTimeToken == 0) {
	    showDialog(error);
	}

	Logger.getLogger(Token.class.getName()).info("\naccess_token=" + access_token + "\n endTime=" + endTimeToken
		+ "\n error=" + error);
    }

    //на высиление =)) , в новой версии javaFX ,однойстрокой можно , через  Dialogs
    private void showDialog(String error) {
	Stage st = new Stage();
	st.initStyle(StageStyle.UTILITY);
	Scene scene = new Scene(new Group(new Text(25, 25, "Что-то пошло не так ! \n" + error)));
	st.setScene(scene);
	st.show();
    }

    @Override
    public String toString() {

	if (access_token == null) {
	    throw new RuntimeException("Token is null");
	}
	return access_token;

    }

}
