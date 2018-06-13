package weceipt.ece150.com.weceipt;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostRequest extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    ItemPreviewActivity activity;
    ProgressDialog pd;

    public HttpPostRequest(ItemPreviewActivity activity) {
        this.activity = activity;
        pd = new ProgressDialog(activity);
    }

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

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
            return sb.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.setTitle("Scanning receipt...");
        pd.setCancelable(false);
        pd.show();
        Log.d("DENNISPOST", "preexecute");
    }

    // On getting result
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        Log.d("DENNISPOST", "postexecute " + result);
        pd.dismiss();
        delegate.processFinish(result);
    }
}