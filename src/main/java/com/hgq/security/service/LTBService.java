package com.hgq.security.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author houguangqiang
 * @date 2019-01-17
 * @since 1.0
 */
public class LTBService {

    private static final String INVITE_CODE = "0bBocdUgAAcP7b04";
    private static final String CURRENT_URL = "https://r.bullet.chat/i/w/" + INVITE_CODE;

    public static void main(String[] args) throws IOException, UnirestException {
        test2();
    }

    private static void test2() throws UnirestException, IOException {
        String url = "https://smsnumbersonline.com/free-sms-number-online-ca-p-15812213551";
        Document document = Jsoup.connect(url).get();
        Elements contentresult = document.getElementsByClass("contentresult");

        System.out.println(contentresult);
    }

    private static boolean test1() throws UnirestException {
        JSONObject params = new JSONObject()
                .put("cellphone", "+15812213551")
                .put("scene", "SIGNUP");
        HttpResponse<JsonNode> response = Unirest.post("https://r.bullet.chat/api/im/vcodes")
                .header(":authority", "r.bullet.chat")
                .header(":method", "POST")
                .header(":path", "/api/im/vcodes")
                .header(":scheme", "https")
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,mg;q=0.6")
                .header("content-type", "application/json")
                .header("origin", "https://r.bullet.chat")
                .header("referer", CURRENT_URL)
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                .body(params)
                .asJson();
        JSONObject object = response.getBody().getObject();
        boolean userExist = object.getBoolean("user_exist");
        System.out.println(object);
        return userExist;
    }
}
