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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ItemPreviewActivity extends ListActivity {

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
            HttpPostRequest postReq = new HttpPostRequest();
            postReq.execute("https://weceipt.herokuapp.com/upload", base64String);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        setContentView(R.layout.activity_item_preview);
        initListItems();
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
    }

    public void addItems(View v) {
        listItems.add("Clicked : "+clickCounter++);
        adapter.notifyDataSetChanged();
    }

    private void initListItems() {
        listItems.add("hey");
        listItems.add("test");
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
