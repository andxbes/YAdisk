/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Andr
 */
public class UIController implements Initializable {

    @FXML
    ProgressIndicator progressBar;

    //get path to save files
    @FXML
    TextField textFieldPathToLocalFolder;
    //watch to the status operation  
    @FXML
    private Label textFieldOperation;

    public void setStatusLebel(String status) {
	System.out.println(status);
	textFieldOperation.setText(status);

    }

    public void setProgressBar(String status) {
	System.out.println(status);
	textFieldOperation.setText(status);
    }

    @FXML
    private void enterTextFieldPath(KeyEvent event) {
	//TODO
	if (event.getCode() == KeyCode.ENTER) {
	    JOptionPane.showMessageDialog(null, textFieldPathToLocalFolder.getText());
	}
    }

    @FXML
    private void clickStatusBar(Event event) {
	//TODO
	JOptionPane.showMessageDialog(null, " event " + event.toString() + "\n" + event.getEventType());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	//TODO
//	progressBar.setProgress(0.5);
//	textFieldPathToLocalFolder.setText("OLOLOLOLO");
//	textFieldOperation.setText("TROLOLO");

    }

}
