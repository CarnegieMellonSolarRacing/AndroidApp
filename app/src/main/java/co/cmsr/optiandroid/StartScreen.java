package co.cmsr.optiandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class StartScreen extends AppCompatActivity {
    EditText init_charge_field, init_charge_per_field;
    EditText trial_name;
//    ToggleButton saveLogButton, enableDebugButton;
    Button startButton;
//    Button loadLogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        trial_name = (EditText) findViewById(R.id.trial_name);
        init_charge_field = (EditText) findViewById(R.id.initial_charge);
        init_charge_per_field = (EditText) findViewById(R.id.init_charge_percent);
//        saveLogButton = (ToggleButton) findViewById(R.id.saveLogButton);
//        enableDebugButton = (ToggleButton) findViewById(R.id.enableDebugButton);
        startButton = (Button) findViewById(R.id.startButton);
//        loadLogButton = (Button) findViewById(R.id.loadLogButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButtonClicked(view);
            }
        });
//        loadLogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                loadLogButtonClicked(view);
//            }
//        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        enableButtons(true);
    }

    private void enableButtons(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Disable start button to prevent repeated clicks.
//                loadLogButton.setEnabled(enabled);
                startButton.setEnabled(enabled);
            }
        });
    }

    public void loadLogButtonClicked(View view) {
        enableButtons(false);

        Intent i = new Intent(getApplicationContext(), DataReviewActivity.class);
        startActivity(i);
    }

    public void startButtonClicked(View view) {
        String trial_name_text = trial_name.getText().toString();
        String init_charge_text = init_charge_field.getText().toString();
        String init_charge_per_text = init_charge_per_field.getText().toString();
//        boolean saveLog = saveLogButton.isChecked();
//        boolean debugEnabled = enableDebugButton.isChecked();

        enableButtons(false);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("trial_name", trial_name_text);

        // If user provided initial charge, parse this, else, let main activity just use default
        // value
        if (init_charge_text.length() != 0) {
            i.putExtra("initial_charge", Double.parseDouble(init_charge_text));
        }
        if (init_charge_per_text.length() != 0) {
            i.putExtra("initial_charge_percent", Double.parseDouble(init_charge_per_text));
        }
//        i.putExtra("save_log", saveLog);
//        i.putExtra("debug_enabled", debugEnabled);
        startActivity(i);
    }
}
