package weceipt.ece150.com.weceipt;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiManager {
    private static final String BASE_URL = "https://weceipt.herokuapp.com";
    private HttpGetRequest httpGetRequest = new HttpGetRequest();
    private HttpPostRequest httpPostRequest = new HttpPostRequest();

    public String getPresignedUrl() {
        String route = "/presigned_url";
        try {
            return httpGetRequest.execute(BASE_URL + route).get();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String postImage(String imageBase64String) {
        String route = "/test";
        try {
            return httpPostRequest.execute(BASE_URL + route, imageBase64String).get();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
