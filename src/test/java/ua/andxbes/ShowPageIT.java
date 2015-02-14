package ua.andxbes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    /**
     * Test of start method, of class ShowPage.
     */
    @Test
    public void testStart() throws Exception {


	    QueryString queryString = new QueryString();
	                queryString.add("response_type", "code")
		    .add("client_id", TokenIT.id)
		    .add("state", "запрос" + 1);
	
            String url  =  Token.urlForReceivingVirifCode+"?"+queryString.toString() ;
	

	
	    ShowPage sp = new ShowPage();
	    String result = sp.run(url);
	    Logger.getLogger(this.getClass().getName()).info("result = "+ result);
	    Assert.assertTrue(result != null);
    }
    
    @Test
    public void testParseUrl(){
            ShowPage.Container vm = new ShowPage.Container();
	    vm.url = "http://www.example.com/token?code=4444&state=значение";
	    String result = vm.resultParser();
	    
	    Logger.getLogger(this.getClass().getName()).info("result = " + result);
	    
	    Assert.assertTrue(result.equals("4444"));
    
    
    }

}
