package ua.andxbes;

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

    private Rest rest;

    private String verifCode = null; //действует 10 минут 
    public static final String urlForReceivingVirifCode = "https://oauth.yandex.ru/authorize";
    public static final String urlForRecivingToken = "https://oauth.yandex.ru";
    private String token = null;

    public Token(String id, String password) {
	this();
	connect(id, password);
    }

    Token() {
	rest = new Rest();

    }

    public void connect(String id, String pas) {
	queryVerificationCode(id);
	exchange_confirmation_code_on_the_token(id, pas);

    }

    /* 
     Запрос кода подтверждения

     https://oauth.yandex.ru/authorize?
     response_type=code
     & client_id=<идентификатор приложения>
     [& state=<произвольная строка>]
     */
    private void queryVerificationCode(String id) {
	QueryString queryString = new QueryString();
	try {
	    queryString.add("response_type", "code")
		    .add("client_id", id)
		    .add("state", "запрос" + serialNumber++);
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}

	ShowPage sp = new ShowPage();
	verifCode = sp.run(urlForReceivingVirifCode + "?" + queryString);

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
    private void exchange_confirmation_code_on_the_token(String id, String pas) {

	//TODO Остановился тут 
	QueryString queryString = new QueryString();
	if (verifCode == null) {
	    throw new RuntimeException("Verification code is null");
	}

	try {
	    queryString.add("grant_type", "authorization_code")
		    .add("code", verifCode)
		    .add("client_id", id)
		    .add("client_secret", pas);
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}

	rest.postRest(urlForRecivingToken);

    }

    @Override
    public String toString() {

	if (token == null) {
	    throw new RuntimeException("Token is null");
	}
	return token;

    }

}
