package weceipt.ece150.com.weceipt;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class PopulateItems extends AppCompatActivity {
    private int price1 = 0;
    private int price2 = 0;
    private int price3 = 0;
    private int price4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_populate_items);

        Intent intent = getIntent();
        ReceiptItemWrapper itemWrapper = (ReceiptItemWrapper) intent.getSerializableExtra("data");
        ArrayList<ReceiptItem> items = itemWrapper.getItems();
        ArrayList<Button> Buttons = new ArrayList<Button>();
        for (ReceiptItem item : items) {
            String description = item.getDescription();
            double price = item.getPrice();

            Button newButton = new Button(this);
            newButton.setText(item.toString());
            newButton.setTag(item);
            Buttons.add(newButton);
        }

        LinearLayout initialLocation = (LinearLayout) findViewById(R.id.top_left);
        for (Button button: Buttons) {
            initialLocation.addView(button);
        }
    }

    ArrayList<Integer> calculatePrices() {
        ArrayList<Integer> prices = new ArrayList<Integer>(4);
        LinearLayout l1 = (LinearLayout) findViewById(R.id.top_left);
        final int childCount = l1.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Button b = (Button) l1.getChildAt(i);
            ReceiptItem item = (ReceiptItem) b.getTag();
            this.price1 += item.getPrice();
        }
        prices.add(this.price1);

        LinearLayout l2 = (LinearLayout) findViewById(R.id.top_right);
        final int childCount2 = l2.getChildCount();
        for (int i = 0; i < childCount2; i++) {
            Button b = (Button) l2.getChildAt(i);
            ReceiptItem item = (ReceiptItem) b.getTag();
            this.price2 += item.getPrice();
        }
        prices.add(this.price2);

        LinearLayout l3 = (LinearLayout) findViewById(R.id.bottom_left);
        final int childCount3 = l3.getChildCount();
        for (int i = 0; i < childCount3; i++) {
            Button b = (Button) l3.getChildAt(i);
            ReceiptItem item = (ReceiptItem) b.getTag();
            this.price3 += item.getPrice();
        }
        prices.add(this.price3);

        LinearLayout l4 = (LinearLayout) findViewById(R.id.bottom_right);
        final int childCount4 = l4.getChildCount();
        for (int i = 0; i < childCount4; i++) {
            Button b = (Button) l4.getChildAt(i);
            ReceiptItem item = (ReceiptItem) b.getTag();
            this.price4 += item.getPrice();
        }
        prices.add(this.price4);

        return prices;
    }
}

