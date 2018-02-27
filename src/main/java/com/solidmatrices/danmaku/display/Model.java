package com.solidmatrices.danmaku.display;

import java.util.*;
import java.util.concurrent.TimeUnit;

public enum Model {
    INSTANCE;

    Window window;
    Controller controller;

    String ServerUrl = "http://danmaku.solidmatrices.com/api";
    String Token;
    boolean LoggedIn = false;
    boolean Fetching = false;
    double FontSize = 40;
    double Duration = 10;
    Integer FetchInterval = 2000;
    List<Danmaku> DanmakuList = new Vector<Danmaku>();

    public boolean connect(String url, String token) {
        if (Request.Login(url, token)) {
            ServerUrl = url;
            Token = token;
            LoggedIn = true;
            return true;
        } else {
            return false;
        }
    }

    public void FetchDanmaku() {
        if (LoggedIn && Fetching) {
            try {
                List<Danmaku> list = Request.Fetch(ServerUrl, Token);
                list.forEach(this::AddDanmaku);
            } catch (Exception e) {
                System.out.print("Exception: " + e.toString());
            }
        }
    }

    public void AddDanmaku(Danmaku d) {
        DanmakuList.add(d);
        d.play();
    }

    public void ClearDanmaku() {
        DanmakuList.clear();
    }
}
