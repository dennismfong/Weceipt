package weceipt.ece150.com.weceipt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FinalPrices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_prices);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        double[] prices = bundle.getDoubleArray("arrayList");

        TextView first = (TextView) findViewById(R.id.finalPrice1);
        TextView second = (TextView) findViewById(R.id.finalPrice2);
        TextView third = (TextView) findViewById(R.id.finalPrice3);
        TextView fourth = (TextView) findViewById(R.id.finalPrice4);

        DecimalFormat df = new DecimalFormat("#.00");

        first.setText("Final Price: " + df.format(prices[0]));
        second.setText("Final Price: " + df.format(prices[1]));
        third.setText("Final Price: " + df.format(prices[2]));
        fourth.setText("Final Price: " + df.format(prices[3]));
    }
}
