package com.example.server;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class VisionApiWrapper {

  private static final String API_URL = "https://api.taggun.io/api/receipt/v1/verbose/url";
  private static final String API_KEY = "f8fdb5206e6c11e8a4f447c2a7b2602f";

  private JSONObject getRawJson(String path) {
    JSONObject json = null;
    try {
      URL url = new URL(API_URL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept","application/json");
      conn.setRequestProperty("apikey", API_KEY);
      conn.setDoOutput(true);
      conn.setDoInput(true);

      JSONObject jsonParam = new JSONObject();
      jsonParam.put("url", path);
      jsonParam.put("refresh", "false");
      jsonParam.put("incognito", "false");
      jsonParam.put("ipAddress", "32.4.2.223");
      jsonParam.put("language", "en");

      DataOutputStream os = new DataOutputStream(conn.getOutputStream());
      os.writeBytes(jsonParam.toString());

      os.flush();
      os.close();

      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line+"\n");
      }
      br.close();

      json = new JSONObject(sb.toString());
      conn.disconnect();
    } catch (Exception e) {
      System.out.println(e);
    }
    return json;
  }

  public double getTotalAmount(String path) {
    JSONObject rawJson = getRawJson(path);
    double totalAmount = 0;
    try {
      totalAmount = rawJson.getJSONObject("totalAmount").getDouble("data");
    } catch (Exception e) {
      System.out.println(e);
    }
    return totalAmount;
  }

  public double getTaxAmount(String path) {
    JSONObject rawJson = getRawJson(path);
    double taxAmount = 0;
    try {
      taxAmount = rawJson.getJSONObject("taxAmount").getDouble("data");
    } catch (Exception e) {
      System.out.println(e);
    }
    return taxAmount;
  }

  public List<ReceiptItem> getReceiptItems(String path) {
    JSONObject rawJson = getRawJson(path);
    List<ReceiptItem> items = new ArrayList<>();
    try {
      JSONArray amounts = rawJson.getJSONArray("amounts");
      System.out.println(amounts);
      for (int i = 0; i < amounts.length(); i++) {
        JSONObject currObj = amounts.getJSONObject(i);
        if (isReceiptItem(currObj)) {
          ReceiptItem currItem = ReceiptItem.builder()
                  .description(currObj.getString("text"))
                  .price(currObj.getDouble("data"))
                  .build();
          items.add(currItem);
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    System.out.println(items);;
    return items;
  }

  private boolean isReceiptItem(JSONObject itemJson) {
    try {
      if (itemJson.getString("classifyResult") == null) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      System.out.println(e);
      return true;
    }
  }
}
