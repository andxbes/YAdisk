/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andr
 */
public class QueryStringIT {

    private Logger log;

    public QueryStringIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	log = Logger.getLogger(this.getClass().getName());

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class QueryString.
     */
    @Test
    public void testAdd() throws Exception {
	System.out.println("add");

	QueryString instance = new QueryString();
	String expResult = "2=2&3=3";
	QueryString result = instance.add("2", "2").add("3", "3");

	log.info(expResult + " =  " + result.toString());

	Assert.assertTrue(expResult.equals(result.toString()));

    }

    /**
     * Test of parseURL method, of class QueryString.
     */
    @Test
    public void testParseURL() {
	System.out.println("parseURL");
	String str = "http://ya.ru/"
		+ "#state=%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%812"
		+ "&access_token=d9e46143738e4489b260a790550b3fab"
		+ "&token_type=bearer"
		+ "&expires_in=31536000";
	Map<String, String> expREsult = new TreeMap<>();
	expREsult.put("state", "%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%812");
	expREsult.put("access_token", "d9e46143738e4489b260a790550b3fab");
	expREsult.put("token_type", "bearer");
	expREsult.put("expires_in", "31536000");
	QueryString qs = new QueryString();
	Map<String, String> result = qs.parseURL(str);

	log.log(Level.INFO, "\n{0}\n = \n{1}", new Object[]{expREsult.toString(), result.toString()});

	Assert.assertEquals(expREsult.toString(), result.toString());
    }

    /**
     * Test of toString method, of class QueryString.
     */
    @Test
    public void testToString() {
	System.out.println("toString");
	QueryString instance = new QueryString();
	String expResult = "";
	String result = instance.toString();
	log.info(result.isEmpty()?"\"\"":result);
	Assert.assertEquals(expResult, result);

    }

}
