package ua.andxbes;

import ua.andxbes.util.QueryString;
import ua.andxbes.util.ShowPage;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*

 */
/**
 *
 * @author Andr
 */
public final class Token {

    private static int serialNumber = 1;


    private String verifCode = null; //действует 10 минут 
    public static final String urlForReceivingVirifCode = "https://oauth.yandex.ru/authorize";
    public static final String urlForRecivingToken = "oauth.yandex.ru";
    private String token = null;

    public Token(String id) {
	this();
	connect(id);
    }

    Token() {
	

    }

    public void connect(String id) {
	queryVerificationCode(id);
	

    }

    /* 
     Запрос кода подтверждения

     https://oauth.yandex.ru/authorize?
     response_type=code
     & client_id=<идентификатор приложения>
     [& state=<произвольная строка>]
     */
    private void queryVerificationCode(String id) {
	try {
	    QueryString queryString = new QueryString();
	    queryString.add("response_type", "token")
		    .add("client_id", Start.softwareId)
		    .add("display", "popup")
		    .add("state", "запрос" + "((: Дайте токен :))");
	    String url = Token.urlForReceivingVirifCode + "?" + queryString.toString();
	    ShowPage sp = new ShowPage();
	    String result = sp.run(url);
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /*
     Обмен кода подтверждения на токен
    
     POST /token HTTP/1.1
     Host: oauth.yandex.ru
     Content-type: application/x-www-form-urlencoded
     Content-Length: <длина тела запроса>

     grant_type=authorization_code
     & code=<код подтверждения>
     & client_id=<идентификатор приложения>
     & client_secret=<пароль приложения>
     */
    

    @Override
    public String toString() {

	if (token == null) {
	    throw new RuntimeException("Token is null");
	}
	return token;

    }

}
