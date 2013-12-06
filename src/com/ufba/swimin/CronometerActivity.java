package com.ufba.swimin;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

public class CronometerActivity  extends Activity {
    Chronometer chronometer;
    Button btControl, btUnsave, btSave;
    EditText meters;
    long timeWhenPaused = 0;
    boolean pause;
    int currentAction = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cronometer);

        pause = false;
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        btControl = (Button) findViewById(R.id.btControl);
        btUnsave = (Button) findViewById(R.id.btUnsave);
        btSave = (Button) findViewById(R.id.btSave);
        meters = (EditText) findViewById(R.id.meters);

        btControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                switch (currentAction) {
                    case 1:
                        if (pause) {
                            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
                        } else {
                            if (meters.getText().toString().equals("")) {
                                Toast.makeText(arg0.getContext(), "Defina os metros", Toast.LENGTH_LONG).show();
                                return;
                            }

                            meters.setEnabled(false);
                            chronometer.setBase(SystemClock.elapsedRealtime());
                        }

                        chronometer.start();
                        btControl.setText("Pausar");
                        btUnsave.setEnabled(false);
                        btSave.setEnabled(false);
                        currentAction = 2;
                        pause = false;

                        break;
                    case 2:
                        timeWhenPaused = chronometer.getBase() - SystemClock.elapsedRealtime();
                        chronometer.stop();
                        btControl.setText("Continuar");
                        btUnsave.setEnabled(true);
                        btSave.setEnabled(true);
                        currentAction = 1;
                        pause = true;

                        break;
                }
            }
        });

        btUnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenPaused = 0;
                pause = false;
                btControl.setText("Iniciar");
                meters.setEnabled(true);
                currentAction = 1;
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenPaused = 0;
                pause = false;
                btControl.setText("Iniciar");
                meters.setEnabled(true);
                currentAction = 1;

                Intent i = new Intent(getApplicationContext(), TrainingActivity.class);
                i.putExtra("meters", meters.getText().toString());
                i.putExtra("time", chronometer.getBase());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

