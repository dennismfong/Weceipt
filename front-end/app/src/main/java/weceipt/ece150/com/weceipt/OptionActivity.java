package weceipt.ece150.com.weceipt;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        final Button button1 = findViewById(R.id.button_bottomleft);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("quadrant", "bottomLeft");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        final Button button2 = findViewById(R.id.button_bottomright);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("quadrant", "bottomRight");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        final Button button3 = findViewById(R.id.button_topleft);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("quadrant", "topLeft");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        final Button button4 = findViewById(R.id.button_topright);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("quadrant", "topRight");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
