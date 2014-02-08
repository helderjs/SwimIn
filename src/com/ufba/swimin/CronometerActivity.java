package com.ufba.swimin;

import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.ufba.swimin.helper.DatabaseHelper;
import com.ufba.swimin.model.Training;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CronometerActivity  extends Activity {
    DatabaseHelper db;
    Chronometer chronometer;
    Button btControl, btUnsave, btSave;
    long timeWhenPaused = 0;
    boolean pause;
    int currentAction = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cronometer);

        db = new DatabaseHelper(this);
        pause = false;
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        btControl = (Button) findViewById(R.id.btControl);
        btUnsave = (Button) findViewById(R.id.btUnsave);
        btSave = (Button) findViewById(R.id.btSave);

        btControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                switch (currentAction) {
                    case 1:
                        if (pause) {
                            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
                        } else {
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
                currentAction = 1;
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pause = false;
                btControl.setText("Iniciar");
                currentAction = 1;

                Bundle extras = getIntent().getExtras();
                String date = extras.getString("date");
                String type = extras.getString("type");
                Long athlete_id = extras.getLong("athlete_id");
                String distance = extras.getString("distance");
                Long time = timeWhenPaused;

                Date swim_date = new Date();
                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    swim_date = dateFormat.parse(date);
                } catch (Exception e) {
                    Toast.makeText(arg0.getContext(), "Data deve ter o formato dd/mm/YYY ",
                            Toast.LENGTH_SHORT).show();
                }

                Training train = new Training(
                    swim_date,
                    type,
                    athlete_id,
                    distance,
                    time
                );

                db.addTraining(train);

                Toast.makeText(arg0.getContext(), "Treinamento salvo com sucesso", Toast.LENGTH_LONG).show();
                
                finish();
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

