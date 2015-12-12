package draughts.com.englishdraughts;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    private CustomDraughtView draughtView;
    private EditText etBoardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        draughtView = (CustomDraughtView)findViewById(R.id.draught_view);
        setCustomViewHeight();
        etBoardSize = (EditText) findViewById(R.id.et_board_size);
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(etBoardSize.getText().toString());
                if(size<6 || size>20){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.error_size), Toast.LENGTH_SHORT).show();
                    return;
                }
                draughtView.resetGame(size);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Sets the height of custom graph
     */
    private void setCustomViewHeight() {
        WindowManager wm = (WindowManager) this
                .getSystemService(this.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                draughtView .getLayoutParams();
        params.height = display.getWidth();
        draughtView .setLayoutParams(params);
    }

}
