package com.solidmatrices.danmaku.display;

import javafx.animation.*;
import javafx.event.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.util.Random;


public class Danmaku {
    public Font font;
    public Color fillColor;
    public Color strokeColor;
    public Integer strokeWidth;
    public String text;
    public Double duration;
    public Transition transition;

    public double x;
    public double rx;
    public double y;
    public Boolean finished;
    public double width;
    public double height;
    public static double windowWidth;
    public static double windowHeight;


    public static Color defaultFillColor = Color.WHITE;
    public static Color defaultStrokeColor = Color.BLACK;
    public static Integer defaultStrokeWidth = 3;
    public static Random ron = new Random();

    public static Font getDefaultFont(double size) {
        return Font.font("Microsoft Yahei", FontWeight.BOLD, size);
    }


    public static double getTextWidth(String text, Font font, Integer strokeWidth) {
        Text node = new Text(text);
        node.setFont(font);
        node.setStrokeWidth(strokeWidth);
        return node.getLayoutBounds().getWidth();
    }

    public static double getTextHeight(String text, Font font, Integer strokeWidth) {
        Text node = new Text(text);
        node.setFont(font);
        node.setStrokeWidth(strokeWidth);
        return node.getLayoutBounds().getHeight();
    }


    public Danmaku(String text, Font font, Color fillColor, Color strokeColor, Integer strokeWidth, Double duration) {
        this.text = text;
        this.font = font;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.duration = duration;
        this.finished = false;

        this.width = getTextWidth(text, font, strokeWidth);
        this.height = getTextHeight(text, font, strokeWidth);
        this.rx = ron.nextDouble() * 200;
        this.x = windowWidth + this.rx;
        this.y = (ron.nextDouble()) * (windowHeight - this.height) + this.height / 2;

        final Danmaku self = this;

        this.transition = new Transition() {
            {
                setCycleCount(1);
                setCycleDuration(Duration.seconds(Model.INSTANCE.Duration));
                setAutoReverse(false);
                setInterpolator(Interpolator.LINEAR);
                setOnFinished(event -> self.finished = true);
            }

            @Override
            protected void interpolate(double frac) {
                self.x = self.rx + windowWidth - frac * (self.rx + windowWidth + self.width);
            }
        };
    }

    public Danmaku(String text, Color color) {
        this(text, getDefaultFont(Model.INSTANCE.FontSize), color, defaultStrokeColor, defaultStrokeWidth, Model.INSTANCE.Duration);
    }

    public Danmaku(String text) {
        this(text, defaultFillColor);
    }


    public void play() {
        this.transition.play();
    }
}