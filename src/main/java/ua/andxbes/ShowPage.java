package ua.andxbes;



import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andr
 */
public class ShowPage extends Application{
    public  static  String url = "empty";
    private WebView wv;
    private Stage st;

    public ShowPage() {
    }
    

    @Override
    public void start(Stage stage) throws Exception {
	stage.setTitle("url -"+ url);
	
	
	
	
	Group group = new Group();
	group.autosize();
        Scene scene = new Scene(group,500,600,Color.LIGHTGREEN);
        stage.setScene(scene);
        stage.show();
         

        final WebView browser = new WebView();
	 browser.setMinSize(500, 400);
	 browser.setPrefSize(500, 400);
	
        final WebEngine webEngine = browser.getEngine();
        wv = browser;
        webEngine.load(url);
        group.getChildren().add(browser);
        
        

        
	
	
	
    }
    public void start2(String url){
	ShowPage.url = url;//TODO как-то не правельно 
        launch( );
    }
    @Override
    public void init(){
	Logger.getLogger("fx").info(url);
    }
    
   
  
    
    
}
