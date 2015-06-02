/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andr
 */
public class TokenIT extends Application {

    private Token instance;
    
    public TokenIT() {
	//instance = Token.getinstance();
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

//    @Test
//    public void getToken(){
//          Assert.assertTrue(getinstance.toString(), getinstance != null);
//    }
    @Test
    public void token() {
	
	Application.launch(this.getClass());
        
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
	primaryStage.setScene(new Scene(new Pane(), 400, 400));
        primaryStage.show();
	Thread t  = new Thread(()->{
	Token inToken = Token.getinstance();
	    System.out.println(inToken.toString());
	});
	t.start();
	t.join();
	primaryStage.close();
	
    }
    
}
