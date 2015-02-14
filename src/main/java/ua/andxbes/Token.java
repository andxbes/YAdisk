package ua.andxbes;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
public final class Token {
    private static int serialNumber = 1 ;
    
    private Rest rest;
   

    private String verifCode = null; //действует 10 минут 
    private static final String urlForReceivingVirifCode = "https://oauth.yandex.ru/authorize";
    private static final String urlForRecivingToken = "oauth.yandex.ru";
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
	//exchange_confirmation_code_on_the_token(id, pas);

    }

    // заапрос кода потверждения 
    private void queryVerificationCode(String id) {
	QueryString queryString = new QueryString();
	try {
	    queryString.add("response_type", "code")
		    .add("client_id", id)
		    .add("state", "запрос" + serialNumber++);
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	
	
	brauzer(urlForReceivingVirifCode,queryString);

    }

    private void brauzer(String url ,QueryString queryString) {

	
	
	
	ShowPage sp = new ShowPage();
	//sp.start2(url+"?"+queryString);
	
	
	
	
	
	
	
    }

    //обмен кода потверждения на токен
    private void exchange_confirmation_code_on_the_token(String id, String pas) {

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

	try {
	    rest.postRest(urlForRecivingToken, queryString);
	} catch (IOException ex) {
	    Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
	}

        
    }

    @Override
    public String toString() {

	if (token == null) {
	    throw new RuntimeException("Token is null");
	}
	return token;

    }

}
