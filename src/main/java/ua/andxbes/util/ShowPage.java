package ua.andxbes.util;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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
public class ShowPage extends Application {

    public static volatile Container cont = new Container();
    private Stage thisStage;
    private WebView wv;
    private String code;
    private Stage st;

    @Override
    public void start(Stage stage) throws Exception {
	thisStage = stage;
	stage.setTitle("url -" + cont.url);
	Pane root = new WebViewPanel();
	stage.setScene(new Scene(root, 400, 500));
	stage.show();
    }

    public class WebViewPanel extends Pane {

	public WebViewPanel() {

	    //  VBox.setVgrow(this, Priority.ALWAYS);
	    setMaxWidth(Double.MAX_VALUE);
	    setMaxHeight(Double.MAX_VALUE);
	    WebView view = new WebView();
	    view.setMinSize(500, 400);
	    view.setPrefSize(500, 400);
	    final WebEngine eng = view.getEngine();
	    eng.load(cont.url);

	    eng.locationProperty().addListener(new ChangeListener<String>() {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    cont.url = newValue;
		    thisStage.setTitle(cont.url);
		    //тут же и отработать нужное дейстрвие с адресом  , а по возможности создать виртуальный метод для 
		}

	    });

	    GridPane grid = new GridPane();
	    grid.setVgap(5);
	    grid.setHgap(5);
	    GridPane.setConstraints(view, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);

	    grid.getChildren().addAll(view);
	    getChildren().add(grid);

	}

	@Override
	protected void layoutChildren() {

	    List<Node> managed = getManagedChildren();
	    double width = getWidth();
	    double height = getHeight();
	    double top = getInsets().getTop();
	    double right = getInsets().getRight();
	    double left = getInsets().getLeft();
	    double bottom = getInsets().getBottom();
	    for (int i = 0; i < managed.size(); i++) {
		Node child = managed.get(i);
		layoutInArea(child, left, top,
			width - left - right, height - top - bottom,
			0, Insets.EMPTY, true, true, HPos.CENTER, VPos.CENTER);
	    }

	}

    }

    public String run(String url) {
	ShowPage.cont.url = url;//TODO как-то не правильно 

	launch();

	//Logger.getLogger(this.getClass().getName()).info("После Launch");
	Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\nVerification code  = {0}", ShowPage.cont.getUrl());

	return cont.getUrl();
    }

    @Override
    public void init() {
	//Logger.getLogger("fx").info(cont.url);
	//Logger.getLogger(this.getClass().getName()).info("Init");
    }

    @Override
    public void stop() {
	//Logger.getLogger(this.getClass().getName()).info("Stop");

	Logger.getLogger(this.getClass().getName()).info("Stop url = " + ShowPage.cont.url);

    }

    public static class Container {

	public String url;

	public String getUrl() {
	    return url;
	}

    }
}
