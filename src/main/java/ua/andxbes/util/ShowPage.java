package ua.andxbes.util;

import java.util.List;
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

    private static volatile ConrolShowPanel cont;
    private Stage thisStage;

    @Override
    public void start(Stage stage) throws Exception {

	//System.out.println("2");

	thisStage = stage;
	stage.setTitle("url -" + cont.getUrl());
	Pane root = new WebViewPanel();
	stage.setScene(new Scene(root, 400, 500));
	stage.show();
    }

    public class WebViewPanel extends Pane {

	public WebViewPanel() {

	   //System.out.println("3");

	    setMaxWidth(Double.MAX_VALUE);
	    setMaxHeight(Double.MAX_VALUE);
	    WebView view = new WebView();
	    view.setMinSize(500, 400);
	    view.setPrefSize(500, 400);
	    final WebEngine eng = view.getEngine();
	    eng.load(cont.getUrl());

	    eng.locationProperty().addListener(new ChangeListener<String>() {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    cont.setUrl( newValue );
		    thisStage.setTitle(newValue);
		    cont.variabelMethodForChangedPage(newValue, thisStage);
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

    public static String run(ConrolShowPanel c) {
	//System.out.println("1");
	cont = c;
	
	launch();
	//System.out.println("4");

	return cont.getUrl();
    }
//============================================================================================================

    public static abstract class ConrolShowPanel {

	private String url;

	public ConrolShowPanel(String startUrl) {
	    setUrl(startUrl);
	}

	public void setUrl(String url) {
	    this.url = url;
	}

	public String getUrl() {
	    return url;
	}
	/*метод который можно позже переопределить .
	 Который вызывается во время загрузки новой страницы 
	 */

	public abstract void variabelMethodForChangedPage(String curentUrl, Stage stage);

    }
}
