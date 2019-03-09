package com.hgq.security;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author houguangqiang
 * @date 2019-02-14
 * @since 1.0
 */
public class Gzlawyer {

    private static final String BASE_URL = "http://oa.gzlawyer.org";
    private static final String PAPER_COURSE_URL = BASE_URL + "/service/rest/dm.MVC/gam.PaperCourse@mvc-list/execute?businessId=gam.PaperCourse%40mvc-list&page=";
    private static final String ADD_TRAINING_RECORD_URL = BASE_URL + "/service/rest/dm.DataService/gam.PaperCourse@addTrainingRecord/invoke?_csrftoken=dcd03f07-eb6e-4275-ad50-d469c224e951";
    private static final Map<String, String> HEADERS = new LinkedHashMap<>();

    static {
        Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
        Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
        HEADERS.put("Cookie", "bad_id36c8e930-c0aa-11e8-a90d-9d1e547c2986=84b9ba31-2ff8-11e9-b425-5f04bb957a92; nice_id36c8e930-c0aa-11e8-a90d-9d1e547c2986=84b9ba32-2ff8-11e9-b425-5f04bb957a92; JSESSIONID=914C53D1530EF322B470D16BA67F21C1; href=http%3A%2F%2Foa.gzlawyer.org%2Fworkbench%2F%23%2F; accessId=7192eb20-c0a9-11e8-a90d-9d1e547c2986; pageViewNum=1; bad_id7192eb20-c0a9-11e8-a90d-9d1e547c2986=c18d0431-2ff8-11e9-9eea-5fae95389fe6; nice_id7192eb20-c0a9-11e8-a90d-9d1e547c2986=c18d0432-2ff8-11e9-9eea-5fae95389fe6; b_Admin_visibility=hidden");
        HEADERS.put("Host", "oa.gzlawyer.org");
        HEADERS.put("Origin", "http://oa.gzlawyer.org");
        HEADERS.put("Referer", "http://oa.gzlawyer.org/workbench/");
        HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
    }

    public static void main(String[] args) throws IOException {
        Integer page = 3;
        Document document = Jsoup.connect(PAPER_COURSE_URL + page + "&_=" + System.currentTimeMillis()).headers(HEADERS).get();
        Elements elements = document.body().getElementsByClass("custom-checkbox");
        List<String> ids = elements.stream().map(e -> e.attr("data-entityId")).collect(Collectors.toList());
        for (String id : ids) {
            try {
                System.out.println("==>> 学习 ID: " + id + " 中...");
                int i = 0 + ThreadLocalRandom.current().nextInt(6);
                for (; i > 0; i--) {
                    System.out.println("ID: " + id + " 学习剩余秒: " + i);
                    TimeUnit.SECONDS.sleep(1);
                }
                JSONObject params = new JSONObject();
                params.put("paperCourseId", id);
                params.put("lawyerId", "7a0a7f97e965449eadf3188e68214267");
                JsonNode body = Unirest.post(ADD_TRAINING_RECORD_URL)
                        .header("content-type", "application/json")
                        .headers(HEADERS)
                        .body(params)
                        .asJson().getBody();
                System.out.println(body);
                System.out.println("<<== 学习 ID: " + id + " 结束");
            } catch (UnirestException e) {
                e.printStackTrace();
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(ids);
    }
}
