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
    EditText nameField;
    ToggleButton saveLogButton;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        nameField = (EditText) findViewById(R.id.nameField);
        saveLogButton = (ToggleButton) findViewById(R.id.saveLogButton);
        startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButtonClicked(view);
            }
        });
    }

    public void startButtonClicked(View view) {
        String name = nameField.getText().toString();
        boolean saveLog = saveLogButton.isChecked();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Disable start button to prevent repeated clicks.
                startButton.setEnabled(false);
            }
        });

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("trial_name", name);
        i.putExtra("save_log", saveLog);
        startActivity(i);
    }
}
