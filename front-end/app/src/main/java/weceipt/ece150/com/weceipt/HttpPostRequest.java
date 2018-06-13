package weceipt.ece150.com.weceipt;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostRequest extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        String result = "test";
        String urlString = params[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("email", "dfong2014@gmail.com");
            jsonParam.put("image", params[1]);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            result = conn.getResponseMessage();
            conn.disconnect();
        } catch (Exception e) {
            Log.d("DENNISPOST: ERROR ", e.toString());
            return e.toString();
        }
        Log.d("DENNISPOST: RESULT ", result);
        return result;
    }

    // On getting result
    protected void onPostExecute(String result){

        super.onPostExecute(result);
        Log.d("DENNISPOST", result);
    }
}