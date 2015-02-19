/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.util;

import java.util.logging.Logger;
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

   
    
    //@Test
    public void testStartToken() throws Exception {
	QueryString queryString = new QueryString();
	queryString.add("response_type", "token")
		.add("client_id", Start.softwareId)
		.add("display", "popup")
		.add("state", "запрос" + 2);
	String url = Token.urlForReceivingVirifCode + "?" + queryString.toString();
	ShowPage sp = new ShowPage();
	String result = sp.run(url);
	
    }

    @Test
    public void testParseUrl() {
	ShowPage.Container vm = new ShowPage.Container();
	vm.url = "http://ya.ru/"
		+ "#state=%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%812"
		+ "&access_token=d9e46143738e4489b260a790550b3fab"
		+ "&token_type=bearer"
		+ "&expires_in=31536000";
	String result = vm.getUrl();
	Logger.getLogger(this.getClass().getName()).info("result = " + result);
	Assert.assertTrue(result.equals("4444"));
    }

}
