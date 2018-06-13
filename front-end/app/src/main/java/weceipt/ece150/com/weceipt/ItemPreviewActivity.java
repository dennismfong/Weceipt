package weceipt.ece150.com.weceipt;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ItemPreviewActivity extends ListActivity implements AsyncResponse{

    ArrayList<ReceiptItem> listItems=new ArrayList<ReceiptItem>();
    ArrayAdapter<ReceiptItem> adapter;
    EditText mDescriptionET;
    EditText mPriceET;

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
            postReq.execute("http://169.231.98.33:8080/upload", base64String);
            Log.d("DENNISPOST", "done with post request");
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        setContentView(R.layout.activity_item_preview);
        adapter=new ArrayAdapter<ReceiptItem>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.splitButton);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ItemPreviewActivity.this, PopulateItems.class);
                intent.putExtra("data", new ReceiptItemWrapper(listItems));
                startActivity(intent);

            }
        });
    }

    public void addItems(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_additem, null);
        mDescriptionET = (EditText)mView.findViewById(R.id.description);
        mPriceET = (EditText)mView.findViewById(R.id.price);
        builder.setView(inflater.inflate(R.layout.dialog_additem, null))
                .setPositiveButton("Add item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String description = mDescriptionET.getText().toString();
                        Log.d("ADDITEM", description);
                        double price = Double.parseDouble(mPriceET.getText().toString());
                        ReceiptItem item = ReceiptItem.builder()
                            .description(description)
                            .price(price)
                            .build();
                        addSingleItem(item);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create();
        builder.show();
    }

    public void addSingleItem(ReceiptItem item) {
        listItems.add(item);
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            PostResponsePojo res = mapper.readValue(output, PostResponsePojo.class);
            for (ReceiptItem item: res.getItems()) {
                addSingleItem(item);
            }
        } catch (IOException e) {
            Log.e("DENNIS", "error mapping JSON\n" + e.toString());
        }

        Log.d("DENNISPROCESSFINISH", output);
    }
}
