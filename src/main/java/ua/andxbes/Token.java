package ua.andxbes;

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

    private static int serialNumber = 1;

    public static final String urlForReceivingToken = "https://oauth.yandex.ru/authorize";

    private String access_token = null;
    private long endTimeToken = 0;

    public Token(String id) {

	this();
	queryToken(id);
    }

    Token() {
	//bla bla =) 
    }

    /* 
     Запрос кода подтверждения

     https://oauth.yandex.ru/authorize?
     response_type=token
     & client_id=<идентификатор приложения>
     [& display=popup]
     [& state=<произвольная строка>]
     */
    private void queryToken(String softwareId) {
	try {
	    QueryString queryString = new QueryString();
	    queryString.add("response_type", "token")
		    .add("client_id", softwareId)
		    .add("display", "popup")
		    .add("state", "((: give me token :))");
	    String url = Token.urlForReceivingToken + "?" + queryString.toString();
	    String endUrl = ShowPage.run(new ShowPage.ConrolShowPanel(url) {

		@Override
		public void variabelMethodForChangedPage(String curentUrl, Stage stage) {
		    if ("http://ya.ru/".equals(curentUrl.split("#")[0])) {
			stage.close();
		    }
		}
	    });

	    Map<String, String> result = queryString.parseURL(endUrl);

	    extractFieldfromMap(result);
	    
	    

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
    
    private void saveFieldinFile(){
    
    
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

    private void showDialog(String error) {
	Stage st = new Stage();
	st.initStyle(StageStyle.UTILITY);
	Scene scene = new Scene(new Group(new Text(25, 25, "Что-то пошло не так ! \n" + error)));
	st.setScene(scene);
	st.show();
    }
    
    @Override
    public String toString() {

	if (getAccess_token() == null) {
	    throw new RuntimeException("Token is null");
	}
	return getAccess_token();

    }

    /**
     * @return the access_token
     */
    public String getAccess_token() {
	return access_token;
    }

    /**
     * @param access_token the access_token to set
     */
    public void setAccess_token(String access_token) {
	this.access_token = access_token;
    }

}
