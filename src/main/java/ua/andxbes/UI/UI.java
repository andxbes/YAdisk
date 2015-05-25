/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ua.andxbes.Synchronizer;

/**
 *
 * @author Andr
 */
public class UI extends Application{
    
    Synchronizer synchronizer;
    UIController uiController;
    @Override
    public void start(Stage primaryStage) {
	FXMLLoader fxmLoader = new FXMLLoader(getClass().getResource("/MyFXFrame.fxml"));
	uiController = new UIController();
	fxmLoader.setController(uiController);
	Parent root = null;
	try {
	   root = fxmLoader.load();
	} catch (Exception e) {
	}
	
	Scene scene = new Scene(root);
	primaryStage.setScene(scene);
	primaryStage.show();
	
        synchronizer  = new Synchronizer();
	uiController.setSynchronizer(synchronizer);
	synchronizer.addObserver(uiController);
	
    }
    
    @Override
    public void stop() throws Exception{
	synchronizer.close();
	super.stop();
    }
    
    
 
    
}
