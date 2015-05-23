/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Andr
 */
public class UI extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
	//Parent root = FXMLLoader.load(getClass().getResource("MyFXFrame.fxml"));
	StackPane root = new StackPane();
	Scene scene = new Scene(root);
	primaryStage.setScene(scene);
	primaryStage.show();
    }
    
}
