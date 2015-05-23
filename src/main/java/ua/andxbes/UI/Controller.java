/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author Andr
 */
public class Controller implements Initializable{

    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event){
           System.out.println("click");
	   label.setText("Hello");
    
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
	//TODO
	
    }
    
}
