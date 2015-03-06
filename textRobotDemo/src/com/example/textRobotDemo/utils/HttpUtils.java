package com.example.textRobotDemo.utils;

import com.example.textRobotDemo.bean.ChatMessage;
import com.example.textRobotDemo.bean.Result;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * USER: liulei
 * DATA: 2015/1/28
 * TIME: 10:01
 */
@SuppressWarnings("FieldCanBeLocal")
public class HttpUtils {

    //申请的apiKey
    private static String API_KEY = "147ea51ac26efb17ab4ffa93ace3fc67";
    private static String REQUEST_URL = "http://www.tuling123.com/openapi/api";

    /**
     * 发送一个消息，并得到返回的消息
     *
     * @param msg json数据
     * @return {#link ChatMessage}
     */
    public static ChatMessage sendMsg(String msg) {
        ChatMessage message = new ChatMessage();
        String url = setParams(msg);
        String res = doGet(url);
        Gson gson = new Gson();
        Result result = gson.fromJson(res, Result.class);
        if (result.getCode() > 400000 || result.getText() == null
                || result.getText().trim().equals("")) {
            message.setMsg("该功能等待开发...");
        } else {
            message.setMsg(result.getText());
        }
        message.setType(ChatMessage.Type.INPUT);
        message.setDate(new Date());
        return message;
    }

    /**
     * 拼接Url
     *
     * @param msg 请求参数
     * @return URL
     */
    private static String setParams(String msg) {
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return REQUEST_URL + "?key=" + API_KEY + "&info=" + msg;
    }

    /**
     * Get请求，获得返回数据
     *
     * @param urlStr 链接
     * @return jsonStr
     */
    private static String doGet(String urlStr) {
        URL url;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len;
                byte[] buf = new byte[128];
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (baos != null)
                    baos.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
