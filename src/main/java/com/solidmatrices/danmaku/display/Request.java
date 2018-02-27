package com.solidmatrices.danmaku.display;

import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;

public class Request {
    public static String getHttpResponse(String url, String method, String data) throws Exception {
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod(method);

        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        out.write(data.getBytes());
        out.close();

        int code = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }

    public static String BuildJsonRpcRequest(String method, Map<String, String> dict) {
        JSONObject params = new JSONObject();
        dict.forEach(params::put);

        JSONObject obj = new JSONObject();
        obj.put("jsonrpc", "2.0");
        obj.put("method", method);
        obj.put("id", 1);
        obj.put("params", params);
        return obj.toString();
    }

    public static Map<String, String> BuildMap(String... e) {
        Map<String, String> dict = new HashMap<>();
        for (int i = 0; i < e.length; i += 2) {
            dict.put(e[i], e[i + 1]);
        }
        return dict;
    }

    public static JSONObject JsonRpcCall(String url, String data) throws Exception {

        String resp = getHttpResponse(url, "POST", data);

        JSONObject obj = new JSONObject(resp);
        if (!obj.has("result")) {
            throw new RuntimeException("json rpc error returned");
        }
        return obj.getJSONObject("result");
    }

    public static boolean Login(String url, String token) {
        String data = BuildJsonRpcRequest("DanmakuService.Login", BuildMap("token", token));
        try {
            JSONObject res = JsonRpcCall(url, data);
            if (res.getString("type").equals("display")) return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static List<Danmaku> Fetch(String url, String token) {
        List<Danmaku> list = new LinkedList<>();
        String data = BuildJsonRpcRequest("DanmakuService.Display", BuildMap("token", token));
        try {
            JSONObject res = JsonRpcCall(url, data);
            if (!res.has("comments")) return list;
            JSONArray comments = res.getJSONArray("comments");
            for (int i = 0; i < comments.length(); i++) {
                JSONObject comment = comments.getJSONObject(i);
                String content = comment.getString("content");
                JSONObject attr = comment.getJSONObject("attributes");
                String color = attr.getString("color");
                list.add(new Danmaku(content, Color.web(color)));
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }
}
