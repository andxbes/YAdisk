/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.UI;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import ua.andxbes.Synchronizer;

/**
 *
 * @author Andr
 */
public class UIController implements Initializable, Observer {

    private Synchronizer synchronizer;

    @FXML
    private volatile ProgressIndicator progressBar;

    //get path to save files
    @FXML
    private volatile TextField textFieldPathToLocalFolder;
    //watch to the status operation  
    @FXML
    private volatile Label textFieldOperation;

    public void setStatusLebel(String status) {
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
	progressBar.setProgress(0);

	new Thread(new Runnable() {

	    public void run() {
		synchronizer.sync();
	    }
	}).start();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public synchronized void update(Observable o, Object arg) {
	//TODO	получать обрабатываемый документ 
	Platform.runLater(new Runnable() {

	    public void run() {
		Double d = (Double) arg;
		Synchronizer s = (Synchronizer) o;
		
		progressBar.setProgress(d);
		textFieldOperation.setText(s.getStatus());
	    }
	});
    }

    /**
     * @param synchronizer the synchronizer to set
     */
    public void setSynchronizer(Synchronizer synchronizer) {
	this.synchronizer = synchronizer;
    }

}
