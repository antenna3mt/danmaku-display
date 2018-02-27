package com.solidmatrices.danmaku.display;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.io.IOException;
import java.util.List;
import java.util.*;

public class Window extends Application {
    //    private Window manager = Window.INSTANCE;
    //  private Controller controller;
    private Stage controlStage;
    private Stage displayStage;
    private Screen displayScreen;
    private GraphicsContext gc;
    
    // build the control scene
    private Scene buildControlScene(Screen screen) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window.fxml"));
        TabPane root = null;
        try {
            root = loader.load();
            Model.INSTANCE.controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return new Scene(root);
    }

    // build the display scene
    private Scene buildDisplayScene(Screen screen) {
        Rectangle2D bds = screen.getBounds();
        Canvas canvas = new Canvas(bds.getWidth(), bds.getHeight());
        canvas.setCache(true);
        canvas.setCacheHint(CacheHint.DEFAULT);
        gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(new Group(canvas), bds.getWidth(), bds.getHeight());
        scene.setFill(Color.TRANSPARENT);
        return scene;
        //scene.getRoot().getChildrenUnmodifiable().get(0)
    }


    private void updateControlStage(Stage stage, Screen screen) {
        stage.setTitle("Control Panel");
        stage.setScene(buildControlScene(screen));
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    private void updateDisplayStage(Stage stage, Screen screen) {
        Rectangle2D bds = screen.getBounds();
        displayScreen = screen;
        Danmaku.windowHeight = bds.getHeight();
        Danmaku.windowWidth = bds.getWidth();
        stage.setX(bds.getMinX());
        stage.setY(bds.getMinY());
        stage.setWidth(bds.getWidth());
        stage.setHeight(bds.getHeight());
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(buildDisplayScene(screen));
    }

    public List<String> getScreens() {
        List<String> list = new ArrayList<>();
        for (Screen s : Screen.getScreens()) {
            list.add(Integer.toString(s.hashCode()));
        }
        return list;
    }

    public void setScreen(String a) {
        for (Screen s : Screen.getScreens()) {
            if (Integer.toString(s.hashCode()).equals(a)) {
                updateDisplayStage(displayStage, s);
                displayStage.show();
            }
        }
    }


    private void setupAnimationTimer() {
        AnimationTimer atimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                Rectangle2D bds = displayScreen.getBounds();

                gc.clearRect(0, 0, bds.getWidth(), bds.getHeight());
                Iterator<Danmaku> i = Model.INSTANCE.DanmakuList.iterator();

                while (i.hasNext()) {
                    Danmaku d = i.next();
                    if (d.finished) {
                        i.remove();
                    } else {
                        gc.setFill(d.fillColor);
                        gc.setFont(d.font);
                        gc.setStroke(d.strokeColor);
                        gc.setLineWidth(d.strokeWidth);
                        gc.strokeText(d.text, d.x, d.y);
                        gc.fillText(d.text, d.x, d.y);
                    }
                }


            }
        };
        atimer.start();
    }

    private void setupFetchTimer(Integer timeInterval) {
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                Model.INSTANCE.FetchDanmaku();
            }
        }, 0, timeInterval);
    }

    public void start(Stage primaryStage) {
        // get screens
        List<Screen> screens = Screen.getScreens();

        // init stages
        controlStage = primaryStage;
        displayStage = new Stage();

        updateControlStage(controlStage, screens.get(0));
        updateDisplayStage(displayStage, screens.get(screens.size() - 1));
        controlStage.show();
        displayStage.show();

        Model.INSTANCE.window = this;
        Model.INSTANCE.controller.Sync();
        setupAnimationTimer();
        setupFetchTimer(Model.INSTANCE.FetchInterval);
    }

    public static void main(String[] args) {
        launch(args);
    }
}