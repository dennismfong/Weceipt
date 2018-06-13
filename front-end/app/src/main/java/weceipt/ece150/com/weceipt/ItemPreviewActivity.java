package weceipt.ece150.com.weceipt;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ItemPreviewActivity extends ListActivity implements AsyncResponse{

    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    int clickCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String imageUri = getIntent().getExtras().getString("imageUri");
        Uri uri = Uri.parse(imageUri);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            String base64String = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
            HttpPostRequest postReq = new HttpPostRequest(this);
            postReq.delegate=this;
            postReq.execute("169.231.98.33:8080/upload", base64String);
            Log.d("DENNISPOST", "done with post request");
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        setContentView(R.layout.activity_item_preview);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
    }

    public void addItems(View v) {
        listItems.add("Clicked : "+clickCounter++);
        adapter.notifyDataSetChanged();
    }

    public void initListItems() {
        listItems.add("hey");
        listItems.add("test");
        adapter.notifyDataSetChanged();
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    // Taking the result from the post request into output
    @Override
    public void processFinish(String output) {
        /** Map String to PostResponsePojo
         *
         */
        initListItems();
        Log.d("DENNISPROCESSFINISH", output);
    }
}
