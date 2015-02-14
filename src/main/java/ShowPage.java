

import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
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
	stage.setTitle("HTML");
        stage.setWidth(600);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());

        VBox root = new VBox();     

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        wv = browser;
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
        webEngine.load(url);

        root.getChildren().addAll(scrollPane);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
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
