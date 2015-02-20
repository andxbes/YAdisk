/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.util;

import java.util.logging.Logger;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.andxbes.Start;
import ua.andxbes.Token;

/**
 *
 * @author Andr
 */
public class ShowPageIT {

    public ShowPageIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testStartToken() throws Exception {
	QueryString queryString = new QueryString();
	queryString.add("response_type", "token")
		.add("client_id", Start.softwareId)
		.add("display", "popup")
		.add("state", "запрос" + 2);

	String url = Token.urlForReceivingToken + "?" + queryString.toString();
	ShowPage.ConrolShowPanel con = new ShowPage.ConrolShowPanel(url) {

	    @Override
	    public void variabelMethodForChangedPage(String curentUrl, Stage stage) {
		//дописать метод для закрытия окна после получения токена 
		if (curentUrl.equals("https://passport.yandex.ru/auth"
			+ "?retpath=https%3A%2F%2Foauth.yandex.ru%2Fauthorize%3Fresponse_type"
			+ "%3Dtoken%26client_id%3D45f708d998054dd29dfc73c7e33c664d%26display%3Dpopup"
			+ "%26state%3D%25D0%25B7%25D0%25B0%25D0%25BF%25D1%2580%25D0%25BE%25D1%25812")){
		
		    stage.hide();
		 
		}
	    }
	};

	String result = ShowPage.run(con);
	
	Logger.getLogger(this.getClass().getName()).info(result);
	
	Assert.assertEquals(result, "https://passport.yandex.ru/auth"
		+ "?retpath=https%3A%2F%2Foauth.yandex.ru%2Fauthorize%3Fresponse_type%3Dtoken"
		+ "%26client_id%3D45f708d998054dd29dfc73c7e33c664d%26display%3Dpopup"
		+ "%26state%3D%25D0%25B7%25D0%25B0%25D0%25BF%25D1%2580%25D0%25BE%25D1%25812");
    }

}
