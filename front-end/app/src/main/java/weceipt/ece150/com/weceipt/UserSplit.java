package weceipt.ece150.com.weceipt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class UserSplit extends AppCompatActivity {

    private ImageView image;
    private ViewGroup top;
    private ViewGroup bottom;
    private int x_pos_change;
    private int y_pos_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_split);

        top = (ViewGroup) findViewById(R.id.top_half);
        bottom = (ViewGroup) findViewById(R.id.bottom_half);

        //View testButton = findViewById(R.id.button_drag);
        Button testButton = (Button) findViewById(R.id.button_drag);
        testButton.setOnLongClickListener(testButton);

    }

}
